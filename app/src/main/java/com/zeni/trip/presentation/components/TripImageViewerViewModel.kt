package com.zeni.trip.presentation.components

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.TripRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = TripImageViewerViewModel.TripImageViewerViewModelFactory::class)
class TripImageViewerViewModel @AssistedInject constructor(
    @Assisted private val tripName: String,
    @Assisted val initialImageUri: Uri,
    tripRepository: TripRepositoryImpl
): ViewModel() {

    val images = tripRepository.getTripImages(tripName)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList()
        )

    @AssistedFactory
    interface TripImageViewerViewModelFactory {
        fun create(tripName: String, initialImageUri: Uri): TripImageViewerViewModel
    }
}