package com.zeni.core.domain.model

import java.time.ZonedDateTime

data class Reservation(
    val id: String,
    val hotelId: String,
    val roomId: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val guestName: String,
    val guestEmail: String,
)
