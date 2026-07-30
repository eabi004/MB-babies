package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUser()

    @Query("UPDATE users SET totalXp = totalXp + :xp, stars = stars + :stars, coins = coins + :coins WHERE id = :userId")
    suspend fun addRewards(userId: String, xp: Int, stars: Int, coins: Int)

    @Query("UPDATE users SET preferredLanguage = :language WHERE id = :userId")
    suspend fun updatePreferredLanguage(userId: String, language: String)
}
