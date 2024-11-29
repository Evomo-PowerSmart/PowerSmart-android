package com.evomo.powersmart.ui.screen.monitoring_detail

import android.icu.util.Calendar
import java.time.Instant
import java.time.LocalDateTime

data class MonitoringDetailState(
    val isLoading: Boolean = false,
    val startDate: Long = Instant.now().minus(java.time.Duration.ofDays(7)).toEpochMilli(),
    val endDate: Long = Instant.now().toEpochMilli(),
    val startTime: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }.timeInMillis,
    val endTime: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }.timeInMillis,
)

data class UsageData(
    val activeEnergyImport: Double,
    val readingTime: LocalDateTime,
)
