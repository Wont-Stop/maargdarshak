package com.techmania.maargdarshak.users.ui.screen.liveTracker

import com.techmania.maargdarshak.R


/**
 * Enum representing the different types of transport available for tracking.
 */
enum class TransportType(val displayName: String, val icon: Int) {
    // Replace R.drawable icons with your actual transport icons
    BUS("Bus", R.drawable.busicon),
    TAXI("Taxi", R.drawable.taxi)
}

/**
 * Represents the UI state for the LiveTracker screen.
 *
 * @param origin The starting location entered by the user.
 * @param destination The destination entered by the user.
 * @param selectedTransportType The currently selected mode of transport.
 * @param availableTransportTypes The list of all available transport options.
 * @param isDropdownExpanded Controls the visibility of the transport type dropdown.
 */
data class LiveTrackerUiState(
    val origin: String = "Current location",
    val destination: String = "",
    val selectedTransportType: TransportType = TransportType.BUS,
    val availableTransportTypes: List<TransportType> = TransportType.values().toList(),
    val isDropdownExpanded: Boolean = false
)