package com.example.itsotest.ui.detail

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.itsotest.R
import com.example.itsotest.data.ResultState
import com.example.itsotest.data.api.response.DataItem
import com.example.itsotest.data.api.response.TamuItem
import com.example.itsotest.databinding.ActivityDetailHistoryBinding
import com.example.itsotest.ui.ViewModelFactory
import com.example.itsotest.ui.home.HomeActivity
import com.example.itsotest.ui.inputktp.InputKtpActivity.Companion.TAG
import com.example.itsotest.ui.inputktp.InputKtpViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailHistoryBinding

    private var lastQuery: String? = null
    private var pegawaiDataList: List<DataItem> = listOf()
    private var nip : String? = null

    private val viewModel by viewModels<DetailHistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tamuItem: TamuItem? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(HISTORY, TamuItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(HISTORY)
        }
        setupAutoCompleteListener()
        tamuItem?.let {
            binding.apply {
                instansiEditText.setText(it.asalInstansi)
                tujuanEditText.setText(it.tujuanKunjungan)
                nomorEditText.setText(it.nomorHp)

                instansiEditText.isEnabled = false
                tujuanEditText.isEnabled = false
                nomorEditText.isEnabled = false

                val fotoBase64 = it.foto
                if (fotoBase64!!.isNotEmpty() && it.foto != "-") {
                    val fotoBitmap = decodeBase64ToBitmap(fotoBase64)
                    fotoKtp.setImageBitmap(fotoBitmap)
                }

            }


        }

        binding.imageSearch.setOnClickListener {
            search()
        }

        binding.btnSave.setOnClickListener {
            if (tamuItem != null) {
                tamuItem.id?.let { id -> setupSend(id) }
            }
        }


    }

    private fun search() {
        val search = binding.penerimaAutoComplete.text.toString()

        // Cek jika kata kunci sama dengan pencarian terakhir, hindari pencarian ulang
        if (search == lastQuery) {
            return
        }
        lastQuery = search

        if (!TextUtils.isEmpty(search)) {
            viewModel.getPegawai(search).observe(this@DetailHistoryActivity) { result ->
                if(result != null) {
                    when(result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            // Simpan data asli dari API
                            pegawaiDataList = result.data.data ?: listOf()

                            // Konversi ke list string dengan format "$nama - $unitKerja"
                            val pegawaiList = pegawaiDataList.map { "${it.nama} - ${it.unitKerja}" }

                            if (pegawaiList.isEmpty()) {
                                showToast("Tidak ada hasil ditemukan")
                            } else {
                                val arrayAdapter = ArrayAdapter(this@DetailHistoryActivity, R.layout.dropdown_item, pegawaiList)
                                binding.penerimaAutoComplete.setAdapter(arrayAdapter)
                                binding.penerimaAutoComplete.showDropDown()
                            }
                            showLoading(false)

                            // Menghapus observer setelah mendapatkan data
                            viewModel.getPegawai(search).removeObservers(this@DetailHistoryActivity)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        } else {
            showAlert(
                getString(R.string.judul_search),
                getString(R.string.pesan_search)
            ) { }
        }
    }

    private fun setupSend(dataId : Int) {
        val pegawai = binding.penerimaAutoComplete.text.toString()

        if (TextUtils.isEmpty(nip)) {
            Log.d(TAG, "Ini masuk NIP kosong")
            if (!TextUtils.isEmpty(pegawai)) {
                sendPenerima(pegawai = pegawai, id = dataId)
            } else {
                showAlert(
                    getString(R.string.judul_dialog),
                    getString(R.string.pesan_dialog)
                ) { }
            }
        } else {
            Log.d(TAG, "Ini NIP ada isinya")
            nip?.let { sendPenerima(pegawai = it, id = dataId) }
        }
    }

    private fun sendPenerima(pegawai : String, id : Int) {
        try {
            viewModel.updatePenerima(id = id, penerima = pegawai).observe(this@DetailHistoryActivity) { result ->
                when (result) {
                    is ResultState.Loading -> showLoading(true)

                    is ResultState.Success -> {
                        MaterialAlertDialogBuilder(this).apply {
                            setTitle("Data Berhasil dikirim")
                            setMessage("Data Tamu Sudah Terkirim")
                            setPositiveButton("Lanjut") { _, _ ->
                                val intent = Intent(context, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        MaterialAlertDialogBuilder(this).apply {
                            setTitle("Data Tidak Berhasil Dikirim")
                            setMessage("Pastikan anda terkoneksi internet")
                            setPositiveButton("Lanjut") { _, _ ->
                                val intent = Intent(context, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                        showLoading(false)
                    }
                }
            }
        }  catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupAutoCompleteListener() {
        binding.penerimaAutoComplete.setOnItemClickListener { _, _, position, _ ->
            // Ambil item DataItem berdasarkan posisi yang dipilih
            val selectedPegawai = pegawaiDataList[position]

            // Simpan nip dari item yang dipilih ke variabel nip
            nip = selectedPegawai.nip ?: ""

            // Tampilkan nip untuk memastikan sudah tersimpan
            Toast.makeText(this, "NIP yang dipilih: $nip", Toast.LENGTH_SHORT).show()
        }
    }

    private fun decodeBase64ToBitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.blurOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showAlert(
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                positiveAction.invoke(dialog)
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val HISTORY = "history"
    }
}