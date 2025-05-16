package com.zeni.core.di

import android.content.Context
import androidx.room.Room
import com.zeni.core.data.local.database.Migrations
import com.zeni.core.data.local.database.TripsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "trips_database"

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, TripsDatabase::class.java, DATABASE_NAME)
            .addMigrations(*Migrations.migrations)
            .build()

    @Provides
    @Singleton
    fun provideUserDao(db: TripsDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideTripDao(db: TripsDatabase) = db.tripDao()

    @Provides
    @Singleton
    fun provideItineraryDao(db: TripsDatabase) = db.itineraryDao()

    @Provides
    @Singleton
    fun provideHotelDao(db: TripsDatabase) = db.hotelDao()

    @Provides
    @Singleton
    fun provideReservationDao(db: TripsDatabase) = db.reservationDao()
}