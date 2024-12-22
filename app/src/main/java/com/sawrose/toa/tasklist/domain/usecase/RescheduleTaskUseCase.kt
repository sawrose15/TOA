package com.sawrose.toa.tasklist.domain.usecase

import com.sawrose.toa.model.Task
import java.time.LocalDate

/**
 * Consume a task and a new date that we want to schedule that
 * task for. We will modify the task, and save that change in our
 * data layer.
 */
interface RescheduleTaskUseCase {
    suspend operator fun invoke(
        task: Task,
        newDate: LocalDate,
    )
}
