package com.sawrose.toa.fake

import com.sawrose.toa.login.domain.model.Token
import com.sawrose.toa.login.repository.TokenRepository
import io.mockk.coVerify
import io.mockk.mockk

class FakeTokenRepository {

    val mock : TokenRepository = mockk(
        relaxUnitFun = true,
    )

    fun verifyTokenStored(token: Token){
        coVerify {
            mock.storeToken(token)
        }
    }

    fun verifyNoTokenStored(){
        coVerify(exactly = 0) {
            mock.storeToken(any())
        }
    }
}