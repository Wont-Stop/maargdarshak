package com.techmania.maargdarshak.users.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.users.navigation.Screen
import com.techmania.maargdarshak.users.ui.screen.shared.AppTopBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is ProfileViewModel.NavigationEvent.NavigateToLogin -> {
                    navController.navigate(Screen.Login.route) {
                        // Clear the entire back stack up to the start destination
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
                is ProfileViewModel.NavigationEvent.NavigateToEditProfile -> {
                    navController.navigate(Screen.EditProfile.route)
                }
            }
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "My Profile", navController = navController) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.user != null) {
            val user = uiState.user!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Note: Make sure you have a `profile.png` or similar in `res/drawable`
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Divider()

                ProfileInfoRow(icon = Icons.Default.Person, label = "Full Name", value = user.fullName)
                ProfileInfoRow(icon = Icons.Default.Email, label = "Email", value = user.email)
                ProfileInfoRow(icon = Icons.Default.Phone, label = "Phone", value = user.phone)
                ProfileInfoRow(icon = Icons.Default.LocationOn, label = "Address", value = user.address)

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = viewModel::onEditProfileClicked, modifier = Modifier.fillMaxWidth()) {
                    Text("Edit Profile")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = viewModel::onSignOutClicked, modifier = Modifier.fillMaxWidth()) {
                    Text("Sign Out")
                }
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
