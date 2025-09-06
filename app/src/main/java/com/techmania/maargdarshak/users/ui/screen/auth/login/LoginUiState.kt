package com.techmania.maargdarshak.users.ui.screen.auth.login

/**
 * Represents the state of the Login screen.
 *
 * @param email The current value in the email input field.
 * @param password The current value in the password input field.
 * @param rememberMe Whether the "Remember Me" checkbox is checked.
 * @param isPasswordVisible Toggles the visibility of the password text.
 * @param isLoading True if a login process is in progress, to show loading indicators.
 * @param emailError An optional error message related to email validation.
 * @param passwordError An optional error message related to password validation.
 * @param generalError An optional error message for general login failures (e.g., incorrect credentials).
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null
)