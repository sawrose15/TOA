package com.sawrose.toa.session

import com.sawrose.toa.login.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A use case that determines if the user is logged in.
 * */
class UserLoggedInUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    fun isUserLoggedIn(): Flow<Boolean> {
        return tokenRepository
            .observeToken()
            .map { token ->
                token != null
            }
    }
}
