package com.sawrose.toa.login.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.sawrose.toa.core.data.Result
import com.sawrose.toa.fake.FakeLoginRepository
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.Email
import com.sawrose.toa.login.domain.model.InvalidCredentialsException
import com.sawrose.toa.login.domain.model.LoginResponse
import com.sawrose.toa.login.domain.model.LoginResult
import com.sawrose.toa.login.domain.model.Password
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ProdCredentialsLoginUseCaseTest {

    @Test
    fun testSuccessfulLogin() = runBlockingTest {
        val inputCredentials = Credentials(
            email = Email("test@test.com"),
            password = Password("test123")
        )

        val loginResponse = Result.Success(
            LoginResponse(
                authToken = "Success"
            )
        )

        val loginRepository = FakeLoginRepository().apply {
            mockLoginWithCredentials(
                inputCredentials,
                loginResponse
            )
        }

        val useCase = ProdCredentialsLoginUseCase(loginRepository.mock)
        val actualResult = useCase(inputCredentials)

        assertThat(actualResult).isEqualTo(LoginResult.Success)
    }

    @Test
    fun testUnknownFailureLogin() = runBlockingTest {
        val inputCredentials = Credentials(
            email = Email("test@test.com"),
            password = Password("test123")
        )

        val loginResponse = Result.Error(
            Throwable("Error Detected")
        )

        val loginRepository = FakeLoginRepository().apply {
            mockLoginWithCredentials(
                inputCredentials,
                loginResponse
            )
        }

        val useCase = ProdCredentialsLoginUseCase(loginRepository.mock)
        val actualResult = useCase(inputCredentials)

        assertThat(actualResult).isEqualTo(LoginResult.Failure.Unknown)
    }

    @Test
    fun testInvalidCredentialLogin() = runBlockingTest {
        val inputCredentials = Credentials(
            email = Email("test@test.com"),
            password = Password("test123")
        )

        val loginResponse = Result.Error(
            InvalidCredentialsException()
        )

        val loginRepository = FakeLoginRepository().apply {
            mockLoginWithCredentials(
                inputCredentials,
                loginResponse
            )
        }

        val useCase = ProdCredentialsLoginUseCase(loginRepository.mock)
        val actualResult = useCase(inputCredentials)

        assertThat(actualResult).isEqualTo(LoginResult.Failure.InvalidCredentials)
    }
}
