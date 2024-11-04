package com.example.itsotest.ui.inputktp

import androidx.lifecycle.ViewModel
import com.example.itsotest.data.UserRepository
import org.json.JSONObject

class InputKtpViewModel(private val repository : UserRepository) : ViewModel() {
    fun getPegawai(search : String) = repository.getPegawai(search)

    fun uploadTamu(upload : String) = repository.uploadTamu(upload)
}