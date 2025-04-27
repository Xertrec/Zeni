package com.zeni.auth.presentation.register

import android.R.attr.name
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.zeni.R
import com.zeni.auth.domain.utils.RegisterErrors
import com.zeni.auth.domain.utils.RegisterResult
import com.zeni.auth.presentation.register.components.RegisterViewModel
import com.zeni.core.domain.model.Country
import com.zeni.core.domain.utils.SelectableDatesNotPast
import com.zeni.core.domain.utils.SelectableDatesOnlyPast
import com.zeni.core.presentation.components.AppIcon
import com.zeni.core.presentation.navigation.ScreenHome
import com.zeni.core.presentation.navigation.ScreenLogin
import com.zeni.core.presentation.navigation.ScreenRegister
import com.zeni.core.presentation.navigation.ScreenVerifyEmail
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavHostController
) {
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()
    lateinit var pagerState: PagerState

    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    val phone by viewModel.phone.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()

    val username by viewModel.username.collectAsState()
    val usernameError by viewModel.usernameError.collectAsState()

    var birthdateDatePickerOpen by remember { mutableStateOf(value = false) }
    val birthDate by viewModel.birthdate.collectAsState()
    val birthDateError by viewModel.birthdateError.collectAsState()

    val address by viewModel.address.collectAsState()
    val addressError by viewModel.addressError.collectAsState()

    var countryPickerOpen by remember { mutableStateOf(value = false) }
    val country by viewModel.country.collectAsState()
    val countryError by viewModel.countryError.collectAsState()

    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    val registerButtonEnabled by remember {
        derivedStateOf {
            email.isNotEmpty() && phone.isNotEmpty() && username.isNotEmpty() &&
                    address.isNotEmpty() && country != null && password.isNotEmpty() &&
                    emailError == null && phoneError == null && usernameError == null && birthDateError == null &&
                    addressError == null && countryError == null && passwordError == null
        }
    }

    var showAlert by remember { mutableStateOf(false) }
    val toRegister = remember {
        suspend {
            val result = viewModel.register()
            if (result is RegisterResult.Success) {
                navController.navigate(ScreenHome)
            } else if (result is RegisterResult.CodeSent) {
                navController.navigate(ScreenVerifyEmail)
            } else {
                showAlert = true
            }
        }
    }

    data class RegisterField(
        val verifyField: suspend () -> Unit,
        val field: @Composable (suspend () -> Unit) -> Unit
    )
    val fields: List<RegisterField> = listOf(
        // Email
        RegisterField(
            verifyField = {
                if (viewModel.verifyEmail()) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            },
            field = { verificationField ->
                RegisterTextField(
                    text = email,
                    onValueChange = viewModel::setEmail,
                    label = stringResource(R.string.register_email_field_label),
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorText = if (emailError == null) null
                    else stringResource(emailError!!.errorRes),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            scope.launch {
                                verificationField()
                            }
                        }
                    )
                )
            }
        ),
        // Phone
        RegisterField(
            verifyField = {
                if (viewModel.verifyPhone() && viewModel.verifyCountry()) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            },
            field = { verificationField ->
                val errorText = if (phoneError != null) stringResource(phoneError!!.errorRes)
                else if (countryError != null) stringResource(countryError!!.errorRes)
                else null

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(intrinsicSize = IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 8.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RegisterButtonField(
                            onClick = { countryPickerOpen = true },
                            label = if (country == null) stringResource(R.string.register_country_field_label)
                            else country!!.phonePrefix,
                            modifier = Modifier
                                .fillMaxHeight(),
                            isError = countryError != null
                        )

                        RegisterTextField(
                            text = phone,
                            onValueChange = viewModel::setPhone,
                            label = stringResource(R.string.register_phone_field_label),
                            modifier = Modifier
                                .weight(weight = 1f),
                            isError = phoneError != null,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    scope.launch {
                                        verificationField()
                                    }
                                }
                            )
                        )
                    }

                    AnimatedVisibility(visible = errorText != null) {
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
        ),
        // Username
        RegisterField(
            verifyField = {
                if (viewModel.verifyUsername()) {
                    focusManager.clearFocus()
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            field = { verificationField ->
                RegisterTextField(
                    text = username,
                    onValueChange = viewModel::setUsername,
                    label = stringResource(R.string.register_username_field_label),
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorText = if (usernameError == null) null
                    else stringResource(usernameError!!.errorRes),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            scope.launch {
                                verificationField()
                            }
                        }
                    )
                )
            }
        ),
        // Birthdate
        RegisterField(
            verifyField = {
                if (viewModel.verifyBirthdate()) {
                    focusManager.moveFocus(FocusDirection.Right)
                }
            },
            field = { verificationField ->
                val formatter = DateTimeFormatter.ofPattern("dd/M/yy")

                RegisterButtonField(
                    onClick = { birthdateDatePickerOpen = true },
                    label = if (birthDate == null) stringResource(R.string.register_birthdate_field_label)
                    else stringResource(R.string.register_birthdate_field_text, birthDate!!.format(formatter)),
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorText = if (birthDateError == null) null
                    else stringResource(birthDateError!!.errorRes)
                )
            }
        ),
        // Address
        RegisterField(
            verifyField = {
                if (viewModel.verifyAddress()) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            },
            field = { verificationField ->
                RegisterTextField(
                    text = address,
                    onValueChange = viewModel::setAddress,
                    label = stringResource(R.string.register_address_field_label),
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorText = if (addressError == null) null
                    else stringResource(addressError!!.errorRes),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            scope.launch {
                                verificationField()
                            }
                        }
                    )
                )
            }
        ),
        // Password
        RegisterField(
            verifyField = {},
            field = {
                var visiblePassword by remember { mutableStateOf(value = false) }

                RegisterTextField(
                    text = password,
                    onValueChange = viewModel::setPassword,
                    label = stringResource(R.string.password_field_label),
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorText = if (passwordError == null) null
                    else stringResource(passwordError!!.errorRes, 6),
                    visualTransformation = if (visiblePassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            scope.launch {
                                toRegister()
                            }
                        }
                    )
                )
            }
        )
    )
    pagerState = rememberPagerState { fields.size }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController,
                pagerState = pagerState,
                enabled = pagerState.canScrollForward || registerButtonEnabled,
                onClick = {
                    scope.launch {
                        if (pagerState.canScrollForward) fields[pagerState.currentPage].verifyField()
                        else toRegister()
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
                    text = stringResource(R.string.register_title),
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            bottom = 8.dp
                        ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier,
                    beyondViewportPageCount = 4,
                    verticalAlignment = Alignment.Top,
                    userScrollEnabled = false,
                    key = { pageIndex ->
                        pageIndex
                    }
                ) { pageIndex ->
                    fields[pageIndex].field(fields[pageIndex].verifyField)
                }
            }
        }
    }

    if (birthdateDatePickerOpen) {
        DatePickers(
            onClose = { birthdateDatePickerOpen = false },
            onSelectedDate = viewModel::setBirthdate,
            initialSelectedDateMillis = birthDate?.toLocalDate()
                ?.atStartOfDay(ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli()
        )
    }
    if (countryPickerOpen) {
        Dialog(
            onDismissRequest = { countryPickerOpen = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp),
                shape = MaterialTheme.shapes.large
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(
                        items = Country.entries,
                        key = { country ->
                            country.name
                        }
                    ) { country ->

                        CountryItem(
                            country = country,
                            modifier = Modifier
                                .clickable {
                                    scope.launch {
                                        viewModel.setCountry(country)
                                        countryPickerOpen = false
                                    }
                                }
                        )
                    }
                }
            }
        }
    }

    BackHandler(enabled = pagerState.currentPage > 0 && !birthdateDatePickerOpen && !countryPickerOpen) {
        focusManager.moveFocus(FocusDirection.Previous)
    }
}

