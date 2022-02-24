package com.sawrose.toa.fake

import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.LoginResult
import com.sawrose.toa.login.domain.usecases.CredentialsLoginUseCase
import io.mockk.coEvery
import io.mockk.mockk

class FakeCredentialLoginUseCase {

    val mock: CredentialsLoginUseCase = mockk()

    fun mockLoginResultForCredentials(
        credentials: Credentials,
        result: LoginResult,
    ) {
        coEvery {
            mock(credentials)
        } returns result
    }
}
