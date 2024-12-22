package com.sawrose.toa.settings

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun SettingsScreen(
    viewmodel: SettingsViewModel = hiltViewModel(),
) {
}
