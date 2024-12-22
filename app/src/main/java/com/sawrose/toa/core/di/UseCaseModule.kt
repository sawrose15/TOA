package com.sawrose.toa.core.di

import com.sawrose.toa.addTask.domain.usecases.AddTaskUseCase
import com.sawrose.toa.addTask.domain.usecases.ProdAddTaskUseCase
import com.sawrose.toa.tasklist.domain.usecase.GetAllTasksUseCase
import com.sawrose.toa.tasklist.domain.usecase.GetTaskForDateUseCase
import com.sawrose.toa.tasklist.domain.usecase.ProdGetAllTasksUseCase
import com.sawrose.toa.tasklist.domain.usecase.ProdGetTaskForDateUseCase
import com.sawrose.toa.tasklist.domain.usecase.ProdRescheduleTaskUseCase
import com.sawrose.toa.tasklist.domain.usecase.RescheduleTaskUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module is responsible for defining the creation of any use case dependencies in the application.
 *
 * NOTE: If this gets very large, we may want to consider refactoring and making modules feature
 * dependent.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetAllTasks(
        getAllTasksUseCase: ProdGetAllTasksUseCase,
    ): GetAllTasksUseCase

    @Binds
    abstract fun bindGetTaskForDate(
        getTaskForDateUseCase: ProdGetTaskForDateUseCase,
    ): GetTaskForDateUseCase

    @Binds
    abstract fun bindAddTaskUseCase(
        addTaskUseCase: ProdAddTaskUseCase,
    ): AddTaskUseCase

    @Binds
    abstract fun bindRescheduleTaskUseCase(
        rescheduleTaskUseCase: ProdRescheduleTaskUseCase,
    ): RescheduleTaskUseCase
}
