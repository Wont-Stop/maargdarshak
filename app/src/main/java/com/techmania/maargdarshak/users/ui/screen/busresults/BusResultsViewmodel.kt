package com.techmania.maargdarshak.users.ui.screen.busresults


import androidx.lifecycle.SavedStateHandle
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
class BusResultsViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BusResultsUiState())
    val uiState = _uiState.asStateFlow()

    // ADD THESE TWO LINES
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val origin: String = savedStateHandle["origin"] ?: "Unknown"
    private val destination: String = savedStateHandle["destination"] ?: "Unknown"

    init {
        _uiState.update { it.copy(origin = origin, destination = destination) }
        findTrips()
    }

    private fun findTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = tripRepository.findAvailableTrips(origin, destination)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, trips = result.data)
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> { }
            }
        }
    }

    sealed class NavigationEvent {
        data class NavigateToMap(
            val busId: String,
            val routeId: String,
            val origin: String,
            val destination: String
        ) : NavigationEvent()
    }

    fun onTripSelected(trip: Trip) {
        viewModelScope.launch {
            _navigationEvent.emit(
                NavigationEvent.NavigateToMap(
                    busId = trip.busId,
                    routeId = trip.routeId,
                    origin = origin, // Pass the origin stored in the ViewModel
                    destination = destination // Pass the destination stored in the ViewModel
                )
            )
        }
    }
}