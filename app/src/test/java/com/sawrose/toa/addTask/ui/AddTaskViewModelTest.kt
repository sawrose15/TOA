package com.sawrose.toa.addTask.ui

import com.sawrose.toa.CoroutinesTestRule
import com.sawrose.toa.R
import com.sawrose.toa.addTask.domain.model.TaskInput
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.model.Task
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AddTaskViewModelTest {
    private val testRobot = AddTaskViewModelRobot()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Test
    fun createWithInitialDateFromSavedStateHandle() {
        val initialDate = LocalDate.now().plusDays(1)

        val expectedDate = AddTaskViewState.Initial(
            initialDate = initialDate,
        )

        testRobot
            .mockInitialDate(initialDate)
            .buildViewmodel()
            .assertViewState(expectedDate)
    }

    @Test
    fun submitWithEmptyDescription() {
        val taskToSubmit = Task(
            id = "Testing",
            description = "",
            scheduledDateMillis = 0L,
            completed = false,
        )

        val expectedViewState = AddTaskViewState.Active(
            taskInput = TaskInput(
                description = taskToSubmit.description,
                scheduledDate = Instant.ofEpochMilli(taskToSubmit.scheduledDateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate(),
            ),
            descriptionInputErrorMessage = UIText.ResourceText(R.string.err_empty_task_description),
        )

        testRobot
            .mockInitialDate(LocalDate.now())
            .buildViewmodel()
            .enterDescription(taskToSubmit.description)
            .selectDate(taskToSubmit.scheduledDateMillis)
            .clickSubmit()
            .assertViewState(expectedViewState)
    }
}
