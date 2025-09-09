package com.techmania.maargdarshak.users.ui.screen.trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTripsScreen(
    navController: NavController,
    viewModel: MyTripsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabTitles = listOf("Saved routes", "Recent Trips")

    // REMOVED the Scaffold from here
    Column(modifier = Modifier.fillMaxSize()) {
        // We add our own TopAppBar since the parent Scaffold doesn't have one
        TopAppBar(
            title = { Text("My Trips", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                }
            }
        )
        TabRow(selectedTabIndex = uiState.selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.selectedTabIndex == index,
                    onClick = { viewModel.onTabSelected(index) },
                    text = { Text(text = title) }
                )
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Content for the selected tab
            when (uiState.selectedTabIndex) {
                0 -> SavedRoutesList(
                    routes = uiState.savedRoutes,
                    onRouteClicked = viewModel::onSavedRouteClicked
                )
                1 -> RecentTripsList(
                    trips = uiState.recentTrips,
                    onTripClicked = viewModel::onRecentTripClicked
                )
            }
        }
    }
}

@Composable
private fun SavedRoutesList(
    routes: List<SavedRoute>,
    onRouteClicked: (String) -> Unit
) {
    Column {
        ListHeader(text = "${routes.size} saved routes")
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = routes, key = { it.id }) { route ->
                SavedRouteItem(route = route, onClick = onRouteClicked)
            }
        }
    }
}

@Composable
private fun RecentTripsList(
    trips: List<RecentTrip>,
    onTripClicked: (String) -> Unit
) {
    Column {
        ListHeader(text = "${trips.size} recent trips")
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = trips, key = { it.id }) { trip ->
                RecentTripItem(trip = trip, onClick = onTripClicked)
            }
        }
    }
}

@Composable
private fun ListHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

