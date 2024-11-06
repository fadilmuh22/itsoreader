package com.example.itsotest.di

import android.content.Context
import com.example.itsotest.data.api.config.ApiConfig
import com.example.itsotest.data.UserRepository
import com.example.itsotest.data.pref.TamuPreference
import com.example.itsotest.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = TamuPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }
}