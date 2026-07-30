package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthRepository
import com.example.data.auth.AuthState
import com.example.data.local.UserEntity
import com.example.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val currentUser: StateFlow<UserEntity?> = authRepository.currentUserFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val authState: StateFlow<AuthState> = combine(
        authRepository.currentUserFlow,
        _isLoading,
        _errorMessage
    ) { user, loading, error ->
        AuthState(
            isAuthenticated = user != null,
            user = user,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthState()
    )

    fun register(username: String, email: String, avatarId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = authRepository.register(username, email, avatarId)
            _isLoading.value = false
            result.onFailure {
                _errorMessage.value = it.localizedMessage ?: "Registration failed"
            }
        }
    }

    fun login(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = authRepository.login(email)
            _isLoading.value = false
            result.onFailure {
                _errorMessage.value = it.localizedMessage ?: "Login failed"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            authRepository.updateLanguage(language)
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
