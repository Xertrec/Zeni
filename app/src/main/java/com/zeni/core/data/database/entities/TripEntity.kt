package com.zeni.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(
    tableName = "trip_table",
    foreignKeys = [
        ForeignKey(
            entity = TripImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["cover_image_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "destination") val destination: String,
    @ColumnInfo(name = "start_date") val startDate: ZonedDateTime,
    @ColumnInfo(name = "end_date") val endDate: ZonedDateTime,
    @ColumnInfo(name = "cover_image_id") val coverImageId: Long?
)
