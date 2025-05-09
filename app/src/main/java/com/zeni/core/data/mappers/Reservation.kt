package com.zeni.core.data.mappers

import com.zeni.core.data.remote.dto.ReservationDto
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.utils.ZonedDateTimeUtils

fun ReservationDto.toDomain(): Reservation {
    return Reservation(
        hotelId = hotelId,
        roomId = roomId,
        startDate = ZonedDateTimeUtils.toZonedDateTime(startDate),
        endDate = ZonedDateTimeUtils.toZonedDateTime(endDate),
        guestName = guestName,
        guestEmail = guestEmail
    )
}

fun Reservation.toDto(): ReservationDto {
    return ReservationDto(
        hotelId = hotelId,
        roomId = roomId,
        startDate = ZonedDateTimeUtils.toString(startDate),
        endDate = ZonedDateTimeUtils.toString(endDate),
        guestName = guestName,
        guestEmail = guestEmail
    )
}