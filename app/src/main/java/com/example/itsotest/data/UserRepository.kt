package com.example.itsotest.data

import androidx.lifecycle.liveData
import com.example.itsotest.data.api.config.ApiService
import com.example.itsotest.data.api.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService
) {
    fun getPegawai() = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = apiService.getPegawai()
            emit(ResultState.Success(successResponse))
        } catch (e : HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message?: "Unknown error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}