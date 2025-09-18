package com.techmania.maargdarshak.users.ui.screen.map

import com.google.android.gms.maps.model.LatLng
import com.techmania.maargdarshak.users.ui.screen.liveTracker.TransportType

/**
 * Represents a single vehicle's state FOR THE UI.
 * This uses LatLng, which is what the Map composable needs.
 */
data class VehicleUiState(
    val id: String,
    val position: LatLng,
    val type: TransportType
)

data class StopInfo(
    val id: String,
    val name: String,
    val position: LatLng
)

// The rest of your UI state remains the same for now
data class LiveMapUiState(
    val routePolyline: List<LatLng> = emptyList(),
    val highlightedPolyline: List<LatLng> = emptyList(), // <-- ADD THIS
    val destinationStopName: String = "", // <-- ADD THIS
    val stops: List<StopInfo> = emptyList(), // Will hold the details of each stop on the route
    val liveLocation: LatLng? = null,
    val liveSpeed: String = "0 km/h",
    val vacantSeats: Int = 0,
    val statusMessage: String = "Fetching status...",
    val isTracking: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

// These classes from your file can remain as they are
data class MapLocation(val name: String, val latLng: LatLng)
data class BottomSheetInfo(
    val routeName: String,
    val vehicleCount: Int,
    val estimatedCost: String,
    val transportType: TransportType
)