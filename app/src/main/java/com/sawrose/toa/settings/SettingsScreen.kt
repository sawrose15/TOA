package com.sawrose.toa.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun SettingsScreen(
    viewmodel: SettingsViewModel = hiltViewModel(),
) {
    val viewstate = viewmodel.viewState.collectAsStateWithLifecycle()
    Text("Hello Setting Page")
}
