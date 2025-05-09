package com.zeni.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReservationDto(
    @SerializedName("hotel_id")
    val hotelId: String,
    @SerializedName("room_id")
    val roomId: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("guest_name")
    val guestName: String,
    @SerializedName("guest_email")
    val guestEmail: String,
)
