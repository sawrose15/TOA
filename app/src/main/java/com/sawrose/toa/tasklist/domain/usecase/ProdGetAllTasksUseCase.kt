package com.sawrose.toa.tasklist.domain.usecase

import com.sawrose.toa.core.repository.TaskRepository
import com.sawrose.toa.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProdGetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) : GetAllTasksUseCase {
    override fun invoke(): Flow<Result<List<Task>>> {
        return repository.fetchAllTasks()
    }
}