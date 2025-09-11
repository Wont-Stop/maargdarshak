package com.techmania.maargdarshak.users.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.model.User
import com.techmania.maargdarshak.data.repository.AuthRepository
import com.techmania.maargdarshak.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentFirebaseUser = authRepository.getCurrentUser()
            if (currentFirebaseUser == null) {
                _uiState.update { it.copy(isLoading = false, error = "User not logged in.") }
                // This could also trigger a navigation event to the login screen
                return@launch
            }

            when (val result = userRepository.getUserProfile(currentFirebaseUser.uid)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, user = result.data) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> { /* Handled by isLoading state */ }
            }
        }
    }

    fun onSignOutClicked() {
        authRepository.signOut()
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToLogin)
        }
    }

    fun onEditProfileClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToEditProfile)
        }
    }

    sealed class NavigationEvent {
        object NavigateToLogin : NavigationEvent()
        object NavigateToEditProfile : NavigationEvent()
    }
}
