package com.zeni.core.data.mappers

import android.R.attr.description
import android.R.attr.name
import androidx.core.net.toUri
import com.zeni.core.data.remote.dto.RoomDto
import com.zeni.core.domain.model.Room

fun RoomDto.toDomain() = Room(
    id = id,
    roomType = roomType,
    price = price,
    images = images.map { it.toUri() },
)

fun Room.toDto() = RoomDto(
    id = id,
    roomType = roomType,
    price = price,
    images = ArrayList(images.map { it.toString() })
)