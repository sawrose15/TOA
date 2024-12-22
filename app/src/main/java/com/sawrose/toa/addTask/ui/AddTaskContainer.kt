package com.sawrose.toa.addTask.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskContainer(
    viewModel: AddTaskViewModel,
    navigator: DestinationsNavigator,
    modifier: Modifier
) {
    val viewState = viewModel.viewState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(viewState.value) {
        if (viewState.value is AddTaskViewState.Completed) {
            keyboardController?.hide()

            navigator.popBackStack()
        }

        onDispose { }
    }


    AddTaskContent(
        viewState = viewState.value,
        onTaskDescriptionChanged = viewModel::onTaskDescriptionChanged,
        onTaskScheduledDateChanged = viewModel::onTaskScheduledDateChanged,
        onSubmitClicked = viewModel::onSubmitButtonClicked,
        modifier = modifier
    )
}