package com.sawrose.toa.addTask.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sawrose.toa.R
import com.sawrose.toa.addTask.domain.model.AddTaskResult
import com.sawrose.toa.addTask.domain.model.TaskInput
import com.sawrose.toa.addTask.domain.usecases.addTask
import com.sawrose.toa.core.repository.TaskRepository
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.destinations.AddTaskScreenDestination
import com.sawrose.toa.model.Task
import com.sawrose.toa.preferences.UserPreferences
import com.sawrose.toa.withAll
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userPreferences: UserPreferences,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    /**
     * Even though this screen can be navigated to using either AddTaskDialogDestination, or
     * AddTaskScreenDestination, because they both have the same typesafe nav arguments delegate of
     * [AddTaskNavArguments], it doesn't matter what we use here to call `argsFrom(savedStateHandle)
     * because both will have the same functionality.
     */
    private val args = AddTaskScreenDestination.argsFrom(savedStateHandle)
    private val _viewState: MutableStateFlow<AddTaskViewState> = MutableStateFlow(
        AddTaskViewState.Initial(
            initialDate = args.initialDate,
        ),
    )

    val viewState = _viewState.asStateFlow()

    fun onTaskDescriptionChanged(
        newDescription: String,
    ) {
        val currentInput = _viewState.value.taskInput
        val newInput = currentInput.copy(
            description = newDescription,
        )
        _viewState.value = AddTaskViewState.Active(
            taskInput = newInput,
            descriptionInputErrorMessage = null,
        )
    }

    fun onTaskScheduledDateChanged(
        newDate: LocalDate,
    ) {
        val currentInput = _viewState.value.taskInput
        val newInput = currentInput.copy(
            scheduledDate = newDate,
        )
        _viewState.value = AddTaskViewState.Active(
            taskInput = newInput,
            descriptionInputErrorMessage = null,
        )
    }

    fun onSubmitButtonClicked() {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            description = _viewState.value.taskInput.description,
            scheduledDateMillis = _viewState.value.taskInput.scheduledDate
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            completed = false,
        )

        viewModelScope.launch {
            val canTry = (_viewState.value as? AddTaskViewState.SubmissionError)?.allowRetry

            val result = withAll(taskRepository, userPreferences) {
                addTask(
                    task = newTask,
                    ignoreTaskLimits = (canTry == true),
                )
            }

            _viewState.value = when (result) {
                is AddTaskResult.Success -> {
                    AddTaskViewState.Completed
                }
                is AddTaskResult.Failure.InvalidInput -> {
                    result.toViewState(
                        taskInput = _viewState.value.taskInput,
                    )
                }

                is AddTaskResult.Failure.MaxTasksPerDayExceeded -> {
                    AddTaskViewState.SubmissionError(
                        taskInput = _viewState.value.taskInput,
                        errorMessage = UIText.ResourceText(R.string.err_max_tasks_per_day_exceeded),
                        overrideButtonText = UIText.ResourceText(R.string.action_submit_anyways),
                        allowRetry = true,
                    )
                }
                is AddTaskResult.Failure.Unknown -> {
                    AddTaskViewState.SubmissionError(
                        taskInput = _viewState.value.taskInput,
                        errorMessage = UIText.StringText("Unable to add task. Please try again later."),
                    )
                }
            }
        }
    }
}

private fun AddTaskResult.Failure.InvalidInput.toViewState(
    taskInput: TaskInput,
): AddTaskViewState {
    return AddTaskViewState.Active(
        taskInput = taskInput,
        descriptionInputErrorMessage = UIText.ResourceText(R.string.err_empty_task_description).takeIf {
            this.emptyDescription
        },
    )
}
