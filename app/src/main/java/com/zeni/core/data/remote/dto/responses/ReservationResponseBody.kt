package com.zeni.core.data.remote.dto.responses

import com.zeni.core.data.remote.dto.ReservationDto

data class ReservationResponseBody(
    val message: String,
    val nights: Int,
    val reservation: ReservationDto
)
