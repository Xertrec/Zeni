package com.zeni.settings.presentation.components

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.auth.domain.model.RegisterErrors
import com.zeni.core.domain.utils.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authenticator: Authenticator
): ViewModel() {

    val email: StateFlow<String>
        field = MutableStateFlow(value = "")
    val emailError: StateFlow<RegisterErrors.RegisterEmailErrors?>
        field = MutableStateFlow(value = null)
    fun setEmail(value: String) {
        viewModelScope.launch {
            email.emit(value)
        }
    }
    fun verifyEmail(): Boolean {
        if (email.value.isEmpty()) {
            emailError.value = RegisterErrors.RegisterEmailErrors.EMPTY
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            emailError.value = RegisterErrors.RegisterEmailErrors.INVALID
        } else if (authenticator.email != email.value) {
            emailError.value = RegisterErrors.RegisterEmailErrors.NOT_MATCH
        } else {
            emailError.value = null
        }

        return emailError.value == null
    }

    val oldPassword: StateFlow<String>
        field = MutableStateFlow(value = "")
    val oldPasswordError: StateFlow<RegisterErrors.RegisterPasswordErrors?>
        field = MutableStateFlow(value = null)
    fun setOldPassword(value: String) {
        viewModelScope.launch {
            oldPassword.emit(value)
        }
    }
    private fun verifyOldPassword(): Boolean {
        if (oldPassword.value.isEmpty()) {
            oldPasswordError.value = RegisterErrors.RegisterPasswordErrors.EMPTY
        } else if (oldPassword.value.length < 6) {
            oldPasswordError.value = RegisterErrors.RegisterPasswordErrors.SHORT
        } else {
            oldPasswordError.value = null
        }

        return oldPasswordError.value == null
    }

    val newPassword: StateFlow<String>
        field = MutableStateFlow(value = "")
    val newPasswordError: StateFlow<RegisterErrors.RegisterPasswordErrors?>
        field = MutableStateFlow(value = null)
    fun setNewPassword(value: String) {
        viewModelScope.launch {
            oldPassword.emit(value)
        }
    }
    private fun verifyNewPassword(): Boolean {
        if (oldPassword.value.isEmpty()) {
            oldPasswordError.value = RegisterErrors.RegisterPasswordErrors.EMPTY
        } else if (oldPassword.value.length < 6) {
            oldPasswordError.value = RegisterErrors.RegisterPasswordErrors.SHORT
        } else {
            oldPasswordError.value = null
        }

        return oldPasswordError.value == null
    }
    
    suspend fun changePassword(): Boolean {
        return if (verifyEmail() && verifyOldPassword() && verifyNewPassword()) {
            authenticator.updatePassword(
                email = email.value,
                oldPassword = oldPassword.value,
                newPassword = newPassword.value
            )
        } else {
            false
        }
    }
}