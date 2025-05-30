package com.zeni.settings.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.zeni.R
import com.zeni.core.domain.utils.extensions.navigateBack
import com.zeni.core.presentation.navigation.ScreenInitial
import com.zeni.settings.presentation.components.ChangePasswordViewModel
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    val oldPassword by viewModel.oldPassword.collectAsState()
    val oldPasswordError by viewModel.oldPasswordError.collectAsState()

    val newPassword by viewModel.newPassword.collectAsState()
    val newPasswordError by viewModel.newPasswordError.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { TopBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.Top
            )
        ) {
            TextField(
                text = email,
                onValueChange = viewModel::setEmail,
                label = stringResource(R.string.register_email_field_label),
                errorText = if (emailError == null) null
                else stringResource(emailError!!.errorRes),
                isError = emailError != null
            )

            TextField(
                text = oldPassword,
                onValueChange = viewModel::setOldPassword,
                label = stringResource(R.string.change_password_current_password),
                errorText = if (oldPasswordError == null) null
                else stringResource(R.string.change_password_current_password_not_match),
                isError = oldPasswordError != null,
                visualTransformation = PasswordVisualTransformation()
            )

            TextField(
                text = newPassword,
                onValueChange = viewModel::setNewPassword,
                label = stringResource(R.string.change_password_new_password),
                errorText = if (newPasswordError == null) null
                else stringResource(newPasswordError!!.errorRes),
                isError = newPasswordError != null,
                visualTransformation = PasswordVisualTransformation()
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
                    scope.launch {
                        if (viewModel.changePassword()) {
                            navController.navigate(ScreenInitial) {
                                popUpTo<ScreenInitial> {
                                    inclusive = true
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(25)
            ) {
                Text(text = stringResource(R.string.change_password_btn))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.change_password_title))
        },
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
private fun TextField(
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth(),
            label = {
                Text(text = label)
            },
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            shape = CircleShape
        )

        AnimatedVisibility(visible = isError && errorText != null) {
            var currentText by remember { mutableStateOf(value = errorText!!) }

            Text(
                text = currentText,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.error
            )

            LaunchedEffect(errorText) {
                if (errorText != null) currentText = errorText
            }
        }
    }
}