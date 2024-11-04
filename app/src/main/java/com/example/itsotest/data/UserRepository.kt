package com.example.itsotest.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.itsotest.data.api.config.ApiService
import com.example.itsotest.data.api.response.ErrorResponse
import com.example.itsotest.data.api.response.TamuItem
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService
) {
    fun getPegawai(search : String) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = apiService.getPegawai(search)
            emit(ResultState.Success(successResponse))
        } catch (e : HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message?: "Unknown error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    fun uploadTamu(tamu : String) = liveData {
        emit(ResultState.Loading)

        val jsonObject = JSONObject(tamu)
        val type = jsonObject.optString("type", "")
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
        val ttd = jsonObject.optString("ttd", "")
        val fingerAuth = jsonObject.optString("fingerAuth", "")
        val index1 = jsonObject.optString("index1", "")
        val index2 = jsonObject.optString("index2", "")
        val tid = jsonObject.optString("tid", "")
        val nomorHp = jsonObject.optString("nomor_hp", "")
        val asalInstansi = jsonObject.optString("asal_instansi", "")
        val tujuanKunjungan = jsonObject.optString("tujuan_kunjungan", "")
        val penerimaTamuNip = jsonObject.optString("penerima_tamu_nip", "")
        try {
            val successResponse = apiService.uploadTamu(
                type = type,
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
                ttd = ttd,
                fingerAuth = fingerAuth,
                index1 = index1,
                index2 = index2,
                tid = tid,
                nomorHp = nomorHp,
                asalInstansi = asalInstansi,
                tujuanKunjungan = tujuanKunjungan,
                penerimaTamuNip = penerimaTamuNip
            )
            Log.d("Repository", "Isi dari error ${successResponse.status}")
            emit(ResultState.Success(successResponse))
        } catch (e : HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.d("Repository", "Isi dari error ${errorResponse.message}")
            emit(ResultState.Error(errorResponse.message?: "Unknown error"))
        } catch (e : Exception) {
            Log.d("Repository", "Isi dari error ${e.message}")
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getTamu(): LiveData<PagingData<TamuItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,               // Jumlah item per halaman
                initialLoadSize = 5,        // Jumlah item yang dimuat pertama kali
                enablePlaceholders = false, // Disarankan nonaktif jika data dinamis
            ),
            pagingSourceFactory = {
                TamuPagingSource(apiService, null)
            }
        ).liveData
    }

    fun getSearchTamu(search : String): LiveData<PagingData<TamuItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,               // Jumlah item per halaman
                initialLoadSize = 5,        // Jumlah item yang dimuat pertama kali
                enablePlaceholders = false, // Disarankan nonaktif jika data dinamis
            ),
            pagingSourceFactory = {
                TamuPagingSource(apiService, search)
            }
        ).liveData
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}