package com.techmania.maargdarshak.users.ui.screen.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.repository.MapRepository
import com.techmania.maargdarshak.data.repository.TripRepository
import com.techmania.maargdarshak.users.ui.screen.liveTracker.TransportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveMapViewModel @Inject constructor(
    private val mapRepository: MapRepository,
    private val tripRepository: TripRepository, // Added TripRepository
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LiveMapUiState())
    val uiState = _uiState.asStateFlow()

    // UPDATED: Get busId and routeId from navigation
    private val busId: String = savedStateHandle["busId"] ?: ""
    private val routeId: String = savedStateHandle["routeId"] ?: ""

    init {
        if (busId.isNotBlank() && routeId.isNotBlank()) {
            fetchRouteDetails()
            startListeningForVehicleDetails()
        }
    }

    private fun fetchRouteDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // First, get the route details to find the start and end stops
            when (val routeResult = tripRepository.getRouteDetails(routeId)) {
                is Resource.Success -> {
                    val stops = routeResult.data.stops
                    if (stops.size >= 2) {
                        // Then, get the directions polyline using the first and last stop
                        val directionsResult = mapRepository.getDirections(stops.first(), stops.last())
                        if (directionsResult is Resource.Success) {
                            _uiState.update { it.copy(routePolyline = directionsResult.data) }
                        }
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = routeResult.message) }
                }
                else -> {}
            }
        }
    }

    private fun startListeningForVehicleDetails() {
        viewModelScope.launch {
            // UPDATED: Listen for updates on a SINGLE bus using the new repository function
            mapRepository.getLiveVehicleDetails(busId).collect { vehicle ->
                val vehicleState = VehicleUiState(
                    id = vehicle.id,
                    position = LatLng(vehicle.location.latitude, vehicle.location.longitude),
                    type = TransportType.BUS // This can be made dynamic later
                )

                // You can also update other details for the bottom sheet here
                // e.g., speed, next stop, etc. from the 'vehicle' object

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        vehicles = listOf(vehicleState) // The list will now only contain one vehicle
                    )
                }
            }
        }
    }
}