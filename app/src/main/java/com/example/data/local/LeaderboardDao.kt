package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard_cache WHERE language = :language ORDER BY rank ASC")
    fun getLeaderboardFlow(language: String): Flow<List<LeaderboardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboard(entries: List<LeaderboardEntity>)

    @Query("DELETE FROM leaderboard_cache WHERE language = :language")
    suspend fun clearLeaderboard(language: String)
}
