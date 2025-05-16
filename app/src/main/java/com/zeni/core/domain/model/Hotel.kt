package com.zeni.core.domain.model

import android.net.Uri

data class Hotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<Room>,
    val imageUrl: Uri
) {
    val minRoomPrice: Double?
        get() = rooms.minOfOrNull { it.price }
}
