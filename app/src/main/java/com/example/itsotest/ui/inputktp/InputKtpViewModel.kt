package com.example.itsotest.ui.inputktp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itsotest.data.UserRepository
import com.example.itsotest.data.pref.TamuModel
import kotlinx.coroutines.launch
import org.json.JSONObject

class InputKtpViewModel(private val repository : UserRepository) : ViewModel() {
    fun getPegawai(search : String) = repository.getPegawai(search)

    fun uploadTamu(upload : String) = repository.uploadTamu(upload)

    fun saveDataToPreferences(data: String) = viewModelScope.launch {
        val tamu = parseDataToTamuModel(data)
        repository.saveSession(tamu)
    }

    private fun parseDataToTamuModel(data: String) :TamuModel {
        val jsonObject = JSONObject(data)
        val nik = jsonObject.optString("nik", "")
        val namaLengkap = jsonObject.optString("namaLengkap", "")
        val jenisKelamin = jsonObject.optString("jenisKelamin", "")
        val tempatLahir = jsonObject.optString("tempatLahir", "")
        val tanggalLahir = jsonObject.optString("tanggalLahir", "")
        val agama = jsonObject.optString("agama", "")
        val statusKawin = jsonObject.optString("statusKawin", "")
        val jenisPekerjaan = jsonObject.optString("jenisPekerjaan", "")
        val namaProvinsi = jsonObject.optString("namaProvinsi", "")
        val namaKabupaten = jsonObject.optString("namaKabupaten", "")
        val namaKecamatan = jsonObject.optString("namaKecamatan", "")
        val namaKelurahan = jsonObject.optString("namaKelurahan", "")
        val alamat = jsonObject.optString("alamat", "")
        val nomorRt = jsonObject.optString("nomorRt", "")
        val nomorRw = jsonObject.optString("nomorRw", "")
        val berlakuHingga = jsonObject.optString("berlakuHingga", "")
        val golonganDarah = jsonObject.optString("golonganDarah", "")
        val kewarganegaraan = jsonObject.optString("kewarganegaraan", "")
        val foto = jsonObject.optString("foto", "")
        val asalInstansi = jsonObject.optString("asal_instansi", "")
        val nomorHp = jsonObject.optString("nomor_hp", "")
        val tujuanKunjungan = jsonObject.optString("tujuan_kunjungan", "")
        val penerimaTamuNip = jsonObject.optString("penerima_tamu_nip", "")


        return TamuModel(
            nik = nik,
            namaLengkap = namaLengkap,
            jenisKelamin = jenisKelamin,
            tempatLahir = tempatLahir,
            tanggalLahir = tanggalLahir,
            agama = agama,
            statusKawin = statusKawin,
            jenisPekerjaan = jenisPekerjaan,
            namaProvinsi = namaProvinsi,
            namaKabupaten = namaKabupaten,
            namaKecamatan = namaKecamatan,
            namaKelurahan = namaKelurahan,
            alamat = alamat,
            nomorRt = nomorRt,
            nomorRw = nomorRw,
            berlakuHingga = berlakuHingga,
            golonganDarah = golonganDarah,
            kewarganegaraan = kewarganegaraan,
            foto = foto,
            asalInstansi = asalInstansi,
            tujuanKunjungan = tujuanKunjungan,
            penerimaTamuNip = penerimaTamuNip,
            nomorHp = nomorHp
        )
    }
}