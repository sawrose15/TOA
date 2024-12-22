package com.sawrose.toa.addTask.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sawrose.toa.R

@Destination(
    navArgsDelegate = AddTaskNavArguments::class
)
@Composable
fun AddTaskScreen(
    navigator: DestinationsNavigator,
    viewModel: AddTaskViewModel = hiltViewModel()
) {

    AddTaskContainer(
        viewModel = viewModel,
        navigator = navigator,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.screen_padding))
            .statusBarsPadding()
    )

}