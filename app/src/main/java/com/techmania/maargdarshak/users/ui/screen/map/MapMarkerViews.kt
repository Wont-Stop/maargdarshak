package com.techmania.maargdarshak.users.ui.screen.map


import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.data.model.Vehicle

@Composable
fun VehicleMarker(
    state: VehicleUiState
) {
    Marker(
        state = MarkerState(position = state.position),
        title = "Vehicle ${state.id}",
        snippet = "Live Location",
        icon = BitmapDescriptorFactory.fromResource(R.drawable.busicon)
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