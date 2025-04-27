package com.zeni.core.data.database.dao

import androidx.test.filters.SmallTest
import com.zeni.core.data.database.entities.TripEntity
import com.zeni.core.data.database.entities.UserEntity
import com.zeni.core.data.mappers.toEntity
import com.zeni.core.data.repository.TripRepositoryImpl
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
class TripDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var tripDao: TripDao

    var userUid = "test_user"

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
        val trip = TripEntity(
            name = "name",
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
            userOwner = userUid
        )

        tripDao.addTrip(trip)
        assert(trip == tripDao.getTrip(trip.name).first().trip)
    }

    @Test
    fun testUpdateTrip() = runBlocking {
        val trip = TripEntity(
            name = "name",
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
            userOwner = userUid
        )

        tripDao.addTrip(trip)
        val updatedTrip = tripDao.getTrip(trip.name).first().trip.copy(
            destination = "Updated Destination"
        )

        tripDao.addTrip(updatedTrip)
        assert(updatedTrip == tripDao.getTrip(trip.name).first().trip)
    }

    @Test
    fun testDeleteTrip() = runBlocking {
        val trip = TripEntity(
            name = "name",
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
            userOwner = userUid
        )

        tripDao.addTrip(trip)
        tripDao.deleteTrip(trip)
        assert(!tripDao.existsTrip(trip.name))
    }
}