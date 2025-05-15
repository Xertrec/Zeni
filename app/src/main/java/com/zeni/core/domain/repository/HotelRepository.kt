package com.zeni.core.domain.repository

import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.model.Room
import kotlinx.coroutines.flow.Flow

interface HotelRepository {

    fun getHotels(): Flow<List<Hotel>>

    fun getHotelById(hotelId: String): Flow<Hotel?>

    fun getHotelsAvailable(
        startDate: String,
        endDate: String,
        city: String? = null
    ): Flow<List<Hotel>>

    fun getHotelAvailability(
        startDate: String,
        endDate: String,
        hotelId: String,
        city: String? = null
    ): Flow<Hotel?>

    fun getRoomsByHotelId(hotelId: String): Flow<List<Room>>

    fun getRoomById(hotelId: String, roomId: String): Flow<Room?>

    fun isRoomAvailable(
        hotelId: String,
        roomId: String,
        startDate: String,
        endDate: String
    ): Flow<Boolean>

    suspend fun reserveRoom(reservation: Reservation, tripName: String): Long

    fun getReservations(): Flow<List<Reservation>>

    fun getReservations(guestEmail: String? = null): Flow<List<Reservation>>

    fun getReservationById(reservationId: String): Flow<Reservation>

    suspend fun cancelReservation(reservation: Reservation)

    suspend fun cancelReservationById(reservationId: String)
}