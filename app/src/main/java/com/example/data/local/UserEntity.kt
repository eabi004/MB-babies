package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val avatarId: String,
    val totalXp: Int = 0,
    val stars: Int = 0,
    val coins: Int = 0,
    val streakDays: Int = 1,
    val lastActiveDate: String,
    val jwtToken: String,
    val preferredLanguage: String = "ENGLISH" // "ENGLISH", "TELUGU", "HINDI"
)
