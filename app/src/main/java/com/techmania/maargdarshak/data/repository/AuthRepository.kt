package com.techmania.maargdarshak.data.repository

import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    // A simple function to sign in a user
    // In a real app, you'd use coroutines and handle results properly
    fun signIn(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // A simple function to create a new user
    fun signUp(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // Function to check if a user is already logged in
    fun getCurrentUser() = auth.currentUser
}