package com.sawrose.toa.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun SettingsScreen(
    viewmodel: SettingsViewModel = hiltViewModel(),
) {
    val viewstate by viewmodel.viewState.collectAsStateWithLifecycle()

    SettingsContent(
        viewState = viewstate,
        onNumTasksChanged = viewmodel::numTasksPerDayChanged,
        onNumTasksEnabledChanged = viewmodel::numTasksPerDayEnabledChanged,
    )
}
