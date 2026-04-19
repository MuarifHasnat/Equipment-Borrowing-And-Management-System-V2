package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.ui.common.GradientHeaderCard
import com.example.equipmentborrowingapp.ui.common.PrimaryActionButton
import com.example.equipmentborrowingapp.ui.common.SectionTitle
import com.example.equipmentborrowingapp.ui.common.SummaryStatCard
import com.example.equipmentborrowingapp.ui.theme.AppBackground
import com.example.equipmentborrowingapp.ui.theme.ErrorLight
import com.example.equipmentborrowingapp.ui.theme.InfoLight
import com.example.equipmentborrowingapp.ui.theme.Primary
import com.example.equipmentborrowingapp.ui.theme.PrimaryLight
import com.example.equipmentborrowingapp.ui.theme.SurfaceWhite
import com.example.equipmentborrowingapp.ui.theme.SuccessLight
import com.example.equipmentborrowingapp.ui.theme.TextLight
import com.example.equipmentborrowingapp.ui.theme.TextPrimary

@Composable
fun StudentDashboardScreen(
    onViewEquipmentClick: () -> Unit,
    onMyRequestsClick: () -> Unit,
    onLabComputersClick: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            GradientHeaderCard(
                title = "Student Panel",
                subtitle = "Lab & Equipment Access",
                description = "Manage requests, explore lab resources, and access available equipment quickly."
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryStatCard(
                    title = "Equipment",
                    value = "Browse",
                    bgColor = PrimaryLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Requests",
                    value = "Track",
                    bgColor = SuccessLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Lab PCs",
                    value = "Access",
                    bgColor = InfoLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            SectionTitle(
                text = "Quick Actions",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            DashboardActionCard(
                title = "View Equipment",
                subtitle = "Browse available items for borrowing",
                icon = {
                    Icon(
                        imageVector = Icons.Filled.QrCodeScanner,
                        contentDescription = null,
                        tint = TextLight,
                        modifier = Modifier.size(28.dp)
                    )
                },
                onClick = onViewEquipmentClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardActionCard(
                title = "My Requests",
                subtitle = "Check your submitted request history",
                icon = {
                    Icon(
                        imageVector = Icons.Filled.ListAlt,
                        contentDescription = null,
                        tint = TextLight,
                        modifier = Modifier.size(28.dp)
                    )
                },
                onClick = onMyRequestsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardActionCard(
                title = "Lab Computers",
                subtitle = "Explore available lab computers",
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Computer,
                        contentDescription = null,
                        tint = TextLight,
                        modifier = Modifier.size(28.dp)
                    )
                },
                onClick = onLabComputersClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            PrimaryActionButton(
                text = "Logout",
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Primary
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            Column {
                Text(
                    text = title,
                    color = TextLight,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    color = TextLight.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}