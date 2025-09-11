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
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private var currentUser: User? = null
    private var currentUid: String? = null

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            currentUid = authRepository.getCurrentUser()?.uid
            if (currentUid == null) {
                _uiState.update { it.copy(isLoading = false, error = "User not logged in.") }
                return@launch
            }

            when (val result = userRepository.getUserProfile(currentUid!!)) {
                is Resource.Success -> {
                    currentUser = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            fullName = currentUser?.fullName ?: "",
                            phone = currentUser?.phone ?: "",
                            address = currentUser?.address ?: ""
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> { /* Handled by isLoading state */ }
            }
        }
    }

    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name, error = null) }
    }

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone, error = null) }
    }

    fun onAddressChange(address: String) {
        _uiState.update { it.copy(address = address, error = null) }
    }

    fun onSaveChangesClicked() {
        val uid = currentUid
        if (uid == null) {
            _uiState.update { it.copy(error = "Cannot save profile. User is not logged in.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            // Construct a new User object from the current state.
            // This works even if the user document didn't exist before.
            val updatedUser = User(
                uid = uid,
                email = authRepository.getCurrentUser()?.email ?: "", // Keep existing email
                fullName = _uiState.value.fullName.trim(),
                phone = _uiState.value.phone.trim(),
                address = _uiState.value.address.trim()
            )

            when (val result = userRepository.updateUserProfile(uid, updatedUser)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    // Use a short delay before navigating to allow user to see success message
                    kotlinx.coroutines.delay(500)
                    _navigationEvent.emit(NavigationEvent.NavigateBack)
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                is Resource.Loading -> { /* No-op */ }
            }
        }
    }

    sealed class NavigationEvent {
        object NavigateBack : NavigationEvent()
    }
}

