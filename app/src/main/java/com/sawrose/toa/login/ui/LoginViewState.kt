package com.sawrose.toa.login.ui

import com.sawrose.toa.login.domain.model.Credentials

/**
 * A sealed class defining all possible states of our login Screen
 *
 * @property[Credentials] the current credential entered by user
 * @param[buttonEnabled] If true, the buttons on the login screen can accept clicks,
 * false otherwise.
 * */
sealed class LoginViewState(
    open val credentials: Credentials,
    open val buttonEnabled: Boolean = true,
) {
    /**
     * Initial state of the login screen when nothing input.
     */
    object Initial : LoginViewState(
        credentials = Credentials(),
    )

    /**
     * the state of screen as the user is entering login information
     */
    data class Active(
        override val credentials: Credentials,
    ) : LoginViewState(
        credentials = credentials
    )

    /**
     * the state of screen the user submits the login information
     */
    data class Submitting(
        override val credentials: Credentials
    ) : LoginViewState(
        credentials = credentials,
        buttonEnabled = false
    )

    /**
     * the state of screen when there is error while submitting
     */
    data class SubmissionError(
        override val credentials: Credentials,
        val errorMessage: String,
    ) : LoginViewState(
        credentials = credentials,
    )

    /**
     * The state of the screen when the user tries to submit with invalid inputs.
     */
    data class InputError(
        override val credentials: Credentials,
        val emailInputErrorMessage: String?,
        val passwordInputErrorMessage: String?,
    ) : LoginViewState(
        credentials = credentials
    )
}
