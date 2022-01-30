package com.sawrose.toa.login.domain.model

@Suppress("UnusedPrivateMember")
@JvmInline
value class Email(val value: String)

@Suppress("UnusedPrivateMember")
@JvmInline
value class Password(val value: String)

data class Credentials(
    val email: Email,
    val password: Password,
)
