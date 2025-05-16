package com.zeni.core.data.mappers

import com.zeni.core.data.local.database.entities.TripEntity
import com.zeni.core.data.local.database.relations.TripRelation
import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.utils.LocalStorage

fun Trip.toEntity() = TripEntity(
    name = name,
    destination = destination,
    startDate = startDate,
    endDate = endDate,
    coverImageId = coverImage?.id,
    userOwner = userOwner
)

fun TripRelation.toDomain(localStorage: LocalStorage) = Trip(
    name = trip.name,
    destination = trip.destination,
    startDate = trip.startDate,
    endDate = trip.endDate,
    coverImage = coverImage?.toDomain(localStorage),
    userOwner = trip.userOwner,
    images = images.map { it.toDomain(localStorage) },
    reservations = reservations.map { it.toDomain() }
)