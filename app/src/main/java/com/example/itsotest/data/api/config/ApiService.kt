package com.example.itsotest.data.api.config

import com.example.itsotest.data.api.response.HistoryResponse
import com.example.itsotest.data.api.response.PegawaiResponse
import com.example.itsotest.data.api.response.UploadResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("pegawai")
    suspend fun getPegawai(
        @Query("search") search : String
    ) : PegawaiResponse

    @FormUrlEncoded
    @POST("ektp/store")
    suspend fun uploadTamu(
        @Field("type") type: String,
        @Field("nik") nik: String,
        @Field("namaLengkap") namaLengkap: String,
        @Field("jenisKelamin") jenisKelamin: String,
        @Field("tempatLahir") tempatLahir: String,
        @Field("tanggalLahir") tanggalLahir: String,
        @Field("agama") agama: String,
        @Field("statusKawin") statusKawin: String,
        @Field("jenisPekerjaan") jenisPekerjaan: String,
        @Field("namaProvinsi") namaProvinsi: String,
        @Field("namaKabupaten") namaKabupaten: String,
        @Field("namaKecamatan") namaKecamatan: String,
        @Field("namaKelurahan") namaKelurahan: String,
        @Field("alamat") alamat: String,
        @Field("nomorRt") nomorRt: String,
        @Field("nomorRw") nomorRw: String,
        @Field("berlakuHingga") berlakuHingga: String,
        @Field("golonganDarah") golonganDarah: String,
        @Field("kewarganegaraan") kewarganegaraan: String,
        @Field("foto") foto: String,
        @Field("ttd") ttd: String,
        @Field("fingerAuth") fingerAuth: String,
        @Field("index1") index1: String,
        @Field("index2") index2: String,
        @Field("tid") tid: String?,
        @Field("nomor_hp") nomorHp: String,
        @Field("asal_instansi") asalInstansi: String,
        @Field("tujuan_kunjungan") tujuanKunjungan: String,
        @Field("penerima_tamu_nip") penerimaTamuNip: String
    ): UploadResponse

    @GET("history-tamu")
    suspend fun histoyTamu(
        @Query("page") page : Int,
        @Query("per_page") size : Int
    ) : HistoryResponse

    @GET("history-tamu")
    suspend fun searchHistoryTamu(
        @Query("page") page : Int,
        @Query("per_page") size : Int,
        @Query("search") search : String
    ) : HistoryResponse

}