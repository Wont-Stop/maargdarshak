package com.techmania.maargdarshak.users.ui.screen.shared


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector
import com.techmania.maargdarshak.users.navigation.Screen

/**
 * A data class to represent a single item in the Bottom Navigation Bar.
 */
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

/**
 * A list containing all the items for the bottom navigation.
 * This is the single source of truth for your bottom bar.
 */
val bottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        icon = Icons.Outlined.Home,
        route = Screen.Home.route
    ),
    BottomNavItem(
        label = "Live Tracker",
        icon = Icons.Outlined.TravelExplore,
        route = Screen.LiveTracker.route
    ),
    BottomNavItem(
        label = "My Trips",
        icon = Icons.Outlined.List,
        route = Screen.MyTrips.route
    ),
    BottomNavItem(
        label = "Settings",
        icon = Icons.Outlined.Settings,
        route = Screen.Settings.route
    )
)

