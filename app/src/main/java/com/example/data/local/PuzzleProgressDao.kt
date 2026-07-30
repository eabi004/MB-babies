package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PuzzleProgressDao {
    @Query("SELECT * FROM puzzle_progress WHERE language = :language")
    fun getProgressByLanguageFlow(language: String): Flow<List<PuzzleProgressEntity>>

    @Query("SELECT * FROM puzzle_progress WHERE puzzleId = :puzzleId LIMIT 1")
    suspend fun getProgressForPuzzle(puzzleId: String): PuzzleProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: PuzzleProgressEntity)

    @Query("SELECT COUNT(*) FROM puzzle_progress WHERE isSolved = 1")
    fun getTotalSolvedCountFlow(): Flow<Int>
}
