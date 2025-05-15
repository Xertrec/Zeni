package com.zeni.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zeni.core.data.local.database.dao.HotelDao
import com.zeni.core.data.local.database.dao.ReservationDao
import com.zeni.core.data.local.database.dao.ItineraryDao
import com.zeni.core.data.local.database.dao.TripDao
import com.zeni.core.data.local.database.dao.UserDao
import com.zeni.core.data.local.database.entities.ActivityEntity
import com.zeni.core.data.local.database.entities.HotelEntity
import com.zeni.core.data.local.database.entities.ReservationEntity
import com.zeni.core.data.local.database.entities.RoomEntity
import com.zeni.core.data.local.database.entities.RoomImageEntity
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
        RoomImageEntity::class,
        ReservationEntity::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TripsDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun tripDao(): TripDao

    abstract fun itineraryDao(): ItineraryDao

    abstract fun hotelDao(): HotelDao

    abstract fun reservationDao(): ReservationDao
}