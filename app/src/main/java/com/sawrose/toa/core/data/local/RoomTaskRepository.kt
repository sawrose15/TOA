package com.sawrose.toa.core.data.local

import com.sawrose.toa.core.repository.TaskListResult
import com.sawrose.toa.core.repository.TaskRepository
import com.sawrose.toa.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class RoomTaskRepository @Inject constructor(
        private val taskDao: TaskDAO
): TaskRepository {
    override fun fetchAllTasks(): Flow<TaskListResult> {
        return taskDao.fetchAllTasks()
            .map {
                Result.success(it.toDomainTaskList())
            }
    }

    override fun fetchTaskForDate(dateInMillis: Long, completed: Boolean): Flow<TaskListResult> {
        val localDate = Instant.ofEpochMilli(dateInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        return taskDao
            .fetchTasksForDate(
                localDate.toPersistableDateString(),
                completed
            )
            .map {
                Result.success(it.toDomainTaskList())
            }
    }

    override suspend fun addTask(task: Task): Result<Unit> {
        taskDao.insertTask(task.toTaskEntity())
        return Result.success(Unit)
    }

    override suspend fun deleteTask(task: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        taskDao.updateTask(task.toTaskEntity())
        return Result.success(Unit)
    }

}

private fun List<PersistableTask>.toDomainTaskList(): List<Task> {
    return this.map(PersistableTask::toTask)
}

private const val PERSISTED_DATE_FORMAT = "yyyy-MM-dd"
private val persistedDateFormatter = DateTimeFormatter.ofPattern(PERSISTED_DATE_FORMAT)

private fun LocalDate.toPersistableDateString(): String {
    return persistedDateFormatter.format(this)
}

private fun PersistableTask.toTask(): Task {
    return Task(
        id = this.id,
        description = this.description,
        scheduleTimeInMillis = LocalDate.parse(this.scheduledDate, persistedDateFormatter)
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
        completed = this.completed
    )
}

private fun Task.toTaskEntity(): PersistableTask {
    val scheduledDate = Instant.ofEpochMilli(this.scheduleTimeInMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return PersistableTask(
        id = this.id,
        description = this.description,
        scheduledDate = scheduledDate.toPersistableDateString(),
        completed = this.completed,
    )
}
