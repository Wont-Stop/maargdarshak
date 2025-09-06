package com.techmania.maargdarshak.users.ui.screen.map


import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.techmania.maargdarshak.users.ui.screen.liveTracker.TransportType

/**
 * Represents a specific point on the map.
 */
data class MapLocation(val name: String, val latLng: LatLng)

/**
 * Represents a single vehicle being tracked on the map.
 */
data class Vehicle(
    val id: String,
    val position: LatLng,
    val type: TransportType
)

/**
 * Holds the information to be displayed in the MapBottomSheet.
 */
data class BottomSheetInfo(
    val routeName: String,
    val vehicleCount: Int,
    val estimatedCost: String,
    val transportType: TransportType
)

/**
 * Represents the entire UI state for the LiveMap screen.
 */
data class LiveMapUiState(
    val origin: MapLocation? = null,
    val destination: MapLocation? = null,
    val routePolyline: List<LatLng> = emptyList(),
    val vehicles: List<Vehicle> = emptyList(),
    val bottomSheetInfo: BottomSheetInfo? = null,
    val isLoading: Boolean = true
)