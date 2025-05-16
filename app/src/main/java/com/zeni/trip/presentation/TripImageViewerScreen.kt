package com.zeni.trip.presentation

import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.zeni.core.domain.utils.SystemBarsUtils
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.trip.presentation.components.TripImageViewerViewModel
import me.saket.telephoto.zoomable.DoubleClickToZoomListener
import me.saket.telephoto.zoomable.ZoomLimit
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable
import kotlin.math.absoluteValue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TripImageViewerScreen(
    viewModel: TripImageViewerViewModel,
    navController: NavController
) {
    val activity = LocalActivity.current
    val view = LocalView.current
    val window = activity!!.window

    val images by viewModel.images.collectAsState()
    var fullScreen by remember { mutableStateOf(value = false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = ScreenStyleDefaults.backgroundColor,
        contentWindowInsets = WindowInsets(0)
    ) { contentPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            if (images.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ImageView(
                        mediaUri = viewModel.initialImageUri,
                        onFullScreenChange = { fullScreen = !fullScreen },
                        modifier = Modifier.weight(weight = 1f)
                    )
                }
            } else {
                val pagerState = rememberPagerState(
                    initialPage = images.indexOfFirst { it.url == viewModel.initialImageUri }
                ) { images.size }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize(),
                    pageSize = PageSize.Fill,
                    beyondViewportPageCount = PagerDefaults.BeyondViewportPageCount,
                    pageSpacing = 0.dp,
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        pagerSnapDistance = PagerSnapDistance.atMost(pages = 1)
                    ),
                    userScrollEnabled = true,
                    reverseLayout = false,
                    key = { images[it].id },
                    pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                        state = pagerState,
                        orientation = Orientation.Horizontal
                    ),
                    snapPosition = SnapPosition.Center
                ) { page ->
                    val media = images[page]

                    Column(
                        modifier = Modifier
                            .graphicsLayer {
                                val pageOffset = ((pagerState.currentPage - page) +
                                        pagerState.currentPageOffsetFraction).absoluteValue

                                alpha = lerp(
                                    start = 0f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )

                                lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ImageView(
                            mediaUri = media.url,
                            onFullScreenChange = { fullScreen = !fullScreen },
                            modifier = Modifier
                                .weight(weight = 1f)
                                .fillMaxWidth()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = !fullScreen,
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 300)
                    ) { -it } + fadeIn(),
                    exit = slideOutVertically(
                        animationSpec = tween(durationMillis = 300)
                    ) { -it } + fadeOut()
                ) {
                    TopBar(
                        navController = navController,
                        modifier = Modifier,
                        windowInsets = WindowInsets.statusBarsIgnoringVisibility
                    )
                }
            }
        }
    }

    // Changes the system bars visibility and the appearance of the status bar
    if (window != null) {
        LaunchedEffect(fullScreen) {
            if (fullScreen) SystemBarsUtils.hideSystemBars(window)
            else SystemBarsUtils.showSystemBars(window)
        }

        val isSystemInDarkTheme = isSystemInDarkTheme()
        DisposableEffect(Unit) {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false

            onDispose {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isSystemInDarkTheme
                SystemBarsUtils.showSystemBars(window)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets
) {
    TopAppBar(
        title = {},
        modifier = modifier
            .fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = navController::navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        },
        windowInsets = windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ScreenStyleDefaults.surfaceColor,
            scrolledContainerColor = ScreenStyleDefaults.surfaceColor,
            navigationIconContentColor = ScreenStyleDefaults.textColor,
            titleContentColor = ScreenStyleDefaults.textColor,
            actionIconContentColor = ScreenStyleDefaults.textColor
        )
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ImageView(
    mediaUri: Uri,
    onFullScreenChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zoomState = rememberZoomableState(
        zoomSpec = ZoomSpec(
            maximum = ZoomLimit(10f),
        )
    )

    SubcomposeAsyncImage(
        model = mediaUri,
        contentDescription = null,
        modifier = modifier
            .zoomable(
                state = zoomState,
                onClick = { onFullScreenChange() },
                onDoubleClick = DoubleClickToZoomListener.cycle(maxZoomFactor = 4f),
            )
    )
}

object ScreenStyleDefaults {
    val backgroundColor = Color.Black
    val surfaceColor = backgroundColor.copy(alpha = 0.6f)
    val textColor = Color.White
}