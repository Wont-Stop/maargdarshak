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

    suspend fun getStopsDetails(stopIds: List<String>): Resource<List<Pair<String, com.google.firebase.firestore.GeoPoint>>> {
        if (stopIds.isEmpty()) {
            return Resource.Success(emptyList())
        }
        return try {
            val stopsQuery = firestore.collection("stops")
                .whereIn(com.google.firebase.firestore.FieldPath.documentId(), stopIds)
                .get()
                .await()

            // Use a map to easily look up details by ID
            val stopsDetailsMap = stopsQuery.documents.associate { doc ->
                val name = doc.getString("name") ?: "Unknown Stop"
                val geoPoint = doc.getGeoPoint("location") ?: com.google.firebase.firestore.GeoPoint(0.0, 0.0)
                doc.id to (name to geoPoint)
            }

            // Reconstruct the list in the original order provided by the route
            val orderedStops = stopIds.mapNotNull { id ->
                stopsDetailsMap[id]?.let { (name, geoPoint) ->
                    name to geoPoint
                }
            }

            Resource.Success(orderedStops)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch stop details.")
        }
    }

    suspend fun getRouteDetails(routeId: String): Resource<com.techmania.maargdarshak.data.model.Route> {
        return try {
            val document = firestore.collection("routes").document(routeId).get().await()
            val route = document.toObject(com.techmania.maargdarshak.data.model.Route::class.java)
            Resource.Success(route!!)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch route details.")
        }
    }


}