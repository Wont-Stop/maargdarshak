package com.techmania.maargdarshak.users.ui.screen.trips


// Data model for a single Saved Route
data class SavedRoute(
    val id: String,
    val startDestination: String,
    val endDestination: String,
    val label: String, // e.g., "Morning Route"
    val distance: String,
    val duration: String,
    val transportType: String // e.g., "Bus" or "Taxi"
)

// Data model for a single Recent Trip (can be reused from Home)
data class RecentTrip(
    val id: String,
    val startDestination: String,
    val endDestination: String,
    val distance: String,
    val duration: String,
    val cost: String
)

// Represents the entire state for the MyTripsScreen
data class MyTripsUiState(
    val selectedTabIndex: Int = 0, // 0 for Saved, 1 for Recent
    val savedRoutes: List<SavedRoute> = emptyList(),
    val recentTrips: List<RecentTrip> = emptyList(),
    val isLoading: Boolean = true
)

