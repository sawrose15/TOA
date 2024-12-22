package com.sawrose.toa.login.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sawrose.toa.fake.FakeLoginRepository
import com.sawrose.toa.fake.FakeTokenRepository
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.LoginResponse
import com.sawrose.toa.login.domain.usecases.ProdCredentialsLoginUseCase

class LoginViewModelRobot {
    private val fakeLoginRepository = FakeLoginRepository()
    private val fakeTokenRepository = FakeTokenRepository()

    private val credentialLoginUseCase = ProdCredentialsLoginUseCase(
        loginRepository = fakeLoginRepository.mock,
        tokenRepository = fakeTokenRepository.mock,
    )
    private lateinit var viewModel: LoginViewModel

    fun buildViewModel() = apply {
        viewModel = LoginViewModel(
            credentialsLoginUseCase = credentialLoginUseCase,
        )
    }

    suspend fun mockLoginResultForCredentials(
        credentials: Credentials,
        result: Result<LoginResponse>,
    ) = apply {
        fakeLoginRepository.mockLoginWithCredentials(credentials = credentials, result = result)
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

    fun clickSignUpButton() = apply {
        viewModel.signupButtonClicked()
    }

    /**
     * Launch a coroutine that will observe our [viewModel]'s view state and ensure that we consume
     * all of the supplied [viewStates] in the same order that they are supplied.
     *
     * We should call this near the front of the test, to ensure that every view state we emit
     * can be collected by this.
     */
    suspend fun expectViewState(
        action: LoginViewModelRobot.() -> Unit,
        viewStates: List<LoginViewState>,
    ) = apply {
        viewModel.viewState.test {
            action()

            for(state in viewStates){
                assertThat(awaitItem()).isEqualTo(state)
            }

            this.cancel()
        }
    }
}
