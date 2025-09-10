package com.techmania.maargdarshak.users.ui.screen.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.model.Vehicle
import com.techmania.maargdarshak.data.repository.MapRepository
import com.techmania.maargdarshak.users.ui.screen.liveTracker.TransportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveMapViewModel @Inject constructor(
    private val mapRepository: MapRepository,
    savedStateHandle: SavedStateHandle // CORRECTED: Inject SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LiveMapUiState())
    val uiState = _uiState.asStateFlow()

    // These lines will now work correctly
    private val origin: String = savedStateHandle["origin"] ?: ""
    private val destination: String = savedStateHandle["destination"] ?: ""
    // We can derive the routeId from origin and destination, or pass it as an argument later
    private val routeId: String = "RajivChowk-IndiaGate" // Example

    init {
        // Only fetch if we have valid origin/destination from navigation
        if (origin.isNotBlank() && destination.isNotBlank()) {
            fetchRoute()
        }
        // Always listen for vehicles on the specified route
        startListeningForVehicles(routeId)
    }

    private fun fetchRoute() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = mapRepository.getDirections(origin, destination)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            routePolyline = result.data,
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    private fun startListeningForVehicles(routeId: String) {
        viewModelScope.launch {
            mapRepository.getVehicleUpdates(routeId)
                .catch { exception ->
                    _uiState.update { it.copy(isLoading = false, error = exception.message) }
                }
                .collect { vehiclesFromFirestore ->
                    val vehicleUiStates = vehiclesFromFirestore.map { dataVehicle ->
                        VehicleUiState(
                            id = dataVehicle.id,
                            position = LatLng(dataVehicle.location.latitude, dataVehicle.location.longitude),
                            type = TransportType.BUS
                        )
                    }
                    _uiState.update {
                        it.copy(vehicles = vehicleUiStates)
                    }
                }
        }
    }
}