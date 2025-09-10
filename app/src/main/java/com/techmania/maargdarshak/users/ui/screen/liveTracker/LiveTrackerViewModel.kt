package com.techmania.maargdarshak.users.ui.screen.liveTracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveTrackerViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LiveTrackerUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private var allStops: List<String> = emptyList()

    init {
        fetchAllStops()
    }

    private fun fetchAllStops() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (allStops.isEmpty()) {
                when (val result = tripRepository.getAllStops()) {
                    is Resource.Success -> {
                        allStops = result.data
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                    else -> {}
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // In LiveTrackerViewModel.kt...

    fun onOriginQueryChanged(query: String) {
        // When the user types, update the query AND clear their previous final selection
        _uiState.update {
            it.copy(
                originQuery = query,
                origin = "" // <-- IMPORTANT: Clear the final selection
            )
        }
        if (query.isBlank()) {
            _uiState.update { it.copy(isOriginDropdownExpanded = false, originSuggestions = emptyList()) }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isOriginDropdownExpanded = true,
                    originSuggestions = allStops.filter { stop ->
                        stop.contains(query, ignoreCase = true)
                    }
                )
            }
        }
    }

    fun onDestinationQueryChanged(query: String) {
        // Also clear the destination when the user types in its field
        _uiState.update {
            it.copy(
                destinationQuery = query,
                destination = "" // <-- IMPORTANT: Clear the final selection
            )
        }
        if (query.isBlank()) {
            _uiState.update { it.copy(isDestinationDropdownExpanded = false, destinationSuggestions = emptyList()) }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isDestinationDropdownExpanded = true,
                    destinationSuggestions = allStops.filter { stop ->
                        stop.contains(query, ignoreCase = true)
                    }
                )
            }
        }
    }

// ... The rest of your ViewModel remains the same.

    fun onOriginSelected(origin: String) {
        _uiState.update {
            it.copy(
                origin = origin,
                originQuery = origin,
                isOriginDropdownExpanded = false
            )
        }
    }

    fun onDestinationSelected(destination: String) {
        _uiState.update {
            it.copy(
                destination = destination,
                destinationQuery = destination,
                isDestinationDropdownExpanded = false
            )
        }
    }

    // NEW: Functions to handle dismissing the dropdowns
    fun onOriginDismiss() {
        _uiState.update { it.copy(isOriginDropdownExpanded = false) }
    }

    fun onDestinationDismiss() {
        _uiState.update { it.copy(isDestinationDropdownExpanded = false) }
    }

    fun onSwapLocations() {
        _uiState.update { currentState ->
            currentState.copy(
                origin = currentState.destination,
                destination = currentState.origin,
                originQuery = currentState.destinationQuery,
                destinationQuery = currentState.originQuery
            )
        }
    }

    // --- Transport Type Functions ---
    fun onTransportTypeSelected(transportType: TransportType) {
        _uiState.update { it.copy(selectedTransportType = transportType, isTransportDropdownExpanded = false) }
    }

    fun onTransportDropdownDismiss() {
        _uiState.update { it.copy(isTransportDropdownExpanded = false) }
    }

    fun onTransportDropdownClicked() {
        _uiState.update { it.copy(isTransportDropdownExpanded = !_uiState.value.isTransportDropdownExpanded) }
    }

    fun onFindTransportClicked() {
        val currentState = _uiState.value
        if (currentState.origin.isBlank() || currentState.destination.isBlank()) {
            return
        }
        viewModelScope.launch {
            _navigationEvent.emit(
                NavigationEvent.NavigateToBusResults(
                    origin = currentState.origin,
                    destination = currentState.destination
                )
            )
        }
    }

    sealed class NavigationEvent {
        data class NavigateToBusResults(
            val origin: String,
            val destination: String,
        ) : NavigationEvent()
    }
}