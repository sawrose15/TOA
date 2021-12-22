package com.sawrose.toa.login.domain.model

import com.sawrose.toa.login.domain.usecases.Email
import com.sawrose.toa.login.domain.usecases.Password

sealed class LoginType {
    data class Credentials(
        val email: Email,
        val password: Password,
    ) : LoginType()

    object Google : LoginType()
}
