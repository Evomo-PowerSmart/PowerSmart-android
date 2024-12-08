package com.evomo.powersmart.ui.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Double.formatToTwoDecimal(): String {
    return if (this % 1.0 == 0.0) {
        // Jika tidak ada angka desimal, tampilkan sebagai bilangan bulat
        this.toInt().toString()
    } else {
        // Jika ada angka desimal, batasi hingga dua angka di belakang koma
        String.format("%.2f", this)
    }
}