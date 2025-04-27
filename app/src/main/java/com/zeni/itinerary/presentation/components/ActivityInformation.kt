package com.zeni.itinerary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeni.R
import com.zeni.core.domain.model.Activity
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ActivityInformation(
    activity: Activity,
    modifier: Modifier = Modifier,
    showTripName: Boolean = false,
    showTimeTo: Boolean = false,
    onEditClick: (() -> Unit)? = null
) {
    val locale = LocalConfiguration.current.locale
    val dateFormater = remember {
        DateTimeFormatter
            .ofPattern("d MMMM yyyy", locale)
            .withLocale(locale)
    }
    val timeFormater = remember {
        DateTimeFormatter
            .ofPattern("HH:mm", locale)
            .withLocale(locale)
    }

    Box(
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
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(
                    id = R.string.trip_activity_name,
                    activity.title
                ),
                modifier = Modifier
                    .padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )

            if (showTripName) {
                Text(
                    text = stringResource(
                        id = R.string.trip_activity_trip_name,
                        activity.tripName
                    ),
                    fontSize = 12.sp
                )
            }

            Text(
                text = stringResource(
                    id = R.string.trip_activity_description,
                    activity.description
                ),
                fontSize = 12.sp
            )

            Text(
                text = stringResource(
                    id = R.string.trip_activity_date,
                    activity.dateTime.format(dateFormater)
                ),
                fontSize = 12.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        id = R.string.trip_activity_time,
                        activity.dateTime.format(timeFormater)
                    ),
                    fontSize = 12.sp
                )

                if (showTimeTo && activity.dateTime.isAfter(ZonedDateTime.now())) {
                    val now = ZonedDateTime.now()
                    val duration = Duration.between(now, activity.dateTime)
                    val hours = duration.toHours() % 24
                    val minutes = duration.toMinutes() % 60

                    val timeRemaining = when {
                        hours > 0 && minutes.toInt() == 1 -> {
                            pluralStringResource(
                                id = R.plurals.time_remaining_hour_one_minute,
                                count = hours.toInt(),
                                hours, minutes
                            )
                        }
                        hours > 0 -> {
                            pluralStringResource(
                                id = R.plurals.time_remaining_hours,
                                count = hours.toInt(),
                                hours, minutes
                            )
                        }
                        else -> {
                            pluralStringResource(
                                id = R.plurals.time_remaining_minutes,
                                count = minutes.toInt(),
                                minutes
                            )
                        }
                    }


                    Text(
                        text = timeRemaining,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (onEditClick != null) {
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    }
}