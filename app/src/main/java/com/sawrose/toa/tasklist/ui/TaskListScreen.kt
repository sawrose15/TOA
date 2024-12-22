package com.sawrose.toa.tasklist.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sawrose.toa.addTask.ui.AddTaskNavArguments
import com.sawrose.toa.destinations.AddTaskDialogDestination
import com.sawrose.toa.destinations.AddTaskScreenDestination

@Destination
@Composable
fun TaskListScreen(
    navigator: DestinationsNavigator,
    windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: TaskListViewModel = hiltViewModel()
){
    val viewState = viewModel.viewState.collectAsStateWithLifecycle()

    TaskListContent(
        viewState = viewState.value,
        onRescheduleClicked = viewModel::onRescheduleButtonClicked,
        onDoneClicked = viewModel::onDoneClicked,
        onAddButtonClicked = {
             val navArgs = AddTaskNavArguments(
                 initialDate = viewState.value.selectedDate,
             )

            val destination = if(windowWidthSizeClass != WindowWidthSizeClass.Compact) {
                AddTaskDialogDestination(navArgs.initialDate)
            }
            else {
                AddTaskScreenDestination(
                    initialDate = navArgs.initialDate
                )
            }
            navigator.navigate(destination)
        },
        onDateSelected = viewModel::onDateSelected,
        onTaskRescheduled = viewModel::onTaskRescheduled,
        onReschedulingCompleted = viewModel::onReschedulingCompleted,
        onAlertMessageShown = viewModel::onAlertMessageShown,
        modifier = Modifier
            .testTag(TaskListScreen.TEST_TAG),
    )
}


object TaskListScreen {
    const val TEST_TAG = "TASK_LIST_SCREEN"
}
