package com.techmania.maargdarshak.users.ui.screen.map

import androidx.compose.runtime.Composable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.utils.BitmapUtils // Import the new utility

@Composable
fun VehicleMarker(
    state: VehicleUiState
) {
    // UPDATED: Use the scaled bitmap descriptor
    val markerIcon = BitmapUtils.rememberScaledBitmapDescriptor(
        vectorResId = R.drawable.buslogo, // Your drawable resource
        widthDp = 40, // Desired width in DP
        heightDp = 40 // Desired height in DP
    )

    if (markerIcon != null) {
        Marker(
            state = MarkerState(position = state.position),
            title = "Vehicle ${state.id}",
            snippet = "Live Location",
            icon = markerIcon // Use the scaled icon
        )
    }
}