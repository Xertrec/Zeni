package com.zeni.auth.presentation.login.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.auth.domain.use_cases.LoginUseCase
import com.zeni.auth.domain.utils.LoginErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    val username: StateFlow<String>
        field = MutableStateFlow(value = "")
    fun setUsername(value: String) {
        viewModelScope.launch {
            username.emit(value)
            loginError.emit(null)
        }
    }

    val password: StateFlow<String>
        field = MutableStateFlow(value = "")
    fun setPassword(value: String) {
        viewModelScope.launch {
            password.emit(value)
            loginError.emit(null)
        }
    }

    val loginError: StateFlow<LoginErrors?>
        field = MutableStateFlow(value = null)
    suspend fun login(): Boolean {
        // TODO: Move to a use case
        Log.i(LoginViewModel::class.java.simpleName, "Login attempt with username: ${username.value}")
        val isLogged = loginUseCase(username.value, password.value)
        if (!isLogged) {
            viewModelScope.launch {
                loginError.emit(LoginErrors.INVALID_CREDENTIALS)
            }
        }

        return isLogged
    }
}