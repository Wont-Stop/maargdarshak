package com.techmania.maargdarshak.users.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.Resource
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
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    // To handle one-off navigation events
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        // Fetch real user data instead of dummy data
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                // Handle user not logged in scenario if necessary
                _uiState.update { it.copy(isLoading = false, error = "User not found") }
                return@launch
            }

            when (val result = userRepository.getUserProfile(currentUser.uid)) {
                is Resource.Success -> {
                    val user = result.data
                    _uiState.update {
                        it.copy(
                            // Use real user data, providing defaults if empty
                            userName = user?.fullName?.takeIf { name -> name.isNotBlank() } ?: "Guest User",
                            userLocation = user?.address?.takeIf { addr -> addr.isNotBlank() } ?: "No location set",
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                else -> { /* No-op for Loading state */ }
            }
        }
    }

    // --- Event Handlers ---
    fun onPlanTripClicked() { /* TODO: Navigate to trip planning screen */ }
    fun onRecentTripClicked(tripId: String) { /* TODO: Navigate to trip details screen */ }
    fun onNotificationsClicked() { /* TODO: Navigate to notifications screen */ }
    fun onSearchClicked() { /* TODO: Navigate to search screen */ }

    /**
     * Called when the user taps on the profile picture or name in the top bar.
     */
    fun onProfileClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToEditProfile)
        }
    }

    /**
     * Sealed class for one-off navigation events.
     */
    sealed class NavigationEvent {
        object NavigateToEditProfile : NavigationEvent()
    }
}
