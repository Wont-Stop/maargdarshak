package com.techmania.maargdarshak.di

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techmania.maargdarshak.data.network.DirectionsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // ADD THIS FUNCTION
    @Provides
    @Singleton
    fun providePlacesClient(app: Application): com.google.android.libraries.places.api.net.PlacesClient {
        // Initialize the SDK if it's not already initialized
        if (!Places.isInitialized()) {
            Places.initialize(app.applicationContext, "AIzaSyCOCCS-A-PLkXsh7fPxb14ehUi-oaSN6BQ")
        }
        return Places.createClient(app)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // ADD THIS
    @Provides
    @Singleton
    fun provideDirectionsApiService(retrofit: Retrofit): DirectionsApiService =
    retrofit.create(DirectionsApiService::class.java)

}