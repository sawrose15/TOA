package com.sawrose.toa.login.ui

import androidx.lifecycle.ViewModel
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.Email
import com.sawrose.toa.login.domain.model.Password
import com.sawrose.toa.login.domain.usecases.CredentialsLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val credentialsLoginUseCase: CredentialsLoginUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<LoginViewState> =
        MutableStateFlow(LoginViewState.Initial)

    val viewState: StateFlow<LoginViewState>
        get() = _viewState

    fun emailChanged(email: String) {
        val currentCredentials = _viewState.value.credentials

        _viewState.value = LoginViewState.Active(
            credentials = currentCredentials.withUpdateEmail(email = email)
        )
    }

    fun passwordChanged(password: String) {
        val currentCredentials = _viewState.value.credentials

        _viewState.value = LoginViewState.Active(
            credentials = currentCredentials.withUpdatePassword(password = password)
        )
    }

    fun loginButtonClicked() {
        TODO()
    }

    fun signupButtonClicked() {
        TODO()
    }
}

private fun Credentials.withUpdateEmail(email: String): Credentials {
    return this.copy(email = Email(email))
}

private fun Credentials.withUpdatePassword(password: String): Credentials {
    return this.copy(password = Password(password))
}
