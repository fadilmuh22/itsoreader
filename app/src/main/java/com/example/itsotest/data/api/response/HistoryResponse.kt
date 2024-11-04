package com.example.itsotest.data.api.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: ItemPaging? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ItemPaging(

	@field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: String? = null,

	@field:SerializedName("data")
	val data: List<TamuItem>? = null,

	@field:SerializedName("next_page_url")
	val nextPageUrl: Any? = null,

	@field:SerializedName("from")
	val from: Int? = null,

	@field:SerializedName("to")
	val to: Int? = null,

	@field:SerializedName("prev_page_url")
	val prevPageUrl: String? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class TamuItem(

	@field:SerializedName("penerima_tamu_nip")
	val penerimaTamuNip: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tujuan_kunjungan")
	val tujuanKunjungan: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("tempat_lahir")
	val tempatLahir: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("asal_instansi")
	val asalInstansi: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null,

	@field:SerializedName("nomor_hp")
	val nomorHp: String? = null,

	@field:SerializedName("penerima_tamu_nama")
	val penerimaPegawai : String? = null,

	@field:SerializedName("penerima_tamu_unitKerja")
	val penerimaUnitKerja : String? = null
)
