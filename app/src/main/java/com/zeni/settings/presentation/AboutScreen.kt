package com.zeni.settings.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zeni.R
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.settings.domain.model.DevsInfo
import com.zeni.settings.presentation.components.DevInformation

/**
 * Screen that shows information about the app.
 *
 * @param navController The navigation controller.
 */
@Composable
fun AboutScreen(navController: NavHostController) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

    val devs = remember {
        listOf(
            DevsInfo(
                name = "Berto puto Dalfo",
                image = R.raw.berto_image,
                github = "https://github.com/xBlanco"
            ),
            DevsInfo(
                name = "Alex Lillo",
                image = R.raw.alex_image,
                github = "https://github.com/Xertrec"
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.4f),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.Top
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.app_version) + packageInfo.versionName,
                        modifier = Modifier
                            .alpha(alpha = 0.6f),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column {
                DevsInformation(
                    devs = devs,
                    modifier = Modifier
                )

                Text(
                    ""
                )
            }
        }
    }
}

/**
 * Top bar of the screen.
 *
 * @param navController The navigation controller.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavHostController) {
    TopAppBar(
        title = {},
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

/**
 * The icon of the app.
 */
@Composable
fun AppIcon(modifier: Modifier = Modifier) {

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.zeni_logo),
            contentDescription = null,
            modifier = Modifier
                .width(this.maxWidth * 0.45f)
                .aspectRatio(ratio = 1f)
        )
    }
}

@Composable
fun DevsInformation(
    devs: List<DevsInfo>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.Bottom
        )
    ) {
        devs.forEach { dev ->
            DevInformation(
                dev = dev,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}