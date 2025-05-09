package com.zeni.auth.presentation.verifyEmail

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeni.R
import com.zeni.auth.presentation.verifyEmail.components.VerifyEmailViewModel
import com.zeni.core.domain.utils.extensions.warning
import androidx.navigation.NavController
import com.zeni.core.presentation.navigation.ScreenInitial
import kotlinx.coroutines.launch

@Composable
fun VerifyEmailScreen(
    viewModel: VerifyEmailViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    space = 48.dp,
                    alignment = Alignment.Top
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_warning_empty),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(ratio = 1f)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.warning
                )

                Column(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.verify_email_title),
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 27.sp
                    )

                    Text(
                        text = stringResource(id = R.string.verify_email_explanation),
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            if (viewModel.isEmailVerifiedWithReload()) {
                                navController.navigate(ScreenInitial)
                            }
                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .clipToBounds(),
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_refresh),
                        contentDescription = null
                    )
                }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_MAIN).apply {
                            addCategory(Intent.CATEGORY_APP_EMAIL)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }

                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.open_email_btn),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}