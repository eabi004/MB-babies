package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard_cache")
data class LeaderboardEntity(
    @PrimaryKey val userId: String,
    val username: String,
    val avatarId: String,
    val totalXp: Int,
    val stars: Int,
    val rank: Int,
    val language: String, // GLOBAL, ENGLISH, TELUGU, HINDI
    val badgeTitle: String
)
