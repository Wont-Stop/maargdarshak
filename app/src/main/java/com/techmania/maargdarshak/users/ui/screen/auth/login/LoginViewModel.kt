package com.techmania.maargdarshak.users.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository // Inject the repository
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

    // In file: com/techmania/maargdarshak/users/ui/screen/auth/login/LoginViewModel.kt


    fun onSignInClicked() {
        // Keep your validation logic
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            if (_uiState.value.email.isBlank()) _uiState.update { it.copy(emailError = "Email cannot be empty") }
            if (_uiState.value.password.isBlank()) _uiState.update { it.copy(passwordError = "Password cannot be empty") }
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        // This is the new part: calling the repository
        repository.signIn(
            email = _uiState.value.email,
            password = _uiState.value.password
        ) { isSuccess, errorMessage ->
            if (isSuccess) {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToHome)
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, generalError = errorMessage ?: "Sign in failed. Please check your credentials.")
                }
            }
        }
    }

    fun onCreateAccountClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToSignup)
        }
    }

    // Placeholder for Forgot Password navigation
    fun onForgotPasswordClicked() {
        // TODO: Implement navigation to Forgot Password screen
    }

    // Placeholders for Social Logins
    fun onGoogleSignInClicked() { /* TODO: Implement Google Sign-In */ }
    fun onAppleSignInClicked() { /* TODO: Implement Apple Sign-In */ }
    fun onFacebookSignInClicked() { /* TODO: Implement Facebook Sign-In */ }

    sealed class NavigationEvent {
        object NavigateToHome : NavigationEvent()
        object NavigateToSignup : NavigationEvent()
    }
}