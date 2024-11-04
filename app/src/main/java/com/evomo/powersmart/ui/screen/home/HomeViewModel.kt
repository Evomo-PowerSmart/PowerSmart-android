package com.evomo.powersmart.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    // StateFlow untuk active energy import
    private val _activeEnergyImport = MutableStateFlow(49927072.0) // nilai awal
    val activeEnergyImport: StateFlow<Double> = _activeEnergyImport.asStateFlow()

    private val _addedEnergy = MutableStateFlow(0.0)
    val addedEnergy: StateFlow<Double> = _addedEnergy.asStateFlow()

    // StateFlow untuk indikator apakah naik atau turun
    private val _status = MutableStateFlow<Status?>(null)
    val status: StateFlow<Status?> = _status.asStateFlow()

    // StateFlow untuk waktu pembaruan terakhir
    private val _lastUpdateTime = MutableStateFlow(getCurrentTime())
    val lastUpdateTime: StateFlow<String> = _lastUpdateTime.asStateFlow()

    private var _previousEnergy = _activeEnergyImport

    init {
        simulateActiveEnergyImport()
        getProfilePictureUrl()
    }

    // Fungsi untuk mensimulasikan data active_energy_import
    private fun simulateActiveEnergyImport() {
        viewModelScope.launch {
            while (true) {
                delay(1000L) // Interval pembaruan setiap 1 detik
                val energyToAdd = Random.nextDouble(-1000.0, 1000.0)
                _addedEnergy.value = energyToAdd
                val newEnergy = _activeEnergyImport.value + energyToAdd

                // Update apakah nilai bertambah atau berkurang
                _status.value = when {
                    newEnergy > _previousEnergy.value -> Status.INCREASING
                    newEnergy < _previousEnergy.value -> Status.DECREASING
                    else -> Status.STABLE
                }
                _previousEnergy.value = newEnergy

                // Update nilai active_energy_import dan last update time
                _activeEnergyImport.value = newEnergy
                _lastUpdateTime.value = getCurrentTime()
            }
        }
    }

    // Fungsi untuk mendapatkan waktu saat ini dalam format yang diinginkan
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yy - HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
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