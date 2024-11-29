package com.evomo.powersmart.ui.screen.home

data class HomeState(
    val profilePictureUrl: String? = null,
    val isRealtimeDataLoading: Boolean = false,
    val activeEnergyImport: Double = 0.0,
    val addedEnergy: Double = 0.0,
    val status: Status = Status.OFFLINE,
    val energyIn: EnergyIn = EnergyIn.NO_CHANGE,
    val lastUpdateTime: String? = null,
    val selectedLocationIndex: Int = 0,
    val selectedLocation: Location = Location.AHU_LANTAI_2,
)
