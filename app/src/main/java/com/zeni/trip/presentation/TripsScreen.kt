package com.zeni.trip.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.zeni.R
import com.zeni.core.domain.model.Trip
import com.zeni.core.presentation.navigation.ScreenTrip
import com.zeni.trip.presentation.components.TripsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun TripsScreen(
    viewModel: TripsViewModel,
    navController: NavController
) {
    val trips by viewModel.trips.collectAsStateWithLifecycle()

    if (trips.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.empty_trips),
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            items(
                items = trips,
                key = { trip -> trip.name }
            ) { trip ->

                TripItem(
                    trip = trip,
                    onClick = {
                        navController.navigate(
                            route = ScreenTrip(tripName = trip.name)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TripItem(
    trip: Trip,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd/M/yy")

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clipToBounds()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(
                onClick = onClick
            )
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
                    id = R.string.trip_location,
                    trip.destination
                ),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(
                    id = R.string.trip_date,
                    trip.startDate.format(formatter),
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