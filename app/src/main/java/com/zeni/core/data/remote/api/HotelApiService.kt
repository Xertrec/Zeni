package com.zeni.core.data.remote.api

import com.zeni.core.data.remote.dto.AvailabilityDto
import com.zeni.core.data.remote.dto.HotelDto
import com.zeni.core.data.remote.dto.ReservationDto
import com.zeni.core.data.remote.dto.responses.ReservationResponseBody
import com.zeni.core.data.remote.dto.responses.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApiService {

    @POST("hotels/{group_id}/reserve")
    suspend fun reserveHotel(
        @Path("group_id") groupId: String,
        @Body reservation: ReservationDto
    ): ReservationResponseBody

    @POST("hotels/{group_id}/cancel")
    suspend fun cancelReservation(
        @Path("group_id") groupId: String,
        @Body reservation: ReservationDto
    ): ResponseBody

    @GET("hotels/{group_id}/hotels")
    suspend fun getHotels(@Path("group_id") groupId: String): List<HotelDto>

    @GET("hotels/{group_id}/availability")
    suspend fun getHotelAvailability(
        @Path("group_id") groupId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("hotel_id") hotelId: String? = null,
        @Query("city") city: String? = null
    ): AvailabilityDto

    @GET("hotels/{group_id}/reservations")
    suspend fun getReservations(
        @Path("group_id") groupId: String,
        @Query("guest_email") guestEmail: String? = null
    ): List<ReservationDto>

    @GET("reservations")
    suspend fun getReservations(): List<ReservationDto>

    @GET("reservations/{res_id}")
    suspend fun getReservationById(@Path("res_id") reservationId: String): ReservationDto

    @DELETE("reservations/{res_id}")
    suspend fun deleteReservationById(@Path("res_id") reservationId: String): ResponseBody
}