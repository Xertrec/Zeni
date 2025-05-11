package com.zeni

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.zeni.core.presentation.navigation.ScreenUpsertTrip
import com.zeni.hotel.domain.utils.EndSelectableDate
import com.zeni.hotel.domain.utils.StartSelectableDate
import com.zeni.hotel.presentation.HotelDatePickerDialog
import com.zeni.hotel.presentation.HotelsScreen
import com.zeni.hotel.presentation.SearchHotelBar
import com.zeni.hotel.presentation.components.HotelsViewModel
import com.zeni.itinerary.presentation.ItineraryScreen
import com.zeni.itinerary.presentation.components.ItineraryViewModel
import com.zeni.settings.presentation.MoreScreen
import com.zeni.trip.presentation.TripsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(
    navController: NavHostController,
    initialScreen: Int = Screen.Hotels.ordinal
) {
    val pagerState = rememberPagerState(
        initialPage = initialScreen,
        pageCount = { Screen.entries.size }
    )

    val currentScreen by remember {
        derivedStateOf {
            Screen.entries[pagerState.targetPage]
        }
    }

    val hotelsViewModel: HotelsViewModel = hiltViewModel()
    var isSearchVisible by remember { mutableStateOf(value = false) }

    var showStartDatePicker by remember { mutableStateOf(value = false) }
    var showEndDatePicker by remember { mutableStateOf(value = false) }
    val startDate by hotelsViewModel.startDate.collectAsState()
    val endDate by hotelsViewModel.endDate.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedContent(
                targetState = currentScreen == Screen.Hotels && isSearchVisible,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(300)) { height -> -height } + 
                    fadeIn(animationSpec = tween(300)))
                        .togetherWith(
                            slideOutVertically(animationSpec = tween(300)) { height -> -height } +
                            fadeOut(animationSpec = tween(300))
                        )
                },
                label = "TopBarAnimation"
            ) { isSearchBarVisible ->
                if (isSearchBarVisible) {
                    val cityQuery by hotelsViewModel.cityQuery.collectAsState()

                    SearchHotelBar(
                        cityQuery = cityQuery,
                        onCityQueryChange = hotelsViewModel::setCity,
                        startDate = startDate,
                        endDate = endDate,
                        onStartDateClick = { showStartDatePicker = true },
                        onEndDateClick = { showEndDatePicker = true },
                        onCloseSearch = { isSearchVisible = false }
                    )
                } else {
                    TopBar(currentScreen = currentScreen)
                }
            }
        },
        bottomBar = {
            BottomBar(
                pagerState = pagerState
            )
        },
        floatingActionButton = {
            FloatingButton(
                navController = navController,
                pagerState = pagerState,
                onSearchClick = if (!isSearchVisible) { { isSearchVisible = !isSearchVisible } }
                else null
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(contentPadding),
            beyondViewportPageCount = Screen.entries.size - 1,
            key = { currentIndex ->
                Screen.entries[currentIndex].name
            }
        ) { currentIndex ->

            when (currentIndex) {
                Screen.Hotels.ordinal -> {
                    HotelsScreen(
                        viewModel = hotelsViewModel,
                        navController = navController
                    )
                }
                Screen.Trips.ordinal -> {
                    TripsScreen(
                        viewModel = hiltViewModel(),
                        navController = navController
                    )
                }
                Screen.Itinerary.ordinal -> {
                    ItineraryScreen(
                        viewModel = hiltViewModel<ItineraryViewModel, ItineraryViewModel.ItineraryViewModelFactory> { factory ->
                            factory.create()
                        },
                        navController = navController
                    )
                }
                Screen.More.ordinal -> {
                    MoreScreen(
                        viewModel = hiltViewModel(),
                        navController = navController
                    )
                }
            }
        }

        if (showStartDatePicker && currentScreen == Screen.Hotels) {
            val startDatePickerState = rememberDatePickerState(
                initialSelectedDateMillis = startDate?.toInstant()?.toEpochMilli()
                    ?: System.currentTimeMillis(),
                selectableDates = StartSelectableDate.createSelectableDates(endDate)
            )

            HotelDatePickerDialog(
                state = startDatePickerState,
                onDateSelected = {
                    hotelsViewModel.setStartDate(it)
                    showStartDatePicker = false
                },
                onDismiss = { showStartDatePicker = false }
            )
        }
        if (showEndDatePicker && currentScreen == Screen.Hotels) {
            val endDatePickerState = rememberDatePickerState(
                initialSelectedDateMillis = endDate?.toInstant()?.toEpochMilli()
                    ?: (startDate?.toInstant()?.toEpochMilli()?.plus(86400000L)
                        ?: (System.currentTimeMillis() + 86400000L)),
                selectableDates = EndSelectableDate.createSelectableDates(startDate)
            )

            HotelDatePickerDialog(
                state = endDatePickerState,
                onDateSelected = {
                    hotelsViewModel.setEndDate(it)
                    showEndDatePicker = false
                },
                onDismiss = { showEndDatePicker = false }
            )
        }
    }

    LaunchedEffect(isSearchVisible) {
        if (!isSearchVisible) hotelsViewModel.clearSearch()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(currentScreen: Screen) {
    TopAppBar(
        title = {
            Text(text = stringResource(currentScreen.topText))
        }
    )
}

@Composable
private fun BottomBar(pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    val currentScreen by remember {
        derivedStateOf {
            Screen.entries[pagerState.targetPage]
        }
    }

    NavigationBar {
        Screen.entries.forEach { screen ->
            val isSelected = currentScreen == screen

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        scope.launch {
                            pagerState.scrollToPage(screen.ordinal)
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = if (isSelected) painterResource(screen.selectedIcon)
                        else painterResource(screen.unselectedIcon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(screen.bottomText))
                }
            )
        }
    }
}

@Composable
private fun FloatingButton(
    navController: NavController,
    pagerState: PagerState,
    onSearchClick: (() -> Unit)? = null
) {
    val currentScreen by remember {
        derivedStateOf {
            Screen.entries[pagerState.targetPage]
        }
    }

    when (currentScreen) {
        Screen.Trips -> {
            FloatingActionButton(
                onClick = { navController.navigate(ScreenUpsertTrip()) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
        Screen.Hotels -> {
            if (onSearchClick != null) {
                FloatingActionButton(
                    onClick = onSearchClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
        }
        else -> {}
    }
}

/**
 * Screens represented in the initial screen.
 */
enum class Screen(
    val topText: Int,
    val bottomText: Int = topText,
    val unselectedIcon: Int,
    val selectedIcon: Int = unselectedIcon
) {
//    Home(R.string.home_title),
    Hotels(
        topText = R.string.hotels_title,
        unselectedIcon = R.drawable.icon_hotel_empty,
        selectedIcon = R.drawable.icon_hotel_fill
    ),
    Trips(
        topText = R.string.trips_title,
        unselectedIcon = R.drawable.icon_trip_empty,
        selectedIcon = R.drawable.icon_trip_fill
    ),
    Itinerary(
        topText = R.string.itinerary_title,
        bottomText = R.string.itinerary_tab_text,
        unselectedIcon = R.drawable.icon_itinerary_empty,
        selectedIcon = R.drawable.icon_itinerary_fill
    ),
    More(
        topText = R.string.more_title,
        unselectedIcon = R.drawable.icon_more
    )
}
