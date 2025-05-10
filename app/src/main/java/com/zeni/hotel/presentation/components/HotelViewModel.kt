package com.zeni.hotel.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@HiltViewModel(assistedFactory = HotelViewModel.HotelViewModelFactory::class)
class HotelViewModel @AssistedInject constructor(
    @Assisted("hotelId") private val hotelId: String,
    @Assisted("startDate") private val startDate: String?,
    @Assisted("endDate") private val endDate: String?,
    hotelRepository: HotelRepositoryImpl
): ViewModel() {

    val startDateTime: StateFlow<ZonedDateTime?>
        field = MutableStateFlow(value = startDate?.let { ZonedDateTimeUtils.toZonedDateTime(it) })
    val endDateTime: StateFlow<ZonedDateTime?>
        field = MutableStateFlow(value = endDate?.let { ZonedDateTimeUtils.toZonedDateTime(it) })

    @OptIn(ExperimentalCoroutinesApi::class)
    val hotel = startDateTime.combine(endDateTime) { start, end ->
        Pair(start, end)
    }
        .flatMapLatest { (start, end) ->
            if (start != null && end != null) {
                hotelRepository.getHotelAvailability(
                    startDate = ZonedDateTimeUtils.toString(start),
                    endDate = ZonedDateTimeUtils.toString(end),
                    hotelId = hotelId
                )
            } else {
                hotelRepository.getHotelById(hotelId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = null
        )

    fun setStartDate(date: ZonedDateTime) {
        viewModelScope.launch {
            startDateTime.emit(date)
        }
    }
    fun setEndDate(date: ZonedDateTime) {
        viewModelScope.launch {
            endDateTime.emit(date)
        }
    }

    @AssistedFactory
    interface HotelViewModelFactory {
        fun create(
            @Assisted("hotelId") hotelId: String,
            @Assisted("startDate") startDate: String?,
            @Assisted("endDate") endDate: String?
        ): HotelViewModel
    }
}