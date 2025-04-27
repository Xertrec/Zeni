package com.zeni.core.data.repository

import com.zeni.core.data.database.dao.UserDao
import com.zeni.core.data.mappers.toDomain
import com.zeni.core.data.mappers.toEntity
import com.zeni.core.domain.model.User
import com.zeni.core.domain.repository.UserRepository
import com.zeni.core.domain.utils.Authenticator
import com.zeni.core.util.DatabaseLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val authenticator: Authenticator
): UserRepository {

    override fun getUserByUsername(username: String): Flow<User> {
        DatabaseLogger.dbOperation("Getting user by username $username")
        return try {
            val user = userDao.getUserByUsername(username)
                .map { userEntity -> userEntity.toDomain() }
            DatabaseLogger.dbOperation("User retrieved successfully")

            user
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error getting user by username: ${e.message}", e)
            throw e
        }
    }

    override suspend fun existsUser(): Boolean {
        DatabaseLogger.dbOperation("Checking if user exists")
        return try {
            val exists = userDao.existsUser(authenticator.uid)
            DatabaseLogger.dbOperation("User exists: $exists")

            exists
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error checking if user exists: ${e.message}", e)
            throw e
        }
    }

    override suspend fun existsUserWithEmail(email: String): Boolean {
        DatabaseLogger.dbOperation("Checking if user exists with email $email")
        return try {
            val exists = userDao.existsUserWithEmail(email)
            DatabaseLogger.dbOperation("User exists with email: $exists")

            exists
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error checking if user exists with email: ${e.message}", e)
            throw e
        }
    }

    override suspend fun existsUserWithPhone(phone: String): Boolean {
        DatabaseLogger.dbOperation("Checking if user exists with phone $phone")
        return try {
            val exists = userDao.existsUserWithPhone(phone)
            DatabaseLogger.dbOperation("User exists with phone: $exists")

            exists
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error checking if user exists with phone: ${e.message}", e)
            throw e
        }
    }

    override suspend fun existsUserWithUsername(username: String): Boolean {
        DatabaseLogger.dbOperation("Checking if user exists with username $username")
        return try {
            val exists = userDao.existsUserWithUsername(username)
            DatabaseLogger.dbOperation("User exists with username: $exists")

            exists
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error checking if user exists with username: ${e.message}", e)
            throw e
        }
    }

    override suspend fun addUser(user: User) {
        DatabaseLogger.dbOperation("Adding user ${user.username}")
        try {
            userDao.upsertUser(user.toEntity())
            DatabaseLogger.dbOperation("User added successfully")
        } catch (e: Exception) {
            DatabaseLogger.dbError("Error adding user: ${e.message}", e)
        }
    }

    override suspend fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }
}