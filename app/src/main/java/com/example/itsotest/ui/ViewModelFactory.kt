package com.example.itsotest.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itsotest.data.UserRepository
import com.example.itsotest.di.Injection
import com.example.itsotest.ui.detail.DetailHistoryViewModel
import com.example.itsotest.ui.history.HistoryViewModel
import com.example.itsotest.ui.inputktp.InputKtpViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(InputKtpViewModel::class.java) -> {
                InputKtpViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailHistoryViewModel::class.java) -> {
                DetailHistoryViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}