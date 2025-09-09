package com.techmania.maargdarshak.users.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.Resource
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
            // ...
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            when (val result = repository.signIn(_uiState.value.email, _uiState.value.password)) {
                is Resource.Success -> {
                    _navigationEvent.emit(NavigationEvent.NavigateToHome)
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, generalError = result.message)
                    }
                }
                is Resource.Loading -> {
                    // This case is handled by the initial isLoading = true
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