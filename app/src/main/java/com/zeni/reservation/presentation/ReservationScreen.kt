package com.zeni.reservation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.zeni.R
import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.model.Room
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.components.shimmerEffect
import com.zeni.reservation.presentation.components.ReservationViewModel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import me.saket.telephoto.zoomable.rememberZoomablePeekOverlayState
import me.saket.telephoto.zoomable.zoomablePeekOverlay
import java.time.temporal.ChronoUnit

@Composable
fun ReservationScreen(
    viewModel: ReservationViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val hotel by viewModel.hotel.collectAsStateWithLifecycle()
    val room by viewModel.room.collectAsStateWithLifecycle()
    if (hotel == null || room == null) return

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = stringResource(R.string.reservation_title),
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(
                room = room!!,
                onConfirmClick = {
                    scope.launch {
                        viewModel.confirmReservation()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                startDate = viewModel.reservationStartDateTime,
                endDate = viewModel.reservationEndDateTime
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
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
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
                HotelImageSection(
                    hotel = hotel!!,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                HotelDetailsSection(
                    hotel = hotel!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }

            item {
                RoomInfoSection(
                    room = room!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }

            item {
                ReservationDatesSection(
                    startDate = viewModel.reservationStartDateTime,
                    endDate = viewModel.reservationEndDateTime,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = navController::navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun BottomBar(
    room: Room,
    onConfirmClick: () -> Unit,
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
            .padding(horizontal = 16.dp, vertical = 4.dp),
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
            onClick = onConfirmClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = stringResource(id = R.string.confirm_button))
        }
    }
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
            contentDescription = hotel.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .clip(MaterialTheme.shapes.extraLarge)
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
    }
}

@Composable
private fun HotelDetailsSection(
    hotel: Hotel,
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
        Text(
            text = stringResource(R.string.hotel_information_title),
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.hotel_address_label),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = " " + hotel.address,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.hotel_rating_label),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Row {
                repeat(5) { index ->
                    val icon = if (index < hotel.rating) R.drawable.icon_star_fill else R.drawable.icon_star_empty

                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp),
                        tint = if (index < hotel.rating) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomInfoSection(
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
        Text(
            text = stringResource(R.string.room_details_title),
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.room_type_label),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = " " + stringResource(room.roomType.text),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.room_price_label),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = " " + stringResource(R.string.room_price_value_format, room.price),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (room.images.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.room_image_title),
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyMedium
            )

            RoomImageSection(
                room = room,
                modifier = Modifier
                    .fillMaxWidth()
            )
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

@Composable
private fun ReservationDatesSection(
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.reservation_dates_title),
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.check_in_date_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = startDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.check_out_date_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = endDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
