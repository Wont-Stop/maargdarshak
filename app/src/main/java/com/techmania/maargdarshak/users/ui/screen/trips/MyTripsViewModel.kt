package com.techmania.maargdarshak.users.ui.screen.trips


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
class MyTripsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MyTripsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchTripsData()
    }

    private fun fetchTripsData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulate a network delay
            delay(1000)

            // Dummy data for demonstration
            val dummySavedRoutes = listOf(
                SavedRoute("sr1", "Bole", "6 Kilo", "Morning Route", "9.2 km", "35 mins", "Bus"),
                SavedRoute("sr2", "Mexico", "CMC", "Work Commute", "11.5 km", "25 mins", "Taxi"),
                SavedRoute("sr3", "Meskel Square", "Shola Market", "Weekend Trip", "5.1 km", "28 mins", "Bus"),
                SavedRoute("sr4", "CMC", "Arat Kilo", "University Route", "8.8 km", "30 mins", "Taxi")
            )

            val dummyRecentTrips = listOf(
                RecentTrip("rt1", "Bole", "Piassa", "7.2 km", "18 mins", "35 ETB"),
                RecentTrip("rt2", "Hayahulet", "Stadium", "4.1 km", "15 mins", "10 ETB"),
                RecentTrip("rt3", "Lamberet", "Kazanchis", "6.5 km", "22 mins", "20 ETB")
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    savedRoutes = dummySavedRoutes,
                    recentTrips = dummyRecentTrips
                )
            }
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun onSavedRouteClicked(routeId: String) {
        // TODO: Navigate to a screen to start this saved route
    }

    fun onRecentTripClicked(tripId: String) {
        // TODO: Navigate to the Trip Detail screen for this trip
    }
}

