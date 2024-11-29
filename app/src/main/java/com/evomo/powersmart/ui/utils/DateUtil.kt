package com.evomo.powersmart.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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