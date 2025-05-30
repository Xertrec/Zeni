package com.zeni.auth.presentation.recoverPassword.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.domain.utils.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val authenticator: Authenticator
): ViewModel() {

    val email: StateFlow<String>
        field = MutableStateFlow(value = "")
    fun setEmail(value: String) {
        viewModelScope.launch {
            email.emit(value)
        }
    }

    suspend fun recoverPassword(email: String): Boolean {
        return authenticator.sendPasswordResetEmail(email)
    }
}