package com.techmania.maargdarshak.users.ui.screen.permissions

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.collect
import androidx.navigation.NavController

@OptIn(ExperimentalPermissionsApi::class) // Add this line
@Composable
fun PermissionRequestScreen(
    navController: NavController,
    viewModel: PermissionViewModel = hiltViewModel()
) {
    // Navigate away once the event is received from the ViewModel
    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is PermissionViewModel.NavigationEvent.NavigateToNextScreen -> {
                    navController.navigate("home") {
                        // Clear the auth/permission flow from the back stack
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }

    // Define the list of permissions based on the Android version
    val permissions = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    // Accompanist Permissions state holder
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    // When all permissions are granted, notify the ViewModel
    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            viewModel.onPermissionsGranted()
        }
    }

    // We have asked for permissions if any of them have been revoked.
    val permissionsAlreadyRequested = permissionState.revokedPermissions.isNotEmpty()

// A permission is permanently denied if it's been requested, it's not granted,
// and we should not show a rationale for it.
    val isPermanentlyDenied = !permissionState.shouldShowRationale && permissionsAlreadyRequested

    PermissionRequestContent(
        onGrantClicked = { permissionState.launchMultiplePermissionRequest() },
        isPermanentlyDenied = isPermanentlyDenied
    )
}

@Composable
private fun PermissionRequestContent(
    onGrantClicked: () -> Unit,
    isPermanentlyDenied: Boolean // Changed from the old parameters
) {
    val context = LocalContext.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isPermanentlyDenied) "Permission Denied" else "Permissions Required",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            val rationaleText = if (isPermanentlyDenied) {
                "You have permanently denied location and notification permissions. To use this app's core features, please enable them in your device settings."
            } else {
                "This app needs location access to find the best routes and provide real-time tracking. Notification access helps us send you timely alerts about your trip."
            }
            Text(
                text = rationaleText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isPermanentlyDenied) {
                        // Intent to open the app's settings screen
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    } else {
                        onGrantClicked()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(if (isPermanentlyDenied) "Open Settings" else "Grant Permissions")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionRequestScreenPreview() {
    PermissionRequestContent(
        onGrantClicked = {},
        isPermanentlyDenied = false
    )
}

@Preview(showBackground = true)
@Composable
fun PermissionPermanentlyDeniedPreview() {
    PermissionRequestContent(
        onGrantClicked = {},
        isPermanentlyDenied = true
    )
}

