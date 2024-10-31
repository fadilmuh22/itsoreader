package com.example.itsotest.ui.inputktp

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.itsotest.R
import com.example.itsotest.data.ResultState
import com.example.itsotest.databinding.ActivityInputKtpBinding
import com.example.itsotest.ui.ViewModelFactory
import org.json.JSONObject

class InputKtpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInputKtpBinding

    private val viewModel by viewModels<InputKtpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputKtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra(RESULT)
        result?.let {
            val jsonObject = JSONObject(it)

            // Mendapatkan nilai tempat lahir dan tanggal lahir dari JSON
            val tempatLahir = jsonObject.optString("tempatLahir", "")
            val tanggalLahir = jsonObject.optString("tanggalLahir", "")
            val tempatTanggalLahir = "$tempatLahir, $tanggalLahir"

            val alamat = jsonObject.optString("alamat", "")
            val kecamatan = jsonObject.optString("namaKecamatan", "")
            val alamatKecamatan = "$alamat, $kecamatan"

            // Mengisi EditText dengan data dari JSON
            binding.apply {
                nikEditText.setText(jsonObject.optString("nik", ""))
                namaEditText.setText(jsonObject.optString("namaLengkap", ""))
                tanggalLahirEditText.setText(tempatTanggalLahir)
                kelaminEditText.setText(jsonObject.optString("jenisKelamin", ""))
                alamatEditText.setText(alamatKecamatan)

                // Membuat EditText menjadi tidak bisa diubah (disabled)
                nikEditText.isEnabled = false
                namaEditText.isEnabled = false
                tanggalLahirEditText.isEnabled = false
                kelaminEditText.isEnabled = false
                alamatEditText.isEnabled = false

                // Dekode foto dari Base64 ke Bitmap dan set ke ImageView
                val fotoBase64 = jsonObject.optString("foto", "")
                if (fotoBase64.isNotEmpty()) {
                    val fotoBitmap = decodeBase64ToBitmap(fotoBase64)
                    fotoKtp.setImageBitmap(fotoBitmap)
                }

                viewModel.getPegawai().observe(this@InputKtpActivity) { result ->
                    if(result != null) {
                        when(result) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                // Mengambil daftar pegawai dari data response
                                val pegawai = result.data.data

                                // Konversi ke list string dengan format "$nama - $unitKerja"
                                val pegawaiList = pegawai?.map { "${it.nama} - ${it.unitKerja}" } ?: listOf()

                                // Mengatur adapter untuk dropdown
                                val arrayAdapter = ArrayAdapter(this@InputKtpActivity, R.layout.dropdown_item, pegawaiList)
                                binding.penerimaAutoComplete.setAdapter(arrayAdapter)

                                binding.penerimaAutoComplete.threshold = 1  // Set 1 agar pencarian mulai setelah satu karakter
                                showLoading(false)
                            }

                            is ResultState.Error -> {
                                showToast(result.error)
                                showLoading(false)
                            }
                        }
                    }
                }
            }



        } ?: run {
            Toast.makeText(this, "Data tidak berhasil di passing", Toast.LENGTH_SHORT).show()
        }
        binding.btnSave.setOnClickListener {
            result?.let { setupAction(it) }
        }


    }

    private fun setupAction(dataKtp : String) {
        val nomorHp = binding.nomorEditText.text.toString()
        val instansi = binding.instansiEditText.text.toString()
        val tujuan = binding.tujuanEditText.text.toString()


        if (!TextUtils.isEmpty(nomorHp) && !TextUtils.isEmpty(instansi) && !TextUtils.isEmpty(tujuan)) {
            try {
                // Konversi dataKtp ke JSONObject
                val jsonObject = JSONObject(dataKtp)

                // Tambahkan key-value baru
                jsonObject.put("nomorHp", nomorHp)
                jsonObject.put("instansi", instansi)
                jsonObject.put("tujuan", tujuan)

                // Konversi kembali ke string jika diperlukan
                val updatedDataKtp = jsonObject.toString()

                // Lakukan sesuatu dengan updatedDataKtp
                Log.d("UpdatedDataKtp", updatedDataKtp)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            showAlert(
                getString(R.string.judul_dialog),
                getString(R.string.pesan_dialog)
            ) { }
        }
    }

    private fun decodeBase64ToBitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val RESULT = "result"
        const val TAG = "InputKtpActivity"
    }
}