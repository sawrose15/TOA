package com.sawrose.toa.tasklist.domain.usecase

import com.sawrose.toa.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Fetch all tasks the user has created for given [date].
 * */
interface GetTaskForDateUseCase {
    /**
     * Fetch all tasks the user has created for given [date].
     * */
    fun invoke(date: LocalDate): Flow<Result<List<Task>>>
}