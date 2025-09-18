package com.techmania.maargdarshak.data.model

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Represents a single vehicle document FROM Firestore.
 * The default values are required for Firestore's automatic data conversion.
 */
data class Vehicle(
    // Note: The bus ID is the document ID, so it's not a field here.
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val routeId: String = "",
    val speed: Long = 0,
    val nextStopIndex: Int = 0,
    val vacantSeats: Int = 0,
    val currentStatusMessage: String = "",
    @ServerTimestamp val lastUpdated: Date? = null
)