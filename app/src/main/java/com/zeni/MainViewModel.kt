package com.zeni

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zeni.core.data.SharedPrefsManager
import com.zeni.core.domain.utils.Authenticator
import com.zeni.core.presentation.navigation.ScreenInitial
import com.zeni.core.presentation.navigation.ScreenLogin
import com.zeni.core.presentation.navigation.ScreenVerifyEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticator: Authenticator,
    sharedPrefsManager: SharedPrefsManager
) : ViewModel() {

    suspend fun getInitialScreen(): KClass<*> {
        return if (!authenticator.isLogged) ScreenLogin::class
        else if (!authenticator.isEmailVerifiedWithReload()) ScreenVerifyEmail::class
        else ScreenInitial::class
    }

    var isManualDarkTheme by mutableStateOf(sharedPrefsManager.autoDarkTheme)
        private set

    var isDarkTheme by mutableStateOf(sharedPrefsManager.darkTheme)
        private set
}