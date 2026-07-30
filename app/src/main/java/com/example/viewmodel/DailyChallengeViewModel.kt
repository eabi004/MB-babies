package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthRepository
import com.example.data.local.DailyChallengeEntity
import com.example.data.repository.DailyChallengeRepository
import com.example.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DailyChallengeViewModel(
    private val challengeRepository: DailyChallengeRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val currentLanguageFlow = MutableStateFlow(Language.ENGLISH)

    val dailyChallenges: StateFlow<List<DailyChallengeEntity>> = currentLanguageFlow
        .flatMapLatest { lang ->
            challengeRepository.getTodayChallengesFlow(lang)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadChallengesForLanguage(language: Language) {
        currentLanguageFlow.value = language
        viewModelScope.launch {
            challengeRepository.seedTodayChallengesIfEmpty(language)
        }
    }

    fun claimReward(challenge: DailyChallengeEntity) {
        if (challenge.isCompleted && !challenge.isClaimed) {
            viewModelScope.launch {
                challengeRepository.claimReward(challenge.id)
                authRepository.addReward(
                    xp = challenge.xpReward,
                    stars = 1,
                    coins = challenge.coinReward
                )
            }
        }
    }
}
