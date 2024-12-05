package com.evomo.powersmart.ui.screen.anomaly_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.anomaly.AnomalyRepository
import com.evomo.powersmart.data.anomaly.model.AnomalyDetailResponse
import com.evomo.powersmart.data.anomaly.model.AnomalyDetailResponseItem
import com.evomo.powersmart.ui.utils.toDateString
import com.evomo.powersmart.ui.utils.toTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnomalyDetailViewModel @Inject constructor(
    private val anomalyRepository: AnomalyRepository
) : ViewModel() {

    // Menggunakan List<AnomalyDetailResponseItem> sebagai tipe data
    private val _anomalyDetail = MutableStateFlow<List<AnomalyDetailResponseItem>?>(null)
    val anomalyDetail: StateFlow<List<AnomalyDetailResponseItem>?> = _anomalyDetail.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Fetch data anomaly detail dari repository
    fun fetchAnomalyDetail(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Menggunakan LiveData dengan collect() untuk mendapatkan data dari repository
                anomalyRepository.getAnomalyDetail(id).observeForever { response ->
                    if (response != null) {
                        _anomalyDetail.value = response
                        _error.value = null
                    } else {
                        _error.value = "Failed to fetch data"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
                Timber.e("Failed to fetch anomaly detail: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}


