package com.sawrose.toa.login.domain.repository

import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.LoginResponse

interface LoginRepository {
    /**
     * given some user [credentials], try to login the user
     *
     * @return a [Result] that contain [LoginResponse] if successful or error otherwise
     */
    suspend fun login(
        credentials: Credentials
    ): Result<LoginResponse>
}
