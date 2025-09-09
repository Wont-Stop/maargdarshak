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
    /**
     * Fetches place autocomplete predictions based on a user's query.
     */
    suspend fun getAutocompletePredictions(query: String): Resource<List<Place.Field>> {
        return try {
            val token = AutocompleteSessionToken.newInstance()
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                // Bias results to a specific country for better relevance
                .setCountries("IN") // Biasing results for India
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()
            val placeFields = response.autocompletePredictions.map {
                // We will just return the primary and secondary text for now
                // In a real app, you would fetch more details using the placeId
                listOf(Place.Field.NAME, Place.Field.ADDRESS)
            }
            Resource.Success(placeFields.flatten())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred fetching places.")
        }
    }
}