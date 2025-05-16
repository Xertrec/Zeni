package com.zeni.core.data.mappers

import androidx.core.net.toUri
import com.zeni.core.data.local.database.entities.HotelEntity
import com.zeni.core.data.remote.dto.HotelDto
import com.zeni.core.di.NetworkModule
import com.zeni.core.domain.model.Hotel

fun HotelDto.toDomain() = Hotel(
    id = id,
    name = name,
    address = address,
    rating = rating,
    rooms = rooms.map { it.toDomain() },
    imageUrl = (NetworkModule.BASE_URL + imageUrl).toUri()
)

fun Hotel.toEntity() = HotelEntity(
    id = id,
    name = name,
    address = address,
    rating = rating,
    imageUrl = imageUrl.toString()
)

//fun Hotel.toDto() = HotelDto(
//    id = id,
//    name = name,
//    address = address,
//    rating = rating,
//    rooms = ArrayList(rooms.map { it.toDto() }),
//    imageUrl = TODO("Is saved with the prefix of the base url, so we need to remove it")
//)