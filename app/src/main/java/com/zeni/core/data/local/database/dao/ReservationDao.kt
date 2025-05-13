package com.zeni.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zeni.core.data.local.database.entities.ReservationEntity
import com.zeni.core.data.local.database.relations.ReservationRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservation_table")
    fun getAllReservations(): Flow<List<ReservationEntity>>

    @Query("SELECT * FROM reservation_table WHERE id = :reservationId")
    fun getReservationById(reservationId: Long): Flow<ReservationEntity?>

    @Upsert
    suspend fun upsertReservation(reservation: ReservationEntity): Long

    @Delete
    suspend fun deleteReservation(reservation: ReservationEntity)
}