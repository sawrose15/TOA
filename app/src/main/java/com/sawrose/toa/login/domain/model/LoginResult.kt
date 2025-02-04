package com.sawrose.toa.login.domain.model

/**
 * A collection of possible results for an attempt to login the user.
 */
sealed class LoginResult {
    /**
     * The attempt to login was successful.
     */
    object Success : LoginResult()

    /**
     * This is the information about an unsuccessful attempt to login.
     */
    sealed class Failure : LoginResult() {
        /**
         * This will be returned if there was no account matching the requested credentials.
         */
        object InvalidCredentials : Failure()

        /**
         * This will be returned for any unknown exceptions when attempting to login.
         */
        object Unknown : Failure()

        /**
         * This will be returned when the user tries to login when either of the input fields
         * are left empty.
         */
        data class EmptyCredentials(
            val emptyEmail: Boolean,
            val emptyPassword: Boolean,
        ) : Failure()
    }
}
