package com.zeni.core.data.database.dao

import androidx.test.filters.SmallTest
import com.zeni.core.data.database.entities.ActivityEntity
import com.zeni.core.data.database.entities.TripEntity
import com.zeni.core.data.database.entities.UserEntity
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
class ItineraryDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var tripDao: TripDao

    @Inject
    lateinit var itineraryDao: ItineraryDao

    var userUid = "test_user"
    var tripName = "Test Trip"

    @Before
    fun setup() {
        runBlocking {
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

            val trip = TripEntity(
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
                coverImageId = null,
                userOwner = user.uid
            )

            userDao.upsertUser(user)
            tripDao.addTrip(trip)
        }
    }

    @Test
    fun testAddItinerary() = runBlocking {
        val activity = ActivityEntity(
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

        val itineraryId = itineraryDao.addActivity(activity)
        assert(activity.copy(id = itineraryId) == itineraryDao.getActivity(tripName, itineraryId).first())
    }

    @Test
    fun testUpdateItinerary() = runBlocking {
        val activity = ActivityEntity(
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

        val itineraryId = itineraryDao.addActivity(activity)
        val updatedActivity = itineraryDao.getActivity(tripName, itineraryId).first()
            .copy(
                title = "Updated Activity"
            )

        itineraryDao.addActivity(updatedActivity)
        assert(updatedActivity == itineraryDao.getActivity(tripName, itineraryId).first())
    }

    @Test
    fun testDeleteItinerary() = runBlocking {
        val activity = ActivityEntity(
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

        val activityId = itineraryDao.addActivity(activity)
        itineraryDao.deleteActivity(activity.copy(id = activityId))
        assert(!itineraryDao.existsActivity(tripName, activityId))
    }
}