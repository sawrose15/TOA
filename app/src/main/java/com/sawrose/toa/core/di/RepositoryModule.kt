package com.sawrose.toa.core.di

import com.sawrose.toa.core.data.local.RoomTaskRepository
import com.sawrose.toa.core.repository.TaskRepository
import com.sawrose.toa.login.domain.repository.DataStoreTokenRepository
import com.sawrose.toa.login.domain.repository.DemoLoginRepository
import com.sawrose.toa.login.domain.repository.LoginRepository
import com.sawrose.toa.login.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module is responsible for defining the creation of any repository dependencies used in the
 * application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTokenRepository(
        tokenRepository: DataStoreTokenRepository,
    ): TokenRepository

    @Binds
    abstract fun bindLoginRepository(
        loginRepository: DemoLoginRepository,
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepository: RoomTaskRepository,
    ): TaskRepository
}
