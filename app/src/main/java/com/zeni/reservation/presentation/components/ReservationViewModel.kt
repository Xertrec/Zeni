package com.zeni.reservation.presentation.components

import android.R.attr.end
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    val reservationStartDateTime = startDate.let { ZonedDateTimeUtils.toZonedDateTime(it) }
    val reservationEndDateTime = endDate.let { ZonedDateTimeUtils.toZonedDateTime(it) }

    suspend fun confirmReservation() {
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