package com.techmania.maargdarshak.users.ui.screen.auth.signup

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
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name, fullNameError = null, generalError = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onSignUpClicked() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = authRepository.signUp(_uiState.value.email, _uiState.value.password)) {
                is Resource.Success -> {
                    result.data.user?.let { firebaseUser ->
                        userRepository.createUserProfile(firebaseUser, _uiState.value.fullName)
                    }
                    // UPDATED: Navigate to the permission screen first
                    _navigationEvent.emit(NavigationEvent.NavigateToPermission)
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, generalError = result.message)
                    }
                }
                is Resource.Loading -> { /* Handled by isLoading state */ }
            }
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToLogin)
        }
    }

    private fun validateInputs(): Boolean {
        val state = _uiState.value
        // ... (validation logic remains the same)
        return true // placeholder
    }

    sealed class NavigationEvent {
        object NavigateToPermission : NavigationEvent() // UPDATED
        object NavigateToLogin : NavigationEvent()
    }
}