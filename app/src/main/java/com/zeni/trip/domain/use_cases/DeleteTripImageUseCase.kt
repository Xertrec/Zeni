package com.zeni.trip.domain.use_cases

import com.zeni.core.data.repository.TripRepositoryImpl
import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.utils.LocalStorage
import com.zeni.core.domain.utils.extensions.completeFileName
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTripImageUseCase @Inject constructor(
    private val tripRepository: TripRepositoryImpl,
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke(imageId: Long) {
        val tripImage = tripRepository.getTripImage(imageId).first()
        tripRepository.deleteTripImage(tripImage)
        localStorage.removeImageFromLocalStorage(tripImage.url.completeFileName)
    }
}