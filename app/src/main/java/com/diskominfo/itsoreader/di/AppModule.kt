package com.diskominfo.itsoreader.di

import com.diskominfo.itsoreader.data.remote.ApiConfig
import com.diskominfo.itsoreader.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }
}