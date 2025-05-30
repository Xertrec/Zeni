package com.zeni.auth.presentation.recoverPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zeni.R
import com.zeni.auth.presentation.recoverPassword.components.RecoverPasswordViewModel
import com.zeni.core.presentation.components.AppIcon
import com.zeni.core.presentation.components.CheckmarkAnimation
import com.zeni.core.presentation.navigation.ScreenInitial
import com.zeni.core.presentation.navigation.ScreenLogin
import com.zeni.core.presentation.navigation.ScreenRecoverPassword
import com.zeni.core.presentation.navigation.ScreenRegister
import com.zeni.core.presentation.navigation.ScreenTrip
import kotlinx.coroutines.launch

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val email by viewModel.email.collectAsState()
    val recoverPasswordButtonEnabled by remember {
        derivedStateOf {
            email.isNotEmpty()
        }
    }

    var emailSent by remember { mutableStateOf(value = false) }
    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomBar(
                enabled = recoverPasswordButtonEnabled,
                onClick = {
                    scope.launch {
                        if (viewModel.recoverPassword(email)) {
                            emailSent = true
                        } else {
                            showAlert = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .imePadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppIcon(
                size = 0.70f,
                modifier = Modifier
                    .weight(weight = 1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                verticalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterVertically
                ),
            ) {
                Text(
                    text = stringResource(R.string.recover_password_title),
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            bottom = 8.dp
                        ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::setEmail,
                    label = { Text(text = stringResource(R.string.recover_password_email_field_label)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    isError = false,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            scope.launch {
                                if (viewModel.recoverPassword(email)) {
                                    emailSent = true
                                } else {
                                    showAlert = true
                                }
                            }
                        }
                    ),
                    singleLine = true,
                    shape = MaterialTheme.shapes.extraLarge
                )
            }
        }
    }

    if (emailSent) {
        AlertDialog(
            onDismissRequest = {
                emailSent = false
                navController.navigate(ScreenLogin) {
                    popUpTo(ScreenRecoverPassword) {
                        inclusive = true
                    }
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.recovery_mail_sent_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CheckmarkAnimation(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .align(Alignment.CenterHorizontally),
                        circleColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        checkmarkColor = Color(0xFF4CAF50)
                    )

                    Text(
                        text = stringResource(R.string.recovery_mail_sent_message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        emailSent = false
                        navController.navigate(ScreenLogin) {
                            popUpTo(ScreenRecoverPassword) {
                                inclusive = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.accept_button))
                }
            }
        )
    }

    // Show an alert dialog if the login fails
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text(stringResource(R.string.alert_title)) },
            text = { Text(stringResource(R.string.email_dont_exists)) },
            confirmButton = {
                Button(onClick = { showAlert = false }) {
                    Text(stringResource(R.string.alert_btn))
                }
            }
        )
    }
}

@Composable
private fun BottomBar(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = 8.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 48.dp),
            enabled = enabled
        ) {
            Text(text = stringResource(R.string.send_recover_email_btn))
        }
    }
}