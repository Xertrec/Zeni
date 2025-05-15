package com.zeni.trip.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.TripRepositoryImpl
import com.zeni.core.domain.model.Trip
import com.zeni.trip.domain.use_cases.DeleteTripUseCase
import com.zeni.trip.domain.use_cases.UpsertTripUseCase
import com.zeni.trip.domain.utils.UpsertTripError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@HiltViewModel(assistedFactory = UpsertTripViewModel.UpsertTripViewModelFactory::class)
class UpsertTripViewModel @AssistedInject constructor(
    @Assisted private val tripName: String? = null,
    @Assisted val toReserve: Boolean = false,
    private val tripRepository: TripRepositoryImpl,
    private val upsertTripUseCase: UpsertTripUseCase,
    private val deleteTripUseCase: DeleteTripUseCase
) : ViewModel() {

    val isEditing: Boolean
        get() = tripName != null

    val name: StateFlow<String>
        field = MutableStateFlow(tripName ?: "")
    val isNameCorrect: StateFlow<Boolean>
        field = MutableStateFlow(true)
    fun setName(value: String) {
        viewModelScope.launch {
            name.emit(value)

            if (!isNameCorrect.value) {
                isNameCorrect.emit(value = true)
            }
        }
    }
    suspend fun verifyName(): Boolean {
        val isCorrect = name.value.isNotBlank() && (isEditing || !tripRepository.existsTrip(name.value))
        isNameCorrect.emit(value = isCorrect)

        return isCorrect
    }

    val destination: StateFlow<String>
        field = MutableStateFlow("")
    val isDestinationCorrect: StateFlow<Boolean>
        field = MutableStateFlow(true)
    fun setDestination(value: String) {
        viewModelScope.launch {
            destination.emit(value)

            if (!isDestinationCorrect.value) {
                isDestinationCorrect.emit(value = true)
            }
        }
    }
    suspend fun verifyDestination(): Boolean {
        val isCorrect = destination.value.isNotBlank()
        isDestinationCorrect.emit(value = isCorrect)

        return isCorrect
    }

    val startDate: StateFlow<ZonedDateTime?>
        field = MutableStateFlow(null)
    val isStartDateCorrect: StateFlow<Boolean>
        field = MutableStateFlow(true)
    fun setStartDate(value: ZonedDateTime) {
        viewModelScope.launch {
            val localDate = value.toLocalDate()
            val selectedStartDate = localDate
                .atTime(LocalTime.MIN)
                .atZone(value.zone)

            startDate.emit(selectedStartDate)

            if (!isStartDateCorrect.value) {
                isStartDateCorrect.emit(value = true)
            }
        }
    }
    suspend fun verifyStartDate(): Boolean {
        val today = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val isCorrect = startDate.value != null && (startDate.value == today ||
                startDate.value?.isAfter(today) == true)
        isStartDateCorrect.emit(value = isCorrect)

        return isCorrect
    }

    val endDate: StateFlow<ZonedDateTime?>
        field = MutableStateFlow(null)
    val isEndDateCorrect: StateFlow<Boolean>
        field = MutableStateFlow(true)
    fun setEndDate(value: ZonedDateTime) {
        viewModelScope.launch {
            val localDate = value.toLocalDate()
            val selectedEndDate = localDate
                .atTime(LocalTime.MAX)
                .atZone(value.zone)

            endDate.emit(selectedEndDate)

            if (!isEndDateCorrect.value) {
                isEndDateCorrect.emit(value = true)
            }
        }
    }
    suspend fun verifyEndDate(): Boolean {
        val isCorrect = endDate.value != null && (endDate.value == startDate.value ||
                endDate.value?.isAfter(startDate.value?.truncatedTo(ChronoUnit.DAYS)) == true)
        isEndDateCorrect.emit(value = isCorrect)

        return isCorrect
    }

    init {
        if (isEditing) {
            // Edit a trip
            viewModelScope.launch {
                val editingTrip = tripRepository.getTrip(tripName!!).first()
                destination.emit(editingTrip.destination)
                startDate.emit(editingTrip.startDate)
                endDate.emit(editingTrip.endDate)
            }
        }
    }

    val addingError: StateFlow<UpsertTripError>
        field = MutableStateFlow(UpsertTripError.NONE)
    fun onDismissError() {
        viewModelScope.launch {
            addingError.emit(UpsertTripError.NONE)
        }
    }

    suspend fun addTrip(): Boolean {
        val nameCorrect = verifyName()
        val destinationCorrect = verifyDestination()
        val startDateCorrect = verifyStartDate()
        val endDateCorrect = verifyEndDate()
        if (!nameCorrect || !destinationCorrect || !startDateCorrect || !endDateCorrect) {
            when {
                destination.value.isBlank() && name.value.isBlank() && startDate.value == null && endDate.value == null -> addingError.emit(UpsertTripError.EMPTY_FIELDS)
                startDate.value == null && endDate.value == null -> addingError.emit(UpsertTripError.START_AND_END_DATE_EMPTY)
                name.value.isBlank() -> addingError.emit(UpsertTripError.NAME_EMPTY)
                destination.value.isBlank() -> addingError.emit(UpsertTripError.DESTINATION_EMPTY)
                startDate.value == null -> addingError.emit(UpsertTripError.START_DATE_EMPTY)
                endDate.value == null -> addingError.emit(UpsertTripError.END_DATE_EMPTY)
                !nameCorrect -> addingError.emit(UpsertTripError.NAME_ALREADY_EXISTS)
                !startDateCorrect -> addingError.emit(UpsertTripError.START_DATE_BEFORE_TODAY)
                !endDateCorrect -> addingError.emit(UpsertTripError.END_DATE_BEFORE_START_DATE)
            }
            return false
        }

        upsertTripUseCase(
            Trip(
                name = tripName ?: name.value,
                destination = destination.value,
                startDate = startDate.value!!,
                endDate = endDate.value!!,
                userOwner = "" // This will be overwritten by the repository
            )
        )

        return true
    }

    suspend fun deleteTrip() {
        deleteTripUseCase(tripRepository.getTrip(tripName!!).first())
    }

    @AssistedFactory
    interface UpsertTripViewModelFactory {
        fun create(tripName: String?, toReserve: Boolean): UpsertTripViewModel
    }
}