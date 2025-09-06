package com.techmania.maargdarshak.users.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlanTripCard(
    onPlanTripClicked: () -> Unit
) {
    val purpleColor = Color(0xFF6A00FF)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Origin
            LocationRow(
                text = "Your location",
                icon = Icons.Outlined.Circle,
                tint = purpleColor
            )

            // Dashed line
            DashedDivider()

            // Destination
            LocationRow(
                text = "Where are you going?",
                icon = Icons.Outlined.LocationOn,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Plan my trip button
            Button(
                onClick = onPlanTripClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = purpleColor)
            ) {
                Text("Plan My Trip", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun LocationRow(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { /* TODO: Handle location selection */ }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun DashedDivider() {
    Box(
        Modifier
            .height(24.dp)
            .width(1.dp)
            .padding(start = 9.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
    }
}

@Preview
@Composable
fun PlanTripCardPreview() {
    PlanTripCard(onPlanTripClicked = {})
}