package com.zeni.hotel.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.domain.model.Hotel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelsViewModel @Inject constructor(
    private val hotelRepository: HotelRepositoryImpl
): ViewModel() {

    val hotels: StateFlow<List<Hotel>>
        field = MutableStateFlow(value = emptyList())

    init {
        viewModelScope.launch {
            hotels.emit(hotelRepository.getHotels())
        }
    }
}