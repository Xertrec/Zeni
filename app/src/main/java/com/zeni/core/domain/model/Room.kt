package com.zeni.core.domain.model

import android.net.Uri

data class Room(
    val id: String,
    val roomType: String,
    val price: Double,
    val images: List<Uri>
)
