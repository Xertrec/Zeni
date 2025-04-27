package com.zeni.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.zeni.R
import com.zeni.core.presentation.navigation.ScreenAbout
import com.zeni.core.presentation.navigation.ScreenChangePassword
import com.zeni.core.presentation.navigation.ScreenLogin
import com.zeni.core.presentation.navigation.ScreenProfile
import com.zeni.core.presentation.navigation.ScreenSettings
import com.zeni.core.presentation.navigation.ScreenTerms
import com.zeni.settings.presentation.components.MoreViewModel
import com.zeni.settings.presentation.components.SettingOption
import com.zeni.settings.presentation.components.SettingsViewModel

@Composable
fun MoreScreen(
    viewModel: MoreViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Option(
            title = stringResource(R.string.profile_title),
            onClick = { navController.navigate(ScreenProfile) },
            modifier = Modifier
        )
        Option(
            title = stringResource(R.string.change_password_title),
            onClick = { navController.navigate(ScreenChangePassword) },
            modifier = Modifier
        )
        Option(
            title = stringResource(R.string.settings_title),
            onClick = { navController.navigate(ScreenSettings) },
            modifier = Modifier
        )
        Option(
            title = stringResource(R.string.terms_title),
            onClick = { navController.navigate(ScreenTerms) },
            modifier = Modifier
        )
        Option(
            title = stringResource(R.string.about_zeni),
            onClick = { navController.navigate(ScreenAbout) },
            modifier = Modifier
        )

        VerticalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Button(
            onClick = {
                viewModel.logOut()
                navController.navigate(ScreenLogin) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(25)
        ) {
            Text(text = stringResource(R.string.log_out_btn))
        }
    }
}

@Composable
private fun Option(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingOption(
        title = title,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    )
}