package com.sawrose.toa.login.ui
/**
 * This defines the state of login Screen
 *
 * @param[email] the current text entered in the email field
 * @param[password] the current text entered in the password field
 * */
data class LoginViewState(
    val email: String,
    val password: String,
)
