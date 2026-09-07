package com.example.rasoifood.di

import com.example.rasoifood.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = if (BuildConfig.DEBUG) {
        BuildConfig.DEBUG_BASE_URL
    } else {
        BuildConfig.RELEASE_BASE_URL
    }
}
