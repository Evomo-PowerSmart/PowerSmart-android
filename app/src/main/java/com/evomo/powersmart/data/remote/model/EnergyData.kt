package com.evomo.powersmart.data.remote.model

data class EnergyData(
    val id: Int? = null,
    val reading_time: String,
    val position: String,
    val meter_type: String,
    val meter_serial_number: Int,
    val active_energy_import: Double,
    val active_energy_export: Double,
    val reactive_energy_import: Double,
    val reactive_energy_export: Double,
    val apparent_energy_import: Double,
    val apparent_energy_export: Double,
)