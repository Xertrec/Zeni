package com.zeni.core.domain.model

import android.net.Uri
import com.zeni.core.data.remote.dto.RoomDto

data class Hotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<Room>,
    val imageUrl: Uri
)
