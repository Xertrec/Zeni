package com.zeni.hotel.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.zeni.R
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import com.zeni.core.presentation.components.horizontalAnimationAppearance
import com.zeni.core.presentation.navigation.ScreenHotel
import com.zeni.hotel.domain.model.City
import com.zeni.hotel.presentation.components.HotelCard
import com.zeni.hotel.presentation.components.HotelsViewModel
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun HotelsScreen(
    viewModel: HotelsViewModel,
    navController: NavController
) {
    val hotels by viewModel.hotels.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.Start
    ) {
        if (hotels.isEmpty()) {
            // TODO: Improve
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(
                items = hotels,
                key = { hotel -> hotel.id }
            ) { hotel ->
                HotelCard(
                    hotel = hotel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalAnimationAppearance()
                        .height(height = 128.dp),
                    onClick = {
                        navController.navigate(
                            route = ScreenHotel(
                                hotelId = hotel.id,
                                startDate = startDate?.let { ZonedDateTimeUtils.toString(it) },
                                endDate = endDate?.let { ZonedDateTimeUtils.toString(it) }
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SearchHotelBar(
    cityQuery: City?,
    onCityQueryChange: (City) -> Unit,
    startDate: ZonedDateTime?,
    endDate: ZonedDateTime?,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onCloseSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(value = false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onCloseSearch) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }

                    Text(
                        text = if (cityQuery == null) stringResource(R.string.select_city)
                        else stringResource(cityQuery.translatableText)
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                City.entries.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(stringResource(city.translatableText)) },
                        onClick = {
                            onCityQueryChange(city)
                            expanded = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onStartDateClick,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null
                    )
                    Text(
                        text = startDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            ?: stringResource(R.string.start_date_default_text)
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )

            Button(
                onClick = onEndDateClick,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null
                    )
                    Text(
                        text = endDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            ?: stringResource(R.string.end_date_default_text)
                    )
                }
            }
        }
    }

    BackHandler(enabled = true) {
        onCloseSearch()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDatePickerDialog(
    state: DatePickerState,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val date = ZonedDateTime.ofInstant(
                            Instant.ofEpochMilli(millis),
                            ZoneId.systemDefault()
                        ).with(LocalTime.NOON)
                        onDateSelected(date)
                    }
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.confirm_button))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    ) {
        DatePicker(state = state)
    }
}