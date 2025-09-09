package com.techmania.maargdarshak.users.ui.screen.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaceSearchUiState(
    val query: String = "",
    val searchResults: List<Place.Field> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    val uiState = mutableStateOf(PlaceSearchUiState())

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        uiState.value = uiState.value.copy(query = newQuery)
        searchJob?.cancel() // Cancel the previous search job
        searchJob = viewModelScope.launch {
            delay(300) // Debounce: wait for 300ms of no typing
            searchPlaces(newQuery)
        }
    }

    private fun searchPlaces(query: String) {
        if (query.length < 3) {
            uiState.value = uiState.value.copy(searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)
            when (val result = placesRepository.getAutocompletePredictions(query)) {
                is Resource.Success -> {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        searchResults = result.data
                    )
                }
                is Resource.Error -> {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> { /* Handled by isLoading state */ }
            }
        }
    }
}