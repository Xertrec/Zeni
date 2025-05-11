package com.zeni.hotel.domain.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.ZonedDateTime

object EndSelectableDate {
    /**
     * Creates a SelectableDates object for end dates that:
     * 1. Does not allow selecting past dates
     * 2. Does not allow selecting dates equal to or before the start date (if it exists)
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun createSelectableDates(startDate: ZonedDateTime?): SelectableDates {
        return object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val currentTime = System.currentTimeMillis()
                val startTimeMillis = startDate?.toInstant()?.toEpochMilli() ?: currentTime
                
                // Date must be equal to or after today AND strictly after the start date
                return utcTimeMillis >= currentTime && utcTimeMillis > startTimeMillis
            }
        }
    }
}
