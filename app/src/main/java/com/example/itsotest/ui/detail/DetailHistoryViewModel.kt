package com.example.itsotest.ui.detail

import androidx.lifecycle.ViewModel
import com.example.itsotest.data.UserRepository

class DetailHistoryViewModel(private val repository : UserRepository) : ViewModel()  {
    fun getPegawai(search : String) = repository.getPegawai(search)

    fun updatePenerima(id : Int, penerima : String) = repository.updatePenerima(id, penerima)
}