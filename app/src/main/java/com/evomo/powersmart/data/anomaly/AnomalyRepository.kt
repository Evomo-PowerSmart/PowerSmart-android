package com.evomo.powersmart.data.anomaly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.evomo.powersmart.data.anomaly.api.AnomalyApiService
import com.evomo.powersmart.data.anomaly.model.AnomalyDetailResponseItem
import com.evomo.powersmart.data.anomaly.model.AnomalyResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class AnomalyRepository @Inject constructor(
    private val anomalyApiService: AnomalyApiService
) {

    fun getAnomalies(): LiveData<List<AnomalyResponseItem>> {
        val liveData = MutableLiveData<List<AnomalyResponseItem>>()

        anomalyApiService.getAnomalies().enqueue(object : Callback<List<AnomalyResponseItem>> {
            override fun onResponse(call: Call<List<AnomalyResponseItem>>, response: Response<List<AnomalyResponseItem>>) {
                if (response.isSuccessful) {
                    val anomalies = response.body() ?: emptyList()
                    Timber.d("Fetched anomalies: $anomalies")
                    liveData.postValue(anomalies)
                } else {
                    Timber.e("Error fetching anomalies: ${response.message()}")
                    liveData.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<AnomalyResponseItem>>, t: Throwable) {
                Timber.e(t, "Error fetching anomalies")
                liveData.postValue(emptyList())
            }
        })

        return liveData
    }


    fun getAnomalyDetail(id: Int): LiveData<List<AnomalyDetailResponseItem>> {
        val liveData = MutableLiveData<List<AnomalyDetailResponseItem>>()

        anomalyApiService.getAnomalyDetails(id).enqueue(object : Callback<List<AnomalyDetailResponseItem>> {
            override fun onResponse(call: Call<List<AnomalyDetailResponseItem>>, response: Response<List<AnomalyDetailResponseItem>>) {
                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                    Timber.d("Fetched anomaly details: ${response.body()}")
                } else {
                    Timber.e("Error fetching anomaly detail: ${response.message()}")
                    liveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<AnomalyDetailResponseItem>>, t: Throwable) {
                Timber.e(t, "Error fetching anomaly details")
                liveData.postValue(null)
            }
        })

        return liveData
    }
}