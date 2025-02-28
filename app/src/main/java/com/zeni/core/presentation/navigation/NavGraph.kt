package com.zeni.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zeni.InitialScreen
import com.zeni.Screen
import com.zeni.profile.presentation.ProfileScreen
import com.zeni.profile.presentation.SettingsScreen
import com.zeni.profile.presentation.TermsScreen
import com.zeni.profile.presentation.components.ProfileViewModel
import com.zeni.settings.presentation.AboutScreen
import kotlin.reflect.KClass

@Composable
fun NavGraph(
    screenInitial: KClass<*> = ScreenHome::class,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = screenInitial,
        modifier = modifier
    ) {
        composable<ScreenInitial> {
            InitialScreen(
                navController = navController
            )
        }

        composable<ScreenHome> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.Home.ordinal
            )
        }
        composable<ScreenTrip> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.Trip.ordinal
            )
        }
        composable<ScreenItinerary> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.Itinerary.ordinal
            )
        }
        composable<ScreenMore> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.More.ordinal
            )
        }
        composable<ScreenProfile> {
            val profileViewModel = ProfileViewModel()

            ProfileScreen(
                viewModel = profileViewModel,
                navController = navController
            )
        }
        composable<ScreenSettings> {
            SettingsScreen(navController = navController)
        }
        composable<ScreenTerms>{
            TermsScreen(navController = navController)
        }
        composable<ScreenAbout> {
            AboutScreen(navController = navController)
        }
    }
}

@Composable
fun NavHostController.currentRoute(): String? {
    val navBackStackEntry by currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}