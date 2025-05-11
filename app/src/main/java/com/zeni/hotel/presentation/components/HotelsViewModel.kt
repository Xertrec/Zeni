package com.zeni.hotel.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import com.zeni.hotel.domain.model.City
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
import javax.inject.Inject

@HiltViewModel
class HotelsViewModel @Inject constructor(
    private val hotelRepository: HotelRepositoryImpl
): ViewModel() {

    val cityQuery: StateFlow<City?>
        field = MutableStateFlow<City?>(null)

    val startDate: StateFlow<ZonedDateTime?>
        field = MutableStateFlow<ZonedDateTime?>(null)
    val endDate: StateFlow<ZonedDateTime?>
        field = MutableStateFlow<ZonedDateTime?>(null)
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val hotels = combine(
        cityQuery,
        startDate,
        endDate
    ) { cityQuery, startDate, endDate ->
        Triple(cityQuery, startDate, endDate)
    }
        .flatMapLatest { (cityQuery, startDate, endDate) ->
            if (startDate != null && endDate != null) {
                hotelRepository.getHotelsAvailable(
                    startDate = ZonedDateTimeUtils.toString(startDate),
                    endDate = ZonedDateTimeUtils.toString(endDate),
                    city = cityQuery?.cityName
                )
            } else {
                hotelRepository.getHotels()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList<Hotel>()
        )
    
    fun setCity(query: City) {
        viewModelScope.launch {
            cityQuery.emit(query)
        }
    }
    
    fun setStartDate(date: ZonedDateTime) {
        viewModelScope.launch {
            startDate.emit(date)
        }
    }
    
    fun setEndDate(date: ZonedDateTime) {
        viewModelScope.launch {
            endDate.emit(date)
        }
    }
    
    fun clearSearch() {
        viewModelScope.launch {
            cityQuery.emit(null)
            startDate.emit(null)
            endDate.emit(null)
        }
    }
}
