package com.sawrose.toa.login.repository
/**
 * This repository is responsible for storing and fetching auth Token
 */
interface AuthTokenRepository {

    /**
     * Given [authToken], store this in memory to retrieve it for later
     */
    suspend fun storeAuthToken(
        authToken: String
    )

    /**
     * fetches the auth token of the signed is user, if there is one
     *
     * @return the auth token or null if not found
     */

    suspend fun fetchAuthToken(): String?
}
