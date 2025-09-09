package com.techmania.maargdarshak.data.model

// Add a default no-argument constructor for Firestore deserialization
data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = ""
)