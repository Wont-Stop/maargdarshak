package com.techmania.maargdarshak.users.ui.screen.liveTracker


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportTypeSelector(
    selectedType: TransportType,
    availableTypes: List<TransportType>,
    isExpanded: Boolean,
    onExpanded: () -> Unit,
    onDismiss: () -> Unit,
    onTypeSelected: (TransportType) -> Unit,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { if (!isExpanded) onExpanded() },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedType.displayName,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = selectedType.icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp) // <-- FIX: Added size modifier
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss
        ) {
            availableTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = { onTypeSelected(type) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = type.icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp) // <-- FIX: Added size modifier for consistency
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransportTypeSelectorPreview() {
    var selectedType by remember { mutableStateOf(TransportType.BUS) }
    var isExpanded by remember { mutableStateOf(false) }

    TransportTypeSelector(
        selectedType = selectedType,
        availableTypes = TransportType.values().toList(),
        isExpanded = isExpanded,
        onExpanded = { isExpanded = !isExpanded },
        onDismiss = { isExpanded = false },
        onTypeSelected = {
            selectedType = it
            isExpanded = false
        }
    )
}