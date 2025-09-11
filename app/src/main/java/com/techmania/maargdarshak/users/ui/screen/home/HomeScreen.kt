package com.techmania.maargdarshak.users.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.users.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current // <-- Add this

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            // This block will re-run every time the screen is resumed
            viewModel.loadUserProfile()
        }
    }


    // Listen for navigation events from the ViewModel
    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is HomeViewModel.NavigationEvent.NavigateToEditProfile -> {
                    navController.navigate(Screen.EditProfile.route)
                }
            }
        }
    }

    Scaffold { paddingValues ->
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
                // Pass the onProfileClicked event from the ViewModel
                HomeTopBar(
                    userName = uiState.userName,
                    location = uiState.userLocation,
                    onNotificationsClicked = viewModel::onNotificationsClicked,
                    onSearchClicked = viewModel::onSearchClicked,
                    onProfileClicked = viewModel::onProfileClicked // Pass the handler here
                )
                Spacer(modifier = Modifier.height(24.dp))
                SloganText()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Track public transport in real time.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                PlanTripCard(onPlanTripClicked = viewModel::onPlanTripClicked)
                Spacer(modifier = Modifier.height(24.dp))
                // The rest of your UI (Recent Trips etc.) remains unchanged
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    userName: String,
    location: String,
    onNotificationsClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit // Add a callback for profile clicks
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(CircleShape) // Makes the whole row clickable with a ripple effect
            .clickable(onClick = onProfileClicked), // Make the entire banner clickable
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
        // These IconButtons stop the click from propagating to the parent Row
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
