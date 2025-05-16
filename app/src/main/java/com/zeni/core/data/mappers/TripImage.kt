package com.zeni.core.data.mappers

import androidx.core.net.toUri
import com.zeni.core.data.local.database.entities.TripImageEntity
import com.zeni.core.domain.model.TripImage
import com.zeni.core.domain.utils.LocalStorage
import com.zeni.core.domain.utils.extensions.completeFileName

fun TripImage.toEntity() = TripImageEntity(
    id = id,
    tripName = tripName,
    url = url.completeFileName,
    description = description
)

fun TripImageEntity.toDomain(localStorage: LocalStorage) = TripImage(
    id = id,
    tripName = tripName,
    url = localStorage.getImageFromLocalStorage(url),
    description = description
)
