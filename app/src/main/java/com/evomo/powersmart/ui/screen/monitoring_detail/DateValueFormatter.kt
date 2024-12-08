package com.evomo.powersmart.ui.screen.monitoring_detail

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DateValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        // Konversi float (epoch detik) kembali ke LocalDateTime
        val localDateTime = Instant.ofEpochSecond(value.toLong())
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime()

        // Format LocalDateTime menggunakan DateTimeFormatter
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\nHH:mm:ss")
        return localDateTime.format(formatter)
    }
}
