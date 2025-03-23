package com.zeni.core.domain.repository

import com.zeni.core.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    fun getTrips(): Flow<List<Trip>>

    fun getTrip(tripName: String): Flow<Trip>

    suspend fun addTrip(trip: Trip)

    suspend fun existsTrip(tripName: String): Boolean

    suspend fun deleteTrip(trip: Trip)
}