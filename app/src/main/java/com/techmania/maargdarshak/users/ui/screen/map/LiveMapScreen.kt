package com.techmania.maargdarshak.users.ui.screen.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.utils.BitmapUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveMapScreen(
    navController: NavController,
    viewModel: LiveMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPositionState = rememberCameraPositionState()

    // This effect will pan and zoom the camera to fit the route when it loads
    LaunchedEffect(uiState.stops) {
        if (uiState.stops.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()
            uiState.stops.forEach { boundsBuilder.include(it.position) }
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 150) // 150px padding
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Tracking") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                // 1. Draw the main (full) bus route in a light purple color
                if (uiState.routePolyline.isNotEmpty()) {
                    Polyline(
                        points = uiState.routePolyline,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        width = 12f
                    )
                }

                // 2. DRAW THE HIGHLIGHTED YELLOW ROUTE ON TOP
                if (uiState.highlightedPolyline.isNotEmpty()) {
                    Polyline(
                        points = uiState.highlightedPolyline,
                        color = Color.Yellow,
                        width = 20f,
                        zIndex = 1f // Ensures it's drawn on top of the main route
                    )
                }

                // 3. Conditionally color the stop markers
                uiState.stops.forEach { stop ->
                    val iconColor = if (stop.name.equals(uiState.destinationStopName, ignoreCase = true)) {
                        BitmapDescriptorFactory.HUE_RED // Red for destination
                    } else {
                        BitmapDescriptorFactory.HUE_AZURE // Blue for all others
                    }
                    Marker(
                        state = MarkerState(position = stop.position),
                        title = stop.name,
                        icon = BitmapDescriptorFactory.defaultMarker(iconColor)
                    )
                }

                // Live marker for the vehicle (this part remains the same)
                if(uiState.liveLocation != null) {
                    val vehicleIcon = BitmapUtils.rememberScaledBitmapDescriptor(
                        vectorResId = R.drawable.buslogo,
                        widthDp = 48,
                        heightDp = 48
                    )
                    Marker(
                        state = MarkerState(position = uiState.liveLocation!!),
                        title = "Live Location",
                        icon = vehicleIcon,
                        zIndex = 2f // Highest zIndex to be on top of everything
                    )
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if(uiState.error == null) {
                LiveInfoCard(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    uiState = uiState,
                    onTrackClicked = viewModel::onTrackButtonClicked
                )
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun LiveInfoCard(
    modifier: Modifier = Modifier,
    uiState: LiveMapUiState,
    onTrackClicked: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = uiState.statusMessage,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoColumn(title = "Speed", value = uiState.liveSpeed)
                InfoColumn(title = "Vacant Seats", value = uiState.vacantSeats.toString())
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onTrackClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (uiState.isTracking) Icons.Default.NotificationsOff else Icons.Default.Notifications,
                    contentDescription = "Track Button"
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = if (uiState.isTracking) "Stop Tracking" else "Track this Bus")
            }
        }
    }
}

@Composable
fun InfoColumn(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

