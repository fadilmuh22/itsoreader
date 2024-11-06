package com.example.itsotest.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class TamuPreference(private val dataStore: DataStore<Preferences>) {

    suspend fun saveTamuData(tamu: TamuModel) {
        dataStore.edit { preferences ->
            preferences[NIK_KEY] = tamu.nik
            preferences[NAME_KEY] = tamu.namaLengkap
            preferences[JENIS_KELAMIN_KEY] = tamu.jenisKelamin
            preferences[TEMPAT_LAHIR_KEY] = tamu.tempatLahir
            preferences[TANGGAL_LAHIR_KEY] = tamu.tanggalLahir
            preferences[AGAMA_KEY] = tamu.agama
            preferences[STATUS_KAWIN_KEY] = tamu.statusKawin
            preferences[JENIS_PEKERJAAN_KEY] = tamu.jenisPekerjaan
            preferences[NAMA_PROVINSI_KEY] = tamu.namaProvinsi
            preferences[NAMA_KABUPATEN_KEY] = tamu.namaKabupaten
            preferences[NAMA_KECAMATAN_KEY] = tamu.namaKecamatan
            preferences[NAMA_KELURAHAN_KEY] = tamu.namaKelurahan
            preferences[ALAMAT_KEY] = tamu.alamat
            preferences[NOMOR_RT_KEY] = tamu.nomorRt
            preferences[NOMOR_RW_KEY] = tamu.nomorRw
            preferences[BERLAKU_HINGGA_KEY] = tamu.berlakuHingga
            preferences[GOLONGAN_DARAH_KEY] = tamu.golonganDarah
            preferences[KEWARGANEGARAAN_KEY] = tamu.kewarganegaraan
            preferences[FOTO_KEY] = tamu.foto
            preferences[ASAL_INSTANSI_KEY] = tamu.asalInstansi
            preferences[TUJUAN_KUNJUNGAN_KEY] = tamu.tujuanKunjungan
            preferences[PENERIMA_TAMU_NIP_KEY] = tamu.penerimaTamuNip
            preferences[NOMOR_HP_KEY] = tamu.nomorHp
            preferences[IS_SEND_KEY] = tamu.isSend
        }
    }

    suspend fun getTamuData(): TamuModel {
        val preferences = dataStore.data.first()
        return TamuModel(
            nik = preferences[NIK_KEY] ?: "",
            namaLengkap = preferences[NAME_KEY] ?: "",
            jenisKelamin = preferences[JENIS_KELAMIN_KEY] ?: "",
            tempatLahir = preferences[TEMPAT_LAHIR_KEY] ?: "",
            tanggalLahir = preferences[TANGGAL_LAHIR_KEY] ?: "",
            agama = preferences[AGAMA_KEY] ?: "",
            statusKawin = preferences[STATUS_KAWIN_KEY] ?: "",
            jenisPekerjaan = preferences[JENIS_PEKERJAAN_KEY] ?: "",
            namaProvinsi = preferences[NAMA_PROVINSI_KEY] ?: "",
            namaKabupaten = preferences[NAMA_KABUPATEN_KEY] ?: "",
            namaKecamatan = preferences[NAMA_KECAMATAN_KEY] ?: "",
            namaKelurahan = preferences[NAMA_KELURAHAN_KEY] ?: "",
            alamat = preferences[ALAMAT_KEY] ?: "",
            nomorRt = preferences[NOMOR_RT_KEY] ?: "",
            nomorRw = preferences[NOMOR_RW_KEY] ?: "",
            berlakuHingga = preferences[BERLAKU_HINGGA_KEY] ?: "",
            golonganDarah = preferences[GOLONGAN_DARAH_KEY] ?: "",
            kewarganegaraan = preferences[KEWARGANEGARAAN_KEY] ?: "",
            foto = preferences[FOTO_KEY] ?: "",
            asalInstansi = preferences[ASAL_INSTANSI_KEY] ?: "",
            tujuanKunjungan = preferences[TUJUAN_KUNJUNGAN_KEY] ?: "",
            penerimaTamuNip = preferences[PENERIMA_TAMU_NIP_KEY] ?: "",
            nomorHp = preferences[NOMOR_HP_KEY] ?: "",
            isSend = preferences[IS_SEND_KEY] ?: false
        )
    }



    companion object {
        @Volatile
        private var INSTANCE: TamuPreference? = null

        // Definisikan key untuk setiap atribut di TamuModel
        private val NIK_KEY = stringPreferencesKey("nik")
        private val NAME_KEY = stringPreferencesKey("name")
        private val JENIS_KELAMIN_KEY = stringPreferencesKey("jenisKelamin")
        private val TEMPAT_LAHIR_KEY = stringPreferencesKey("tempatLahir")
        private val TANGGAL_LAHIR_KEY = stringPreferencesKey("tanggalLahir")
        private val AGAMA_KEY = stringPreferencesKey("agama")
        private val STATUS_KAWIN_KEY = stringPreferencesKey("statusKawin")
        private val JENIS_PEKERJAAN_KEY = stringPreferencesKey("jenisPekerjaan")
        private val NAMA_PROVINSI_KEY = stringPreferencesKey("namaProvinsi")
        private val NAMA_KABUPATEN_KEY = stringPreferencesKey("namaKabupaten")
        private val NAMA_KECAMATAN_KEY = stringPreferencesKey("namaKecamatan")
        private val NAMA_KELURAHAN_KEY = stringPreferencesKey("namaKelurahan")
        private val ALAMAT_KEY = stringPreferencesKey("alamat")
        private val NOMOR_RT_KEY = stringPreferencesKey("nomorRt")
        private val NOMOR_RW_KEY = stringPreferencesKey("nomorRw")
        private val BERLAKU_HINGGA_KEY = stringPreferencesKey("berlakuHingga")
        private val GOLONGAN_DARAH_KEY = stringPreferencesKey("golonganDarah")
        private val KEWARGANEGARAAN_KEY = stringPreferencesKey("kewarganegaraan")
        private val FOTO_KEY = stringPreferencesKey("foto")
        private val NOMOR_HP_KEY = stringPreferencesKey("nomorHp")
        private val ASAL_INSTANSI_KEY = stringPreferencesKey("asalInstansi")
        private val TUJUAN_KUNJUNGAN_KEY = stringPreferencesKey("tujuanKunjungan")
        private val PENERIMA_TAMU_NIP_KEY = stringPreferencesKey("penerimaTamuNip")
        private val IS_SEND_KEY = booleanPreferencesKey("isSend")

        fun getInstance(dataStore: DataStore<Preferences>): TamuPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = TamuPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}