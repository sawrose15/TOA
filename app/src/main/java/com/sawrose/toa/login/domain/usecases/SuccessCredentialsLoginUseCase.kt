package com.sawrose.toa.login.domain.usecases

import com.sawrose.toa.login.domain.model.LoginResult

/**
* This is a temporary class to create a mock implementation of [CredentialsLoginUseCase] that is
* always successful
*/
class SuccessCredentialsLoginUseCase : CredentialsLoginUseCase {

    override suspend fun invoke(email: Email, password: Password): LoginResult {
        return LoginResult.Success
    }
}
