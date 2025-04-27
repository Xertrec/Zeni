package com.zeni.auth.domain.utils

import com.google.firebase.auth.PhoneAuthProvider
import com.zeni.auth.domain.model.RegisterErrors

sealed interface RegisterResult {
    /**
     * Register and login successfully.
     */
    object Success : RegisterResult

    /**
     * The code has been sent to the user's email.
     */
    object CodeSent : RegisterResult

    /**
     * An error has happened during the registration process.
     *
     * @param error The error that occurred.
     */
    data class Error(val error: RegisterErrors.RegisterPasswordErrors) : RegisterResult
}