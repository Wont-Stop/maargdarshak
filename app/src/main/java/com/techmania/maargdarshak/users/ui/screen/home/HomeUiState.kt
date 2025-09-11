package com.techmania.maargdarshak.users.ui.screen.home

/**
 * Represents a single recent trip.
 */
data class RecentTrip(
    val id: String,
    val startDestination: String,
    val endDestination: String,
    val distance: String,
    val duration: String,
    val cost: String
)

/**
 * Represents the entire state for the HomeScreen.
 *
 * @param userName The name of the logged-in user.
 * @param userLocation The current city/location of the user.
 * @param recentTrips A list of the user's recent trips.
 * @param isLoading Indicates if the screen data is currently being loaded.
 */
data class HomeUiState(
    val userName: String = "",
    val userLocation: String = "",
    val recentTrips: List<RecentTrip> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null // <-- Add this line
)