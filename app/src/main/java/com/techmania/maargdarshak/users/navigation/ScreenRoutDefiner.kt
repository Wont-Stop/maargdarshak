package com.techmania.maargdarshak.users.navigation


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Permission : Screen("permission")
    object Main : Screen("main") // <-- ADD THIS LINE
    object Home : Screen("home")
    object LiveTracker : Screen("live_tracker")
    object MyTrips : Screen("my_trips")
    object Settings : Screen("settings")
    object PlaceSearch : Screen("place_search") // <-- ADD THIS LINE


    // Route for the map screen with arguments
    object LiveMap : Screen("map/{origin}/{destination}/{transportType}") {
        fun createRoute(origin: String, destination: String, transportType: String): String {
            return "map/$origin/$destination/$transportType"
        }
    }

    // ADD THIS NEW ROUTE DEFINITION
    object BusResults : Screen("bus_results/{origin}/{destination}") {
        fun createRoute(origin: String, destination: String): String {
            return "bus_results/$origin/$destination"
        }
    }
}