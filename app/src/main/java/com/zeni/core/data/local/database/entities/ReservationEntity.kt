package com.zeni.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "reservation_table",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["user_reservation"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HotelEntity::class,
            parentColumns = ["id"],
            childColumns = ["hotel_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["name"],
            childColumns = ["trip_name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReservationEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "hotel_id")
    val hotelId: String,

    @ColumnInfo(name = "room_id")
    val roomId: String,

    @ColumnInfo(name = "start_date")
    val startDate: String,

    @ColumnInfo(name = "end_date")
    val endDate: String,

    @ColumnInfo(name = "guest_name")
    val guestName: String,

    @ColumnInfo(name = "guest_email")
    val guestEmail: String,

    @ColumnInfo(name = "user_reservation")
    val userReservation: String,

    @ColumnInfo(name = "trip_name")
    val tripName: String
)