@Composable
private fun RegisterTextField(
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

@Composable
private fun RegisterButtonField(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    isError: Boolean = errorText != null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (!isError) ButtonDefaults.outlinedButtonColors().containerColor
            else MaterialTheme.colorScheme.errorContainer,
            contentColor = if (!isError) ButtonDefaults.outlinedButtonColors().contentColor
            else MaterialTheme.colorScheme.error
        )
    ) {
        Text(text = label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickers(
    onClose: () -> Unit,
    onSelectedDate: (ZonedDateTime) -> Unit,
    initialSelectedDateMillis: Long? = null,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis ?: LocalDate.now()
            .plusDays(1).atStartOfDay()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli(),
        selectableDates = SelectableDatesOnlyPast
    )

    DatePickerDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(
                onClick = {
                    onClose()
                    onSelectedDate(
                        ZonedDateTime.of(
                            LocalDateTime.ofEpochSecond(
                                datePickerState.selectedDateMillis!! / 1000,
                                0,
                                ZoneOffset.UTC
                            ),
                            ZoneId.systemDefault()
                        )
                    )
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = stringResource(id = R.string.accept_button))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onClose) {
                Text(text = stringResource(id = R.string.cancel_button))
            }
        },
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
private fun CountryItem(country: Country, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp), // Aumenta el padding vertical
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Alinea los elementos a los extremos
    ) {
        // Grupo para Bandera y Nombre
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = country.flagUnicode,
                fontSize = 24.sp, // Tamaño de la bandera
                modifier = Modifier.width(40.dp) // Ancho fijo para alinear nombres
            )
//            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre bandera y nombre
//            Text(
//                text = country.countryName,
//                style = MaterialTheme.typography.bodyLarge // Estilo de texto
//            )
        }
        // Prefijo telefónico
        Text(
            text = country.phonePrefix,
            style = MaterialTheme.typography.bodyMedium, // Estilo de texto
            fontFamily = FontFamily.Monospace, // Fuente monoespaciada para prefijos
            color = MaterialTheme.colorScheme.onSurfaceVariant // Color secundario
        )
    }
}

@Composable
private fun BottomBar(
    navController: NavController,
    pagerState: PagerState,
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
                Text(
                    text = if (pagerState.canScrollForward) stringResource(R.string.register_next_btn)
                    else stringResource(R.string.register_btn)
                )
            }
        }

        Box(
            modifier = modifier
                .wrapContentSize()
                .clip(shape = MaterialTheme.shapes.large)
                .clipToBounds()
                .clickable {
                    navController.navigate(ScreenLogin) {
                        popUpTo(ScreenRegister) {
                            inclusive = true
                        }
                    }
                }
        ) {
            Text(
                text = stringResource(id = R.string.i_have_account),
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