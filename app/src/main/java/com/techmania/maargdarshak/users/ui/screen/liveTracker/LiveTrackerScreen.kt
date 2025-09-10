package com.techmania.maargdarshak.users.ui.screen.liveTracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.users.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackerScreen(
    navController: NavController,
    viewModel: LiveTrackerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val originFocusRequester = remember { FocusRequester() }
    val destinationFocusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LiveTrackerViewModel.NavigationEvent.NavigateToBusResults -> {
                    val route = Screen.BusResults.createRoute(
                        origin = event.origin,
                        destination = event.destination
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
            .verticalScroll(rememberScrollState()),
    ) {
        // The Top Bar is now included
        LiveTrackerTopBar()
        Spacer(modifier = Modifier.height(16.dp))

        // The original UI with the card is restored here
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Circle, "Origin", tint = MaterialTheme.colorScheme.primary)
                    DashedVerticalDivider()
                    Icon(Icons.Outlined.LocationOn, "Destination", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    AutocompleteTextField(
                        modifier = Modifier.focusRequester(originFocusRequester), // 2. Assign requester
                        query = uiState.originQuery,
                        onQueryChanged = viewModel::onOriginQueryChanged,
                        label = "Starting Location",
                        suggestions = uiState.originSuggestions,
                        isDropdownExpanded = uiState.isOriginDropdownExpanded,
                        onSuggestionSelected = { selected ->
                            viewModel.onOriginSelected(selected)
                            // 3. Move focus when an item is selected
                            destinationFocusRequester.requestFocus()
                        },
                        onDismissRequest = viewModel::onOriginDismiss
                    )
                    Divider()
                    AutocompleteTextField(
                        modifier = Modifier.focusRequester(destinationFocusRequester), // 2. Assign requester
                        query = uiState.destinationQuery,
                        onQueryChanged = viewModel::onDestinationQueryChanged,
                        label = "Destination",
                        suggestions = uiState.destinationSuggestions,
                        isDropdownExpanded = uiState.isDestinationDropdownExpanded,
                        onSuggestionSelected = viewModel::onDestinationSelected, // 3. No focus change here
                        onDismissRequest = viewModel::onDestinationDismiss
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Swap button is now connected to the ViewModel
                IconButton(onClick = viewModel::onSwapLocations) {
                    Icon(Icons.Default.SwapVert, "Swap Locations")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TransportTypeSelector(
            selectedType = uiState.selectedTransportType,
            availableTypes = TransportType.values().toList(),
            isExpanded = uiState.isTransportDropdownExpanded,

            onExpanded = viewModel::onTransportDropdownClicked,
            onDismiss = viewModel::onTransportDropdownDismiss,
            onTypeSelected = viewModel::onTransportTypeSelected
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = viewModel::onFindTransportClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = uiState.origin.isNotBlank() && uiState.destination.isNotBlank()
        ) {
            Icon(Icons.Default.Search, "Find Buses")
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Find Transport to track", fontSize = 16.sp)
        }
    }
}
// In LiveTrackerScreen.kt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompleteTextField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    label: String,
    suggestions: List<String>,
    isDropdownExpanded: Boolean,
    onSuggestionSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded && suggestions.isNotEmpty(),
        onExpandedChange = {}
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = { Text(label) },
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(),
            singleLine = true,
            // UPDATED: Add a trailing "clear" icon
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChanged("") }) { // onClick clears the query
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear text"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        ExposedDropdownMenu(
            expanded = isDropdownExpanded && suggestions.isNotEmpty(),
            onDismissRequest = onDismissRequest
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion) },
                    onClick = { onSuggestionSelected(suggestion) }
                )
            }
        }
    }
}
@Composable
fun DashedVerticalDivider() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(
        Modifier
            .height(32.dp) // Increased height for better spacing
            .width(1.dp)
    ) {
        drawLine(
            color = Color.Gray,
            start = Offset.Zero,
            end = Offset(x = 0f, y = size.height),
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
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
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