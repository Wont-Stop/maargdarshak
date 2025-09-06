package com.techmania.maargdarshak.users.navigation


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Permission : Screen("permission")
    object Home : Screen("home")
    object LiveTracker : Screen("live_tracker")
    object MyTrips : Screen("my_trips")
    object Settings : Screen("settings")

    // Route for the map screen with arguments
    object LiveMap : Screen("map/{origin}/{destination}/{transportType}") {
        fun createRoute(origin: String, destination: String, transportType: String): String {
            return "map/$origin/$destination/$transportType"
        }
    }
}