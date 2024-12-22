package com.sawrose.toa.tasklist.domain.usecase

import com.sawrose.toa.core.repository.TaskRepository
import com.sawrose.toa.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class ProdGetTaskForDateUseCase @Inject constructor(
    private val repository: TaskRepository,
) : GetTaskForDateUseCase {
    override fun invoke(
        date: LocalDate,
    ): Flow<Result<List<Task>>> {
        val dateInMillis = date.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val incompleteTask = repository.fetchTaskForDate(dateInMillis, false)
        val completedTask = repository.fetchTaskForDate(dateInMillis, true)
        return incompleteTask.combineTransform(completedTask) { incomplete, complete ->
            val completeTasks = complete.getOrNull()
            val incomplete = incomplete.getOrNull()
            if (completeTasks != null && incomplete != null) {
                emit(Result.success(completeTasks + incomplete))
            } else {
                emit(Result.failure(Throwable("Error fetching tasks for a $date")))
            }
        }
    }
}
