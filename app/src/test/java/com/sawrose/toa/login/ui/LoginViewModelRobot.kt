package com.sawrose.toa.login.ui

import com.google.common.truth.Truth.assertThat
import com.sawrose.toa.fake.FakeCredentialLoginUseCase
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.LoginResult

class LoginViewModelRobot {
    private val fakeCredentialLoginUseCase = FakeCredentialLoginUseCase()
    private lateinit var viewModel: LoginViewModel

    fun buildViewModel() = apply {
        viewModel = LoginViewModel(
            credentialsLoginUseCase = fakeCredentialLoginUseCase.mock,
        )
    }

    fun mockLoginResultForCredentials(
        credentials: Credentials,
        result: LoginResult
    ) = apply {
        fakeCredentialLoginUseCase.mockLoginResultForCredentials(credentials, result)
    }

    fun enterEmail(email: String) = apply {
        viewModel.emailChanged(email)
    }

    fun enterPassword(password: String) = apply {
        viewModel.passwordChanged(password)
    }

    fun clickLoginButton() = apply {
        viewModel.loginButtonClicked()
    }

    fun clickPasswordButton() = apply {
        viewModel.signupButtonClicked()
    }

    fun assertViewState(expectedViewState: LoginViewState) = apply {
        assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
    }
}
