package com.techmania.maargdarshak.users.ui.screen.map


import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.techmania.maargdarshak.R

@Composable
fun VehicleMarker(
    vehicle: Vehicle
) {
    Marker(
        state = MarkerState(position = vehicle.position),
        title = "Vehicle ${vehicle.id}",
        // Use different icons based on vehicle type
        icon = BitmapDescriptorFactory.fromResource(R.drawable.busicon) // Replace with your marker icon
    )
}

@Composable
fun OriginMarker(
    location: MapLocation
) {
    Marker(
        state = MarkerState(position = location.latLng),
        title = "Origin",
        snippet = location.name,
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
    )
}

@Composable
fun DestinationMarker(
    location: MapLocation
) {
    Marker(
        state = MarkerState(position = location.latLng),
        title = "Destination",
        snippet = location.name
    )
}