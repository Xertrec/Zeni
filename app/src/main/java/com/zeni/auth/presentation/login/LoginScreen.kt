package com.zeni.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.zeni.R
import com.zeni.core.presentation.components.AppIcon
import com.zeni.core.presentation.navigation.ScreenInitial
import com.zeni.auth.domain.utils.LoginErrors
import com.zeni.auth.presentation.login.components.LoginViewModel
import com.zeni.core.presentation.navigation.ScreenLogin
import com.zeni.core.presentation.navigation.ScreenRecoverPassword
import com.zeni.core.presentation.navigation.ScreenRegister
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()

    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    val loginButtonEnabled by remember {
        derivedStateOf {
            username.isNotEmpty() && password.isNotEmpty() && loginError == null
        }
    }

    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController,
                enabled = loginButtonEnabled,
                onClick = {
                    scope.launch {
                        if (viewModel.login()) {
                            navController.navigate(ScreenInitial)
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
                    text = stringResource(R.string.login_title),
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            bottom = 8.dp
                        ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = viewModel::setUsername,
                    label = { Text(text = stringResource(R.string.login_user_field_label)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    isError = loginError == LoginErrors.INVALID_USERNAME || loginError == LoginErrors.INVALID_CREDENTIALS,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    shape = MaterialTheme.shapes.extraLarge
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = 4.dp,
                        alignment = Alignment.CenterVertically
                    ),
                ) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = viewModel::setPassword,
                        label = { Text(text = stringResource(R.string.password_field_label)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        isError = loginError == LoginErrors.INVALID_PASSWORD || loginError == LoginErrors.INVALID_CREDENTIALS,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions {
                            scope.launch {
                                if (viewModel.login()) {
                                    navController.navigate(ScreenInitial)
                                } else {
                                    showAlert = true
                                }
                            }
                        },
                        singleLine = true,
                        shape = MaterialTheme.shapes.extraLarge
                    )

                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .clip(shape = MaterialTheme.shapes.large)
                            .clipToBounds()
                            .clickable {
                                navController.navigate(ScreenRecoverPassword) {
                                    popUpTo(ScreenLogin) {
                                        inclusive = true
                                    }
                                }
                            }
                    ) {
                        Text(
                            text = stringResource(id = R.string.forget_password_btn),
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }

    // Show an alert dialog if the login fails
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text(stringResource(R.string.alert_title)) },
            text = { Text(stringResource(R.string.alert_message)) },
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
    navController: NavController,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Row(
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
                Text(text = stringResource(R.string.login_btn))
            }
        }

        Box(
            modifier = modifier
                .wrapContentSize()
                .clip(shape = MaterialTheme.shapes.large)
                .clipToBounds()
                .clickable {
                    navController.navigate(ScreenRegister) {
                        popUpTo(ScreenLogin) {
                            inclusive = true
                        }
                    }
                }
        ) {
            Text(
                text = stringResource(id = R.string.i_have_not_account),
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}