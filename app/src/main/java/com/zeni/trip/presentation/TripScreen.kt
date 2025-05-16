package com.zeni.trip.presentation

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.zeni.R
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.model.TripImage
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.navigation.ScreenReservationInfo
import com.zeni.core.presentation.navigation.ScreenTripImageViewer
import com.zeni.core.presentation.navigation.ScreenUpsertActivity
import com.zeni.core.presentation.navigation.ScreenUpsertTrip
import com.zeni.itinerary.presentation.components.ActivityInformation
import com.zeni.trip.presentation.components.TripViewModel
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TripScreen(
    viewModel: TripViewModel,
    navController: NavHostController
) {
    val trip by viewModel.trip.collectAsStateWithLifecycle()
    val activities by viewModel.activities.collectAsStateWithLifecycle()
    if (trip == null) return

    val photosPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = viewModel::addMedias
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                navController = navController,
                trip = trip!!
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
                .padding(horizontal = 16.dp),
            contentPadding = WindowInsets.navigationBarsIgnoringVisibility.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.Top
            ),
        ) {
            item {
                TripData(
                    trip = trip!!,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TripImagesGallery(
                    images = trip!!.images,
                    coverImage = trip!!.coverImage,
                    onAddImageClick = {
                        photosPickerLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                                isOrderedSelection = true
                            )
                        )
                    },
                    onImageClick = { imageUri ->
                        navController.navigate(
                            ScreenTripImageViewer(
                                tripName = trip!!.name,
                                initialImageUri = imageUri.toString()
                            )
                        )
                    },
                    onFavoriteImage = viewModel::setCoverImage,
                    onDeleteImage = viewModel::deleteTripImage,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (trip!!.reservations.isNotEmpty()) {
                item {
                    ReservationsData(
                        reservations = trip!!.reservations,
                        onClick = { reservationId ->
                            navController.navigate(ScreenReservationInfo(reservationId))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(
                            start = 4.dp,
                            top = 8.dp,
                            end = 8.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.Start
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.trip_activities_header_text),
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .clip(MaterialTheme.shapes.extraLarge),
                        thickness = 2.dp
                    )

                    IconButton(
                        onClick = {
                            navController.navigate(ScreenUpsertActivity(trip!!.name))
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = LocalContentColor.current.copy(alpha = 0.65f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            }

            if (activities.isNotEmpty()) {
                items(activities) { activity ->
                    ActivityInformation(
                        activity = activity,
                        modifier = Modifier.fillMaxWidth(),
                        onEditClick = {
                            navController.navigate(
                                ScreenUpsertActivity(trip!!.name, activity.id)
                            )
                        },
                        showTimeTo = true
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            space = 16.dp,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.trip_activities_empty_title),
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = stringResource(
                                id = R.string.trip_activities_empty_text,
                                trip!!.destination
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun TopBar(
    navController: NavController,
    trip: Trip
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.trip_title, trip.destination)
            )
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
        },
        actions = {
            IconButton(
                onClick = { navController.navigate(ScreenUpsertTrip(trip.name)) }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        },
        windowInsets = WindowInsets.statusBarsIgnoringVisibility
    )
}

@Composable
private fun TripData(
    trip: Trip,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locale
    val formatter = remember {
        DateTimeFormatter
            .ofPattern("d MMMM yyyy", locale)
            .withLocale(locale)
    }

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clipToBounds()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                vertical = 16.dp,
                horizontal = 16.dp
            )
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
        ) {
            Text(
                text = stringResource(
                    id = R.string.trip_name,
                    trip.name
                ),
                modifier = Modifier
                    .padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(
                    id = R.string.trip_destiny,
                    trip.destination
                ),
                fontSize = 12.sp
            )

            Text(
                text = stringResource(
                    id = R.string.trip_start_date,
                    trip.startDate.format(formatter)
                ),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(
                    id = R.string.trip_end_date,
                    trip.endDate.format(formatter)
                ),
                fontSize = 12.sp
            )
        }

        AnimatedVisibility(
            visible = trip.coverImage != null,
            enter = fadeIn() + slideInHorizontally { it },
            exit = fadeOut() + slideOutHorizontally { it }
        ) {
            var imageUrl by remember { mutableStateOf(value = trip.coverImage?.url) }

            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop
            )

            LaunchedEffect(trip.coverImage?.id) {
                trip.coverImage?.url?.let { imageUrl = it }
            }

            DisposableEffect(Unit) {
                onDispose {
                    imageUrl = null
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReservationsData(
    reservations: List<Reservation>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locale
    val dateFormatter = remember {
        DateTimeFormatter
            .ofPattern("d MMMM yyyy", locale)
            .withLocale(locale)
    }
    val pagerState = rememberPagerState { reservations.size }

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .clipToBounds(),
            pageSpacing = 16.dp,
            key = { page ->
                reservations[page].hotelId + reservations[page].roomId
            }
        ) { page ->
            val reservation = reservations[page]

            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .clipToBounds()
                    .clickable { onClick(reservation.id) }
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.hotel_reservation_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(R.string.hotel_reservation_id, reservation.id),
                        maxLines = 1,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )

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
                        text = stringResource(R.string.check_out_date_label),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = " " + reservation.endDate.format(dateFormatter),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                val days = ChronoUnit.DAYS.between(
                    reservation.startDate.toLocalDate(),
                    reservation.endDate.toLocalDate()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.hotel_duration_nights,
                            count = days.toInt(),
                            days
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (reservations.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                PageIndicator(
                    pagerState = pagerState,
                    pageCount = reservations.size,
                    dotSize = 8.dp,
                    dotSpacing = 6.dp
                )
            }
        }
    }
}

@Composable
private fun TripImagesGallery(
    images: List<TripImage>,
    coverImage: TripImage?,
    onAddImageClick: () -> Unit,
    onImageClick: (String) -> Unit,
    onFavoriteImage: (Long?) -> Unit,
    onDeleteImage: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImageId by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clipToBounds()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(top = 8.dp, bottom = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.trip_images_header),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )

            IconButton(onClick = onAddImageClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )

        if (images.isEmpty()) {
            Text(
                text = stringResource(R.string.trip_images_empty),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(MaterialTheme.shapes.large)
                    .clipToBounds(),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(
                    items = images,
                    key = { it.id },
                ) { image ->
                    Box(contentAlignment = Alignment.Center) {
                        SubcomposeAsyncImage(
                            model = image.url,
                            contentDescription = image.description,
                            modifier = Modifier
                                .size(width = 120.dp, height = 90.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .then(
                                    if (selectedImageId == image.id) Modifier.blur(4.dp)
                                    else Modifier
                                )
                                .combinedClickable(
                                    onLongClick = {
                                        selectedImageId = if (selectedImageId == image.id) null
                                        else image.id
                                    },
                                    onClick = { onImageClick(image.url.toString()) }
                                ),
                            contentScale = ContentScale.Crop,
                        )

                        if (selectedImageId == image.id) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    space = 8.dp,
                                    alignment = Alignment.CenterHorizontally
                                ),
                            ) {
                                IconButton(
                                    onClick = {
                                        selectedImageId = null

                                        if (image.id != coverImage?.id) onFavoriteImage(image.id)
                                        else onFavoriteImage(null)
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = if (image.id == coverImage?.id) Icons.Rounded.Favorite
                                        else Icons.Rounded.FavoriteBorder,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(all = 4.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        selectedImageId = null
                                        onDeleteImage(image.id)
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    modifier = Modifier
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(all = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PageIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    dotSpacing: Dp = 4.dp
) {
    val indicatorWidth = dotSize
    val totalWidth = (indicatorWidth + dotSpacing) * pageCount - dotSpacing
    val currentPageOffset by remember {
        derivedStateOf {
            pagerState.currentPage + pagerState.currentPageOffsetFraction
        }
    }

    val transition = updateTransition(
        targetState = pagerState.currentPage,
        label = "pageTransition"
    )

    val indicatorStart by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = 0.8f,
                stiffness = 300f
            )
        },
        label = "indicatorStart"
    ) { page ->
        val position = (indicatorWidth + dotSpacing) * page
        if (currentPageOffset < page) {
            val offset = (page - currentPageOffset).coerceIn(0f, 1f)
            position - (indicatorWidth + dotSpacing) * offset * 0.5f
        } else {
            position
        }
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = 0.8f,
                stiffness = 300f
            )
        },
        label = "indicatorEnd"
    ) { page ->
        val position = (indicatorWidth + dotSpacing) * page
        if (currentPageOffset > page) {
            val offset = (currentPageOffset - page).coerceIn(0f, 1f)
            position + (indicatorWidth + dotSpacing) * offset * 0.5f
        } else {
            position
        }
    }

    Box(
        modifier = modifier
            .width(totalWidth)
            .height(dotSize)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(pageCount) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )
            }
        }

        val dotPositionOffset = (indicatorWidth + dotSpacing)
        val currentPosition = dotPositionOffset * currentPageOffset

        val left = minOf(indicatorStart, currentPosition).coerceAtLeast(0.dp)
        val right = maxOf(
            indicatorEnd + indicatorWidth,
            currentPosition + indicatorWidth
        ).coerceAtMost(totalWidth)

        Box(
            modifier = Modifier
                .offset(x = left)
                .width(right - left)
                .height(dotSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

