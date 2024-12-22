package com.sawrose.toa.core.di

import com.sawrose.toa.preferences.AndroidPreferences
import com.sawrose.toa.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {
    @Binds
    abstract fun bindPreferences(
        preferences: AndroidPreferences,
    ): Preferences
}
