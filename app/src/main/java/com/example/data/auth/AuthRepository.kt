package com.example.data.auth

import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import com.example.model.Language
import kotlinx.coroutines.flow.Flow
import java.util.UUID

data class AuthState(
    val isAuthenticated: Boolean = false,
    val user: UserEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthRepository(private val userDao: UserDao) {

    val currentUserFlow: Flow<UserEntity?> = userDao.getCurrentUserFlow()

    suspend fun register(username: String, email: String, avatarId: String): Result<UserEntity> {
        return try {
            if (username.isBlank() || email.isBlank()) {
                return Result.failure(IllegalArgumentException("Username and email cannot be empty"))
            }

            val mockJwtHeader = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
            val mockJwtPayload = "eyJzdWIiOiIke3VzZXJuYW1lfSIsImVtYWlsIjoie2VtYWlsfSIsImlhdCI6MTY3MjUxOTIwMH0"
            val mockJwtSig = UUID.randomUUID().toString().replace("-", "").take(16)
            val generatedJwt = "$mockJwtHeader.$mockJwtPayload.$mockJwtSig"

            val newUser = UserEntity(
                id = UUID.randomUUID().toString(),
                username = username.trim(),
                email = email.trim(),
                avatarId = avatarId,
                totalXp = 100, // Starter bonus
                stars = 5,
                coins = 50,
                streakDays = 1,
                lastActiveDate = System.currentTimeMillis().toString(),
                jwtToken = generatedJwt,
                preferredLanguage = Language.ENGLISH.name
            )

            userDao.insertUser(newUser)
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String): Result<UserEntity> {
        return try {
            if (email.isBlank()) {
                return Result.failure(IllegalArgumentException("Email is required"))
            }

            val mockJwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc2MDAwMDAwfQ.signatureSample"
            val existingUser = userDao.getCurrentUser()

            val userToLogin = existingUser?.copy(email = email, jwtToken = mockJwt)
                ?: UserEntity(
                    id = UUID.randomUUID().toString(),
                    username = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email.trim(),
                    avatarId = "avatar_owl",
                    totalXp = 250,
                    stars = 12,
                    coins = 120,
                    streakDays = 3,
                    lastActiveDate = System.currentTimeMillis().toString(),
                    jwtToken = mockJwt,
                    preferredLanguage = Language.ENGLISH.name
                )

            userDao.insertUser(userToLogin)
            Result.success(userToLogin)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        userDao.clearUser()
    }

    suspend fun updateLanguage(language: Language) {
        val currentUser = userDao.getCurrentUser() ?: return
        userDao.updatePreferredLanguage(currentUser.id, language.name)
    }

    suspend fun addReward(xp: Int, stars: Int, coins: Int) {
        val currentUser = userDao.getCurrentUser() ?: return
        userDao.addRewards(currentUser.id, xp, stars, coins)
    }
}
