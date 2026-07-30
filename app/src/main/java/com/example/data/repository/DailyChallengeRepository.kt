package com.example.data.repository

import com.example.data.local.DailyChallengeDao
import com.example.data.local.DailyChallengeEntity
import com.example.model.Language
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyChallengeRepository(private val dao: DailyChallengeDao) {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getTodayChallengesFlow(language: Language): Flow<List<DailyChallengeEntity>> {
        val todayStr = dateFormatter.format(Date())
        return dao.getChallengesForDateFlow(todayStr)
    }

    suspend fun seedTodayChallengesIfEmpty(language: Language) {
        val todayStr = dateFormatter.format(Date())
        val defaultChallenges = listOf(
            DailyChallengeEntity(
                id = "${todayStr}_1",
                dateString = todayStr,
                title = "1st Grade Word Explorer",
                description = "Solve 3 CBSE words in ${language.displayName}",
                language = language.name,
                targetCount = 3,
                currentProgress = 0,
                xpReward = 150,
                coinReward = 50
            ),
            DailyChallengeEntity(
                id = "${todayStr}_2",
                dateString = todayStr,
                title = "Phonics Master",
                description = "Complete 2 Letter Unscramble Puzzles",
                language = language.name,
                targetCount = 2,
                currentProgress = 0,
                xpReward = 200,
                coinReward = 75
            ),
            DailyChallengeEntity(
                id = "${todayStr}_3",
                dateString = todayStr,
                title = "Super Reader Streak",
                description = "Score 300 XP today with 3 Stars rating",
                language = language.name,
                targetCount = 1,
                currentProgress = 0,
                xpReward = 300,
                coinReward = 100
            )
        )
        dao.insertChallenges(defaultChallenges)
    }

    suspend fun recordPuzzleSolved(language: Language) {
        val todayStr = dateFormatter.format(Date())
        dao.incrementProgress("${todayStr}_1")
        dao.incrementProgress("${todayStr}_2")
    }

    suspend fun claimReward(challengeId: String) {
        dao.markClaimed(challengeId)
    }
}
