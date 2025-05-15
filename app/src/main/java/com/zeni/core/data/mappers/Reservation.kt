package com.zeni.core.data.mappers

import com.zeni.core.data.local.database.entities.ReservationEntity
import com.zeni.core.data.local.database.relations.ReservationRelation
import com.zeni.core.data.remote.dto.ReservationDto
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.utils.ZonedDateTimeUtils

fun ReservationDto.toDomain(): Reservation {
    return Reservation(
        hotelId = hotelId,
        roomId = roomId,
        startDate = ZonedDateTimeUtils.fromString(startDate),
        endDate = ZonedDateTimeUtils.fromString(endDate),
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

fun ReservationEntity.toDomain(): Reservation {
    return Reservation(
        hotelId = hotelId,
        roomId = roomId,
        startDate = ZonedDateTimeUtils.fromString(startDate),
        endDate = ZonedDateTimeUtils.fromString(endDate),
        guestName = guestName,
        guestEmail = guestEmail
    )
}

fun Reservation.toEntity(userUid: String, tripName: String): ReservationEntity {
    return ReservationEntity(
        hotelId = hotelId,
        roomId = roomId,
        startDate = ZonedDateTimeUtils.toString(startDate),
        endDate = ZonedDateTimeUtils.toString(endDate),
        guestName = guestName,
        guestEmail = guestEmail,
        userReservation = userUid,
        tripName = tripName
    )
}