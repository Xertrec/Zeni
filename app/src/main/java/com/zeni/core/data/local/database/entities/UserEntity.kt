package com.zeni.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "birthdate")
    val birthdate: ZonedDateTime,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "country")
    val country: String,
)
