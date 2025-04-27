package com.zeni.auth.presentation.verifyEmail.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.core.domain.utils.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val auth: Authenticator,
): ViewModel() {

    suspend fun isEmailVerifiedWithReload(): Boolean {
        return auth.isEmailVerifiedWithReload()
    }
}