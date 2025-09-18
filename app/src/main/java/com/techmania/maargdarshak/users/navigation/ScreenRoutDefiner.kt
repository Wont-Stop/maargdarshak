package com.techmania.maargdarshak.users.navigation


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Permission : Screen("permission")
    object Main : Screen("main")
    object Home : Screen("home")
    object LiveTracker : Screen("live_tracker")
    object MyTrips : Screen("my_trips")
    object Settings : Screen("settings")
    object PlaceSearch : Screen("place_search")
    object ForgotPassword : Screen("forgot_password")
    object EditProfile : Screen("edit_profile") // <-- ADD THIS NEW ROUTE

    // Route for the map screen with arguments
    object LiveMap : Screen("map/{busId}/{routeId}/{originStopName}/{destinationStopName}") {
        fun createRoute(
            busId: String,
            routeId: String,
            originStopName: String,
            destinationStopName: String
        ): String {
            return "map/$busId/$routeId/$originStopName/$destinationStopName"
        }
    }

    object BusResults : Screen("bus_results/{origin}/{destination}") {
        fun createRoute(origin: String, destination: String): String {
            return "bus_results/$origin/$destination"
        }
    }
}
