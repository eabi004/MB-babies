package com.example.data.repository

import com.example.data.local.LeaderboardDao
import com.example.data.local.LeaderboardEntity
import com.example.model.Language
import kotlinx.coroutines.flow.Flow

class LeaderboardRepository(private val dao: LeaderboardDao) {

    fun getLeaderboardFlow(languageFilter: String): Flow<List<LeaderboardEntity>> {
        return dao.getLeaderboardFlow(languageFilter)
    }

    suspend fun refreshLeaderboard(languageFilter: String) {
        dao.clearLeaderboard(languageFilter)

        val sampleLeaderboard = when (languageFilter) {
            "TELUGU" -> listOf(
                LeaderboardEntity("u101", "Aarav Reddi", "avatar_owl", 3420, 48, 1, "TELUGU", "తెలుగు జ్ఞాని (Telugu Scholar)"),
                LeaderboardEntity("u102", "Ananya Rao", "avatar_cat", 2980, 39, 2, "TELUGU", "అక్షర విజేత (Akshara Champ)"),
                LeaderboardEntity("u103", "Vihaan Verma", "avatar_lion", 2650, 34, 3, "TELUGU", "పద రత్న (Word Gem)"),
                LeaderboardEntity("u104", "Saanvi Murthy", "avatar_bear", 2210, 28, 4, "TELUGU", "బాల మేధావి (Child Genius)"),
                LeaderboardEntity("u105", "Kavya Patel", "avatar_owl", 1890, 22, 5, "TELUGU", "వర్ణశిల్పి (Word Sculptor)")
            )
            "HINDI" -> listOf(
                LeaderboardEntity("u201", "Aditya Sharma", "avatar_lion", 3650, 52, 1, "HINDI", "हिंदी सम्राट (Hindi King)"),
                LeaderboardEntity("u202", "Diya Gupta", "avatar_owl", 3100, 42, 2, "HINDI", "शब्द रक्षक (Word Guard)"),
                LeaderboardEntity("u203", "Reyansh Joshi", "avatar_cat", 2740, 36, 3, "HINDI", "भाषा ज्ञानी (Language Scholar)"),
                LeaderboardEntity("u204", "Isha Malhotra", "avatar_bear", 2300, 29, 4, "HINDI", "बाल विद्वान (Junior Scholar)"),
                LeaderboardEntity("u205", "Vivaan Singh", "avatar_cat", 1950, 24, 5, "HINDI", "शब्द रत्न (Word Gem)")
            )
            "ENGLISH" -> listOf(
                LeaderboardEntity("u301", "Sophia Chen", "avatar_owl", 3890, 56, 1, "ENGLISH", "Spelling Bee Champion"),
                LeaderboardEntity("u302", "Ethan Brown", "avatar_lion", 3210, 45, 2, "ENGLISH", "Grand Wordmaster"),
                LeaderboardEntity("u303", "Maya Patel", "avatar_cat", 2820, 38, 3, "ENGLISH", "Phonics Wizard"),
                LeaderboardEntity("u304", "Lucas Miller", "avatar_bear", 2410, 31, 4, "ENGLISH", "Grade 1 Star"),
                LeaderboardEntity("u305", "Zoe Wilson", "avatar_owl", 2050, 26, 5, "ENGLISH", "Vocabulary Explorer")
            )
            else -> listOf(
                LeaderboardEntity("u001", "Aarav Reddi", "avatar_owl", 4250, 62, 1, "GLOBAL", "Global Wordmaster 👑"),
                LeaderboardEntity("u002", "Sophia Chen", "avatar_cat", 3890, 56, 2, "GLOBAL", "Tri-Lingual Star ✨"),
                LeaderboardEntity("u003", "Aditya Sharma", "avatar_lion", 3650, 52, 3, "GLOBAL", "CBSE Grade 1 Champ 🌟"),
                LeaderboardEntity("u004", "Ananya Rao", "avatar_bear", 3100, 42, 4, "GLOBAL", "Polyglot Prodigy 🏆"),
                LeaderboardEntity("u005", "Maya Patel", "avatar_owl", 2820, 38, 5, "GLOBAL", "Word Explorer 🚀")
            )
        }

        dao.insertLeaderboard(sampleLeaderboard)
    }
}
