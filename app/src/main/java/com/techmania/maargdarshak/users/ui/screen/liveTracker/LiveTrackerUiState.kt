package com.techmania.maargdarshak.users.ui.screen.liveTracker

import com.techmania.maargdarshak.R


/**
 * Enum representing the different types of transport available for tracking.
 */
enum class TransportType(val displayName: String, val icon: Int) {
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
    // Fields for the selected values
    val origin: String = "",
    val destination: String = "",

    // Fields to manage the autocomplete state for the ORIGIN input
    val originQuery: String = "",
    val originSuggestions: List<String> = emptyList(),
    val isOriginDropdownExpanded: Boolean = false,

    // Fields to manage the autocomplete state for the DESTINATION input
    val destinationQuery: String = "",
    val destinationSuggestions: List<String> = emptyList(),
    val isDestinationDropdownExpanded: Boolean = false,

    // Fields for Transport Type selection
    val selectedTransportType: TransportType = TransportType.BUS,
    val isTransportDropdownExpanded: Boolean = false, // UPDATED: Renamed for clarity

    // General state
    val isLoading: Boolean = false,
    val error: String? = null
)