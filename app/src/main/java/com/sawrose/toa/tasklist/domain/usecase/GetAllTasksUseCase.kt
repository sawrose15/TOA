package com.sawrose.toa.tasklist.domain.usecase

import com.sawrose.toa.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Fetch all tasks the user has created.
 * */
interface GetAllTasksUseCase {
    operator fun invoke(): Flow<Result<List<Task>>>
}