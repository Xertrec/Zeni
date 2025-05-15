package com.zeni.core.data.mappers

import android.R.attr.description
import android.R.attr.name
import androidx.core.net.toUri
import com.zeni.core.data.local.database.entities.RoomEntity
import com.zeni.core.data.remote.dto.RoomDto
import com.zeni.core.di.NetworkModule
import com.zeni.core.domain.model.Room
import com.zeni.core.domain.model.RoomType

fun RoomDto.toDomain() = Room(
    id = id,
    roomType = RoomType.valueOf(roomType.uppercase()),
    price = price,
    images = images.map { (NetworkModule.BASE_URL + it).toUri() },
)

fun Room.toEntity(hotelId: String) = RoomEntity(
    id = id,
    hotelId = hotelId,
    roomType = roomType.name,
    price = price
)

//fun Room.toDto() = RoomDto(
//    id = id,
//    roomType = roomType,
//    price = price,
//    images = TODO("Is saved with the prefix of the base url, so we need to remove it")
//)