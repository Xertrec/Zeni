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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@HiltViewModel(assistedFactory = RoomViewModel.RoomViewModelFactory::class)
class RoomViewModel @AssistedInject constructor(
    @Assisted("hotelId") private val hotelId: String,
    @Assisted("roomId") private val roomId: String,
    @Assisted("startDate") private val startDate: String?,
    @Assisted("endDate") private val endDate: String?,
    hotelRepository: HotelRepositoryImpl
): ViewModel() {

    val startDateTime: StateFlow<ZonedDateTime?>
        field = MutableStateFlow(value = startDate?.let { ZonedDateTimeUtils.fromString(it) })
    val endDateTime: StateFlow<ZonedDateTime?>
        field = MutableStateFlow(value = endDate?.let { ZonedDateTimeUtils.fromString(it) })

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
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val isRoomAvailable = combine(startDateTime, endDateTime) { start, end ->
        Pair(start, end)
    }.flatMapLatest { (start, end) ->
        if (start == null || end == null) {
            flowOf(true)
        } else {
            hotelRepository.isRoomAvailable(
                hotelId = hotelId,
                roomId = roomId,
                startDate = ZonedDateTimeUtils.toString(start),
                endDate = ZonedDateTimeUtils.toString(end)
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = startDateTime.value == null || endDateTime.value == null
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
    interface RoomViewModelFactory {
        fun create(
            @Assisted("hotelId") hotelId: String,
            @Assisted("roomId") roomId: String,
            @Assisted("startDate") startDate: String?,
            @Assisted("endDate") endDate: String?
        ): RoomViewModel
    }
}
