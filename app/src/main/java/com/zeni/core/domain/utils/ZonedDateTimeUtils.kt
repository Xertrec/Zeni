package com.zeni.core.domain.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ZonedDateTimeUtils {

    fun toUTCMillis(dateTime: ZonedDateTime): Long =
        dateTime.toInstant().toEpochMilli()

    fun fromUTCMillis(millis: Long): ZonedDateTime =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())

    fun toZonedDateTime(string: String): ZonedDateTime {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val localDate = LocalDate.parse(string, formatter)
        return localDate.atStartOfDay(ZoneId.systemDefault())
    }

    fun toString(dateTime: ZonedDateTime): String {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return dateTime.format(formatter)
    }
}