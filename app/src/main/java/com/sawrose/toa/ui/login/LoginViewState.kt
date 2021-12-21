package com.sawrose.toa.ui.login


/**
 * This defines the state of login Screen
 *
 * @param[username] the current text entered in the username field
 * @param[password] the current text entered in the password field
 * */
data class LoginViewState(
    val username: String,
    val password:String,
)