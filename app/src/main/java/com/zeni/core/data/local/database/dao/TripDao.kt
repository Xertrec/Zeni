package com.zeni.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.zeni.core.data.local.database.entities.TripEntity
import com.zeni.core.data.local.database.entities.TripImageEntity
import com.zeni.core.data.local.database.relations.TripRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Transaction
    @Query("""
        SELECT * FROM trip_table 
        WHERE user_owner = :ownerUid
        ORDER BY start_date ASC
    """)
    fun getTrips(ownerUid: String): Flow<List<TripRelation>>

    @Transaction
    @Query("SELECT * FROM trip_table WHERE name = :tripName")
    fun getTrip(tripName: String): Flow<TripRelation>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM trip_table 
            WHERE name = :tripName AND user_owner = :ownerUid
        )
    """)
    suspend fun existsTrip(tripName: String, ownerUid: String): Boolean

    @Upsert
    suspend fun addTrip(trip: TripEntity)

    @Upsert
    suspend fun addTripImages(images: List<TripImageEntity>)

    @Upsert
    suspend fun addTripImage(image: TripImageEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)
}