package com.example.itsotest.ui.inputktp

import androidx.lifecycle.ViewModel
import com.example.itsotest.data.UserRepository

class InputKtpViewModel(private val repository : UserRepository) : ViewModel() {
    fun getPegawai() = repository.getPegawai()
}