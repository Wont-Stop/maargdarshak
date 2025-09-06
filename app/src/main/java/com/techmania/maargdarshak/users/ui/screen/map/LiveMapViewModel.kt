package com.techmania.maargdarshak.users.ui.screen.map


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.techmania.maargdarshak.users.ui.screen.liveTracker.TransportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LiveMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LiveMapUiState())
    val uiState = _uiState.asStateFlow()

    // Retrieve arguments passed from LiveTrackerScreen
    private val originName: String = savedStateHandle["origin"] ?: "Unknown"
    private val destinationName: String = savedStateHandle["destination"] ?: "Unknown"
    private val transportType: TransportType = TransportType.valueOf(savedStateHandle["transportType"] ?: "BUS")

    init {
        loadMapData()
        startVehicleUpdates()
    }

    private fun loadMapData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulate API calls to get coordinates, route, etc.
            delay(1500)

            // Hardcoded coordinates for Addis Ababa for demonstration
            val originLocation = MapLocation(originName, LatLng(9.0215, 38.7513)) // Meskel Square
            val destinationLocation = MapLocation(destinationName, LatLng(9.0352, 38.7699)) // Shola Market

            // A simple, hardcoded polyline route
            val route = listOf(
                originLocation.latLng,
                LatLng(9.0250, 38.7550),
                LatLng(9.0280, 38.7600),
                LatLng(9.0320, 38.7650),
                destinationLocation.latLng
            )

            val initialVehicles = List(3) { index ->
                Vehicle(
                    id = "bus_$index",
                    position = route.random(), // Place buses randomly on the route initially
                    type = transportType
                )
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    origin = originLocation,
                    destination = destinationLocation,
                    routePolyline = route,
                    vehicles = initialVehicles,
                    bottomSheetInfo = BottomSheetInfo(
                        routeName = "$originName → $destinationName",
                        vehicleCount = initialVehicles.size,
                        estimatedCost = "10-15 ETB",
                        transportType = transportType
                    )
                )
            }
        }
    }

    private fun startVehicleUpdates() {
        viewModelScope.launch {
            // Wait for the initial route to be loaded
            while (_uiState.value.routePolyline.isEmpty()) {
                delay(100)
            }

            // Flow to simulate live vehicle movement
            flow {
                while (true) {
                    delay(3000) // Update every 3 seconds
                    emit(Unit)
                }
            }.collect {
                _uiState.update { currentState ->
                    val updatedVehicles = currentState.vehicles.map { vehicle ->
                        // Simple simulation: move vehicle slightly along a random axis
                        val newLat = vehicle.position.latitude + Random.nextDouble(-0.0005, 0.0005)
                        val newLng = vehicle.position.longitude + Random.nextDouble(-0.0005, 0.0005)
                        vehicle.copy(position = LatLng(newLat, newLng))
                    }
                    currentState.copy(vehicles = updatedVehicles)
                }
            }
        }
    }
}