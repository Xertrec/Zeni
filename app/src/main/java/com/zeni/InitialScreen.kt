package com.zeni

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.zeni.core.presentation.navigation.ScreenUpsertTrip
import com.zeni.hotel.presentation.HotelsScreen
import com.zeni.itinerary.presentation.ItineraryScreen
import com.zeni.itinerary.presentation.components.ItineraryViewModel
import com.zeni.settings.presentation.MoreScreen
import com.zeni.trip.presentation.TripsScreen
import kotlinx.coroutines.launch

@Composable
fun InitialScreen(
    navController: NavHostController,
    initialScreen: Int = Screen.Hotels.ordinal
) {
    val pagerState = rememberPagerState(
        initialPage = initialScreen,
        pageCount = { Screen.entries.size }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(pagerState = pagerState)
        },
        bottomBar = {
            BottomBar(
                pagerState = pagerState
            )
        },
        floatingActionButton = {
            FloatingButton(
                navController = navController,
                pagerState = pagerState
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
                        viewModel = hiltViewModel(),
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(pagerState: PagerState) {
    val currentScreen by remember {
        derivedStateOf {
            Screen.entries[pagerState.targetPage]
        }
    }

    TopAppBar(
        title = {
            Text(text = stringResource(currentScreen.top))
        },
        actions = {
            // Search icon
            IconButton(
                onClick = { /*TODO()*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
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
                    Text(text = stringResource(screen.bottom))
                }
            )
        }
    }
}

@Composable
private fun FloatingButton(
    navController: NavController,
    pagerState: PagerState
) {
    val currentScreen by remember {
        derivedStateOf {
            Screen.entries[pagerState.targetPage]
        }
    }

    if (currentScreen == Screen.Trips) {
        FloatingActionButton(
            onClick = { navController.navigate(ScreenUpsertTrip()) }
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null
            )
        }
    }
}

/**
 * Screens represented in the initial screen.
 */
enum class Screen(
    val top: Int,
    val bottom: Int = top,
    val unselectedIcon: Int,
    val selectedIcon: Int = unselectedIcon
) {
//    Home(R.string.home_title),
    Hotels(
        top = R.string.hotels_title,
        unselectedIcon = R.drawable.icon_hotel_empty,
        selectedIcon = R.drawable.icon_hotel_fill
    ),
    Trips(
        top = R.string.trips_title,
        unselectedIcon = R.drawable.icon_trip_empty,
        selectedIcon = R.drawable.icon_trip_fill
    ),
    Itinerary(
        top = R.string.itinerary_title,
        bottom = R.string.itinerary_tab_text,
        unselectedIcon = R.drawable.icon_itinerary_empty,
        selectedIcon = R.drawable.icon_itinerary_fill
    ),
    More(
        top = R.string.more_title,
        unselectedIcon = R.drawable.icon_more
    )
}