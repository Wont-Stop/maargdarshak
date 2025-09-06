package com.techmania.maargdarshak.users.ui.screen.auth.signup

/**
 * Represents the state of the Signup screen.
 *
 * @param fullName The current value in the full name input field.
 * @param email The current value in the email input field.
 * @param password The current value in the password input field.
 * @param isPasswordVisible Toggles the visibility of the password text.
 * @param isLoading True if a signup process is in progress.
 * @param fullNameError An optional error message for full name validation.
 * @param emailError An optional error message for email validation.
 * @param passwordError An optional error message for password validation.
 * @param generalError An optional error message for general signup failures.
 */
data class SignupUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null
)