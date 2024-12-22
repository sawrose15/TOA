package com.sawrose.toa.addTask.domain.usecases

import com.sawrose.toa.addTask.domain.model.AddTaskResult
import com.sawrose.toa.model.Task

/**
 * Given a new task, store that in the user's task list.
 */
interface AddTaskUseCase {

    suspend operator fun invoke(
        task: Task,
        ignoreTaskLimits: Boolean,
    ): AddTaskResult
}