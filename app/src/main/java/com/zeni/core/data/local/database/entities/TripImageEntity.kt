package com.zeni.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trip_images_table",
    indices = [
        Index(value = ["trip_name"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["name"],
            childColumns = ["trip_name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TripImageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "trip_name") val tripName: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "description") val description: String
)
