package com.zeni.core.domain.model

import java.time.ZonedDateTime

data class User(
    val uid: String,
    val email: String,
    val phone: String,
    val username: String,
    val birthdate: ZonedDateTime,
    val address: String,
    val country: Country,
)
