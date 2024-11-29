package com.evomo.powersmart.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoricalDataResponse(

	@field:SerializedName("apparent_energy_import")
	val apparentEnergyImport: Int,

	@field:SerializedName("reading_time")
	val readingTime: String,

	@field:SerializedName("active_energy_import")
	val activeEnergyImport: Long,

	@field:SerializedName("reactive_energy_export")
	val reactiveEnergyExport: Long,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("position")
	val position: String,

	@field:SerializedName("meter_type")
	val meterType: String,

	@field:SerializedName("reactive_energy_import")
	val reactiveEnergyImport: Long,

	@field:SerializedName("apparent_energy_export")
	val apparentEnergyExport: Long,

	@field:SerializedName("active_energy_export")
	val activeEnergyExport: Long,

	@field:SerializedName("meter_serial_number")
	val meterSerialNumber: Long
)
