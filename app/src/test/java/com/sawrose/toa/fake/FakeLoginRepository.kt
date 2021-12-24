package com.sawrose.toa.fake

import com.sawrose.toa.core.data.Result
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.LoginResponse
import com.sawrose.toa.login.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk

/**
 * A fake implementation of a [LoginRepository] that wraps all of our mock work.
 */
class FakeLoginRepository {
    val mock: LoginRepository = mockk()

    fun mockLoginWithCredentials(
        credentials: Credentials,
        result: Result<LoginResponse>
    ) {
        coEvery {
            mock.loginWithCredentials(credentials)
        } returns result
    }
}
