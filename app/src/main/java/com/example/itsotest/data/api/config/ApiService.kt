package com.example.itsotest.data.api.config

import com.example.itsotest.data.api.response.PegawaiResponse
import retrofit2.http.GET

interface ApiService {
    @GET("pegawai")
    suspend fun getPegawai() : PegawaiResponse
}