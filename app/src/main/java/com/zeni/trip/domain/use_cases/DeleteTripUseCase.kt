package com.zeni.trip.domain.use_cases

import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.data.repository.TripRepositoryImpl
import com.zeni.core.domain.model.Trip
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTripUseCase @Inject constructor(
    private val tripRepository: TripRepositoryImpl,
    private val hotelRepository: HotelRepositoryImpl
) {
    suspend operator fun invoke(trip: Trip) {
        trip.reservations.forEach { reservation ->
            hotelRepository.cancelReservationById(reservation.id)
        }

        tripRepository.deleteTrip(trip)
    }
}