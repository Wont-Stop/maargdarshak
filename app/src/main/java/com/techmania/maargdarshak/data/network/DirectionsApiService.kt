package com.techmania.maargdarshak.data.network


import retrofit2.http.GET
import retrofit2.http.Query

// A data class to match the JSON response from Google. We only care about the polyline.
data class DirectionsResponse(val routes: List<Route>)
data class Route(val overview_polyline: OverviewPolyline)
data class OverviewPolyline(val points: String)

interface DirectionsApiService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): DirectionsResponse
}