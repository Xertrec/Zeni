package com.zeni.core.domain.model

import android.net.Uri

data class Room(
    val id: String,
    val roomType: RoomType,
    val price: Double,
    val images: List<Uri>
)
