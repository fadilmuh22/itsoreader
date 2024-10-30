package com.example.itsotest.data.api.config

class UserRepository private constructor(
    private val apiService: ApiService) {


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}