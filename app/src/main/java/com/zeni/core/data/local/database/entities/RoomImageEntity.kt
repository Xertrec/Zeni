package com.zeni.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "room_images_table",
    primaryKeys = ["room_id", "image_url"],
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RoomImageEntity(
    @ColumnInfo(name = "room_id")
    val roomId: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)
