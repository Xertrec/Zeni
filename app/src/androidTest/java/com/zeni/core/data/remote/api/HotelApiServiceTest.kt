package com.zeni.core.data.remote.api

import androidx.test.filters.SmallTest
import com.zeni.core.data.local.database.dao.TripDao
import com.zeni.core.data.local.database.entities.TripEntity
import com.zeni.core.data.local.database.entities.UserEntity
import com.zeni.hotel.domain.model.City
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
class HotelApiServiceTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var hotelApiService: HotelApiService

    private val gid = "G07"

    @Before
    fun setup() {
        runBlocking {
            hiltRule.inject()
        }
    }

    @Test
    fun testAvailability() = runBlocking {
        val startDate = "2025-05-10"
        val endDate = "2025-05-15"
        val city = City.BARCELONA.cityName

        val hotels = hotelApiService.getHotelAvailability(
            groupId = gid,
            startDate = startDate,
            endDate = endDate,
            city = city
        ).availableHotels

        assert(hotels.isNotEmpty()) { "No hotels available" }
    }
}