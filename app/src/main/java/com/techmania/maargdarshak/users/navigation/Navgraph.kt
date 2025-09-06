package com.techmania.maargdarshak.users.navigation


import androidx.compose.runtime.Composable
import com.techmania.maargdarshak.users.navigation.Screen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techmania.maargdarshak.users.ui.screen.auth.login.LoginScreen
import com.techmania.maargdarshak.users.ui.screen.auth.signup.SignupScreen
import com.techmania.maargdarshak.users.ui.screen.home.HomeScreen
import com.techmania.maargdarshak.users.ui.screen.liveTracker.LiveTrackerScreen
import com.techmania.maargdarshak.users.ui.screen.map.LiveMapScreen
import com.techmania.maargdarshak.users.ui.screen.permissions.PermissionRequestScreen
import com.techmania.maargdarshak.users.ui.screen.splash.SplashScreen

@Composable
fun NavGraph(
    // In a real app, you'd pass the start destination based on
    // whether the user is already logged in.
    startDestination: String = Screen.Splash.route
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        // Auth Flow
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Signup.route) {
            SignupScreen(navController = navController)
        }

        // Permissions Screen
        composable(route = Screen.Permission.route) {
            PermissionRequestScreen(navController = navController)
        }

        // Main App Screens
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.LiveTracker.route) {
            LiveTrackerScreen(navController = navController)
        }

        // Live Map Screen (with arguments)
        composable(
            route = Screen.LiveMap.route,
            arguments = listOf(
                navArgument("origin") { type = NavType.StringType },
                navArgument("destination") { type = NavType.StringType },
                navArgument("transportType") { type = NavType.StringType }
            )
        ) {
            // The LiveMapViewModel will automatically receive these arguments
            LiveMapScreen(navController = navController)
        }

        // TODO: Add composables for MyTrips and Settings screens
        // composable(route = Screen.MyTrips.route) { MyTripsScreen(navController) }
        // composable(route = Screen.Settings.route) { SettingsScreen(navController) }
    }
}