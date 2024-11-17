package com.evomo.powersmart.ui.screen.monitoring_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MonitoringDetailViewModel @Inject constructor() : ViewModel() {

    private val _usageData = MutableStateFlow<List<UsageData>>(emptyList())

    private val _startDate = MutableStateFlow<Long>(0)
    val startDate: StateFlow<Long> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Long>(0)
    val endDate: StateFlow<Long> = _endDate.asStateFlow()

    // Fungsi untuk mendapatkan data yang sudah difilter
    val filteredUsageData: StateFlow<List<UsageData>> = combine(
        _usageData,
        startDate,
        endDate
    ) { data, start, end ->
        // if start or end date is null, return the original data
        if (start == 0L || end == 0L) {
            return@combine data
        } else {
            data.filter { usageData ->
                (usageData.timestampMillis >= start) &&
                        (usageData.timestampMillis <= end)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        generateDummyData()
    }

    private fun generateDummyData() {
        val dummyData = mutableListOf<UsageData>()
        val initialTimestamp = System.currentTimeMillis() // Waktu saat ini dalam milidetik

        for (i in 0 until 100) { // Membuat 100 data sebagai contoh
            // Mengurangi waktu setiap data dengan interval satu hari (86400000 ms)
            val timestamp = initialTimestamp - (i * 86400000L)
            val usage =
                (50..100).random() + (0..99).random() / 100.0 // Nilai acak antara 50.0 hingga 100.99

            dummyData.add(UsageData(usage, timestamp)) // Menyimpan timestamp sebagai Long
        }

        // Membalik urutan data agar timestamp yang lebih baru berada di akhir list
        _usageData.value = dummyData.reversed()
    }

    fun updateStartDate(timestamp: Long) {
        _startDate.update { timestamp }
    }

    fun updateEndDate(timestamp: Long) {
        _endDate.update { timestamp }
    }
}