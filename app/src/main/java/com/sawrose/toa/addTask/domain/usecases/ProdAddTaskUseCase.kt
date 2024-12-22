package com.sawrose.toa.addTask.domain.usecases

import com.sawrose.toa.addTask.domain.model.AddTaskResult
import com.sawrose.toa.core.data.local.RoomTaskRepository
import com.sawrose.toa.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class ProdAddTaskUseCase @Inject constructor(
    private val repository: RoomTaskRepository
) : AddTaskUseCase {
    override suspend fun invoke(task: Task, ignoreTaskLimits: Boolean): AddTaskResult {
        val sanitizedTask = task.copy(
            description = task.description.trim(),
        )

        val validationResult = validateTask(sanitizedTask)
        if (validationResult != null) {
            return validationResult
        }
        // if (!ignoreTaskLimits){
        //
        // }
        val result = repository.addTask(sanitizedTask)

        return result.fold(
            onSuccess = {
                AddTaskResult.Success
            },
            onFailure = {
                AddTaskResult.Failure.Unknown
            }
        )
    }


    private fun validateTask(task: Task): AddTaskResult.Failure.InvalidInput? {
       val emptyDescription = task.description.isBlank()
        val scheduleDate = Instant
            .ofEpochMilli(task.scheduleTimeInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val scheduleDateInPast = scheduleDate.isBefore(LocalDate.now())

        return if (emptyDescription || scheduleDateInPast) {
            AddTaskResult.Failure.InvalidInput(
                emptyDescription = emptyDescription,
                scheduledDateInPast = scheduleDateInPast,
            )
        } else {
            null
        }
    }
}