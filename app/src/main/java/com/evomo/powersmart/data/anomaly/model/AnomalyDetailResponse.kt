package com.evomo.powersmart.data.anomaly.model

import com.google.gson.annotations.SerializedName

data class AnomalyDetailResponseItem(

	@field:SerializedName("reading_time")
	val readingTime: String,

	@field:SerializedName("apparent_energy_import")
	val apparentEnergyImport: Double,  // Menggunakan tipe data Double

	@field:SerializedName("active_energy_import")
	val activeEnergyImport: Double,  // Menggunakan tipe data Double

	@field:SerializedName("reactive_energy_export")
	val reactiveEnergyExport: Double,  // Menggunakan tipe data Double

	@field:SerializedName("anomaly_type")
	val anomalyType: String,

	@field:SerializedName("predicted_energy")
	val predictedEnergy: Double,  // Menggunakan tipe data Double

	@field:SerializedName("position")
	val position: String,

	@field:SerializedName("meter_type")
	val meterType: String,

	@field:SerializedName("reactive_energy_import")
	val reactiveEnergyImport: Double,  // Menggunakan tipe data Double

	@field:SerializedName("apparent_energy_export")
	val apparentEnergyExport: Int,  // Jika ini adalah nilai integer

	@field:SerializedName("meter_serial_number")
	val meterSerialNumber: Int,

	@field:SerializedName("active_energy_export")
	val activeEnergyExport: Int
)

// Karena respons API adalah array langsung, kita bisa mendefinisikan model seperti ini:
typealias AnomalyDetailResponse = List<AnomalyDetailResponseItem>

