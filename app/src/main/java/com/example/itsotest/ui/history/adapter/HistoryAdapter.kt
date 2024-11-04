package com.example.itsotest.ui.history.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.itsotest.data.api.response.TamuItem
import com.example.itsotest.databinding.ItemTipsBinding

class HistoryAdapter(private val onItemClicked: (String) -> Unit) :
    PagingDataAdapter<TamuItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    // Set untuk menyimpan posisi item yang diperluas
    private val expandedPositionSet = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { holder.bind(it, position) }

        holder.itemView.setOnClickListener {
            val nama = data?.nama
            nama?.let {
                onItemClicked(it)
            } ?: run {
                onItemClicked("Kosong")
            }
            Log.d("Adapter", "Pilih ${data?.nama}")
        }
    }

    inner class MyViewHolder(private val binding: ItemTipsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TamuItem, position: Int) {
            // Set data di tampilan header
            binding.tvTipsTitle.text = data.nama
            binding.tvAsal.text = data.asalInstansi
            binding.tvPenerimaTamu.text = data.penerimaPegawai

            // Set gambar jika ada
            if (!data.foto.isNullOrEmpty() && isBase64(data.foto)) {
                val fotoBitmap = decodeBase64ToBitmap(data.foto)
                binding.ivTips.setImageBitmap(fotoBitmap)
            } else {
                Log.e("MyViewHolder", "Invalid Base64 string for foto: ${data.foto}")
            }

        }

        private fun decodeBase64ToBitmap(base64Str: String): Bitmap {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }

        private fun isBase64(str: String): Boolean {
            return try {
                Base64.decode(str, Base64.DEFAULT)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TamuItem>() {
            override fun areItemsTheSame(oldItem: TamuItem, newItem: TamuItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TamuItem, newItem: TamuItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
