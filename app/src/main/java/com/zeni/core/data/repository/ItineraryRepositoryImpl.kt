package com.zeni.core.data.repository

import com.zeni.core.data.database.dao.ItineraryDao
import com.zeni.core.data.mappers.toDomain
import com.zeni.core.data.mappers.toEntity
import com.zeni.core.domain.model.Activity
import com.zeni.core.domain.repository.ItineraryRepository
import com.zeni.core.domain.utils.Authenticator
import com.zeni.core.util.DatabaseLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItineraryRepositoryImpl @Inject constructor(
    private val itineraryDao: ItineraryDao,
    private val authenticator: Authenticator,
): ItineraryRepository {

    override fun getActivitiesByTrip(tripName: String): Flow<List<Activity>> {
        DatabaseLogger.dbOperation("Getting activities for trip $tripName")
        return try {
            val activitiesFlow = itineraryDao.getActivitiesByTrip(tripName, authenticator.uid)
                .map { activities -> activities.map { it.toDomain() } }
            DatabaseLogger.dbOperation("Activities retrieved successfully")

            activitiesFlow
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error getting activities: ${e.message}", e)
            throw e
        }
    }

    override fun getActivitiesByDate(date: ZonedDateTime): Flow<List<Activity>> {
        DatabaseLogger.dbOperation("Getting activities for date ${date.toLocalDate()}")
        return try {
            val startDateTime = date.withHour(0).withMinute(0).withSecond(0).withNano(0)
                .toInstant().toEpochMilli()
            val endDateTime = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                .toInstant().toEpochMilli()
            val activitiesFlow = itineraryDao.getActivitiesByDate(startDateTime, endDateTime, authenticator.uid)
                .map { activities -> activities.map { it.toDomain() } }
            DatabaseLogger.dbOperation("Activities retrieved successfully")

            activitiesFlow
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error getting activities: ${e.message}", e)
            throw e
        }
    }

    override fun getActivity(tripName: String, activityId: Long): Flow<Activity> {
        DatabaseLogger.dbOperation("Getting activity with id $activityId for trip $tripName")
        return try {
            val activityFlow = itineraryDao.getActivity(tripName, activityId)
                .map { it.toDomain() }
            DatabaseLogger.dbOperation("Activity retrieved successfully")

            activityFlow
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error getting activity: ${e.message}", e)
            throw e
        }
    }

    override suspend fun addActivity(activity: Activity): Long {
        DatabaseLogger.dbOperation("Adding activity to trip ${activity.tripName}")
        return try {
            val activityId = itineraryDao.addActivity(activity.toEntity())
            DatabaseLogger.dbOperation("Activity added successfully")

            activityId
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error adding activity: ${e.message}", e)
            throw e
        }
    }

    override suspend fun existsActivity(tripName: String, activityId: Long): Boolean {
        DatabaseLogger.dbOperation("Checking if activity with id $activityId exists for trip $tripName")
        return try {
            itineraryDao.existsActivity(tripName, activityId, authenticator.uid)
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error checking if activity exists: ${e.message}", e)
            throw e
        }
    }

    override suspend fun deleteActivity(activity: Activity) {
        DatabaseLogger.dbOperation("Deleting activity with id ${activity.id}")
        try {
            itineraryDao.deleteActivity(activity.toEntity())
            DatabaseLogger.dbOperation("Activity deleted successfully")
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error deleting activity: ${e.message}", e)
        }
    }
}