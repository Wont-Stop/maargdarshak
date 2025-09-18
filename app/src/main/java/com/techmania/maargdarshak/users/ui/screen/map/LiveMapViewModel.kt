package com.techmania.maargdarshak.users.ui.screen.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.repository.MapRepository
import com.techmania.maargdarshak.data.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveMapViewModel @Inject constructor(
    private val mapRepository: MapRepository,
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LiveMapUiState())
    val uiState = _uiState.asStateFlow()

    private val busId: String = savedStateHandle["busId"] ?: ""
    private val routeId: String = savedStateHandle["routeId"] ?: ""

    private val originStopName: String = savedStateHandle["originStopName"] ?: ""
    private val destinationStopName: String = savedStateHandle["destinationStopName"] ?: ""


    init {

        _uiState.update { it.copy(destinationStopName = this.destinationStopName) }


        if (busId.isNotBlank() && routeId.isNotBlank()) {
            fetchRouteAndStops()
            startListeningForVehicleUpdates()
        } else {
            _uiState.update { it.copy(isLoading = false, error = "Bus or Route ID missing.") }
        }
    }

    private fun fetchRouteAndStops() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Get the list of stop IDs for the selected route
            when (val routeResult = tripRepository.getRouteDetails(routeId)) {
                is Resource.Success -> {
                    val stopIds = routeResult.data.stops
                    if (stopIds.size >= 2) {
                        // 2. If successful, fetch the details (name, location) for each stop ID
                        fetchStopsAndDirections(stopIds)
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = "Route has invalid stop data.") }
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = routeResult.message) }
                }
                else -> {}
            }
        }
    }

    private suspend fun fetchStopsAndDirections(stopIds: List<String>) {
        // 3. Get stop details (ID, Name, GeoPoint) from the repository
        when (val stopsResult = tripRepository.getStopsDetails(stopIds)) {
            is Resource.Success -> {
                // CRITICAL FIX: Create StopInfo with the REAL name from Firestore
                val stopsInfo = stopsResult.data.map { (name, geoPoint) ->
                    StopInfo(
                        id = name, // Use the real name as the ID for consistency
                        name = name,
                        position = LatLng(geoPoint.latitude, geoPoint.longitude)
                    )
                }
                _uiState.update { it.copy(stops = stopsInfo) }

                // 4. Get the polyline for the ENTIRE bus route using waypoints
                val origin = stopsInfo.first().position
                val destination = stopsInfo.last().position
                val waypoints = stopsInfo.subList(1, stopsInfo.size - 1).joinToString(separator = "|") {
                    "${it.position.latitude},${it.position.longitude}"
                }
                val directionsResult = mapRepository.getDirections(
                    origin = "${origin.latitude},${origin.longitude}",
                    destination = "${destination.latitude},${destination.longitude}",
                    waypoints = waypoints.ifEmpty { null }
                )
                if (directionsResult is Resource.Success) {
                    _uiState.update { it.copy(routePolyline = directionsResult.data) }
                } else if (directionsResult is Resource.Error) {
                    _uiState.update { it.copy(error = directionsResult.message) }
                }

                // 5. Get the polyline for the USER'S SPECIFIC JOURNEY
                fun String.norm() = trim().lowercase()

                val userOriginIndex = stopsInfo.indexOfFirst { it.name.norm().contains(originStopName.norm()) }
                val userDestIndex   = stopsInfo.indexOfFirst { it.name.norm().contains(destinationStopName.norm()) }

                if (userOriginIndex != -1 && userDestIndex != -1 && userOriginIndex < userDestIndex) {
                    val userStops = stopsInfo.subList(userOriginIndex, userDestIndex + 1)
                    val userOrigin = userStops.first().position
                    val userDestination = userStops.last().position
                    val userWaypoints = userStops.subList(1, userStops.size - 1)
                        .joinToString(separator = "|") { "${it.position.latitude},${it.position.longitude}" }

                    val highlightedResult = mapRepository.getDirections(
                        origin = "${userOrigin.latitude},${userOrigin.longitude}",
                        destination = "${userDestination.latitude},${userDestination.longitude}",
                        waypoints = userWaypoints.ifEmpty { null }
                    )
                    if (highlightedResult is Resource.Success) {
                        _uiState.update { it.copy(highlightedPolyline = highlightedResult.data) }
                    }
                    // Optional: You could add an else block here to log if the highlight fails
                }
            }
            is Resource.Error -> {
                _uiState.update { it.copy(isLoading = false, error = stopsResult.message) }
            }
            else -> {}
        }
    }

    private fun startListeningForVehicleUpdates() {
        viewModelScope.launch {
            // 5. Start listening for live data from the specific bus document
            mapRepository.getLiveVehicleDetails(busId).collect { vehicle ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        liveLocation = LatLng(vehicle.location.latitude, vehicle.location.longitude),
                        liveSpeed = "${vehicle.speed} km/h",
                        vacantSeats = vehicle.vacantSeats,
                        statusMessage = vehicle.currentStatusMessage.ifEmpty { "On the way" }
                    )
                }
            }
        }
    }

    fun onTrackButtonClicked() {
        _uiState.update { it.copy(isTracking = !it.isTracking) }
        // TODO: Implement logic to subscribe/unsubscribe from Firebase Cloud Messaging (FCM)
        // for this specific bus topic (e.g., "bus_DL3SDA2472").
    }
}