package com.techmania.maargdarshak.users.ui.screen.liveTracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.users.navigation.Screen

// CORRECTED: ViewModel import path

@Composable
fun LiveTrackerScreen(
    navController: NavController,
    viewModel: LiveTrackerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LiveTrackerViewModel.NavigationEvent.NavigateToMap -> {
                    val route = Screen.LiveMap.createRoute(
                        origin = event.origin,
                        destination = event.destination,
                        transportType = event.transportType.name
                    )
                    navController.navigate(route)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        LiveTrackerTopBar()
        Spacer(modifier = Modifier.height(16.dp))

        // This whole Card section is updated to match the UI
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Column for Icons and Dashed Line
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = "Current Location Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    DashedVerticalDivider()
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Destination Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Column for the text rows (border is removed from here)
                Column(modifier = Modifier.weight(1f)) {
                    LocationRow(text = uiState.origin)
                    Divider()
                    LocationRow(text = if (uiState.destination.isNotBlank()) uiState.destination else "Where are you going?")
                }

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = viewModel::onSwapLocations) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Swap Locations"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TransportTypeSelector(
            selectedType = uiState.selectedTransportType,
            availableTypes = uiState.availableTransportTypes,
            isExpanded = uiState.isDropdownExpanded,
            onExpanded = viewModel::onDropdownClicked,
            onDismiss = viewModel::onDropdownDismiss,
            onTypeSelected = viewModel::onTransportTypeSelected
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = viewModel::onFindTransportClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Find Transport to track", fontSize = 16.sp)
        }
    }
}

/**
 * NEW: A composable for the static location text rows.
 */
@Composable
fun LocationRow(text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp), // Increased padding for a taller row
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * NEW: A composable to draw the vertical dashed line.
 */
@Composable
fun DashedVerticalDivider() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(
        Modifier
            .height(24.dp)
            .width(1.dp)
    ) {
        drawLine(
            color = Color.Gray,
            start = Offset.Zero, // The top-left corner (0, 0)
            end = Offset(x = 0f, y = size.height), // The bottom-left corner
            pathEffect = pathEffect
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackerTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.liveicon),
                    contentDescription = "Live",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp) // <-- Add this line
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Live Map", fontWeight = FontWeight.SemiBold)
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun LiveTrackerScreenPreview() {
    // A simple preview of the screen content

}