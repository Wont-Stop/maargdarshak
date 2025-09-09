package com.techmania.maargdarshak.users.ui.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.model.Vehicle // <-- IMPORTANT: The correct import
import com.techmania.maargdarshak.data.repository.MapRepository
import com.techmania.maargdarshak.users.ui.screen.liveTracker.TransportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveMapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LiveMapUiState())
    val uiState = _uiState.asStateFlow()
    private val origin: String = savedStateHandle["origin"] ?: ""
    private val destination: String = savedStateHandle["destination"] ?: ""
    private val routeId: String = "RajivChowk-IndiaGate"


    init {
        startListeningForVehicles("RajivChowk-IndiaGate")
    }

    private fun startListeningForVehicles(routeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            mapRepository.getVehicleUpdates(routeId)
                .catch { exception ->
                    _uiState.update { it.copy(isLoading = false, error = exception.message) }
                }
                .collect { vehiclesFromFirestore: List<Vehicle> -> // Explicitly typed for clarity
                    // Convert the data model (Vehicle) to the UI model (VehicleUiState)
                    val vehicleUiStates = vehiclesFromFirestore.map { dataVehicle ->
                        VehicleUiState(
                            id = dataVehicle.id,
                            position = LatLng(dataVehicle.location.latitude, dataVehicle.location.longitude),
                            type = TransportType.BUS // Hardcode type for now
                        )
                    }

                    _uiState.update {
                        it.copy(isLoading = false, vehicles = vehicleUiStates)
                    }
                }
        }
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
                            // Also update origin/destination if you need to show markers
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



}