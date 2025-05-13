package com.zeni.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zeni.core.data.local.database.dao.ReservationDao
import com.zeni.core.data.local.database.dao.ItineraryDao
import com.zeni.core.data.local.database.dao.TripDao
import com.zeni.core.data.local.database.dao.UserDao
import com.zeni.core.data.local.database.entities.ActivityEntity
import com.zeni.core.data.local.database.entities.HotelEntity
import com.zeni.core.data.local.database.entities.ReservationEntity
import com.zeni.core.data.local.database.entities.RoomEntity
import com.zeni.core.data.local.database.entities.RoomImagesEntity
import com.zeni.core.data.local.database.entities.TripEntity
import com.zeni.core.data.local.database.entities.TripImageEntity
import com.zeni.core.data.local.database.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TripEntity::class,
        TripImageEntity::class,
        ActivityEntity::class,
        HotelEntity::class,
        RoomEntity::class,
        RoomImagesEntity::class,
        ReservationEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TripsDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun tripDao(): TripDao

    abstract fun itineraryDao(): ItineraryDao

    abstract fun reservationDao(): ReservationDao
}