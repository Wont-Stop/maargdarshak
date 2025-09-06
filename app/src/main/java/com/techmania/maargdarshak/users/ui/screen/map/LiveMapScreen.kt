package com.techmania.maargdarshak.users.ui.screen.map


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveMapScreen(
    navController: NavController,
    viewModel: LiveMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPositionState = rememberCameraPositionState()

    // Effect to move camera when route is loaded
    LaunchedEffect(uiState.routePolyline) {
        if (uiState.routePolyline.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()
            uiState.routePolyline.forEach { boundsBuilder.include(it) }
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100)
            )
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            if (uiState.bottomSheetInfo != null) {
                MapBottomSheet(info = uiState.bottomSheetInfo!!)
            } else {
                // Placeholder for when bottom sheet info isn't available
                Spacer(modifier = Modifier.height(1.dp))
            }
        },
        sheetPeekHeight = 120.dp,
        topBar = {
            TopAppBar(
                title = { Text("Live Map") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
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
                cameraPositionState = cameraPositionState
            ) {
                // Draw route polyline
                if (uiState.routePolyline.isNotEmpty()) {
                    Polyline(
                        points = uiState.routePolyline,
                        color = Color.Blue,
                        width = 15f
                    )
                }

                // Draw origin and destination markers
                uiState.origin?.let { OriginMarker(location = it) }
                uiState.destination?.let { DestinationMarker(location = it) }

                // Draw vehicle markers
                uiState.vehicles.forEach { vehicle ->
                    VehicleMarker(vehicle = vehicle)
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}