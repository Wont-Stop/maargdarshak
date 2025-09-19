package com.techmania.maargdarshak.users.ui.screen.busresults


import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.Date

// This data class matches your 'scheduledTrips' documents in Firestore.
// @IgnoreExtraProperties helps prevent crashes if a field is missing.
@IgnoreExtraProperties
data class Trip(
    val routeId: String = "",
    val busId: String = "",
    val operatorName: String = "Unknown Operator", // Assuming you add this to your Firestore doc
    val busType: String = "A/C Seater", // Assuming you add this
    val departureTime: Date? = null,
    val arrivalTime: Date? = null,
    val fare: Int = 0,
    val seatsAvailable: Int = 0,
    val rating: Double = 4.0, // Assuming you add this
    val reviewCount: Int = 0 // Assuming you add this
)

data class BusResultsUiState(
    val origin: String = "",
    val destination: String = "",
    val trips: List<Trip> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)