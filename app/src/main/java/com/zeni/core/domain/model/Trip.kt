package com.zeni.core.domain.model

import java.time.ZonedDateTime

data class Trip(
    val name: String,
    val destination: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val coverImage: TripImage? = null,
    val userOwner: String,
    val images: List<TripImage> = emptyList(),
    val reservations: List<Reservation> = emptyList()
)