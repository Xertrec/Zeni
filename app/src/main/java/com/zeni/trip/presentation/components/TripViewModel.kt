package com.zeni.trip.presentation.components

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.local.database.entities.TripImageEntity
import com.zeni.core.data.repository.HotelRepositoryImpl
import com.zeni.core.data.repository.ItineraryRepositoryImpl
import com.zeni.core.data.repository.TripRepositoryImpl
import com.zeni.core.domain.model.TripImage
import com.zeni.core.domain.utils.LocalStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TripViewModel.TripViewModelFactory::class)
class TripViewModel @AssistedInject constructor(
    @Assisted private val tripName: String,
    private val tripRepository: TripRepositoryImpl,
    itineraryRepository: ItineraryRepositoryImpl,
    private val localStorage: LocalStorage
) : ViewModel() {

    val trip = tripRepository.getTrip(tripName)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = null
        )

    val activities = itineraryRepository.getActivitiesByTrip(tripName)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList()
        )

    fun addMedias(uris: List<Uri>) {
        viewModelScope.launch {
            val tripImages = uris.map { uri ->
                TripImage(
                    id = 0,
                    tripName = tripName,
                    url = localStorage.copyImageToLocalStorage(uri),
                    description = ""
                )
            }
            tripRepository.addTripImages(tripImages)
        }
    }

    @AssistedFactory
    interface TripViewModelFactory {
        fun create(tripName: String): TripViewModel
    }
}