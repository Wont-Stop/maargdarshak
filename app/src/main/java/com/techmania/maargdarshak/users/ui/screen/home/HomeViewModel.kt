package com.techmania.maargdarshak.users.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchHomeScreenData()
    }

    private fun fetchHomeScreenData() {
        viewModelScope.launch {
            // Start in a loading state
            _uiState.update { it.copy(isLoading = true) }

            // Simulate a network delay
            delay(1500)

            // Hardcoded data for demonstration
            val dummyTrips = listOf(
                RecentTrip("1", "Bole", "Piassa", "7.2 km", "18 mins", "35 ETB"),
                RecentTrip("2", "Kebena", "Meskel Square", "5.4 km", "25 mins", "10 ETB"),
                RecentTrip("3", "Bole Airport", "Africa Avenue", "12.8 km", "45 mins", "20 ETB")
            )

            _uiState.update {
                it.copy(
                    userName = "John Cena",
                    userLocation = "Addis Ababa, Ethiopia",
                    recentTrips = dummyTrips,
                    isLoading = false
                )
            }
        }
    }

    // --- Event Handlers ---
    fun onPlanTripClicked() { /* TODO: Navigate to trip planning screen */ }
    fun onRecentTripClicked(tripId: String) { /* TODO: Navigate to trip details screen */ }
    fun onNotificationsClicked() { /* TODO: Navigate to notifications screen */ }
    fun onSearchClicked() { /* TODO: Navigate to search screen */ }
}