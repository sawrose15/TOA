package com.sawrose.toa.login.ui

import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.login.domain.model.Credentials

/**
 * A sealed class defining all possible states of our login Screen
 *
 * @property[Credentials] the current credential entered by user
 * @param[inputEnabled] If true, the buttons on the login screen can accept clicks,
 * false otherwise.
 * */
sealed class LoginViewState(
    open val credentials: Credentials,
    open val inputEnabled: Boolean = true,
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
        val emailInputErrorMessage: UIText? = null,
        val passwordInputErrorMessage: UIText? = null,
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
        inputEnabled = false
    )

    /**
     * the state of screen when there is error while submitting
     */
    data class SubmissionError(
        override val credentials: Credentials,
        val errorMessage: UIText,
    ) : LoginViewState(
        credentials = credentials,
    )

    object Completed : LoginViewState(
        credentials = Credentials(),
        inputEnabled = false,
    )
}
