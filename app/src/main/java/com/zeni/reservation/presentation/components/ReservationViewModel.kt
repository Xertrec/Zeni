package com.zeni.reservation.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.hotel.presentation.components.RoomViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel(assistedFactory = ReservationViewModel.ReservationViewModelFactory::class)
class ReservationViewModel @AssistedInject constructor(
    @Assisted("hotelId") private val hotelId: String,
    @Assisted("roomId") private val roomId: String,
    @Assisted("startDate") private val startDate: String,
    @Assisted("endDate") private val endDate: String,
    hotelRepository: HotelRepositoryImpl
): ViewModel() {

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