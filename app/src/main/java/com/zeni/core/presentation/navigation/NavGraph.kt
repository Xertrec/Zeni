package com.zeni.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zeni.InitialScreen
import com.zeni.Screen
import com.zeni.auth.presentation.login.LoginScreen
import com.zeni.auth.presentation.login.components.LoginViewModel
import com.zeni.auth.presentation.register.RegisterScreen
import com.zeni.auth.presentation.register.components.RegisterViewModel
import com.zeni.auth.presentation.verifyEmail.VerifyEmailScreen
import com.zeni.auth.presentation.verifyEmail.components.VerifyEmailViewModel
import com.zeni.hotel.presentation.HotelScreen
import com.zeni.hotel.presentation.RoomScreen
import com.zeni.hotel.presentation.components.HotelViewModel
import com.zeni.hotel.presentation.components.RoomViewModel
import com.zeni.itinerary.presentation.UpsertItineraryScreen
import com.zeni.itinerary.presentation.components.UpsertActivityViewModel
import com.zeni.reservation.presentation.ConfirmReservationScreen
import com.zeni.reservation.presentation.ReservationInfoScreen
import com.zeni.reservation.presentation.components.ConfirmReservationViewModel
import com.zeni.reservation.presentation.components.ReservationInfoViewModel
import com.zeni.settings.presentation.ProfileScreen
import com.zeni.settings.presentation.AboutScreen
import com.zeni.settings.presentation.ChangePasswordScreen
import com.zeni.settings.presentation.SettingsScreen
import com.zeni.settings.presentation.TermsScreen
import com.zeni.settings.presentation.components.ChangePasswordViewModel
import com.zeni.settings.presentation.components.ProfileViewModel
import com.zeni.settings.presentation.components.SettingsViewModel
import com.zeni.trip.presentation.SelectTripScreen
import com.zeni.trip.presentation.TripImageViewerScreen
import com.zeni.trip.presentation.UpsertTripScreen
import com.zeni.trip.presentation.TripScreen
import com.zeni.trip.presentation.components.SelectTripViewModel
import com.zeni.trip.presentation.components.TripImageViewerViewModel
import com.zeni.trip.presentation.components.UpsertTripViewModel
import com.zeni.trip.presentation.components.TripViewModel
import kotlin.reflect.KClass

@Composable
fun NavGraph(
    screenInitial: KClass<*>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = screenInitial,
        modifier = modifier
    ) {
        composable<ScreenRegister> {
            val viewModel = hiltViewModel<RegisterViewModel>()

            RegisterScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenLogin> {
            val viewModel = hiltViewModel<LoginViewModel>()

            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenVerifyEmail> {
            val viewModel = hiltViewModel<VerifyEmailViewModel>()

            VerifyEmailScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<ScreenInitial> {
            InitialScreen(
                navController = navController
            )
        }

        composable<ScreenHotels> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.Hotels.ordinal
            )
        }
        composable<ScreenHotel> {
            val args = it.toRoute<ScreenHotel>()
            val viewModel = hiltViewModel<HotelViewModel, HotelViewModel.HotelViewModelFactory> { factory ->
                factory.create(args.hotelId, args.startDate, args.endDate)
            }

            HotelScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenRoom> {
            val args = it.toRoute<ScreenRoom>()
            val viewModel = hiltViewModel<RoomViewModel, RoomViewModel.RoomViewModelFactory> { factory ->
                factory.create(args.hotelId, args.roomId, args.startDate, args.endDate)
            }

            RoomScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<ScreenTrips> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.Trips.ordinal
            )
        }
        composable<ScreenUpsertTrip> {
            val args = it.toRoute<ScreenUpsertTrip>()
            val viewModel = hiltViewModel<UpsertTripViewModel, UpsertTripViewModel.UpsertTripViewModelFactory> { factory ->
                factory.create(args.tripName, args.toReserve)
            }

            UpsertTripScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenSelectTrip> {
            val viewModel = hiltViewModel<SelectTripViewModel>()

            SelectTripScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenTrip> {
            val args = it.toRoute<ScreenTrip>()
            val viewModel = hiltViewModel<TripViewModel, TripViewModel.TripViewModelFactory> { factory ->
                factory.create(args.tripName)
            }

            TripScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenTripImageViewer> {
            val args = it.toRoute<ScreenTripImageViewer>()
            val viewModel = hiltViewModel<TripImageViewerViewModel, TripImageViewerViewModel.TripImageViewerViewModelFactory> { factory ->
                factory.create(args.tripName, args.initialImageUri.toUri())
            }

            TripImageViewerScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<ScreenConfirmReservation> {
            val args = it.toRoute<ScreenConfirmReservation>()
            val viewModel = hiltViewModel<ConfirmReservationViewModel, ConfirmReservationViewModel.ReservationViewModelFactory> { factory ->
                factory.create(args.hotelId, args.roomId, args.startDate, args.endDate)
            }

            ConfirmReservationScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenReservationInfo> {
            val args = it.toRoute<ScreenReservationInfo>()
            val viewModel = hiltViewModel<ReservationInfoViewModel, ReservationInfoViewModel.ReservationInfoViewModelFactory> { factory ->
                factory.create(args.reservationId)
            }

            ReservationInfoScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<ScreenItinerary> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.Itinerary.ordinal
            )
        }
        composable<ScreenUpsertActivity> {
            val args = it.toRoute<ScreenUpsertActivity>()
            val viewModel = hiltViewModel<UpsertActivityViewModel, UpsertActivityViewModel.UpsertItineraryViewModelFactory> { factory ->
                factory.create(args.tripName, args.activityId)
            }
            UpsertItineraryScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<ScreenMore> {
            InitialScreen(
                navController = navController,
                initialScreen = Screen.More.ordinal
            )
        }

        composable<ScreenProfile> {
            val viewModel = hiltViewModel<ProfileViewModel>()

            ProfileScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<ScreenSettings> {
            val viewModel = hiltViewModel<SettingsViewModel>()

            SettingsScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenChangePassword> {
            val viewModel = hiltViewModel<ChangePasswordViewModel>()

            ChangePasswordScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenTerms>{
            TermsScreen(navController = navController)
        }
        composable<ScreenAbout> {
            AboutScreen(navController = navController)
        }
    }
}