package com.zeni.core.domain.repository

import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.model.Reservation
import kotlinx.coroutines.flow.Flow

interface HotelRepository {

    suspend fun getHotels(): List<Hotel>

    fun getHotelById(hotelId: String): Flow<Hotel?>

    suspend fun getHotelAvailability(
        startDate: String,
        endDate: String,
        hotelId: String? = null,
        city: String? = null
    ): List<Hotel>

    suspend fun getReservations(): List<Reservation>

    suspend fun getReservations(guestEmail: String? = null): List<Reservation>

    suspend fun getReservationById(reservationId: String): Reservation

    suspend fun reserveHotel(reservation: Reservation)

    suspend fun cancelReservation(reservation: Reservation)

    suspend fun cancelReservationById(reservationId: String)
}