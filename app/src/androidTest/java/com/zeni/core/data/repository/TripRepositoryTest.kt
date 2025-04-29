package com.zeni.core.data.repository

import androidx.test.filters.SmallTest
import com.zeni.core.data.local.database.dao.UserDao
import com.zeni.core.data.local.database.entities.UserEntity
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
class TripRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var tripRepository: TripRepositoryImpl

    val userUid = "test_user"
    val tripName = "Test Trip"

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

            userDao.upsertUser(user)
        }
    }

    @Test
    fun testAddTrip() = runBlocking {
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
            userOwner = userUid
        )

        tripRepository.addTrip(trip)
        assert(trip == tripRepository.getTrip(tripName).first())
    }

    @Test
    fun testUpdateTrip() = runBlocking {
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
            userOwner = userUid
        )

        tripRepository.addTrip(trip)
        val updatedTrip = tripRepository.getTrip(tripName).first().copy(
            destination = "Updated Destination"
        )

        tripRepository.addTrip(updatedTrip)
        assert(updatedTrip == tripRepository.getTrip(tripName).first())
    }

    @Test
    fun testDeleteTrip() = runBlocking {
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
            userOwner = userUid
        )

        tripRepository.addTrip(trip)
        tripRepository.deleteTrip(trip)
        assert(!tripRepository.existsTrip(tripName))
    }
}