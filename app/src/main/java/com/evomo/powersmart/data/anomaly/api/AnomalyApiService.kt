package com.evomo.powersmart.data.anomaly.api

import com.evomo.powersmart.data.anomaly.model.AnomalyDetailResponse
import com.evomo.powersmart.data.anomaly.model.AnomalyResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AnomalyApiService {
    @GET("/api/fetch_data/anomaly")
    fun getAnomalies(): Call<List<AnomalyResponseItem>>

    @GET("/api/fetch_data/anomaly/{id}")
    fun getAnomalyDetails(@Path("id") id: Int): Call<AnomalyDetailResponse>
}