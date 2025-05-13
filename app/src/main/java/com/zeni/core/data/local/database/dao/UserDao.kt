package com.zeni.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zeni.core.data.local.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE uid = :uid")
    fun getUserByUid(uid: String): Flow<UserEntity>

    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUserByUsername(username: String): Flow<UserEntity>

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE uid = :uid)")
    suspend fun existsUser(uid: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE email = :email)")
    suspend fun existsUserWithEmail(email: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE phone = :phone)")
    suspend fun existsUserWithPhone(phone: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE username = :username)")
    suspend fun existsUserWithUsername(username: String): Boolean

    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}