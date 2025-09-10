package com.techmania.maargdarshak.users.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onRememberMeChange(isChecked: Boolean) {
        _uiState.update { it.copy(rememberMe = isChecked) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onSignInClicked() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            // Validation logic...
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            when (val result = repository.signIn(_uiState.value.email, _uiState.value.password)) {
                is Resource.Success -> {
                    // UPDATED: Navigate to the permission screen first
                    _navigationEvent.emit(NavigationEvent.NavigateToPermission)
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, generalError = result.message)
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun onCreateAccountClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToSignup)
        }
    }

    fun onForgotPasswordClicked() {
        // TODO: Implement navigation to Forgot Password screen
    }

    fun onGoogleSignInClicked() { /* TODO: Implement Google Sign-In */ }
    fun onAppleSignInClicked() { /* TODO: Implement Apple Sign-In */ }
    fun onFacebookSignInClicked() { /* TODO: Implement Facebook Sign-In */ }

    sealed class NavigationEvent {
        object NavigateToPermission : NavigationEvent() // UPDATED
        object NavigateToSignup : NavigationEvent()
    }
}