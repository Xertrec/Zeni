package com.zeni.core.domain.repository

import com.zeni.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserByUsername(username: String): Flow<User>

    suspend fun existsUser(): Boolean

    suspend fun existsUserWithEmail(email: String): Boolean

    suspend fun existsUserWithPhone(phone: String): Boolean

    suspend fun existsUserWithUsername(username: String): Boolean

    suspend fun addUser(user: User)

    suspend fun deleteUser(user: User)
}