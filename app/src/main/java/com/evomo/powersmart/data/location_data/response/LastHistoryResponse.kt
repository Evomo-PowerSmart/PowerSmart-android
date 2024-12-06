package com.evomo.powersmart.data.location_data.response

import com.google.gson.annotations.SerializedName

data class LastHistoryResponse(

	@field:SerializedName("apparent_energy_import")
	val apparentEnergyImport: Long,

	@field:SerializedName("reading_time")
	val readingTime: String,

	@field:SerializedName("active_energy_import")
	val activeEnergyImport: Long,

	@field:SerializedName("reactive_energy_export")
	val reactiveEnergyExport: Long,

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
	val meterSerialNumber: String,
)
