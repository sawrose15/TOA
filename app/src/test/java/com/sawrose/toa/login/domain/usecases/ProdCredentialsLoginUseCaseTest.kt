package com.sawrose.toa.login.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.sawrose.toa.fake.FakeLoginRepository
import com.sawrose.toa.fake.FakeTokenRepository
import com.sawrose.toa.login.domain.model.AuthToken
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.Email
import com.sawrose.toa.login.domain.model.InvalidCredentialsException
import com.sawrose.toa.login.domain.model.LoginResponse
import com.sawrose.toa.login.domain.model.LoginResult
import com.sawrose.toa.login.domain.model.Password
import com.sawrose.toa.login.domain.model.RefreshToken
import com.sawrose.toa.login.domain.model.Token
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProdCredentialsLoginUseCaseTest {
    private val defaultCredentials = Credentials(
        email = Email("test@test.com"),
        password = Password("test123"),
    )

    private val defaultToken = Token(
        authToken = AuthToken("Auth"),
        refreshToken = RefreshToken("Refresh"),
    )

    private lateinit var loginRepository: FakeLoginRepository
    private lateinit var tokenRepository: FakeTokenRepository

    @Before
    fun setUp() {
        loginRepository = FakeLoginRepository()
        tokenRepository = FakeTokenRepository()
    }

    @Test
    fun testSuccessfulLogin() =
        runTest {
            val loginResponse = Result.success(
                LoginResponse(
                    token = defaultToken,
                ),
            )

            loginRepository.mockLoginWithCredentials(
                defaultCredentials,
                loginResponse,
            )

            val useCase = ProdCredentialsLoginUseCase(
                loginRepository = loginRepository.mock,
                tokenRepository = tokenRepository.mock,
            )

            val result = useCase.login(defaultCredentials)
            assertThat(result).isEqualTo(LoginResult.Success)
            tokenRepository.verifyTokenStored(defaultToken)
        }

    @Test
    fun testUnknownFailureLogin() =
        runBlocking {
            val loginResponse: Result<LoginResponse> = Result.failure(
                Throwable("Error Detected"),
            )

            loginRepository.mockLoginWithCredentials(
                defaultCredentials,
                loginResponse,
            )

            val useCase = ProdCredentialsLoginUseCase(
                loginRepository = loginRepository.mock,
                tokenRepository = tokenRepository.mock,
            )

            val result = useCase.login(defaultCredentials)
            assertThat(result).isEqualTo(LoginResult.Failure.Unknown)
            tokenRepository.verifyNoTokenStored()
        }

    @Test
    fun testInvalidCredentialLogin() =
        runBlocking {
            val loginResponse: Result<LoginResponse> = Result.failure(
                InvalidCredentialsException(),
            )

            loginRepository.mockLoginWithCredentials(
                defaultCredentials,
                loginResponse,
            )

            val useCase = ProdCredentialsLoginUseCase(
                loginRepository = loginRepository.mock,
                tokenRepository = tokenRepository.mock,
            )
            val result = useCase.login(defaultCredentials)

            assertThat(result).isEqualTo(LoginResult.Failure.InvalidCredentials)
            tokenRepository.verifyNoTokenStored()
        }
}
