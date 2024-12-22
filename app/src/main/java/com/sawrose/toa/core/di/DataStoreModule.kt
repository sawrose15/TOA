package com.sawrose.toa.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.sawrose.toa.DataStoreToken
import com.sawrose.toa.core.data.local.preferences.tokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun provideTokenDataStore(
        @ApplicationContext appContext: Context,
    ): DataStore<DataStoreToken> = appContext.tokenDataStore

}
