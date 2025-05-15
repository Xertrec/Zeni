package com.zeni.reservation.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.model.Room
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.components.shimmerEffect
import com.zeni.reservation.presentation.components.ReservationInfoViewModel
import kotlinx.coroutines.launch
import me.saket.telephoto.zoomable.rememberZoomablePeekOverlayState
import me.saket.telephoto.zoomable.zoomablePeekOverlay
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ReservationInfoScreen(
    viewModel: ReservationInfoViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val reservation by viewModel.reservation.collectAsStateWithLifecycle()
    val hotel by viewModel.hotel.collectAsStateWithLifecycle()
    val room by viewModel.room.collectAsStateWithLifecycle()
    
    if (reservation == null || hotel == null || room == null) return
    val durationNights by remember {
        derivedStateOf {
            ChronoUnit.DAYS.between(
                reservation!!.startDate.toLocalDate(),
                reservation!!.endDate.toLocalDate()
            ).toInt()
        }
    }

    var showCancelDialog by remember { mutableStateOf(value = false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(navController = navController)
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
                            color = MaterialTheme.colorScheme.primaryContainer,
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
                ReservationDetailsSection(
                    reservation = reservation!!,
                    durationNights = durationNights,
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
                Button(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = stringResource(R.string.cancel_reservation_button),
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { 
                Text(
                    text = stringResource(R.string.alert_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = { 
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DeleteAnimation(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    
                    Text(
                        text = stringResource(R.string.verify_reservation_cancelation_dialog),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            showCancelDialog = false
                            viewModel.cancelReservation()
                            navController.navigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCancelDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.cancel_button))
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.hotel_reservation_title)) },
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
private fun ReservationDetailsSection(
    reservation: Reservation,
    durationNights: Int,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locale
    val dateFormatter = remember {
        DateTimeFormatter
            .ofPattern("d MMMM yyyy", locale)
            .withLocale(locale)
    }
    val timeFormatter = remember {
        DateTimeFormatter.ofPattern("HH:mm")
    }
    
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
            text = stringResource(R.string.reservation_data_title),
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.check_in_at_name_of),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = " " + reservation.guestName,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.duration_label),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = " " + pluralStringResource(
                    id = R.plurals.hotel_duration_nights_value,
                    count = durationNights,
                    durationNights
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.check_in_date_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = " " + reservation.startDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.hotel_check_in_time, ""),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = reservation.startDate.format(timeFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                )
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.check_out_date_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " " + reservation.endDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.hotel_check_out_time, ""),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = reservation.endDate.format(timeFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                )
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
fun DeleteAnimation(
    modifier: Modifier = Modifier,
    circleColor: Color = MaterialTheme.colorScheme.errorContainer,
    crossColor: Color = MaterialTheme.colorScheme.error
) {
    val animatedProgress = remember { Animatable(0f) }
    val circleScale = remember { Animatable(0.95f) }

    LaunchedEffect(key1 = Unit) {
        // Animar la X
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )

        // Animar el círculo con una sola pulsación
        circleScale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
    }

    Canvas(
        modifier = modifier
            .size(100.dp)
            .padding(8.dp)
    ) {
        // Dibujar círculo
        drawCircle(
            color = circleColor,
            radius = size.minDimension / 2 * circleScale.value,
            style = Stroke(width = 8f)
        )

        val progress = animatedProgress.value
        val crossSize = size.minDimension * 0.5f * progress
        val strokeWidth = 10f

        // Dibujar X (línea diagonal 1)
        drawLine(
            color = crossColor,
            start = Offset(
                x = center.x - crossSize / 2,
                y = center.y - crossSize / 2
            ),
            end = Offset(
                x = center.x + crossSize / 2,
                y = center.y + crossSize / 2
            ),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Dibujar X (línea diagonal 2)
        drawLine(
            color = crossColor,
            start = Offset(
                x = center.x + crossSize / 2,
                y = center.y - crossSize / 2
            ),
            end = Offset(
                x = center.x - crossSize / 2,
                y = center.y + crossSize / 2
            ),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

