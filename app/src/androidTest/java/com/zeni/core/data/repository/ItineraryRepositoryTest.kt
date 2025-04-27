package com.zeni.core.data.repository

import androidx.test.filters.SmallTest
import com.zeni.core.data.database.dao.UserDao
import com.zeni.core.data.database.entities.UserEntity
import com.zeni.core.domain.model.Activity
import com.zeni.core.domain.model.Trip
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
class ItineraryRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var tripRepository: TripRepositoryImpl

    @Inject
    lateinit var itineraryRepository: ItineraryRepositoryImpl

    val userUid = "test_user"
    val tripName = "name"

    @Before
    fun setup() = runBlocking {
        hiltRule.inject()

        val user = UserEntity(
            uid = userUid,
            email = "",
            phone = "",
            username = "Test User",
            birthdate = ZonedDateTime.now(),
            address = "",
            country = "",
        )

        val trip = Trip(
            name = tripName,
            destination = "Test Destination",
            startDate = ZonedDateTime.of(
                2022, 1, 1,
                0, 0, 0, 0,
                ZonedDateTime.now().zone
            ),
            endDate = ZonedDateTime.of(
                2022, 1, 2,
                0, 0, 0, 0,
                ZonedDateTime.now().zone
            ),
            userOwner = userUid,
        )

        userDao.upsertUser(user)
        tripRepository.addTrip(trip)
    }

    @Test
    fun testAddItinerary() = runBlocking {
        val activity = Activity(
            id = 0,
            tripName = tripName,
            title = "Test Activity",
            description = "Test Description",
            dateTime = ZonedDateTime.of(
                2022, 1, 1,
                0, 0, 0, 0,
                ZonedDateTime.now().zone
            ),
            userOwner = userUid
        )

        val itineraryId = itineraryRepository.addActivity(activity)
        assert(activity.copy(id = itineraryId) == itineraryRepository.getActivity(tripName, itineraryId).first())
    }

    @Test
    fun testUpdateItinerary() = runBlocking {
        val activity = Activity(
            id = 0,
            tripName = tripName,
            title = "Test Activity",
            description = "Test Description",
            dateTime = ZonedDateTime.of(
                2022, 1, 1,
                0, 0, 0, 0,
                ZonedDateTime.now().zone
            ),
            userOwner = userUid
        )

        val itineraryId = itineraryRepository.addActivity(activity)
        val updatedActivity = itineraryRepository.getActivity(tripName, itineraryId).first()
            .copy(
                title = "Updated Activity"
            )

        itineraryRepository.addActivity(updatedActivity)
        assert(updatedActivity == itineraryRepository.getActivity(tripName, itineraryId).first())
    }

    @Test
    fun testDeleteItinerary() = runBlocking {
        val activity = Activity(
            id = 0,
            tripName = tripName,
            title = "Test Activity",
            description = "Test Description",
            dateTime = ZonedDateTime.of(
                2022, 1, 1,
                0, 0, 0, 0,
                ZonedDateTime.now().zone
            ),
            userOwner = userUid
        )

        val activityId = itineraryRepository.addActivity(activity)
        itineraryRepository.deleteActivity(activity.copy(id = activityId))
        assert(!itineraryRepository.existsActivity(tripName, activityId))
    }
}