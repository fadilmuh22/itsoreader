package com.example.itsotest.ui.history.adapter

import android.content.Intent
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
import com.example.itsotest.ui.detail.DetailHistoryActivity
import com.example.itsotest.ui.inputktp.InputKtpActivity
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private val onItemClicked: (String) -> Unit) :
    PagingDataAdapter<TamuItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { holder.bind(it, position) }

        holder.itemView.setOnClickListener {
            data?.let { item ->
                val context = holder.itemView.context
                val intent = Intent(context, DetailHistoryActivity::class.java).apply {
                    putExtra(DetailHistoryActivity.HISTORY, item)
                }
                context.startActivity(intent)
                Log.d("Adapter", "Pilih ${item.nama}")
            }
        }
    }

    inner class MyViewHolder(private val binding: ItemTipsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TamuItem, position: Int) {
            // Set data di tampilan header
            binding.tvTipsTitle.text = data.nama
            binding.tvAsal.text = data.asalInstansi
            binding.tvPenerimaTamu.text = data.tujuanKunjungan
            binding.tvWaktuKedatangan.text = data.penerimaPegawai
            val newFormat = data.createdAt?.let { formatDate(it) }
            binding.tvAsalInstansi.text = newFormat.toString()


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

        // Fungsi untuk mengubah format tanggal
        private fun formatDate(dateStr: String): String {
            return try {
                // Debugging: print original string
                Log.d("DateFormatter", "Original date string: $dateStr")

                // Attempt to parse the date using the correct format
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
                val date = inputFormat.parse(dateStr)

                // Debugging: print parsed date
                Log.d("DateFormatter", "Parsed date: $date")

                // If the date is successfully parsed, format it in the desired format
                date?.let {
                    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
                    val formattedDate = outputFormat.format(it)

                    // Debugging: print formatted date
                    Log.d("DateFormatter", "Formatted date: $formattedDate")
                    return formattedDate
                } ?: run {
                    Log.e("DateFormatter", "Date parsing failed.")
                    return dateStr // Return the original string if parsing fails
                }
            } catch (e: Exception) {
                // Catching any exception and returning the original date string
                Log.e("DateFormatter", "Error parsing date: $e")
                return dateStr
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
