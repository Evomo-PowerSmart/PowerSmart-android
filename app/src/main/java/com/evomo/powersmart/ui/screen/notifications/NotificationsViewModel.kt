package com.evomo.powersmart.ui.screen.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evomo.powersmart.data.anomaly.AnomalyRepository
import com.evomo.powersmart.data.anomaly.model.AnomalyResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val anomalyRepository: AnomalyRepository
) : ViewModel() {

    private val _anomalies = MutableLiveData<List<AnomalyResponseItem>>()
    val anomalies: LiveData<List<AnomalyResponseItem>> get() = _anomalies

    fun fetchAnomalies() {
        anomalyRepository.getAnomalies().observeForever {
            _anomalies.value = it
        }
    }
}