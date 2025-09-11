package com.techmania.maargdarshak.data.model

import com.google.firebase.firestore.GeoPoint

/**
 * Represents a single vehicle document FROM Firestore.
 * The default values are required for Firestore's automatic data conversion.
 */
data class Vehicle(
    val id: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val routeId: String = "",
    val speed: Long = 0, // Add this
    val nextStop: String = "" // Add this
)