package com.techmania.maargdarshak.users.ui.screen.liveTracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveTrackerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LiveTrackerUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onOriginChange(origin: String) {
        _uiState.update { it.copy(origin = origin) }
    }

    fun onDestinationChange(destination: String) {
        _uiState.update { it.copy(destination = destination) }
    }

    fun onTransportTypeSelected(transportType: TransportType) {
        _uiState.update { it.copy(selectedTransportType = transportType, isDropdownExpanded = false) }
    }

    fun onDropdownDismiss() {
        _uiState.update { it.copy(isDropdownExpanded = false) }
    }

    fun onDropdownClicked() {
        _uiState.update { it.copy(isDropdownExpanded = true) }
    }

    fun onSwapLocations() {
        _uiState.update {
            it.copy(
                origin = it.destination,
                destination = it.origin
            )
        }
    }

    fun onFindTransportClicked() {
        viewModelScope.launch {
            // TODO: Add validation for origin and destination fields
            val currentState = _uiState.value
            _navigationEvent.emit(
                NavigationEvent.NavigateToMap(
                    origin = currentState.origin,
                    destination = currentState.destination,
                    transportType = currentState.selectedTransportType
                )
            )
        }
    }

    sealed class NavigationEvent {
        data class NavigateToMap(
            val origin: String,
            val destination: String,
            val transportType: TransportType
        ) : NavigationEvent()
    }
}