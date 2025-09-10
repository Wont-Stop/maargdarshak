package com.techmania.maargdarshak.data.repository

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.techmania.maargdarshak.data.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val placesClient: PlacesClient
) {

    data class PlaceSuggestion(val placeId: String, val primaryText: String, val secondaryText: String)

    /**
     * Fetches place autocomplete predictions based on a user's query.
     */
    suspend fun getAutocompletePredictions(query: String): Resource<List<PlaceSuggestion>> {
        return try {
            val token = AutocompleteSessionToken.newInstance()
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .setCountries("IN") // Biasing results for India
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()
            // CORRECTED: Map the response to our new PlaceSuggestion data class
            val suggestions = response.autocompletePredictions.map { prediction ->
                PlaceSuggestion(
                    placeId = prediction.placeId,
                    primaryText = prediction.getPrimaryText(null).toString(),
                    secondaryText = prediction.getSecondaryText(null).toString()
                )
            }
            Resource.Success(suggestions)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred fetching places.")
        }
    }
}