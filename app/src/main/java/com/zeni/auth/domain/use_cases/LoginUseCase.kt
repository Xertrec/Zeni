package com.zeni.auth.domain.use_cases

import com.zeni.auth.domain.model.LoginErrors
import com.zeni.core.data.repository.UserRepositoryImpl
import com.zeni.core.domain.utils.Authenticator
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val authenticator: Authenticator
) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): Boolean {
        if (!userRepository.existsUserWithUsername(username)) {
            return false
        }

        val userEmail = userRepository.getUserByUsername(username).first().email
        return authenticator.login(
            email = userEmail,
            password = password
        ) == LoginErrors.NONE
    }
}