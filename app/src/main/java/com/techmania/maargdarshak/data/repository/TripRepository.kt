package com.techmania.maargdarshak.data.repository

// The incorrect import is now removed

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.users.ui.screen.busresults.Trip
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // THIS IS THE CORRECTED LINE THAT WAS MISSING
    private val TAG = "TripRepositoryDebug"

    suspend fun findAvailableTrips(origin: String, destination: String): Resource<List<Trip>> {
        // LOG 1: Confirm the function is called with the correct data
        Log.d(TAG, "--- Searching for trips ---")
        Log.d(TAG, "Origin: '$origin', Destination: '$destination'")

        if (origin.isBlank() || destination.isBlank()) {
            return Resource.Success(emptyList())
        }

        return try {
            val snapshot = firestore.collection("scheduledTrips")
                .whereArrayContains("stopNames", origin)
                .whereGreaterThanOrEqualTo("departureTime", Timestamp.now())
                .get()
                .await()

            // LOG 2: Check if Firestore returns ANY documents for the origin
            Log.d(TAG, "Step 1 (Firestore Query): Found ${snapshot.size()} documents matching origin and time.")

            val validTrips = snapshot.documents.filter { doc ->
                val stopNames = doc.get("stopNames") as? List<*>
                if (stopNames != null) {
                    val originIndex = stopNames.indexOf(origin)
                    val destinationIndex = stopNames.indexOf(destination)
                    destinationIndex > originIndex
                } else {
                    false
                }
            }.mapNotNull { doc ->
                doc.toObject(Trip::class.java)
            }

            // LOG 3: Check the result of the client-side filtering
            Log.d(TAG, "Step 2 (Client Filter): Found ${validTrips.size} valid trips after checking destination.")
            Log.d(TAG, "------------------------")

            Resource.Success(validTrips)
        } catch (e: Exception) {
            // LOG 4: Catch any unexpected crashes
            Log.e(TAG, "An error occurred while finding trips. THIS IS A CRASH OR PERMISSION ISSUE.", e)
            Resource.Error(e.message ?: "An error occurred while finding trips.")
        }
    }

    // ... The rest of your functions (getAllStops, etc.) remain the same

    suspend fun getAllStops(): Resource<List<String>> {
        return try {
            val snapshot = firestore.collection("stops").get().await()
            val stopNames = snapshot.documents.mapNotNull { document ->
                document.getString("name")
            }.sorted()
            Resource.Success(stopNames)
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

            val stopsDetailsMap = stopsQuery.documents.associate { doc ->
                val name = doc.getString("name") ?: "Unknown Stop"
                val geoPoint = doc.getGeoPoint("location") ?: com.google.firebase.firestore.GeoPoint(0.0, 0.0)
                doc.id to (name to geoPoint)
            }

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