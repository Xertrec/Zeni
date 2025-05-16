package com.zeni.core.domain.repository

import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.model.TripImage
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    fun getTrips(): Flow<List<Trip>>

    fun getTrip(tripName: String): Flow<Trip>

    fun getTripImages(tripName: String): Flow<List<TripImage>>

    fun getTripImage(imageId: Long): Flow<TripImage>

    suspend fun addTrip(trip: Trip)

    suspend fun addTripImages(images: List<TripImage>)

    suspend fun existsTrip(tripName: String): Boolean

    suspend fun deleteTrip(trip: Trip)

    suspend fun deleteTripImage(image: TripImage)
}
