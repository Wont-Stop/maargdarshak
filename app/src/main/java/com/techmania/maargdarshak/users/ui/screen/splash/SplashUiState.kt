package com.techmania.maargdarshak.users.ui.screen.splash

/**
 * Represents the UI state for the Splash screen.
 *
 * For the current design, this state is empty because the screen is a static
 * welcome page. The only action ("Get Started") triggers a one-off navigation event,
 * which is handled separately from the continuous UI state.
 *
 * This file is kept for structural consistency and can be populated if the
 * screen's complexity increases (e.g., to show a loading indicator).
 */
data class SplashUiState(
    val isLoading: Boolean = false
)