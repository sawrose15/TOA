@file:OptIn(ExperimentalMaterial3Api::class)

package com.sawrose.toa.tasklist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.AlertMessage
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.core.ui.adaptiveWidth
import com.sawrose.toa.core.ui.components.Material3CircularProgressIndicator
import com.sawrose.toa.core.ui.components.TOADatePickerDialog
import com.sawrose.toa.core.ui.getString
import com.sawrose.toa.core.ui.theme.TOATheme
import com.sawrose.toa.core.utils.toEpochMillisUTC
import com.sawrose.toa.core.utils.toLocalDateUTC
import com.sawrose.toa.model.Task
import java.time.LocalDate

const val ADD_TASK_BUTTON_TAG = "ADD_TASK_BUTTON"

@Composable
fun TaskListContent(
    viewState: TaskListViewState,
    onRescheduleClicked: (Task) -> Unit,
    onDoneClicked: (Task) -> Unit,
    onAddButtonClicked: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onTaskRescheduled: (Task, LocalDate) -> Unit,
    onReschedulingCompleted: () -> Unit,
    onAlertMessageShown: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    TaskListSnackbar(
        alertMessage = viewState.alertMessage.firstOrNull(),
        snackbarHostState = snackBarHostState,
        onAlertMessageShown = onAlertMessageShown,
    )

    Scaffold(
        floatingActionButton = {
            AddTaskButton(onclick = onAddButtonClicked)
        },
        topBar = {
            ToolbarAndDialog(
                viewState,
                onDateSelected,
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        if (viewState.showTask) {
            if (viewState.incompleteTasks.isNullOrEmpty() &&
                viewState.completedTask.isNullOrEmpty()
            ) {
                TaskListEmptyState()
            } else {
                RescheduleTaskDialog(
                    viewState = viewState,
                    onTaskRescheduled = onTaskRescheduled,
                    onDismissed = onReschedulingCompleted,
                )

                TaskList(
                    incompleteTasks = viewState.incompleteTasks.orEmpty(),
                    completedTasks = viewState.completedTask.orEmpty(),
                    onRescheduleClicked = onRescheduleClicked,
                    onDoneClicked = onDoneClicked,
                    modifier = Modifier
                        .padding(paddingValues)
                        .adaptiveWidth(),
                )
            }
        }

        if (viewState.showLoading) {
            TaskListLoadingContent()
        }
    }
}

@Composable
private fun TaskListLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Material3CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
        )
    }
}

@Composable
private fun TaskListSnackbar(
    alertMessage: AlertMessage?,
    snackbarHostState: SnackbarHostState,
    onAlertMessageShown: (Long) -> Unit,
) {
    if (alertMessage != null) {
        val message = alertMessage.message.getString()
        val actionLabel = alertMessage.actionText?.getString()
        val duration = when (alertMessage.duration) {
            AlertMessage.Duration.SHORT -> SnackbarDuration.Short
            AlertMessage.Duration.LONG -> SnackbarDuration.Long
            AlertMessage.Duration.INDEFINITE -> SnackbarDuration.Indefinite
        }

        LaunchedEffect(alertMessage.id) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
            )

            onAlertMessageShown.invoke(alertMessage.id)

            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    alertMessage.onDismissed.invoke()
                }

                SnackbarResult.ActionPerformed -> {
                    alertMessage.onActionClicked.invoke()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RescheduleTaskDialog(
    viewState: TaskListViewState,
    onTaskRescheduled: (Task, LocalDate) -> Unit,
    onDismissed: () -> Unit,
) {
    val taskToReschedule = viewState.taskToReschedule

    if (taskToReschedule != null) {
        TOADatePickerDialog(
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = taskToReschedule.scheduleTimeInMillis.toLocalDateUTC()
                    .toEpochMillisUTC(),
                selectableDates = TOADatePickerDialog.FutureDates,
            ),
            onDismiss = onDismissed,
            onComplete = { selectedDateMillis ->
                if (selectedDateMillis != null) {
                    val newDate = selectedDateMillis.toLocalDateUTC()
                    onTaskRescheduled.invoke(taskToReschedule, newDate)
                }
            },
        )
    }
}

@Composable
fun ToolbarAndDialog(
    viewState: TaskListViewState,
    onDateSelected: (LocalDate) -> Unit,
) {
    val showDatePickerDialog = remember { mutableStateOf(false) }

    if (showDatePickerDialog.value) {
        TOADatePickerDialog(
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewState.selectedDate.toEpochMillisUTC(),
            ),
            onDismiss = {
                showDatePickerDialog.value = false
            },
            onComplete = { selectedDateMillis ->
                showDatePickerDialog.value = false

                if (selectedDateMillis != null) {
                    onDateSelected.invoke(selectedDateMillis.toLocalDateUTC())
                }
            },
        )
    }

    TaskListToolbar(
        title = viewState.selectedDateString.getString(),
        onCalendarIconClicked = {
            showDatePickerDialog.value = true
        },
    )
}

@Composable
fun AddTaskButton(
    onclick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onclick,
        modifier = Modifier
            .testTag(ADD_TASK_BUTTON_TAG),
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_Task),
        )
    }
}

@Composable
private fun TaskListEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.no_task_scheduled),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.Center)
                .adaptiveWidth(),
        )
    }
}

@Suppress("MagicNumber")
class TaskListViewStateProvider : PreviewParameterProvider<TaskListViewState> {
    override val values: Sequence<TaskListViewState>
        get() {
            val incompleteTasks = (1..3).map { index ->
                Task(
                    id = "INCOMPLETE_TASK_$index",
                    description = "Test task: $index",
                    scheduleTimeInMillis = 0L,
                    completed = false,
                )
            }

            val completedTasks = (1..3).map { index ->
                Task(
                    id = "COMPLETED_TASK_$index",
                    description = "Test task: $index",
                    scheduleTimeInMillis = 0L,
                    completed = true,
                )
            }

            val loadingState = TaskListViewState(
                showLoading = true,
            )

            val taskListState = TaskListViewState(
                showLoading = false,
                incompleteTasks = incompleteTasks,
                completedTask = completedTasks,
            )

            val emptyState = TaskListViewState(
                showLoading = false,
                incompleteTasks = emptyList(),
                completedTask = emptyList(),
            )

            val errorState = TaskListViewState(
                showLoading = false,
                errorMessage = UIText.StringText("Something went wrong"),
            )

            return sequenceOf(
                loadingState,
                taskListState,
                emptyState,
                errorState,
            )
        }
}

@Preview
@Composable
fun TaskListContentPreview(
    @PreviewParameter(TaskListViewStateProvider::class)
    viewState: TaskListViewState,
) {
    TOATheme {
        TaskListContent(
            viewState = viewState,
            onRescheduleClicked = {},
            onDoneClicked = {},
            onAddButtonClicked = {},
            onDateSelected = {},
            onTaskRescheduled = { _, _ ->
            },
            onReschedulingCompleted = {},
            onAlertMessageShown = {},
        )
    }
}
