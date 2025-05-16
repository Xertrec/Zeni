package com.zeni.hotel.presentation.components

import android.R.attr.fontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.zeni.R
import com.zeni.core.domain.model.Hotel
import com.zeni.core.presentation.components.shimmerEffect


@Composable
fun HotelCard(
    hotel: Hotel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Row(
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
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Start
        )
    ) {
        SubcomposeAsyncImage(
            model = hotel.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(ratio = 1f)
                .clip(shape = MaterialTheme.shapes.large),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            },
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(weight = 1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = hotel.name,
                modifier = Modifier
                    .padding(bottom = 4.dp),
                style = MaterialTheme.typography.titleMedium
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.hotel_address),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = " " + hotel.address,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.hotel_rating),
                    fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall
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

            val minPrice = hotel.minRoomPrice
            if (minPrice != null) {
                Row {
                    Text(
                        text = stringResource(id = R.string.room_min_price_prefix),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = " " + stringResource(id = R.string.room_price_value, minPrice),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
