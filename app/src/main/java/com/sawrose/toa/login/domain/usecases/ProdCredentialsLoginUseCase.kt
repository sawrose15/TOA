package com.sawrose.toa.login.domain.usecases

import com.sawrose.toa.core.data.Result
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.InvalidCredentialsException
import com.sawrose.toa.login.domain.model.LoginResult
import com.sawrose.toa.login.repository.LoginRepository

/**
 * A concrete implementation of a [CredentialsLoginUseCase] that will request logging in
 * via the [loginRepository].
 */
class ProdCredentialsLoginUseCase(
    private val loginRepository: LoginRepository
) : CredentialsLoginUseCase {
    override suspend fun invoke(credentials: Credentials): LoginResult {
        val repoResult = loginRepository.loginWithCredentials(credentials)

        return when (repoResult) {
            is Result.Success -> {
                return LoginResult.Success
            }
            is Result.Error -> {
                loginResultForFailure(repoResult)
            }
        }
    }

    /**
     * Checks the possible error fo the [repoResult] and map it into the appropriate
     * [LoginResult.Failure]
     */
    private fun loginResultForFailure(repoResult: Result.Error) =
        when (repoResult.error) {
            is InvalidCredentialsException -> {
                LoginResult.Failure.InvalidCredentials
            }
            else -> {
                LoginResult.Failure.Unknown
            }
        }
}
