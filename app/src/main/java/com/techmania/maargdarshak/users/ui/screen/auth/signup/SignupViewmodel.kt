package com.techmania.maargdarshak.users.ui.screen.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.repository.AuthRepository
import com.techmania.maargdarshak.data.repository.UserRepository
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
class SignupViewModel @Inject constructor(private val authRepository: AuthRepository,
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
                    // After successful auth, create the user profile in Firestore
                    result.data.user?.let { firebaseUser ->
                        userRepository.createUserProfile(firebaseUser, _uiState.value.fullName)
                    }
                    _navigationEvent.emit(NavigationEvent.NavigateToHome)
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
        val hasError = listOf(
            { if (state.fullName.isBlank()) _uiState.update { it.copy(fullNameError = "Full name cannot be empty") } },
            { if (!state.email.contains("@")) _uiState.update { it.copy(emailError = "Invalid email format") } },
            { if (state.password.length < 6) _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") } }
        ).map { it.invoke() }.any { _uiState.value.fullNameError != null || _uiState.value.emailError != null || _uiState.value.passwordError != null }

        return !hasError
    }

    sealed class NavigationEvent {
        object NavigateToHome : NavigationEvent()
        object NavigateToLogin : NavigationEvent()
    }
}