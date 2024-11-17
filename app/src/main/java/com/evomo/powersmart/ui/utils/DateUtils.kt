package com.evomo.powersmart.ui.utils

import java.util.Date
import java.util.Locale

fun Long.toDateString(): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}