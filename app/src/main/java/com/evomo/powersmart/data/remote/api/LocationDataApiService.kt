package com.evomo.powersmart.data.remote.api

import com.evomo.powersmart.data.remote.response.HistoricalDataResponse
import com.evomo.powersmart.data.remote.response.LastHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationDataApiService {

    @GET("api/fetch_data/{location}/last_history")
    suspend fun fetchLastHistory(
        @Path("location") location: String,
    ): List<LastHistoryResponse>

    @GET("api/fetch_data/{location}/{startdate}&{enddate}")
    suspend fun fetchHistoricalData(
        @Path("location") location: String,
        @Path("startdate") startDate: String,
        @Path("enddate") endDate: String,
    ): List<HistoricalDataResponse>
}