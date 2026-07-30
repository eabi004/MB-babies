package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LeaderboardEntity
import com.example.data.repository.LeaderboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LeaderboardViewModel(private val leaderboardRepository: LeaderboardRepository) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("GLOBAL") // GLOBAL, ENGLISH, TELUGU, HINDI
    val selectedFilter: StateFlow<String> = _selectedFilter

    val leaderboardEntries: StateFlow<List<LeaderboardEntity>> = _selectedFilter
        .flatMapLatest { filter ->
            leaderboardRepository.getLeaderboardFlow(filter)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        setFilter("GLOBAL")
    }

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        viewModelScope.launch {
            leaderboardRepository.refreshLeaderboard(filter)
        }
    }
}
