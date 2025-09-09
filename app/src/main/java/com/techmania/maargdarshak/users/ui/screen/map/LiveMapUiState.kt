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

// The rest of your UI state remains the same for now
data class LiveMapUiState(
    val origin: MapLocation? = null,
    val destination: MapLocation? = null,
    val routePolyline: List<LatLng> = emptyList(),
    val vehicles: List<VehicleUiState> = emptyList(), // This now uses VehicleUiState
    val bottomSheetInfo: BottomSheetInfo? = null,
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