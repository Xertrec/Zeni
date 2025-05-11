package com.zeni.hotel.domain.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

object StartSelectableDate {
    /**
     * Creates a SelectableDates object for start dates that:
     * 1. Does not allow selecting past dates (today is selectable)
     * 2. Does not allow selecting dates after the end date (if it exists)
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun createSelectableDates(endDate: ZonedDateTime?): SelectableDates {
        return object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val today = LocalDate.now()
                val dateToCheck = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val endLocalDate = endDate?.toLocalDate()
                return !dateToCheck.isBefore(today) && 
                       (endLocalDate == null || dateToCheck.isBefore(endLocalDate))
            }
        }
    }
}
