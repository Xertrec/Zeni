package com.zeni.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zeni.R
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.components.AppIcon
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
                name = context.getString(R.string.developer1_name),
                image = R.raw.alberto_image,
                github = context.getString(R.string.developer1_github)
            ),
            DevsInfo(
                name = context.getString(R.string.developer2_name),
                image = R.raw.may_image,
                github = context.getString(R.string.developer2_github)
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
                    size = 0.45f,
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
                        text = stringResource(R.string.app_version_prefix) + packageInfo.versionName,
                        modifier = Modifier
                            .alpha(alpha = 0.6f),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            DevsInformation(
                devs = devs,
                modifier = Modifier
                    .padding(
                        bottom = 16.dp
                    )
            )
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

@Composable
private fun DevsInformation(
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
        Text(
            text = stringResource(R.string.developers_title),
            modifier = Modifier
                .padding(
                    start = 4.dp,
                    bottom = 8.dp
                ),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        devs.forEach { dev ->
            DevInformation(
                dev = dev,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}