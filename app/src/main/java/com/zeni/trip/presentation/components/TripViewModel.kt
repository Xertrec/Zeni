package com.zeni.trip.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.data.repository.ItineraryRepositoryImpl
import com.zeni.core.data.repository.TripRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = TripViewModel.TripViewModelFactory::class)
class TripViewModel @AssistedInject constructor(
    @Assisted private val tripName: String,
    tripRepository: TripRepositoryImpl,
    itineraryRepository: ItineraryRepositoryImpl
) : ViewModel() {

    val trip = tripRepository.getTrip(tripName)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val activities = itineraryRepository.getActivitiesByTrip(tripName)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    @AssistedFactory
    interface TripViewModelFactory {
        fun create(tripName: String): TripViewModel
    }
}