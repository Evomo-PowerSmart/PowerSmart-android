package com.evomo.powersmart.ui.screen.home

import com.evomo.powersmart.data.anomaly.model.AnomalyResponseItem

data class HomeState(
    val profilePictureUrl: String? = null,
    val activeEnergyImport: Double = 0.0,
    val addedEnergy: Double = 0.0,
    val status: Status = Status.OFFLINE,
    val energyIn: EnergyIn = EnergyIn.NO_CHANGE,
    val lastUpdateTime: String? = null,
    val anomalies: List<AnomalyResponseItem> = emptyList()
)

