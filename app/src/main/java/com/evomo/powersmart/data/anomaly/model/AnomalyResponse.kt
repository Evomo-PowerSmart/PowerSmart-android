package com.evomo.powersmart.data.anomaly.model

import com.google.gson.annotations.SerializedName

data class AnomalyResponseItem(
	@field:SerializedName("reading_time")
	val readingTime: String,

	@field:SerializedName("anomaly_type")
	val anomalyType: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("position")
	val position: String
)

typealias AnomalyList = List<AnomalyResponseItem>

