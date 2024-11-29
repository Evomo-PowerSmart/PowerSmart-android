package com.evomo.powersmart.data.remote

import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.remote.api.LocationDataApiService
import com.evomo.powersmart.data.remote.response.HistoricalDataResponse
import com.evomo.powersmart.data.remote.response.LastHistoryResponse
import javax.inject.Inject

class LocationDataRepository @Inject constructor(
    private val locationDataApiService: LocationDataApiService,
) {

    suspend fun fetchLastHistory(location: String): Resource<List<LastHistoryResponse>> = try {
        val response = locationDataApiService.fetchLastHistory(location)
        Resource.Success(response)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Something went wrong!")
    }

    suspend fun fetchHistoricalData(
        location: String,
        startDate: String,
        endDate: String,
    ): Resource<List<HistoricalDataResponse>> = try {
        val response = locationDataApiService.fetchHistoricalData(location, startDate, endDate)
        Resource.Success(response)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Something went wrong!")
    }
}