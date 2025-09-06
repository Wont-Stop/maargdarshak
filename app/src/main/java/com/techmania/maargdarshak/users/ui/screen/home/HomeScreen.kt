package com.techmania.maargdarshak.users.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.techmania.maargdarshak.R

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        // In a real app, this would be your shared BottomNavBar composable
        bottomBar = { /* TODO: Add BottomNavBar */ }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Custom Top Bar
                HomeTopBar(
                    userName = uiState.userName,
                    location = uiState.userLocation,
                    onNotificationsClicked = viewModel::onNotificationsClicked,
                    onSearchClicked = viewModel::onSearchClicked
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Slogan
                SloganText()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Track public transport in real time.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Plan Trip Card
                PlanTripCard(onPlanTripClicked = viewModel::onPlanTripClicked)
                Spacer(modifier = Modifier.height(24.dp))

                // Recent Trips
                Text(
                    text = "Recent Trips",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.recentTrips.forEach { trip ->
                        RecentTripItem(
                            trip = trip,
                            onClick = viewModel::onRecentTripClicked
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    userName: String,
    location: String,
    onNotificationsClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile), // Replace with your avatar
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hi, $userName!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = location,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onNotificationsClicked) {
            Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Notifications")
        }
        IconButton(onClick = onSearchClicked) {
            Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
        }
    }
}

@Composable
private fun SloganText() {
    Text(
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        text = buildAnnotatedString {
            append("Plan. ")
            withStyle(style = SpanStyle(color = Color(0xFF6A00FF))) {
                append("Ride. ")
            }
            append("Arrive.")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // This is a simplified preview and won't have the ViewModel logic
    // You can build out more complex previews for different states if needed.
    val dummyTrips = listOf(
        RecentTrip("1", "Bole", "Piassa", "7.2 km", "18 mins", "35 ETB"),
        RecentTrip("2", "Kebena", "Meskel Square", "5.4 km", "25 mins", "10 ETB"),
    )
    // Wrap in a theme to get proper styling
    // YourTheme {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HomeTopBar("John Cena", "Addis Ababa", {}, {})
        Spacer(modifier = Modifier.height(24.dp))
        SloganText()
        Spacer(modifier = Modifier.height(24.dp))
        PlanTripCard {}
        Spacer(modifier = Modifier.height(24.dp))
        Text("Recent Trips", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            dummyTrips.forEach { RecentTripItem(trip = it, onClick = {}) }
        }
    }
    // }
}