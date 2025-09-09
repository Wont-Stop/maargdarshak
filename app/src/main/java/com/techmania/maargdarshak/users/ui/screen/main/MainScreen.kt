package com.techmania.maargdarshak.users.ui.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techmania.maargdarshak.users.navigation.Screen
import com.techmania.maargdarshak.users.ui.screen.home.HomeScreen
import com.techmania.maargdarshak.users.ui.screen.liveTracker.LiveTrackerScreen
import com.techmania.maargdarshak.users.ui.screen.shared.BottomNavBar
import com.techmania.maargdarshak.users.ui.screen.trips.MyTripsScreen
// Import your SettingsScreen composable when you create it

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.LiveTracker.route) { LiveTrackerScreen(navController = navController) }
            composable(Screen.MyTrips.route) { MyTripsScreen(navController = navController) }
            // composable(Screen.Settings.route) { SettingsScreen(navController = navController) }
        }
    }
}