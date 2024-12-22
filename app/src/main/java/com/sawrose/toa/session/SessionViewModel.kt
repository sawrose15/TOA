package com.sawrose.toa.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val userLoggedInUseCase: UserLoggedInUseCase
): ViewModel() {
    private val _sessionState: MutableStateFlow<SessionState> = MutableStateFlow(SessionState.UNINSTANTIATED)
    val sessionState = _sessionState.asStateFlow()

    init {
        getSessionStateFromLoggedInState()
    }

    /**
     * Observe logged in status, updates the MainActivity.
     * */
    private fun getSessionStateFromLoggedInState() {
        userLoggedInUseCase
            .isUserLoggedIn()
            .distinctUntilChanged()
            .onEach { loggedIn ->
                val newSessionState = if(loggedIn){
                    SessionState.LOGGED_IN
                } else {
                    SessionState.LOGGED_OUT
                }

                _sessionState.update {
                    newSessionState
                }
            }
            .launchIn(viewModelScope)
    }
}