package com.example.lume.Util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime


fun formatInstant(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year.toString()
    return "$day/$month/$year"
}

fun parseDate(dateString: String): Instant? {
    val normalized = dateString.replace("/", "")
    if (normalized.length != 8) return null
    val day = normalized.substring(0, 2).toIntOrNull() ?: return null
    val month = normalized.substring(2, 4).toIntOrNull() ?: return null
    val year = normalized.substring(4, 8).toIntOrNull() ?: return null
    return LocalDate(year, month, day)
        .atStartOfDayIn(TimeZone.currentSystemDefault())
}