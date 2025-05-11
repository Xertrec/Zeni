package com.zeni.hotel.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.zeni.R
import com.zeni.core.domain.model.Room
import com.zeni.core.presentation.components.shimmerEffect
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.hotel.presentation.components.RoomViewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import com.zeni.core.domain.utils.SelectableDatesNotPast
import me.saket.telephoto.zoomable.rememberZoomablePeekOverlayState
import me.saket.telephoto.zoomable.zoomablePeekOverlay
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

@Composable
fun RoomScreen(
    viewModel: RoomViewModel,
    navController: NavController
) {
    val hotel by viewModel.hotel.collectAsStateWithLifecycle()
    val room by viewModel.room.collectAsStateWithLifecycle()
    if (room == null || hotel == null) return

    val startDate by viewModel.startDateTime.collectAsStateWithLifecycle()
    val endDate by viewModel.endDateTime.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                roomName = stringResource(room!!.roomType.text),
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(
                room = room!!,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                startDate = startDate,
                endDate = endDate
            )
        }
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.Top
            )
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .padding(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    Text(
                        text = hotel!!.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            item {
                RoomImageSection(
                    room = room!!,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
            }

            item {
                ReservationDate(
                    startDate = startDate,
                    setStartDate = viewModel::setStartDate,
                    endDate = endDate,
                    setEndDate = viewModel::setEndDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }

//            item {
//                RoomInfo(
//                    room = room!!,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp)
//                )
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    roomName: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.room_type) + " $roomName") },
        navigationIcon = {
            IconButton(onClick = navController::navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun BottomBar(
    room: Room,
    navController: NavController,
    modifier: Modifier = Modifier,
    startDate: ZonedDateTime? = null,
    endDate: ZonedDateTime? = null
) {
    val daysCount = if (startDate != null && endDate != null) {
        ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()).toInt()
    } else {
        1
    }
    val totalPrice = room.price * daysCount

    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = pluralStringResource(
                    id = R.plurals.room_price_prefix,
                    count = daysCount,
                    daysCount
                ),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = R.string.room_price_value, totalPrice),
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Button(
            onClick = {
                TODO("Navigate to reservation details")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = stringResource(id = R.string.reserve_room_button))
        }
    }
}

@Composable
private fun RoomImageSection(
    room: Room,
    modifier: Modifier = Modifier
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        if (room.images.isNotEmpty()) {
            if (room.images.size > 1) {
                val pagerState = rememberPagerState { room.images.size }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clipToBounds()
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 1.5f),
                        pageSize = PageSize.Fixed(pageSize = screenWidth - 48.dp),
                        pageSpacing = 16.dp,
                    ) { page ->

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = MaterialTheme.shapes.extraLarge)
                                .clipToBounds()
                        ) {
                            SubcomposeAsyncImage(
                                model = room.images[page],
                                contentDescription = stringResource(id = R.string.room_image_description),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .zoomablePeekOverlay(
                                        state = rememberZoomablePeekOverlayState()
                                    ),
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .shimmerEffect()
                                    )
                                },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .padding(all = 8.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1} / ${room.images.size}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                // Solo una imagen, sin pager ni contador
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clipToBounds()
                ) {
                    SubcomposeAsyncImage(
                        model = room.images.first(),
                        contentDescription = stringResource(id = R.string.room_image_description),
                        modifier = Modifier
                            .fillMaxSize()
                            .zoomablePeekOverlay(
                                state = rememberZoomablePeekOverlayState()
                            ),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmerEffect()
                            )
                        },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReservationDate(
    startDate: ZonedDateTime?,
    setStartDate: (ZonedDateTime) -> Unit,
    endDate: ZonedDateTime?,
    setEndDate: (ZonedDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    var showStartDatePicker by remember { mutableStateOf(value = false) }
    var showEndDatePicker by remember { mutableStateOf(value = false) }

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Top
        )
    ) {
        Text(
            text = stringResource(id = R.string.reservation_dates_title),
            modifier = Modifier
                .padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )

        Button(
            onClick = { showStartDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = if (startDate != null) {
                    stringResource(
                        id = R.string.start_date_label,
                        startDate.format(dateFormatter)
                    )
                } else {
                    stringResource(id = R.string.select_start_date_button)
                }
            )
        }

        Button(
            onClick = { showEndDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = if (endDate != null) {
                    stringResource(
                        id = R.string.end_date_label,
                        endDate.format(dateFormatter)
                    )
                } else {
                    stringResource(id = R.string.select_end_date_button)
                }
            )
        }
    }

    if (showStartDatePicker) {
        val startDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = startDate?.toInstant()?.toEpochMilli()
                ?: System.currentTimeMillis(),
            selectableDates = SelectableDatesNotPast
        )

        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        startDatePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                            setStartDate(selectedDate)
                        }
                        showStartDatePicker = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.accept_button))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showStartDatePicker = false }) {
                    Text(text = stringResource(id = R.string.cancel_button))
                }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    if (showEndDatePicker) {
        val endDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDate?.toInstant()?.toEpochMilli()
                ?: (System.currentTimeMillis() + 86400000L),
            selectableDates = SelectableDatesNotPast
        )

        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        endDatePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                            setEndDate(selectedDate)
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.accept_button))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEndDatePicker = false }) {
                    Text(text = stringResource(id = R.string.cancel_button))
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }
}

@Composable
private fun RoomInfo(
    room: Room,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        TODO("Add room info here when available")
    }
}
