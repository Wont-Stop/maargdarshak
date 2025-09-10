package com.techmania.maargdarshak.users.ui.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceSearchScreen(
    navController: NavController,
    viewModel: PlaceSearchViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Destination") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g., India Gate, Delhi") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // In PlaceSearchScreen.kt, inside the else block
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.searchResults) { place ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Pass the full address as the result for now
                                    val fullAddress = "${place.primaryText}, ${place.secondaryText}"
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selected_place", fullAddress)
                                    navController.popBackStack()
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            Text(text = place.primaryText, fontWeight = FontWeight.Bold)
                            Text(text = place.secondaryText, style = MaterialTheme.typography.bodySmall)
                        }
                        Divider()
                    }
                }
            }
        }
    }
}