package com.zeni.core.data.repository

import android.util.Log.e
import com.zeni.core.data.mappers.toDomain
import com.zeni.core.data.mappers.toDto
import com.zeni.core.data.remote.api.HotelApiService
import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.repository.HotelRepository
import com.zeni.core.util.HotelApiLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepositoryImpl @Inject constructor(
    private val hotelApiService: HotelApiService
): HotelRepository {
    private val gid = "G07"

    override suspend fun getHotels(): List<Hotel> {
        HotelApiLogger.apiOperation("Getting hotels")
        return try {
            val hotels = hotelApiService.getHotels(gid)
            HotelApiLogger.apiOperation("Hotels retrieved successfully")

            hotels.map { it.toDomain() }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting hotels: ${e.message}", e)
            throw e
        }
    }

    override fun getHotelById(hotelId: String): Flow<Hotel?> {
        HotelApiLogger.apiOperation("Getting hotel with id $hotelId")
        return try {
            flow {
                val hotel = hotelApiService.getHotels(gid).find { it.id == hotelId }
                HotelApiLogger.apiOperation("Hotel retrieved successfully")

                emit(hotel?.toDomain())
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting hotel by id: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getHotelAvailability(
        startDate: String,
        endDate: String,
        hotelId: String?,
        city: String?
    ): List<Hotel> {
        HotelApiLogger.apiOperation("Getting hotel availability from $startDate to $endDate")
        return try {
            val hotels = hotelApiService.getHotelAvailability(
                groupId = gid,
                startDate = startDate,
                endDate = endDate,
                hotelId = hotelId,
                city = city
            )
            HotelApiLogger.apiOperation("Hotel availability retrieved successfully")

            hotels.map { it.toDomain() }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting hotel availability: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getReservations(guestEmail: String?): List<Reservation> {
        HotelApiLogger.apiOperation("Getting reservations for guest $guestEmail")
        return try {
            val reservations = hotelApiService.getReservations(gid, guestEmail)
            HotelApiLogger.apiOperation("Reservations retrieved successfully")

            reservations.map { it.toDomain() }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting reservations: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getReservations(): List<Reservation> {
        HotelApiLogger.apiOperation("Getting all reservations")
        return try {
            val reservations = hotelApiService.getReservations()
            HotelApiLogger.apiOperation("All reservations retrieved successfully")

            reservations.map { it.toDomain() }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting all reservations: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getReservationById(reservationId: String): Reservation {
        HotelApiLogger.apiOperation("Getting reservation with id $reservationId")
        return try {
            val reservation = hotelApiService.getReservationById(reservationId)
            HotelApiLogger.apiOperation("Reservation retrieved successfully")

            reservation.toDomain()
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting reservation by id: ${e.message}", e)
            throw e
        }
    }

    override suspend fun reserveHotel(reservation: Reservation) {
        HotelApiLogger.apiOperation("Reserving hotel with id ${reservation.hotelId}")
        try {
            hotelApiService.reserveHotel(
                groupId = gid,
                reservation = reservation.toDto()
            )
            HotelApiLogger.apiOperation("Hotel reserved successfully")

            // TODO: Save in local db
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error reserving hotel: ${e.message}", e)
            throw e
        }
    }

    override suspend fun cancelReservation(reservation: Reservation) {
        HotelApiLogger.apiOperation("Cancelling reservation in room ${reservation.roomId} for hotel ${reservation.hotelId}")
        try {
            hotelApiService.cancelReservation(
                groupId = gid,
                reservation = reservation.toDto()
            )
            HotelApiLogger.apiOperation("Reservation cancelled successfully")
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error cancelling reservation: ${e.message}", e)
            throw e
        }
    }

    override suspend fun cancelReservationById(reservationId: String) {
        HotelApiLogger.apiOperation("Deleting reservation with id $reservationId")
        try {
            hotelApiService.deleteReservationById(reservationId)
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error deleting reservation by id: ${e.message}", e)
            throw e
        }
    }
}