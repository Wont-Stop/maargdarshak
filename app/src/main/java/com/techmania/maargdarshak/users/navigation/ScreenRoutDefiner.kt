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
    object ForgotPassword : Screen("forgot_password") // <-- ADD THIS


    // Route for the map screen with arguments
    object LiveMap : Screen("map/{busId}/{routeId}") {
        fun createRoute(busId: String, routeId: String): String {
            return "map/$busId/$routeId"
        }
    }

    // ADD THIS NEW ROUTE DEFINITION
    object BusResults : Screen("bus_results/{origin}/{destination}") {
        fun createRoute(origin: String, destination: String): String {
            return "bus_results/$origin/$destination"
        }
    }
}