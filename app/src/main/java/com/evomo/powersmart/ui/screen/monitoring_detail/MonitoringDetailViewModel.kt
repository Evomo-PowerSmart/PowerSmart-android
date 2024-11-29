package com.evomo.powersmart.ui.screen.monitoring_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.remote.LocationDataRepository
import com.evomo.powersmart.ui.utils.getStringDateFromEpochMilli
import com.evomo.powersmart.ui.utils.getTimeStringFromTimeInMillis
import com.evomo.powersmart.ui.utils.stringToLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MonitoringDetailViewModel @Inject constructor(
    private val locationDataRepository: LocationDataRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MonitoringDetailState())
    val state: StateFlow<MonitoringDetailState> = _state.asStateFlow()

    private val _usageDatas = MutableLiveData<List<UsageData>>(emptyList())
    val usageData: LiveData<List<UsageData>> = _usageDatas

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun fetchHistoricalData(
        location: String,
        startDate: Long,
        endDate: Long,
        startTime: Long,
        endTime: Long,
    ) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        val response = locationDataRepository.fetchHistoricalData(
            location = location,
            startDate = "${getStringDateFromEpochMilli(startDate)} ${
                getTimeStringFromTimeInMillis(
                    startTime
                )
            }",
            endDate = "${getStringDateFromEpochMilli(endDate)} ${
                getTimeStringFromTimeInMillis(
                    endTime
                )
            }",
        )

        when (response) {
            is Resource.Error -> {
                _errorMessage.emit(response.message ?: "Something went wrong!")
            }

            is Resource.Loading -> {}
            is Resource.Success -> {
                _usageDatas.value = response.data?.map { usageData ->
                    Timber.d(
                        "reading time: ${usageData.readingTime}\nconverted: ${
                            stringToLocalDateTime(
                                usageData.readingTime
                            )
                        }"
                    )
                    UsageData(
                        activeEnergyImport = usageData.activeEnergyImport.toDouble(),
                        readingTime = stringToLocalDateTime(usageData.readingTime)
                    )
                }
            }
        }

        _state.update { it.copy(isLoading = false) }
    }

    fun updateStartDate(newDate: Long) {
        _state.update { it.copy(startDate = newDate) }
    }

    fun updateEndDate(newDate: Long) {
        _state.update { it.copy(endDate = newDate) }
    }

    fun updateStartTime(newTime: Long) {
        _state.update { it.copy(startTime = newTime) }
    }

    fun updateEndTime(newTime: Long) {
        _state.update { it.copy(endTime = newTime) }
    }
}