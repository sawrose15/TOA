package com.sawrose.toa.login.domain.usecases

import com.sawrose.toa.login.domain.model.LoginResult

/**
* This is a temporary class to create a mock implementation of [LoginUseCase] that is
* always successful
*/
class SuccessLoginUseCase : LoginUseCase {

    override suspend fun invoke(email: Email, password: Password): LoginResult {
        return LoginResult.Success
    }
}
