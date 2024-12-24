package com.sawrose.toa.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sawrose.toa.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreference: UserPreferences,
) : ViewModel() {
    private val _viewState = MutableStateFlow(SettingsViewState())
    val viewState = _viewState.asStateFlow()

    init {
        getInitialPreferences()
    }

    private fun getInitialPreferences() {
        viewModelScope.launch {
            val numTasks = userPreference.getPreferredNumTasksPerDay()
            val numTasksEnabled = userPreference.getPreferredNumTasksPerDayEnabled()

            _viewState.update { currentState ->
                currentState.copy(
                    numTasksPerDay = numTasks,
                    numTasksPreferenceEnabled = numTasksEnabled,
                )
            }
        }
    }

    fun numTasksPerDayChanged(
        input: String,
    ) {
        val numTask = input
            .filter(Char::isDigit)
            .toIntOrNull()

        // Update preferences
        // And update state
        // Ideally, we update preferences, and the state observes this change.
        viewModelScope.launch {
            userPreference.setPreferredNumTasksPerDay(numTask)
            _viewState.update { currentState ->
                currentState.copy(
                    numTasksPerDay = numTask,
                )
            }
        }
    }

    fun numTasksPerDayEnabledChanged(
        enabled: Boolean,
    ) {
        // Update preferences
        // And update state
        // Ideally, we update preferences, and the state observes this change.
        viewModelScope.launch {
            userPreference.setPreferredNumTasksPerDayEnabled(enabled)

            _viewState.update { currentState ->
                currentState.copy(
                    numTasksPreferenceEnabled = enabled,
                )
            }
        }
    }
}
