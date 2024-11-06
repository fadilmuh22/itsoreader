package com.example.itsotest.ui.inputktp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
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
import com.example.itsotest.data.api.response.DataItem
import com.example.itsotest.data.service.RetryService
import com.example.itsotest.databinding.ActivityInputKtpBinding
import com.example.itsotest.ui.ViewModelFactory
import com.example.itsotest.ui.home.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject

class InputKtpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInputKtpBinding

    private var lastQuery: String? = null
    private var nip : String? = null
    private var pegawaiDataList: List<DataItem> = listOf()

    private val viewModel by viewModels<InputKtpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputKtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAutoCompleteListener()

        val result = intent.getStringExtra(RESULT)
        binding.penerimaAutoComplete.threshold = 1  // Set 1 agar pencarian mulai setelah satu karakter
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
            }



        } ?: run {
            Toast.makeText(this, "Data tidak berhasil di passing", Toast.LENGTH_SHORT).show()
        }
        binding.btnSave.setOnClickListener {
            result?.let { setupAction(it) }
        }

        binding.imageSearch.setOnClickListener {
            search()
        }


    }

    private fun setupAction(dataKtp: String) {
        val nomorHp = binding.nomorEditText.text.toString()
        val instansi = binding.instansiEditText.text.toString()
        val tujuan = binding.tujuanEditText.text.toString()
        val pegawai = binding.penerimaAutoComplete.text.toString()

        if (TextUtils.isEmpty(nip)) {
            Log.d(TAG, "Ini masuk NIP kosong")
            if (!TextUtils.isEmpty(instansi) && !TextUtils.isEmpty(tujuan) && !TextUtils.isEmpty(nomorHp)) {
                sendTamu(pegawai = pegawai, tujuan = tujuan, dataKtp = dataKtp, instansi = instansi, nomorHp = nomorHp)
            } else {
                showAlert(
                    getString(R.string.judul_dialog),
                    getString(R.string.pesan_dialog)
                ) { }
            }
        } else {
            Log.d(TAG, "Ini NIP ada isinya")
            if (!TextUtils.isEmpty(instansi) && !TextUtils.isEmpty(tujuan) && !TextUtils.isEmpty(nomorHp)) {
                nip?.let { sendTamu(pegawai = it, tujuan = tujuan, dataKtp = dataKtp, instansi = instansi, nomorHp = nomorHp) }
            } else {
                showAlert(
                    getString(R.string.judul_dialog),
                    getString(R.string.pesan_dialog)
                ) { }
            }
        }
    }


    private fun sendTamu(pegawai: String, dataKtp: String, instansi: String, tujuan: String, nomorHp: String) {
        try {
            // Convert dataKtp to JSONObject
            val jsonObject = JSONObject(dataKtp)

            // Add additional key-values
            jsonObject.put("asal_instansi", instansi)
            jsonObject.put("nomor_hp", nomorHp)
            jsonObject.put("tujuan_kunjungan", tujuan)

            // Add "penerima_tamu_nip" with a value or null based on pegawai
            if (pegawai.isNotEmpty()) {
                jsonObject.put("penerima_tamu_nip", pegawai)
            } else {
                jsonObject.put("penerima_tamu_nip", JSONObject.NULL) // Set to null if pegawai is empty
            }

            val updatedDataKtp = jsonObject.toString()

            // Continue with the updated JSON data
            viewModel.uploadTamu(updatedDataKtp).observe(this@InputKtpActivity) { result ->
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
                            setMessage("Jangan khawatir, data akan dikirim saat server kembali online dan pastikan koneksi internet terhubung")
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
                        viewModel.saveDataToPreferences(updatedDataKtp) // Save data if it fails
                        scheduleRetryService()
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
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
            viewModel.getPegawai(search).observe(this@InputKtpActivity) { result ->
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
                                val arrayAdapter = ArrayAdapter(this@InputKtpActivity, R.layout.dropdown_item, pegawaiList)
                                binding.penerimaAutoComplete.setAdapter(arrayAdapter)
                                binding.penerimaAutoComplete.showDropDown()
                            }
                            showLoading(false)

                            // Menghapus observer setelah mendapatkan data
                            viewModel.getPegawai(search).removeObservers(this@InputKtpActivity)
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

    private fun decodeBase64ToBitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        binding.blurOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun scheduleRetryService() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RetryService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val triggerTime = SystemClock.elapsedRealtime() + 3600000 // 1 jam dalam milidetik
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_HOUR,
            pendingIntent
        )
    }

    companion object {
        const val RESULT = "result"
        const val TAG = "InputKtpActivity"
    }
}