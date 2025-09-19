package com.techmania.maargdarshak.data.repository

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.PolyUtil
import com.techmania.maargdarshak.BuildConfig
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.model.Vehicle
import com.techmania.maargdarshak.data.network.DirectionsApiService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val directionsApi: DirectionsApiService // CORRECTED: Inject the API service
) {
    /**
     * Listens for real-time updates to vehicles on a specific route.
     * Returns a Flow that emits a new list of vehicles whenever there's a change.
     */
    fun getVehicleUpdates(routeId: String): Flow<List<Vehicle>> = callbackFlow {
        val collection = firestore.collection("vehicles").whereEqualTo("routeId", routeId)

        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Close the flow with an exception on error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val vehicles = snapshot.documents.mapNotNull { doc ->
                    val geoPoint = doc.getGeoPoint("location") ?: GeoPoint(0.0, 0.0)
                    Vehicle(
                        location = geoPoint,
                        routeId = doc.getString("routeId") ?: ""
                    )
                }
                trySend(vehicles).isSuccess // Send the latest list of vehicles
            }
        }

        // When the Flow is cancelled, remove the Firestore listener
        awaitClose { listener.remove() }
    }

    suspend fun getDirections(origin: String, destination: String, waypoints: String?): Resource<List<LatLng>> {
        return try {
            val response = directionsApi.getDirections(origin, destination, waypoints, BuildConfig.MAPS_API_KEY)
            if (response.routes.isNotEmpty()) {
                val points = response.routes[0].overview_polyline.points
                val decodedPath = PolyUtil.decode(points)
                Resource.Success(decodedPath)
            } else {
                Resource.Error("No routes found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get directions.")
        }
    }

    fun getLiveVehicleDetails(busId: String): Flow<Vehicle> = callbackFlow {
        val docRef = firestore.collection("vehicles").document(busId)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val vehicle = snapshot?.toObject(Vehicle::class.java)
            if (vehicle != null) {

                Log.d("MaargdarshakApp", "Firestore listener received update. Location: ${vehicle.location}")

                trySend(vehicle).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }

}