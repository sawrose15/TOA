package com.sawrose.toa.core.repository

import com.sawrose.toa.model.Task
import kotlinx.coroutines.flow.Flow

typealias TaskListResult = Result<List<Task>>

/**
 * This is the data contract between the data layer and the domain layer.
* */
interface TaskRepository {
    /**
     * Request for all the task created by authenticated user.
     * */
    fun fetchAllTasks(): Flow<TaskListResult>

    /**
     * Request for all the task that has been created for the supplied TimeInMillis.
     * */
    fun fetchTaskForDate(
        dateInMillis: Long,
        completed: Boolean,
    ): Flow<TaskListResult>

    /**
     * Add new [Task] from the user's input.
     * */
    suspend fun addTask(
        task: Task,
    ): Result<Unit>

    /**
     * Delete the supplied [Task].
     * */
    suspend fun deleteTask(
        task: Task,
    ): Result<Unit>

    /**
     * Update the supplied [Task].
     * */
    suspend fun updateTask(
        task: Task,
    ): Result<Unit>
}
