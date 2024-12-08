package com.evomo.powersmart.ui.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(pattern: String = "dd/MM/yyyy"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(this))
}

fun String.toTimestamp(pattern: String = "yyyy-MM-dd HH:mm:ss"): Timestamp? {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val date = sdf.parse(this)
        Timestamp(date!!)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Timestamp.toDateString(pattern: String = "dd MMM yyyy, HH:mm:ss"): String {
    val date = this.toDate()
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date)
}