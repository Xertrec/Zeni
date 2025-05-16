package com.zeni.core.data.repository

import com.zeni.core.data.local.database.dao.TripDao
import com.zeni.core.data.mappers.toDomain
import com.zeni.core.data.mappers.toEntity
import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.model.TripImage
import com.zeni.core.domain.repository.TripRepository
import com.zeni.core.domain.utils.Authenticator
import com.zeni.core.domain.utils.LocalStorage
import com.zeni.core.util.DatabaseLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao,
    private val authenticator: Authenticator,
    private val localStorage: LocalStorage
): TripRepository {

    override fun getTrips(): Flow<List<Trip>> {
        DatabaseLogger.dbOperation("Getting all trips from database")
        return try {
            val tripsFlow = tripDao.getTrips(authenticator.uid)
                .map { trips -> trips.map { it.toDomain(localStorage) } }
            DatabaseLogger.dbOperation("Trips retrieved successfully")

            tripsFlow
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error getting trips: ${e.message}", e)
            throw e
        }
    }

    override fun getTrip(tripName: String): Flow<Trip> {
        DatabaseLogger.dbOperation("Getting trip $tripName")
        return try {
            val tripFlow = tripDao.getTrip(tripName)
                .map { it.toDomain(localStorage) }
            DatabaseLogger.dbOperation("Trip retrieved successfully")

            tripFlow
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error getting trip: ${e.message}", e)
            throw e
        }
    }

    override suspend fun addTrip(trip: Trip) {
        DatabaseLogger.dbOperation("Adding trip to ${trip.destination}")
        return try {
            tripDao.addTrip(trip.copy(userOwner = authenticator.uid).toEntity())
            DatabaseLogger.dbOperation("Trip added successfully")
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error adding trip: ${e.message}", e)
            throw e
        }
    }

    override suspend fun addTripImages(images: List<TripImage>) {
        DatabaseLogger.dbOperation("Adding trip images")
        return try {
            tripDao.addTripImages(images.map { it.toEntity() })
            DatabaseLogger.dbOperation("Trip images added successfully")
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error adding trip images: ${e.message}", e)
            throw e
        }
    }

    override suspend fun existsTrip(tripName: String): Boolean {
        DatabaseLogger.dbOperation("Checking if trip $tripName exists")
        return try {
            tripDao.existsTrip(tripName, authenticator.uid)
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error checking if trip exists: ${e.message}", e)
            throw e
        }
    }

    override suspend fun deleteTrip(trip: Trip) {
        DatabaseLogger.dbOperation("Deleting trip: ${trip.name}")
        try {
            tripDao.deleteTrip(trip.toEntity())
            DatabaseLogger.dbOperation("Trip deleted successfully")
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error deleting trip: ${e.message}", e)
        }
    }
}
