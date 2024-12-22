package com.sawrose.toa.login.domain.usecases

import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.InvalidCredentialsException
import com.sawrose.toa.login.domain.model.LoginResult
import com.sawrose.toa.login.domain.repository.LoginRepository
import com.sawrose.toa.login.domain.repository.TokenRepository
import javax.inject.Inject

/**
 * A concrete implementation of a [CredentialsLoginUseCase] that will request logging in
 * via the [loginRepository].
 */
class ProdCredentialsLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend fun login(credentials: Credentials): LoginResult {
        val validateResult = validateCredentials(credentials)

        if (validateResult != null){
            return validateResult
        }

        val repoResult = loginRepository.login(credentials)

        return repoResult.fold(
            onSuccess = { loginResponse ->
                tokenRepository.storeToken(loginResponse.token)
                LoginResult.Success
            },
            onFailure = { error ->
                loginResultForError(error)
            },
        )
    }

    private fun validateCredentials(credentials: Credentials): LoginResult.Failure.EmptyCredentials? {
        val emptyEmail = credentials.email.value.isEmpty()
        val emptyPassword = credentials.password.value.isEmpty()

        return if (emptyEmail || emptyPassword) {
            LoginResult.Failure.EmptyCredentials(
                emptyEmail = emptyEmail,
                emptyPassword = emptyPassword,
            )
        } else {
            null
        }
    }
    /**
     * Checks the possible error fo the [repoResult] and map it into the appropriate
     * [LoginResult.Failure]
     */
    private fun loginResultForError(error: Throwable): LoginResult.Failure =
        when (error) {
            is InvalidCredentialsException -> {
                LoginResult.Failure.InvalidCredentials
            }
            else -> {
                LoginResult.Failure.Unknown
            }
        }
}
