package com.zeni.trip.domain.use_cases

import android.net.Uri
import com.zeni.core.data.repository.TripRepositoryImpl
import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.model.TripImage
import com.zeni.core.domain.utils.LocalStorage
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class UpsertTripImageUseCase @Inject constructor(
    private val tripRepository: TripRepositoryImpl,
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke(uris: List<Uri>, tripName: String) {
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