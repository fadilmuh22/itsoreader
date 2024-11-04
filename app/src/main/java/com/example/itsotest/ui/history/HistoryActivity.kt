package com.example.itsotest.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itsotest.R
import com.example.itsotest.databinding.ActivityHistoryBinding
import com.example.itsotest.databinding.ActivityInputKtpBinding
import com.example.itsotest.ui.ViewModelFactory
import com.example.itsotest.ui.history.adapter.HistoryAdapter
import com.example.itsotest.ui.history.adapter.LoadingStateAdapter
import com.example.itsotest.ui.inputktp.InputKtpViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding

    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvList.layoutManager = LinearLayoutManager(this)

        getData()
    }

    private fun getData() {
        showLoading(true) // Menampilkan indikator loading

        val adapter = HistoryAdapter { selectedValue ->
            Log.d("HistoryActivity", "masuk $selectedValue")
        }

        binding.rvList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showLoading(true) // Masih loading
            } else {
                showLoading(false) // Selesai loading
            }
        }

        viewModel.quote.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}