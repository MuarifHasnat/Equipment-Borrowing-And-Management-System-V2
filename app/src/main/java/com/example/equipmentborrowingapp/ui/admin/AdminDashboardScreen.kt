package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.ui.common.*
import com.example.equipmentborrowingapp.ui.theme.*

@Composable
fun AdminDashboardScreen(
    totalEquipmentCount: Int,
    availableItemsCount: Int,
    lowStockCount: Int,
    pendingRequestsCount: Int,
    approvedRequestsCount: Int,
    returnedItemsCount: Int,
    overdueItemsCount: Int,
    onAddEquipmentClick: () -> Unit,
    onViewPendingRequestsClick: () -> Unit,
    onViewApprovedRequestsClick: () -> Unit,
    onManageEquipmentClick: () -> Unit,
    onManageLabComputersClick: () -> Unit,
    onViewSoftwareReportsClick: () -> Unit,
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
            Spacer(modifier = Modifier.height(24.dp))

            // 🔥 Modern Header
            GradientHeaderCard(
                title = "Equipment Borrowing System",
                subtitle = "Admin Panel",
                description = "Monitor equipment, manage requests, and control lab resources efficiently."
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 Summary Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryStatCard(
                    title = "Total",
                    value = totalEquipmentCount.toString(),
                    bgColor = InfoLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Available",
                    value = availableItemsCount.toString(),
                    bgColor = SuccessLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryStatCard(
                    title = "Low Stock",
                    value = lowStockCount.toString(),
                    bgColor = WarningLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Pending",
                    value = pendingRequestsCount.toString(),
                    bgColor = WarningLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryStatCard(
                    title = "Approved",
                    value = approvedRequestsCount.toString(),
                    bgColor = SuccessLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Returned",
                    value = returnedItemsCount.toString(),
                    bgColor = InfoLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryStatCard(
                    title = "Overdue",
                    value = overdueItemsCount.toString(),
                    bgColor = ErrorLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Active",
                    value = (pendingRequestsCount + approvedRequestsCount).toString(),
                    bgColor = PrimaryLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle(
                text = "Quick Actions",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            AdminActionCard(
                title = "Add New Equipment",
                icon = Icons.Filled.AddCircleOutline,
                onClick = onAddEquipmentClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                title = "Pending Requests",
                icon = Icons.Filled.HourglassEmpty,
                onClick = onViewPendingRequestsClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                title = "Approved Requests",
                icon = Icons.Filled.CheckCircleOutline,
                onClick = onViewApprovedRequestsClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                title = "Manage Equipment",
                icon = Icons.Filled.Inventory,
                onClick = onManageEquipmentClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                title = "Manage Lab PCs",
                icon = Icons.Filled.Inventory,
                onClick = onManageLabComputersClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                title = "Software Issue Reports",
                icon = Icons.Filled.Inventory,
                onClick = onViewSoftwareReportsClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            SecondaryActionButton(
                text = "Logout",
                onClick = onLogout
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AdminActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Primary
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(5.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextLight
            )

            Spacer(modifier = Modifier.width(16.dp))

            androidx.compose.material3.Text(
                text = title,
                color = TextLight,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}