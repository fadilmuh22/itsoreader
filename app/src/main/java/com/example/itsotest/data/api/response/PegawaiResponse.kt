package com.example.itsotest.data.api.response

import com.google.gson.annotations.SerializedName

data class PegawaiResponse(
	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("unitKerja")
	val unitKerja: String? = null,

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null
)
