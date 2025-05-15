package com.zeni.trip.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.zeni.R
import com.zeni.core.domain.model.Trip
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.navigation.ScreenTrip
import com.zeni.core.presentation.navigation.ScreenUpsertTrip
import com.zeni.trip.presentation.components.SelectTripViewModel
import java.time.format.DateTimeFormatter

@Composable
fun SelectTripScreen(
    viewModel: SelectTripViewModel,
    navController: NavController
) {
    val trips by viewModel.trips.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(navController = navController)
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { contentPadding ->

        if (trips.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
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
                    .padding(contentPadding)
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
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(
                                    key = "selected_trip",
                                    value = trip.name
                                )
                            navController.navigateBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.select_trip_title)
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
        }
    )
}

@Composable
private fun TripItem(
    trip: Trip,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd/M/yy")

    Column(
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
}