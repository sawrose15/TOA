package com.sawrose.toa.login.domain.model

sealed class LoginType {
    data class Credentials(
        val credentials: Credentials
    ) : LoginType()

    object Google : LoginType()
}
