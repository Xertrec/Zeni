package com.zeni.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HotelDto(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: ArrayList<RoomDto>,
    @SerializedName("image_url")
    val imageUrl: String
)
