package com.techmania.maargdarshak.data.repository


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.users.ui.screen.busresults.Trip
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun findAvailableTrips(origin: String, destination: String): Resource<List<Trip>> {
        return try {
            // Step 1: Find routes that contain the origin stop.
            // Note: Firestore can't query for two 'array-contains' in one go.
            // A more advanced solution might use a search service, but this is a good start.
            val routeQuery = firestore.collection("routes")
                .whereArrayContains("stops", origin)
                .get().await()

            // Step 2: From those routes, find the ones that also contain the destination.
            val matchingRouteIds = routeQuery.documents.filter {
                val stops = it.get("stops") as? List<String> ?: emptyList()
                stops.contains(destination) && stops.indexOf(origin) < stops.indexOf(destination)
            }.map { it.id }

            if (matchingRouteIds.isEmpty()) {
                return Resource.Success(emptyList())
            }

            // Step 3: Find all scheduled trips for those matching routes that are in the future.
            val tripQuery = firestore.collection("scheduledTrips")
                .whereIn("routeId", matchingRouteIds)
                .whereGreaterThan("departureTime", Date())
                .orderBy("departureTime", Query.Direction.ASCENDING)
                .get().await()

            val trips = tripQuery.toObjects(Trip::class.java)
            Resource.Success(trips)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred while finding trips.")
        }
    }

    suspend fun getAllStops(): Resource<List<String>> {
        return try {
            val routesSnapshot = firestore.collection("routes").get().await()
            // Get the 'stops' array from every route document, flatten into a single list,
            // and remove duplicates.
            val allStops = routesSnapshot.documents
                .flatMap { it.get("stops") as? List<String> ?: emptyList() }
                .distinct()
                .sorted()
            Resource.Success(allStops)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch stops.")
        }
    }

}