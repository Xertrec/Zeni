package com.zeni.core.data.local.database.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotel_table")
data class HotelEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)
