package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey val id: String,
    val dateString: String,
    val title: String,
    val description: String,
    val language: String,
    val targetCount: Int,
    val currentProgress: Int = 0,
    val xpReward: Int = 150,
    val coinReward: Int = 50,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false
)
