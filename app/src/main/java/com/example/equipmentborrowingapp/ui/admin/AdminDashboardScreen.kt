package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.ui.theme.*
import androidx.compose.material.icons.filled.Person
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
    onProfileClick: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = AppBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            AdminHeroCard()

            Spacer(modifier = Modifier.height(18.dp))

            Text("Dashboard Overview", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.ExtraBold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                AdminStatCard("Total", totalEquipmentCount.toString(), InfoLight, Modifier.weight(1f))
                AdminStatCard("Available", availableItemsCount.toString(), SuccessLight, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                AdminStatCard("Low Stock", lowStockCount.toString(), WarningLight, Modifier.weight(1f))
                AdminStatCard("Pending", pendingRequestsCount.toString(), WarningLight, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                AdminStatCard("Approved", approvedRequestsCount.toString(), SuccessLight, Modifier.weight(1f))
                AdminStatCard("Overdue", overdueItemsCount.toString(), ErrorLight, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (lowStockCount > 0 || pendingRequestsCount > 0 || overdueItemsCount > 0) {
                AdminAlertCard(
                    lowStockCount = lowStockCount,
                    pendingRequestsCount = pendingRequestsCount,
                    overdueItemsCount = overdueItemsCount
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            Text("Quick Actions", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                title = "My Profile",
                subtitle = "View admin details",
                icon = Icons.Filled.Person,
                onClick = onProfileClick
            )
            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard("Add New Equipment", "Create new lab equipment item", Icons.Filled.AddCircleOutline, onAddEquipmentClick)
            AdminActionCard("Pending Requests", "Review and approve student requests", Icons.Filled.HourglassEmpty, onViewPendingRequestsClick)
            AdminActionCard("Approved Requests", "Track approved and overdue items", Icons.Filled.CheckCircleOutline, onViewApprovedRequestsClick)
            AdminActionCard("Manage Equipment", "Edit stock, condition and availability", Icons.Filled.Inventory, onManageEquipmentClick)
            AdminActionCard("Manage Lab PCs", "Add, edit and monitor lab computers", Icons.Filled.Computer, onManageLabComputersClick)
            AdminActionCard("Software Issue Reports", "Review software problems reported by students", Icons.Filled.ReportProblem, onViewSoftwareReportsClick)

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceWhite, contentColor = Error)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AdminHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(PrimaryDark, Secondary)), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column {
            Text("Equipment Borrowing System", color = TextLight.copy(alpha = 0.9f), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Admin Panel", color = TextLight, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Monitor equipment, manage student requests, and control lab resources efficiently.", color = TextLight.copy(alpha = 0.92f))
            Spacer(modifier = Modifier.height(14.dp))
            Surface(color = TextLight.copy(alpha = 0.18f), shape = RoundedCornerShape(50)) {
                Text("Role: Admin", color = TextLight, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AdminStatCard(title: String, value: String, bgColor: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier.height(94.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(modifier = Modifier.fillMaxSize().background(bgColor.copy(alpha = 0.55f)).padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(value, color = TextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = TextSecondary, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AdminAlertCard(lowStockCount: Int, pendingRequestsCount: Int, overdueItemsCount: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = WarningLight), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.WarningAmber, contentDescription = null, tint = Warning)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Attention Needed", color = TextPrimary, fontWeight = FontWeight.ExtraBold)
                Text("Low stock: $lowStockCount • Pending: $pendingRequestsCount • Overdue: $overdueItemsCount", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun AdminActionCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = PrimaryLight, shape = RoundedCornerShape(14.dp), modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Primary)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}