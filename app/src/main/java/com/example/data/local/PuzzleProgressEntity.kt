package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "puzzle_progress")
data class PuzzleProgressEntity(
    @PrimaryKey val puzzleId: String,
    val language: String, // ENGLISH, TELUGU, HINDI
    val category: String, // FRUITS, ANIMALS, NATURE, SIGHT_WORDS, NUMBERS, COLORS
    val isSolved: Boolean = false,
    val bestScore: Int = 0,
    val starsEarned: Int = 0,
    val solvedTimestamp: Long = 0L
)
