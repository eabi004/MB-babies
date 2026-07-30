package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenges WHERE dateString = :dateString")
    fun getChallengesForDateFlow(dateString: String): Flow<List<DailyChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<DailyChallengeEntity>)

    @Query("UPDATE daily_challenges SET currentProgress = currentProgress + 1, isCompleted = (currentProgress + 1 >= targetCount) WHERE id = :challengeId")
    suspend fun incrementProgress(challengeId: String)

    @Query("UPDATE daily_challenges SET isClaimed = 1 WHERE id = :challengeId")
    suspend fun markClaimed(challengeId: String)
}
