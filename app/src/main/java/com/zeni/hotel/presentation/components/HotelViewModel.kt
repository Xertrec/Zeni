package com.zeni.hotel.presentation.components

import androidx.lifecycle.ViewModel
import com.zeni.trip.presentation.components.TripViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel(assistedFactory = HotelViewModel.HotelViewModelFactory::class)
class HotelViewModel @AssistedInject constructor(
    @Assisted private val hotelId: String,
): ViewModel() {

    @AssistedFactory
    interface HotelViewModelFactory {
        fun create(hotelId: String): HotelViewModel
    }
}