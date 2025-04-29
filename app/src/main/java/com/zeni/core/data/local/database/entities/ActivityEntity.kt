package com.zeni.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(
    tableName = "activity_table",
    indices = [
        Index(value = ["trip_name"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["name"],
            childColumns = ["trip_name"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["user_owner"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "trip_name")
    val tripName: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "date_time")
    val dateTime: ZonedDateTime,

    @ColumnInfo(name = "user_owner")
    val userOwner: String,
)
