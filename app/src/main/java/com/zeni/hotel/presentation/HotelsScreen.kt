package com.zeni.hotel.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.zeni.core.presentation.navigation.ScreenHotel
import com.zeni.hotel.presentation.components.HotelCard
import com.zeni.hotel.presentation.components.HotelsViewModel

@Composable
fun HotelsScreen(
    viewModel: HotelsViewModel,
    navController: NavController
) {
    val hotels by viewModel.hotels.collectAsStateWithLifecycle()

    if (hotels.isEmpty()) {
        // TODO: Text saying no hotels available at the moment
        CircularProgressIndicator()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            items(
                items = hotels,
                key = { hotel -> hotel.id }
            ) { hotel ->

                HotelCard(
                    hotel = hotel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 128.dp),
                    onClick = {
                        navController.navigate(ScreenHotel(hotelId = hotel.id))
                    }
                )
            }
        }
    }
}