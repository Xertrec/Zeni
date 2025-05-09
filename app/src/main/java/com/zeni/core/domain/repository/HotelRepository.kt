package com.zeni.core.domain.repository

import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.model.Reservation

interface HotelRepository {

    suspend fun reserveHotel(reservation: Reservation)

    suspend fun cancelReservation(reservation: Reservation)

    suspend fun getHotels(): List<Hotel>

    suspend fun getHotelAvailability(
        startDate: String,
        endDate: String,
        hotelId: String? = null,
        city: String? = null
    ): List<Hotel>

    suspend fun getReservations(guestEmail: String? = null): List<Reservation>

    suspend fun getReservations(): List<Reservation>

    suspend fun getReservationById(reservationId: String): Reservation

    suspend fun deleteReservationById(reservationId: String)
}