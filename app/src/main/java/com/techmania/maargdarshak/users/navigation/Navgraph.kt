package com.techmania.maargdarshak.users.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techmania.maargdarshak.users.ui.screen.auth.forgotpassword.ForgotPasswordScreen
import com.techmania.maargdarshak.users.ui.screen.auth.login.LoginScreen
import com.techmania.maargdarshak.users.ui.screen.auth.signup.SignupScreen
import com.techmania.maargdarshak.users.ui.screen.busresults.BusResultsScreen
import com.techmania.maargdarshak.users.ui.screen.main.MainScreen
import com.techmania.maargdarshak.users.ui.screen.map.LiveMapScreen
import com.techmania.maargdarshak.users.ui.screen.permissions.PermissionRequestScreen
import com.techmania.maargdarshak.users.ui.screen.search.PlaceSearchScreen
import com.techmania.maargdarshak.users.ui.screen.splash.SplashScreen

@Composable
fun NavGraph(
    startDestination: String = Screen.Splash.route
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(route = Screen.Permission.route) {
            PermissionRequestScreen(navController = navController)
        }

        composable(route = Screen.Main.route) {
            // UPDATED: Pass the top-level navController into MainScreen
            MainScreen(navController = navController)
        }


        // ADD THIS NEW BLOCK for the search screen
        composable(route = Screen.PlaceSearch.route) {
            PlaceSearchScreen(navController = navController)
        }

        composable(
            route = Screen.BusResults.route,
            arguments = listOf(
                navArgument("origin") { type = NavType.StringType },
                navArgument("destination") { type = NavType.StringType }
            )
        ) {
            // The ViewModel will automatically receive these arguments
            BusResultsScreen(navController = navController)
        }

        composable(
            route = Screen.LiveMap.route,
            arguments = listOf(
                navArgument("busId") { type = NavType.StringType },
                navArgument("routeId") { type = NavType.StringType }
            )
        ) {
            LiveMapScreen(navController = navController)
        }

    }
}