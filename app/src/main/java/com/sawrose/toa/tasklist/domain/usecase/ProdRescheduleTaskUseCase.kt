package com.sawrose.toa.tasklist.domain.usecase

import com.sawrose.toa.core.repository.TaskRepository
import com.sawrose.toa.core.utils.toEpochMillis
import com.sawrose.toa.model.Task
import java.time.LocalDate
import javax.inject.Inject

/**
 * Concrete implementation of a [RescheduleTaskUseCase] that will save the task
 * change inside of the given [repository].
 */
class ProdRescheduleTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) : RescheduleTaskUseCase {
    override suspend fun invoke(task: Task, newDate: LocalDate) {
        val updatedTask = task.copy(
            scheduleTimeInMillis = newDate.toEpochMillis()
        )

        repository.updateTask(updatedTask)
    }
}