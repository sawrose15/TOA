package com.sawrose.toa.login.domain.repository

import com.sawrose.toa.login.domain.model.AuthToken
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.LoginResponse
import com.sawrose.toa.login.domain.model.RefreshToken
import com.sawrose.toa.login.domain.model.Token
import javax.inject.Inject

/**
 * This is a sample [LoginRepository] that does not interact with any real data source, but allows
 * us to quickly modify return values for manual testing sake.
 */

class DemoLoginRepository @Inject constructor() : LoginRepository {
    override suspend fun login(
        credentials: Credentials,
    ): Result<LoginResponse> {
        val defaultToken = Token(
            AuthToken("DemoAuthToken"),
            RefreshToken("DemoRefreshToken"),
        )

        val defaultResponse = LoginResponse(
            defaultToken,
        )

        return Result.success(defaultResponse)
    }
}
