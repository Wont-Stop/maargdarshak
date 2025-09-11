package com.techmania.maargdarshak.users.ui.screen.profile


import com.techmania.maargdarshak.data.model.User

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

data class EditProfileUiState(
    val fullName: String = "",
    val phone: String = "",
    val address: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false,
    val isLoading: Boolean = true

)