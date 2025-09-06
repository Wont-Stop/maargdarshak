package com.techmania.maargdarshak.users.ui.screen.liveTracker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
// <-- IMPORTANT: import your app's generated R
import com.techmania.maargdarshak.R

// If LiveTrackerViewModel is in the same package you don't need to import it.
// If it's in a different package, change the import below to the correct package.
import com.yourapp.users.ui.screen.liveTracker.LiveTrackerViewModel

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
                    // Navigate to map screen with the selected data
                    navController.navigate("map/${event.origin}/${event.destination}/${event.transportType.name}")
                }
            }
        }
    }

    LiveTrackerContent(
        uiState = uiState,
        onOriginChange = viewModel::onOriginChange,
        onDestinationChange = viewModel::onDestinationChange,
        onSwapLocations = viewModel::onSwapLocations,
        onTransportTypeSelected = viewModel::onTransportTypeSelected,
        onDropdownClicked = viewModel::onDropdownClicked,
        onDropdownDismiss = viewModel::onDropdownDismiss,
        onFindTransportClicked = viewModel::onFindTransportClicked
    )
}

@Composable
fun LiveTrackerContent(
    uiState: LiveTrackerUiState,
    onOriginChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onSwapLocations: () -> Unit,
    onTransportTypeSelected: (TransportType) -> Unit,
    onDropdownClicked: () -> Unit,
    onDropdownDismiss: () -> Unit,
    onFindTransportClicked: () -> Unit
) {
    Scaffold(
        topBar = { LiveTrackerTopBar() },
        bottomBar = { /* TODO: Add Shared BottomNavBar */ }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            LocationTextField(
                                value = uiState.origin,
                                onValueChange = onOriginChange,
                                placeholder = "Current location",
                                icon = Icons.Outlined.Circle
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LocationTextField(
                                value = uiState.destination,
                                onValueChange = onDestinationChange,
                                placeholder = "Where are you going?",
                                icon = Icons.Outlined.LocationOn
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onSwapLocations) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap Locations"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TransportTypeSelector(
                selectedType = uiState.selectedTransportType,
                availableTypes = uiState.availableTransportTypes,
                isExpanded = uiState.isDropdownExpanded,
                onExpanded = onDropdownClicked,
                onDismiss = onDropdownDismiss,
                onTypeSelected = onTransportTypeSelected
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onFindTransportClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Find Transport to track", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun LocationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackerTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Use the drawable from your app resources
                Icon(
                    painter = painterResource(id = R.drawable.liveicon),
                    contentDescription = "Live"
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
    LiveTrackerContent(
        uiState = LiveTrackerUiState(),
        onOriginChange = {},
        onDestinationChange = {},
        onSwapLocations = {},
        onTransportTypeSelected = {},
        onDropdownClicked = {},
        onDropdownDismiss = {},
        onFindTransportClicked = {}
    )
}
