package com.sawrose.toa.session

/**
 * Represents the current state of the user's session. Related to userLoginStatus.
 */

enum class SessionState {
    /**
     * The user's session state is unknown.
     */
    UNINSTANTIATED,

    /**
     * The user is logged in.
     */
    LOGGED_IN,

    /**
     * The user is logged out.
     */
    LOGGED_OUT,
}
