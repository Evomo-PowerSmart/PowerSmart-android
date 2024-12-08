package com.evomo.powersmart.ui.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

// Function to convert a String to LocalDateTime
fun stringToLocalDateTime(dateTimeString: String): LocalDateTime {
    // Define the input format
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    // Parse the string to LocalDateTime
    return LocalDateTime.parse(dateTimeString, formatter)
}

fun epochMilliToString(epochMilli: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return Instant.ofEpochMilli(epochMilli)
        .atZone(ZoneId.systemDefault()) // Convert to local timezone
        .toLocalDateTime()
        .format(formatter)
}

fun getStringDateFromEpochMilli(epochMilli: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = Instant.ofEpochMilli(epochMilli)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    return localDate.format(formatter)
}

fun getTimeStringFromTimeInMillis(timeInMillis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return Instant.ofEpochMilli(timeInMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(formatter)
}

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