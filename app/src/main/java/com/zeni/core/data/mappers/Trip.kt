package com.zeni.core.data.mappers

import com.zeni.core.data.database.entities.TripEntity
import com.zeni.core.data.database.relations.TripRelation
import com.zeni.core.domain.model.Trip

fun Trip.toEntity() = TripEntity(
    name = name,
    destination = destination,
    startDate = startDate,
    endDate = endDate,
    coverImageId = coverImage?.id,
    userOwner = userOwner
)

fun TripRelation.toDomain() = Trip(
    name = trip.name,
    destination = trip.destination,
    startDate = trip.startDate,
    endDate = trip.endDate,
    coverImage = image?.toDomain(),
    userOwner = trip.userOwner
)