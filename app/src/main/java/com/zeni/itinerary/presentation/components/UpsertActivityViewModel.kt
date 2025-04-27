package com.zeni.itinerary.presentation.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.data.repository.ItineraryRepositoryImpl
import com.zeni.core.data.repository.TripRepositoryImpl
import com.zeni.core.domain.model.Activity
import com.zeni.core.domain.model.Trip
import com.zeni.itinerary.domain.use_cases.use_cases.UpsertActivityUseCase
import com.zeni.itinerary.domain.use_cases.use_cases.DeleteItineraryUseCase
import com.zeni.itinerary.domain.use_cases.utils.UpsertItineraryError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@HiltViewModel(assistedFactory = UpsertActivityViewModel.UpsertItineraryViewModelFactory::class)
class UpsertActivityViewModel @AssistedInject constructor(
    @Assisted private val tripName: String,
    @Assisted private val activityId: Long? = null,
    private val tripRepository: TripRepositoryImpl,
    private val itineraryRepository: ItineraryRepositoryImpl,
    private val upsertItineraryUseCase: UpsertActivityUseCase,
    private val deleteItineraryUseCase: DeleteItineraryUseCase
) : ViewModel() {

    val isEditing: Boolean
        get() = activityId != null

    val trip: StateFlow<Trip?>
        field = MutableStateFlow(value = null)

    val title: StateFlow<String>
        field = MutableStateFlow(value = "")
    val isTitleCorrect: StateFlow<Boolean>
        field = MutableStateFlow(true)
    fun setTitle(value: String) {
        viewModelScope.launch {
            title.emit(value)

            if (!isTitleCorrect.value) {
                isTitleCorrect.emit(value = true)
            }
        }
    }
    suspend fun verifyTitle(): Boolean {
        val isCorrect = title.value.isNotBlank()
        isTitleCorrect.emit(value = isCorrect)

        return isCorrect
    }

    val description: StateFlow<String>
        field = MutableStateFlow(value = "")
    val isDescriptionCorrect: StateFlow<Boolean>
        field = MutableStateFlow(true)
    fun setDescription(value: String) {
        viewModelScope.launch {
            description.emit(value)

            if (!isDescriptionCorrect.value) {
                isDescriptionCorrect.emit(value = true)
            }
        }
    }
    suspend fun verifyDescription(): Boolean {
        val isCorrect = description.value.isNotBlank()
        isDescriptionCorrect.emit(value = isCorrect)

        return isCorrect
    }

    val isDateTimeCorrect: StateFlow<Boolean>
        field = MutableStateFlow(true)
    suspend fun verifyDateTime(): Boolean {
        val isCorrect = dateTime.value != null && dateTime.value!!.isAfter(ZonedDateTime.now()) &&
                (dateTime.value!! == trip.value!!.startDate || dateTime.value!!.isAfter(trip.value!!.startDate)) &&
                (dateTime.value!! == trip.value!!.endDate || dateTime.value!!.isBefore(trip.value!!.endDate))
        isDateTimeCorrect.emit(value = isCorrect)

        return isCorrect
    }

    val date: StateFlow<LocalDate?>
        field = MutableStateFlow(value = null)
    fun setDate(value: LocalDate) {
        viewModelScope.launch {
            date.emit(value)

            if (!isDateTimeCorrect.value) {
                isDateTimeCorrect.emit(value = true)
            }
        }
    }

    val time: StateFlow<LocalTime?>
        field = MutableStateFlow(value = null)
    fun setTime(value: LocalTime) {
        viewModelScope.launch {
            time.emit(value)

            if (!isDateTimeCorrect.value) {
                isDateTimeCorrect.emit(value = true)
            }
        }
    }

    val dateTime = date.combine(time) { date, time ->
        if (date == null || time == null) {
            null
        } else {
            ZonedDateTime.of(date, time, ZoneId.systemDefault())
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        viewModelScope.launch {
            if (isEditing) {
                // Edit the activity
                Log.d(UpsertActivityViewModel::class.java.simpleName, "Editing activity $activityId")
                val editingActivity = itineraryRepository.getActivity(tripName, activityId!!).firstOrNull() ?: return@launch
                title.emit(editingActivity.title)
                description.emit(editingActivity.description)

                val savedDate = editingActivity.dateTime
                date.emit(savedDate.toLocalDate())
                time.emit(savedDate.toLocalTime())
            }
            trip.emit(tripRepository.getTrip(tripName).first())
        }
    }

    val addingError: StateFlow<UpsertItineraryError>
        field = MutableStateFlow(UpsertItineraryError.NONE)
    fun onDismissError() {
        viewModelScope.launch {
            addingError.emit(UpsertItineraryError.NONE)
        }
    }

    suspend fun addActivity(): Long? {
        val titleCorrect = verifyTitle()
        val descriptionCorrect = verifyDescription()
        val dateTimeCorrect = verifyDateTime()

        if (!titleCorrect || !descriptionCorrect || !dateTimeCorrect) {
            when {
                title.value.isBlank() && description.value.isBlank() && description.value.isBlank() -> {
                    addingError.emit(value = UpsertItineraryError.EMPTY_FIELDS)
                }
                title.value.isBlank() -> addingError.emit(value = UpsertItineraryError.TITLE_EMPTY)
                description.value.isBlank() -> addingError.emit(value = UpsertItineraryError.DESCRIPTION_EMPTY)
                dateTime.value == null -> addingError.emit(value = UpsertItineraryError.DATE_TIME_EMPTY)
                dateTime.value!!.isBefore(ZonedDateTime.now()) -> addingError.emit(value = UpsertItineraryError.DATE_TIME_BEFORE_NOW)
                !dateTimeCorrect -> addingError.emit(value = UpsertItineraryError.DATE_TIME_NOT_IN_TRIP_PERIOD)
                else -> addingError.emit(value = UpsertItineraryError.NONE)
            }

            return null
        }

        val itineraryId = upsertItineraryUseCase(
            Activity(
                id = activityId ?: 0,
                tripName = tripName,
                title = title.value,
                description = description.value,
                dateTime = dateTime.value!!,
                userOwner = trip.value!!.userOwner
            )
        )

        return itineraryId
    }

    suspend fun deleteItinerary() = deleteItineraryUseCase(tripName, activityId!!)

    @AssistedFactory
    interface UpsertItineraryViewModelFactory {
        fun create(tripName: String, activityId: Long?): UpsertActivityViewModel
    }
}