package com.techmania.maargdarshak.users.ui.screen.auth.forgotpassword


data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val successMessage: String? = null
)