package com.zeni.trip.presentation

import android.R.attr.name
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zeni.R
import com.zeni.core.domain.utils.SelectableDatesNotPast
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.navigation.ScreenInitial
import com.zeni.core.presentation.navigation.ScreenTrip
import com.zeni.trip.domain.utils.UpsertTripError
import com.zeni.trip.presentation.components.UpsertTripViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertTripScreen(
    viewModel: UpsertTripViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val name by viewModel.name.collectAsStateWithLifecycle()
    val isNameCorrect by viewModel.isNameCorrect.collectAsStateWithLifecycle()

    val destinationName by viewModel.destination.collectAsStateWithLifecycle()
    val isDestinationCorrect by viewModel.isDestinationCorrect.collectAsStateWithLifecycle()

    var isStartDatePickerOpen by remember { mutableStateOf(value = false) }
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val isStartDateCorrect by viewModel.isStartDateCorrect.collectAsStateWithLifecycle()

    var isEndDatePickerOpen by remember { mutableStateOf(value = false) }
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()
    val isEndDateCorrect by viewModel.isEndDateCorrect.collectAsStateWithLifecycle()

    val reservations by viewModel.reservations.collectAsStateWithLifecycle()
    val addingError by viewModel.addingError.collectAsStateWithLifecycle()

    var validationToDelete by remember { mutableStateOf(value = false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                navController = navController,
                isEditing = viewModel.isEditing
            )
        },
        bottomBar = {
            BottomBar(
                viewModel = viewModel,
                navController = navController,
                onDeleteClick = { validationToDelete = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            if (!viewModel.isEditing) {
                OutlinedTextField(
                    value = name,
                    onValueChange = viewModel::setName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.name_text_label)
                        )
                    },
                    isError = !isNameCorrect,
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
            }

            OutlinedTextField(
                value = destinationName,
                onValueChange = viewModel::setDestination,
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.destination_text_label)
                    )
                },
                isError = !isDestinationCorrect,
                singleLine = true,
                shape = MaterialTheme.shapes.large
            )

            OutlinedButton(
                onClick = { isStartDatePickerOpen = true },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isStartDateCorrect) ButtonDefaults.outlinedButtonColors().containerColor
                    else MaterialTheme.colorScheme.errorContainer,
                    contentColor = if (isStartDateCorrect) ButtonDefaults.outlinedButtonColors().contentColor
                    else MaterialTheme.colorScheme.error
                )
            ) {
                if (startDate != null) {
                    val formatter = DateTimeFormatter.ofPattern("dd/M/yy")
                    Text(
                        text = stringResource(
                            id = R.string.select_start_date_text,
                            startDate!!.format(formatter)
                        )
                    )
                } else {
                    Text(
                        text = stringResource(R.string.select_start_date_text_label),
                        fontSize = 14.sp
                    )
                }
            }

            OutlinedButton(
                onClick = { isEndDatePickerOpen = true },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isEndDateCorrect) ButtonDefaults.outlinedButtonColors().containerColor
                    else MaterialTheme.colorScheme.errorContainer,
                    contentColor = if (isEndDateCorrect) ButtonDefaults.outlinedButtonColors().contentColor
                    else MaterialTheme.colorScheme.error
                )
            ) {
                if (endDate != null) {
                    val formatter = DateTimeFormatter.ofPattern("dd/M/yy")
                    Text(
                        text = stringResource(
                            id = R.string.select_end_date_text,
                            endDate!!.format(formatter)
                        )
                    )
                } else {
                    Text(text = stringResource(R.string.select_end_date_text_label))
                }
            }
        }
    }

    if (isStartDatePickerOpen) {
        DatePickers(
            onClose = { isStartDatePickerOpen = false },
            onSelectedDate = viewModel::setStartDate,
            initialSelectedDateMillis = startDate?.toLocalDate()
                ?.atStartOfDay(ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli()
        )
    }
    if (isEndDatePickerOpen) {
        DatePickers(
            onClose = { isEndDatePickerOpen = false },
            onSelectedDate = viewModel::setEndDate,
            initialSelectedDateMillis = endDate?.toLocalDate()
                ?.atStartOfDay(ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli()
        )
    }

    if (addingError != UpsertTripError.NONE) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissError,
            confirmButton = {
                Button(
                    onClick = viewModel::onDismissError
                ) {
                    Text(text = stringResource(id = R.string.accept_button))
                }
            },
            icon = {
                Icon(
                    painterResource(R.drawable.icon_alert_empty),
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = when (addingError) {
                        UpsertTripError.EMPTY_FIELDS -> stringResource(R.string.error_creating_trip_fields_empty)
                        UpsertTripError.START_AND_END_DATE_EMPTY -> stringResource(R.string.error_creating_trip_start_and_end_empty)
                        UpsertTripError.NAME_EMPTY -> stringResource(R.string.error_creating_trip_name_empty)
                        UpsertTripError.DESTINATION_EMPTY -> stringResource(R.string.error_creating_trip_destination_empty)
                        UpsertTripError.START_DATE_EMPTY -> stringResource(R.string.error_creating_trip_start_empty)
                        UpsertTripError.END_DATE_EMPTY -> stringResource(R.string.error_creating_trip_end_empty)
                        UpsertTripError.NAME_ALREADY_EXISTS -> stringResource(R.string.error_creating_trip_name_already_exists)
                        UpsertTripError.START_DATE_BEFORE_TODAY -> stringResource(R.string.error_creating_trip_start_in_past)
                        UpsertTripError.END_DATE_BEFORE_START_DATE -> stringResource(R.string.error_creating_trip_end_before_start)
                        UpsertTripError.NONE -> ""
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        )
    }

    if (validationToDelete) {
        AlertDialog(
            onDismissRequest = { validationToDelete = false },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.deleteTrip()
                            validationToDelete = false
                            navController.navigate(ScreenInitial) {
                                popUpTo(ScreenInitial) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = stringResource(id = R.string.confirm_delete))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { validationToDelete = false }
                ) {
                    Text(text = stringResource(id = R.string.cancel_button))
                }
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.icon_alert_empty),
                    contentDescription = null
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.advert_title_deleting_trip),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = if (reservations.isEmpty()) stringResource(R.string.advert_text_deleting_trip)
                    else stringResource(R.string.advert_text_deleting_trip_with_reservations),
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavController,
    isEditing: Boolean = false
) {
    TopAppBar(
        title = {
            if (isEditing) Text(text = stringResource(R.string.editing_trip_title))
            else Text(text = stringResource(R.string.add_trip_title))
        },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun BottomBar(
    viewModel: UpsertTripViewModel,
    navController: NavController,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val tripName by viewModel.name.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()

    Row(
        modifier = modifier
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        // Delete button
        if (viewModel.isEditing) {
            FilledIconButton(
                onClick = onDeleteClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null
                )
            }
        }

        Button(
            onClick = {
                scope.launch {
                    if (viewModel.addTrip()) {
                        if (viewModel.toReserve) {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set<String>("selected_trip", tripName)
                            navController.navigateBack()
                        } else {
                            navController.popBackStack()
                            navController.navigate(ScreenTrip(tripName = tripName)) {
                                popUpTo<ScreenTrip> {
                                    inclusive = true
                                }
                            }
                        }

                    }
                }
            },
            modifier = Modifier
                .weight(weight = 1f),
            enabled = isFormValid
        ) {
            Text(
                text = if (viewModel.isEditing) stringResource(R.string.save_trip_button)
                else stringResource(R.string.add_trip_button)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickers(
    onClose: () -> Unit,
    onSelectedDate: (ZonedDateTime) -> Unit,
    initialSelectedDateMillis: Long? = null,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis ?: LocalDate.now()
            .plusDays(1).atStartOfDay()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli(),
        selectableDates = SelectableDatesNotPast
    )

    DatePickerDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(
                onClick = {
                    onClose()
                    onSelectedDate(
                        ZonedDateTime.of(
                            LocalDateTime.ofEpochSecond(
                                datePickerState.selectedDateMillis!! / 1000,
                                0,
                                ZoneOffset.UTC
                            ),
                            ZoneId.systemDefault()
                        )
                    )
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = stringResource(id = R.string.accept_button))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onClose) {
                Text(text = stringResource(id = R.string.cancel_button))
            }
        },
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}