package com.zeni.core.data.mappers

import androidx.core.net.toUri
import com.zeni.core.data.local.database.entities.TripImageEntity
import com.zeni.core.domain.model.TripImage

fun TripImage.toEntity() = TripImageEntity(
    id = id,
    tripName = tripName,
    url = url.toString(),
    description = description
)

fun TripImageEntity.toDomain() = TripImage(
    id = id,
    tripName = tripName,
    url = url.toUri(),
    description = description
)