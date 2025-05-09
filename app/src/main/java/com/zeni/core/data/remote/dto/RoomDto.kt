package com.zeni.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RoomDto(
    val id: String,
    @SerializedName("room_type")
    val roomType: String,
    val price: Double,
    val images: ArrayList<String>
)
