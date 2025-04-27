package com.zeni.auth.domain.use_cases

import com.zeni.auth.domain.utils.RegisterResult
import com.zeni.core.data.repository.UserRepositoryImpl
import com.zeni.core.domain.model.Country
import com.zeni.core.domain.model.User
import com.zeni.core.domain.repository.UserRepository
import com.zeni.core.domain.utils.Authenticator
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val authenticator: Authenticator
) {
    suspend operator fun invoke(
        email: String,
        phone: String,
        username: String,
        birthdate: ZonedDateTime,
        address: String,
        country: Country,
        password: String,
    ): RegisterResult {
        val result = authenticator.register(email, password)
        userRepository.addUser(
            User(
                uid = authenticator.uid,
                username = username,
                email = email,
                phone = phone,
                birthdate = birthdate,
                address = address,
                country = country
            )
        )

        // TODO("Maybe upload data to firestore")

        return result
    }
}