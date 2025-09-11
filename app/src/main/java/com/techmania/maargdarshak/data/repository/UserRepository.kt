package com.techmania.maargdarshak.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.techmania.maargdarshak.data.Resource
import com.techmania.maargdarshak.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    /**
     * Creates a new user document in the 'users' collection in Firestore.
     * This is typically called right after a user successfully signs up.
     */
    suspend fun createUserProfile(user: FirebaseUser, fullName: String) {
        val userDocument = mapOf(
            "uid" to user.uid,
            "fullName" to fullName,
            "email" to user.email,
            "phone" to "", // Initially empty
            "address" to "" // Initially empty
        )
        firestore.collection("users").document(user.uid).set(userDocument).await()
    }

    /**
     * Fetches a user's profile from Firestore based on their UID.
     */
    // Make sure this import exists, or add it:
//... inside the UserRepository class

    suspend fun getUserProfile(uid: String): Resource<User?> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            // Explicitly use your app's data model for conversion
            val user = document.toObject(com.techmania.maargdarshak.data.model.User::class.java)
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch user profile.")
        }
    }

    /**
     * Updates a user's profile document in Firestore.
     */
    suspend fun updateUserProfile(uid: String, updatedUser: com.techmania.maargdarshak.data.model.User): Resource<Unit> {
        return try {
            firestore.collection("users").document(uid).set(updatedUser).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update profile.")
        }
    }
}