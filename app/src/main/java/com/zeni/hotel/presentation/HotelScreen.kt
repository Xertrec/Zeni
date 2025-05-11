package com.zeni.hotel.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import android.content.Intent
import android.net.Uri
import coil.compose.SubcomposeAsyncImage
import com.zeni.R
import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.components.shimmerEffect
import com.zeni.hotel.presentation.components.HotelViewModel
import me.saket.telephoto.zoomable.rememberZoomablePeekOverlayState
import me.saket.telephoto.zoomable.zoomablePeekOverlay
import androidx.core.net.toUri
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.draw.clipToBounds
import com.zeni.core.domain.model.Room
import com.zeni.core.presentation.navigation.ScreenRoom
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import android.app.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.SelectableDates
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeni.core.domain.utils.SelectableDatesNotPast
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import com.zeni.hotel.domain.utils.EndSelectableDate
import com.zeni.hotel.domain.utils.StartSelectableDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Composable
fun HotelScreen(
    viewModel: HotelViewModel,
    navController: NavController
) {
    val hotel by viewModel.hotel.collectAsStateWithLifecycle()
    if (hotel == null) return

    val startDate by viewModel.startDateTime.collectAsStateWithLifecycle()
    val endDate by viewModel.endDateTime.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val isTitleVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                hotelName = hotel!!.name,
                navController = navController,
                titleVisible = isTitleVisible
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
            .exclude(WindowInsets.systemBars)
            .exclude(WindowInsets.ime)
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 8.dp),
            state = listState,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.Top
            )
        ) {
            item {
                HotelImageSection(
                    hotel = hotel!!,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
            }

            item {
                HotelInfo(
                    hotel = hotel!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
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

            if (startDate != null && endDate != null) {
                stickyHeader {
                    RoomsHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        afterModifier = Modifier
                            .padding(top = 8.dp)
                    )
                }

                items(
                    items = hotel!!.rooms,
                    key = { room -> room.id },
                    contentType = { Room::class }
                ) { room ->
                    RoomInfo(
                        room = room,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                            .padding(horizontal = 8.dp),
                        onClick = {
                            navController.navigate(
                                route = ScreenRoom(
                                    hotelId = hotel!!.id,
                                    roomId = room.id,
                                    startDate = startDate?.let { ZonedDateTimeUtils.toString(it) },
                                    endDate = endDate?.let { ZonedDateTimeUtils.toString(it) }
                                )
                            )
                        },
                        startDate = startDate,
                        endDate = endDate
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 16.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .padding(all = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.rooms_dates_required),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    hotelName: String,
    navController: NavController,
    titleVisible: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = titleVisible,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                Text(text = hotelName)
            }
        },
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
private fun HotelImageSection(
    hotel: Hotel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        SubcomposeAsyncImage(
            model = hotel.imageUrl,
            contentDescription = "Hotel Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1f)
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .clipToBounds()
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.extraLarge
                )
        ) {
            Text(
                text = hotel.name,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .padding(vertical = 16.dp),
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize * 1.25,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun HotelInfo(
    hotel: Hotel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
            text = stringResource(id = R.string.hotel_info_title),
            modifier = Modifier
                .padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.hotel_address),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = " " + hotel.address,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.hotel_rating),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            repeat(times = 5) { index ->
                val icon = when {
                    index < hotel.rating -> R.drawable.icon_star_fill
                    else -> R.drawable.icon_star_empty
                }

                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "Star ${index + 1}",
                    modifier = Modifier
                        .size(size = 24.dp),
                    tint = if (index < hotel.rating) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    val gmmIntentUri = "geo:0,0?q=${Uri.encode(hotel.address)}".toUri()
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                        setPackage("com.google.android.apps.maps")
                    }
                    context.startActivity(mapIntent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = stringResource(id = R.string.see_in_maps_button))
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
            selectableDates = StartSelectableDate.createSelectableDates(endDate)
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
                ?: (startDate?.toInstant()?.toEpochMilli()?.plus(86400000L)
                ?: (System.currentTimeMillis() + 86400000L)),
            selectableDates = EndSelectableDate.createSelectableDates(startDate)
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
private fun RoomsHeader(
    modifier: Modifier = Modifier,
    afterModifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .then(afterModifier)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.hotel_rooms_title),
            fontSize = MaterialTheme.typography.titleLarge.fontSize * 0.9,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalDivider(
            modifier = Modifier
                .weight(weight = 1f),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun RoomInfo(
    room: Room,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    startDate: ZonedDateTime? = null,
    endDate: ZonedDateTime? = null
) {
    val daysCount = if (startDate != null && endDate != null) {
        ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()).toInt()
    } else {
        1
    }
    val totalPrice = room.price * daysCount

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row {
            Text(
                text = stringResource(id = R.string.room_type),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = " " + stringResource(id = room.roomType.text),
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (room.images.isNotEmpty()) {
            val pagerState = rememberPagerState { room.images.size }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .clip(MaterialTheme.shapes.large)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = 16.dp,
                ) { page ->

                    SubcomposeAsyncImage(
                        model = room.images[page],
                        contentDescription = "Room Image",
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
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
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
}

