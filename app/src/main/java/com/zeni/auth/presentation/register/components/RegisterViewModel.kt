package com.zeni.auth.presentation.register.components

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeni.auth.domain.use_cases.RegisterUseCase
import com.zeni.auth.domain.utils.LoginErrors
import com.zeni.auth.domain.utils.RegisterErrors
import com.zeni.auth.domain.utils.RegisterResult
import com.zeni.core.data.repository.UserRepositoryImpl
import com.zeni.core.domain.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val email: StateFlow<String>
        field = MutableStateFlow(value = "")
    val emailError: StateFlow<com.zeni.auth.domain.model.RegisterErrors.RegisterEmailErrors?>
        field = MutableStateFlow(value = null)
    fun setEmail(value: String) {
        viewModelScope.launch {
            email.emit(value)
        }
    }
    suspend fun verifyEmail(): Boolean {
        if (email.value.isEmpty()) {
            emailError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterEmailErrors.EMPTY
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            emailError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterEmailErrors.INVALID
        } else if (userRepository.existsUserWithEmail(email.value)) {
            emailError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterEmailErrors.TAKEN
        } else {
            emailError.value = null
        }

        return emailError.value == null
    }

    val phone: StateFlow<String>
        field = MutableStateFlow(value = "")
    val phoneError: StateFlow<com.zeni.auth.domain.model.RegisterErrors.RegisterPhoneErrors?>
        field = MutableStateFlow(value = null)
    fun setPhone(value: String) {
        viewModelScope.launch {
            phone.emit(value)
        }
    }
    suspend fun verifyPhone(): Boolean {
        if (phone.value.isEmpty()) {
            phoneError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterPhoneErrors.EMPTY
        } else if (!Patterns.PHONE.matcher(phone.value).matches()) {
            phoneError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterPhoneErrors.INVALID
        } else if (userRepository.existsUserWithPhone(phone.value)) {
            phoneError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterPhoneErrors.TAKEN
        } else {
            phoneError.value = null
        }

        return phoneError.value == null
    }

    val username: StateFlow<String>
        field = MutableStateFlow(value = "")
    val usernameError: StateFlow<com.zeni.auth.domain.model.RegisterErrors.RegisterUsernameErrors?>
        field = MutableStateFlow(value = null)
    fun setUsername(value: String) {
        viewModelScope.launch {
            username.emit(value)
        }
    }
    suspend fun verifyUsername(): Boolean {
        if (username.value.isEmpty()) {
            usernameError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterUsernameErrors.EMPTY
        } else if (userRepository.existsUserWithUsername(username.value)) {
            usernameError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterUsernameErrors.TAKEN
        } else {
            usernameError.value = null
        }

        return usernameError.value == null
    }

    val birthdate: StateFlow<ZonedDateTime?>
        field = MutableStateFlow(value = null)
    val birthdateError: StateFlow<com.zeni.auth.domain.model.RegisterErrors.RegisterBirthdateErrors?>
        field = MutableStateFlow(value = null)
    fun setBirthdate(value: ZonedDateTime) {
        viewModelScope.launch {
            birthdate.emit(value)
        }
    }
    fun verifyBirthdate(): Boolean {
        if (birthdate.value == null) {
            birthdateError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterBirthdateErrors.EMPTY
        } else if (birthdate.value!!.isAfter(ZonedDateTime.now())) {
            birthdateError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterBirthdateErrors.INVALID
        } else {
            birthdateError.value = null
        }

        return birthdateError.value == null
    }

    val address: StateFlow<String>
        field = MutableStateFlow(value = "")
    val addressError: StateFlow<com.zeni.auth.domain.model.RegisterErrors.RegisterAddressErrors?>
        field = MutableStateFlow(value = null)
    fun setAddress(value: String) {
        viewModelScope.launch {
            address.emit(value)
        }
    }
    fun verifyAddress(): Boolean {
        if (address.value.isEmpty()) {
            addressError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterAddressErrors.EMPTY
        } else {
            addressError.value = null
        }

        return addressError.value == null
    }

    val country: StateFlow<Country?>
        field = MutableStateFlow(value = null)
    val countryError: StateFlow<com.zeni.auth.domain.model.RegisterErrors.RegisterCountryErrors?>
        field = MutableStateFlow(value = null)
    fun setCountry(value: Country) {
        viewModelScope.launch {
            country.emit(value)
        }
    }
    fun verifyCountry(): Boolean {
        if (country.value == null) {
            countryError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterCountryErrors.EMPTY
        } else {
            countryError.value = null
        }

        return countryError.value == null
    }

    val password: StateFlow<String>
        field = MutableStateFlow(value = "")
    val passwordError: StateFlow<com.zeni.auth.domain.model.RegisterErrors.RegisterPasswordErrors?>
        field = MutableStateFlow(value = null)
    fun setPassword(value: String) {
        viewModelScope.launch {
            password.emit(value)
        }
    }
    private fun verifyPassword(): Boolean {
        if (password.value.isEmpty()) {
            passwordError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterPasswordErrors.EMPTY
        } else if (password.value.length < 6) {
            passwordError.value = com.zeni.auth.domain.model.RegisterErrors.RegisterPasswordErrors.SHORT
        } else {
            passwordError.value = null
        }

        return passwordError.value == null
    }

    fun verifyCredentials(): Boolean {
        TODO("Implement registration handling, and show errors in registerErrors")
    }

    suspend fun register(): RegisterResult {
        return if (verifyPassword()) {
            registerUseCase(
                email = email.value,
                phone = phone.value,
                username = username.value,
                birthdate = birthdate.value!!,
                address = address.value,
                country = country.value!!,
                password = password.value
            )
        } else {
            RegisterResult.Error(com.zeni.auth.domain.model.RegisterErrors.RegisterPasswordErrors.INSECURE)
        }
    }
}