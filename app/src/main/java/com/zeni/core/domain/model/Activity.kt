package com.zeni.core.domain.model

import java.time.ZonedDateTime

data class Activity(
    val id: Long,
    val tripName: String,
    val title: String,
    val description: String,
    val dateTime: ZonedDateTime,
    val userOwner: String
)
