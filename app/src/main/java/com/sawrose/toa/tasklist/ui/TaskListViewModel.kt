package com.sawrose.toa.tasklist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sawrose.toa.R
import com.sawrose.toa.core.repository.TaskRepository
import com.sawrose.toa.core.ui.AlertMessage
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.model.Task
import com.sawrose.toa.tasklist.domain.usecase.GetTaskForDateUseCase
import com.sawrose.toa.tasklist.domain.usecase.RescheduleTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTaskForDateUseCase: GetTaskForDateUseCase,
    private val rescheduleTaskUseCase: RescheduleTaskUseCase,
    private val repository: TaskRepository,
) : ViewModel() {
    private val _viewState = MutableStateFlow(TaskListViewState())
    val viewState = _viewState.asStateFlow()

    init {
        _viewState.map { viewState ->
            viewState.selectedDate
        }
            .distinctUntilChanged()
            .flatMapLatest { selectedDate ->
                _viewState.update {
                    it.copy(
                        showLoading = true,
                        incompleteTasks = null,
                        completedTask = null,
                    )
                }

                getTaskForDateUseCase.invoke(selectedDate)
            }
            .onEach { result ->
                _viewState.update {
                    getViewStateForTask(result)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getViewStateForTask(
        result: Result<List<Task>>,
    ): TaskListViewState {
        return result.fold(
            onSuccess = { taskList ->
                val (complete, incomplete) = taskList.partition { task ->
                    task.completed
                }

                _viewState.value.copy(
                    showLoading = false,
                    incompleteTasks = incomplete,
                    completedTask = complete,
                )
            },
            onFailure = {
                _viewState.value.copy(
                    errorMessage = UIText.StringText("Something went wrong"),
                    showLoading = false,
                )
            },
        )
    }

    /**
     * When the done button is clicked, we will render an alert message that states a task has
     * been accomplished, but it provides an undo button to revert this action. We show a temporary
     * state, that indicates the task is done, but we don't actually commit anything to the
     * [taskRepository] until the message is dismissed.
     */
    fun onDoneClicked(
        task: Task,
    ) {
        markTaskAsComplete(task)
        val taskAccomplishedAlertMessage = getUndoAlertMessageForTask(task)
        addAlertMessageToState(taskAccomplishedAlertMessage)
    }

    private fun markTaskAsComplete(
        task: Task,
    ) {
        viewModelScope.launch {
            val taskAsComplete = task.copy(completed = true)
            repository.updateTask(taskAsComplete)
        }
    }

    private fun addAlertMessageToState(
        taskAccomplishedAlertMessage: AlertMessage,
    ) {
        _viewState.update { currentState ->
            currentState.copy(
                alertMessage = currentState.alertMessage + taskAccomplishedAlertMessage,
            )
        }
    }

    private fun getUndoAlertMessageForTask(
        task: Task,
    ) = AlertMessage(
        message = UIText.ResourceText(R.string.task_accomplished, listOf(task.description)),
        actionText = UIText.ResourceText(R.string.undo),
        onActionClicked = {
            val taskAsIncomplete = task.copy(completed = false)
            viewModelScope.launch {
                repository.updateTask(taskAsIncomplete)
            }
        },
        onDismissed = {
        },
        duration = AlertMessage.Duration.LONG,
    )

    fun onDateSelected(
        newDate: LocalDate,
    ) {
        _viewState.value = _viewState.value.copy(
            selectedDate = newDate,
        )
    }

    fun onRescheduleButtonClicked(
        task: Task,
    ) {
        _viewState.update {
            it.copy(
                taskToReschedule = task,
            )
        }
    }

    /**
     * When a task is rescheduled, we will render an alert message that states a task has
     * been rescheduled, but it provides an undo button to revert this action. We show a temporary
     * state, that indicates the task is rescheduled, but we don't actually commit anything to the
     * [rescheduleTaskUseCase] until the message is dismissed.
     */
    fun onTaskRescheduled(
        task: Task,
        newDate: LocalDate,
    ) {
        if (newDate < LocalDate.now()) {
            _viewState.update {
                it.copy(
                    taskToReschedule = null,
                    alertMessage = it.alertMessage + AlertMessage(
                        message = UIText.ResourceText(R.string.task_rescheduled_to_past),
                    ),
                )
            }
            return
        }

        val taskRescheduledAlertMessage = AlertMessage(
            message = UIText.ResourceText(R.string.task_rescheduled),
            actionText = UIText.ResourceText(R.string.undo),
            onActionClicked = {
                _viewState.update {
                    val updatedTask = it.incompleteTasks?.plus(task)
                    it.copy(
                        incompleteTasks = updatedTask,
                    )
                }
            },
            onDismissed = {
                viewModelScope.launch {
                    rescheduleTaskUseCase.invoke(task, newDate)
                }

                _viewState.update {
                    it.copy(
                        taskToReschedule = null,
                    )
                }
            },
            duration = AlertMessage.Duration.LONG,
        )

        _viewState.update {
            val tempTasks = it.incompleteTasks?.minus(task)
            it.copy(
                taskToReschedule = null,
                incompleteTasks = tempTasks,
                alertMessage = it.alertMessage + taskRescheduledAlertMessage,
            )
        }
    }

    fun onReschedulingCompleted() {
        _viewState.update {
            it.copy(
                taskToReschedule = null,
            )
        }
    }

    fun onAlertMessageShown(
        shownId: Long,
    ) {
        _viewState.update {
            val newAlertMessages = it.alertMessage.filter { alertMessage ->
                alertMessage.id != shownId
            }

            it.copy(
                alertMessage = newAlertMessages,
            )
        }
    }
}
