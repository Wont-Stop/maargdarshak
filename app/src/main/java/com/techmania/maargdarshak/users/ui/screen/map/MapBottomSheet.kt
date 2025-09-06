package com.techmania.maargdarshak.users.ui.screen.map


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techmania.maargdarshak.users.ui.screen.liveTracker.TransportType

@Composable
fun MapBottomSheet(
    info: BottomSheetInfo
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = info.routeName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(
                    text = "${info.vehicleCount} ${info.transportType.displayName}s",
                    icon = {
                        Icon(
                            painter = painterResource(id = info.transportType.icon),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
                InfoChip(text = info.estimatedCost)
            }
        }
    }
}

@Composable
private fun InfoChip(
    text: String,
    icon: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (icon != null) {
            icon()
        }
        Text(text = text, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Preview
@Composable
fun MapBottomSheetPreview() {
    MapBottomSheet(
        info = BottomSheetInfo(
            routeName = "Meskel Square → Shola Market",
            vehicleCount = 3,
            estimatedCost = "10-15 ETB",
            transportType = TransportType.BUS
        )
    )
}