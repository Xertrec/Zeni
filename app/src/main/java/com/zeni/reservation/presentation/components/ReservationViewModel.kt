package com.zeni.reservation.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.data.repository.TripRepositoryImpl
import com.zeni.core.data.repository.UserRepositoryImpl
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReservationViewModel.ReservationViewModelFactory::class)
class ReservationViewModel @AssistedInject constructor(
    @Assisted("hotelId") private val hotelId: String,
    @Assisted("roomId") private val roomId: String,
    @Assisted("startDate") private val startDate: String,
    @Assisted("endDate") private val endDate: String,
    userRepository: UserRepositoryImpl,
    tripsRepository: TripRepositoryImpl,
    private val hotelRepository: HotelRepositoryImpl
): ViewModel() {

    val user = userRepository.getCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = null
        )

    val hotel = hotelRepository.getHotelById(hotelId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = null
        )

    val room = hotelRepository.getRoomById(hotelId, roomId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = null
        )
    
    val trips = tripsRepository.getTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList()
        )

    val selectedTrip: StateFlow<Trip?>
        field = MutableStateFlow(null)
    
    fun selectTrip(trip: Trip) {
        viewModelScope.launch {
            selectedTrip.emit(trip)
        }
    }

    val isReservationValid = selectedTrip.map { trip ->
        trip != null
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = false
        )

    val reservationStartDateTime = startDate.let { ZonedDateTimeUtils.fromString(it) }
    val reservationEndDateTime = endDate.let { ZonedDateTimeUtils.fromString(it) }

    suspend fun confirmReservation(): String {
        return hotelRepository.reserveRoom(
            Reservation(
                id = "",
                hotelId = hotelId,
                roomId = roomId,
                startDate = reservationStartDateTime,
                endDate = reservationEndDateTime,
                guestName = user.value!!.username,
                guestEmail = user.value!!.email
            ),
            tripName = selectedTrip.value!!.name
        )
    }

    @AssistedFactory
    interface ReservationViewModelFactory {
        fun create(
            @Assisted("hotelId") hotelId: String,
            @Assisted("roomId") roomId: String,
            @Assisted("startDate") startDate: String,
            @Assisted("endDate") endDate: String
        ): ReservationViewModel
    }
}
