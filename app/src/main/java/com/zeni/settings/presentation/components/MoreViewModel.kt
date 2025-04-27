package com.zeni.settings.presentation.components

import androidx.lifecycle.ViewModel
import com.zeni.core.domain.utils.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val authenticator: Authenticator,
) : ViewModel() {

    fun logOut() {
        authenticator.logOut()
    }
}