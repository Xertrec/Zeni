package com.zeni.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "room_table",
    foreignKeys = [
        ForeignKey(
            entity = HotelEntity::class,
            parentColumns = ["id"],
            childColumns = ["hotel_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RoomEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "hotel_id")
    val hotelId: String,

    @ColumnInfo(name = "room_type")
    val roomType: String,

    @ColumnInfo(name = "price")
    val price: Double
)
