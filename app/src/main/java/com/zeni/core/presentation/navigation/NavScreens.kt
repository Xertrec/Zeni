package com.zeni.core.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ScreenRegister
@Serializable
object ScreenLogin
@Serializable
object ScreenRecoverPassword

@Serializable
object ScreenVerifyEmail

@Serializable
object ScreenInitial

@Serializable
object ScreenHotels
@Serializable
data class ScreenHotel(
    val hotelId: String,
    val startDate: String?,
    val endDate: String?
)
@Serializable
data class ScreenRoom(
    val hotelId: String,
    val roomId: String,
    val startDate: String?,
    val endDate: String?
)

@Serializable
object ScreenTrips
@Serializable
data class ScreenUpsertTrip(
    val tripName: String? = null,
    val toReserve: Boolean = false
)
@Serializable
object ScreenSelectTrip
@Serializable
data class ScreenTrip(
    val tripName: String
)
@Serializable
data class ScreenTripImageViewer(
    val tripName: String,
    val initialImageUri: String
)

@Serializable
data class ScreenConfirmReservation(
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String
)
@Serializable
data class ScreenReservationInfo(
    val reservationId: String
)

@Serializable
data class ScreenUpsertActivity(
    val tripName: String,
    val activityId: Long? = null
)
@Serializable
data class ScreenItinerary(
    val itineraryId: Long
)

@Serializable
object ScreenMore

@Serializable
object ScreenChangePassword

@Serializable
object ScreenProfile

@Serializable
object ScreenSettings

@Serializable
object ScreenTerms

@Serializable
object ScreenAbout