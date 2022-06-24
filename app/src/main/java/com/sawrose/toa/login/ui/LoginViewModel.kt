package com.sawrose.toa.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.Email
import com.sawrose.toa.login.domain.model.LoginResult
import com.sawrose.toa.login.domain.model.Password
import com.sawrose.toa.login.domain.usecases.CredentialsLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val credentialsLoginUseCase: CredentialsLoginUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<LoginViewState> =
        MutableStateFlow(LoginViewState.Initial)

    val viewState: StateFlow<LoginViewState>
        get() = _viewState

    fun emailChanged(email: String) {
        val currentCredentials = _viewState.value.credentials

        val currentPasswordErrorMessage =
            (_viewState.value as? LoginViewState.Active)?.passwordInputErrorMessage

        _viewState.value = LoginViewState.Active(
            credentials = currentCredentials.withUpdateEmail(email = email),
            emailInputErrorMessage = null,
            passwordInputErrorMessage = currentPasswordErrorMessage,
        )
    }

    fun passwordChanged(password: String) {
        val currentCredentials = _viewState.value.credentials
        val currentEmailErrorMessage =
            (_viewState.value as? LoginViewState.Active)?.emailInputErrorMessage

        _viewState.value = LoginViewState.Active(
            credentials = currentCredentials.withUpdatePassword(password = password),
            emailInputErrorMessage = currentEmailErrorMessage,
            passwordInputErrorMessage = null,
        )
    }

    fun loginButtonClicked() {
        val currentCredential = _viewState.value.credentials
        _viewState.value = LoginViewState.Submitting(
            credentials = currentCredential,
        )
        viewModelScope.launch {
            val loginResult = credentialsLoginUseCase.invoke(currentCredential)
            handleLoginResult(loginResult, currentCredential)
        }
    }

    fun handleLoginResult(
        loginResult: LoginResult,
        currentCredentials: Credentials,
    ) {
        _viewState.value = when (loginResult) {
            is LoginResult.Failure.InvalidCredentials -> {
                LoginViewState.SubmissionError(
                    credentials = currentCredentials,
                    errorMessage = UIText.ResourceText(R.string.err_invalid_credentials)
                )
            }
            is LoginResult.Failure.Unknown -> {
                LoginViewState.SubmissionError(
                    credentials = currentCredentials,
                    errorMessage = UIText.ResourceText(R.string.err_login_failure)
                )
            }
            is LoginResult.Success -> {
                LoginViewState.Completed
            }
        }
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
