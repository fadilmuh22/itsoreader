package com.example.itsotest.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.itsotest.data.UserRepository
import com.example.itsotest.data.api.response.TamuItem

class HistoryViewModel(private val repository : UserRepository) : ViewModel() {

    val quote: LiveData<PagingData<TamuItem>> =
        repository.getTamu().cachedIn(viewModelScope)
}