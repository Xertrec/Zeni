package com.zeni.reservation.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.model.Room
import com.zeni.trip.presentation.components.TripViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = ReservationInfoViewModel.ReservationInfoViewModelFactory::class)
class ReservationInfoViewModel @AssistedInject constructor(
    @Assisted private val reservationId: String,
    private val hotelRepository: HotelRepositoryImpl
): ViewModel() {
    
    val reservation = hotelRepository.getReservationById(reservationId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = null
        )
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val hotel = reservation.flatMapLatest { reservation ->
        reservation?.let {
            hotelRepository.getHotelById(it.hotelId)
        } ?: flowOf(null)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = null
    )
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val room = reservation.flatMapLatest { reservation ->
        reservation?.let {
            hotelRepository.getRoomById(it.hotelId, it.roomId)
        } ?: flowOf(null)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = null
    )
    
    suspend fun cancelReservation() {
        hotelRepository.cancelReservationById(reservationId)
    }

    @AssistedFactory
    interface ReservationInfoViewModelFactory {
        fun create(reservationId: String): ReservationInfoViewModel
    }
}
