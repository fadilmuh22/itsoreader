package com.example.itsotest.di

import android.content.Context
import com.example.itsotest.data.api.config.ApiConfig
import com.example.itsotest.data.api.config.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}