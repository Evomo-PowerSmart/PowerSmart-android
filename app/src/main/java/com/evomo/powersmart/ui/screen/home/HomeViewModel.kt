package com.evomo.powersmart.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.anomaly.AnomalyRepository
import com.evomo.powersmart.data.auth.AuthRepository
import com.evomo.powersmart.data.location_data.LocationDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val locationDataRepository: LocationDataRepository,
    private val anomalyRepository: AnomalyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    private val activeEnergyImportTreshold = 180000000.0

    init {
        getLastHistory()
        getProfilePictureUrl()
        observeAnomalies()
    }

    private fun getLastHistory() = viewModelScope.launch {
        updateIsRealtimeDataLoading(true)

        val response = locationDataRepository.fetchLastHistory(
            location = _state.value.selectedLocation.location,
        )

        if (response is Resource.Success) {
            val latestActiveEnergyImport =
                response.data?.first()?.activeEnergyImport?.toDouble() ?: 0.0

            val addedEnergy =
                response.data?.last()?.activeEnergyImport?.toDouble() ?: 0.0

            updateActiveEnergyImport(latestActiveEnergyImport)
            updateAddedEnergy(addedEnergy)
            updateStatus(
                if (latestActiveEnergyImport > activeEnergyImportTreshold) {
                    Status.DANGER
                } else if (latestActiveEnergyImport > activeEnergyImportTreshold - 20000000) {
                    Status.WARNING
                } else {
                    Status.GOOD
                }
            )
            updateEnergyIn(
                if (latestActiveEnergyImport > addedEnergy) {
                    EnergyIn.INCREASE
                } else if (latestActiveEnergyImport < addedEnergy) {
                    EnergyIn.DECREASE
                } else {
                    EnergyIn.NO_CHANGE
                }
            )
            updateLastUpdateTime(response.data?.first()?.readingTime ?: "N/A")
        } else if (response is Resource.Error) {
            _messageFlow.emit(response.message ?: "Something went wrong!")
        }

        updateIsRealtimeDataLoading(false)
    }

    private fun observeAnomalies() {
        anomalyRepository.getAnomalies().observeForever { anomalies ->
            _state.update { currentState ->
                currentState.copy(anomalies = anomalies)
            }
        }
    }

    private fun updateIsRealtimeDataLoading(newValue: Boolean) {
        _state.update { _state.value.copy(isRealtimeDataLoading = newValue) }
    }

    private fun updateActiveEnergyImport(newValue: Double) {
        _state.update { _state.value.copy(activeEnergyImport = newValue) }
    }

    private fun updateAddedEnergy(newValue: Double) {
        _state.update { _state.value.copy(addedEnergy = newValue) }
    }

    private fun updateStatus(newValue: Status) {
        _state.update { _state.value.copy(status = newValue) }
    }

    private fun updateEnergyIn(newValue: EnergyIn) {
        _state.update { _state.value.copy(energyIn = newValue) }
    }

    private fun updateLastUpdateTime(newValue: String) {
        _state.update { _state.value.copy(lastUpdateTime = newValue) }
    }

    fun updateSelectedLocationAndIndex(newValue: Int) {
        _state.update {
            _state.value.copy(
                selectedLocationIndex = newValue,
                selectedLocation = Location.entries[newValue]
            )
        }
        getLastHistory()
    }

    private fun getProfilePictureUrl() {
        val response = authRepository.getSignedInUser()

        if (response is Resource.Success) {
            _state.value = _state.value.copy(
                profilePictureUrl = response.data?.profilePictureUrl
            )
        }
    }
}