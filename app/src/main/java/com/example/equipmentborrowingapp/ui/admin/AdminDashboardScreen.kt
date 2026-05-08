package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.ui.common.NotificationCard

// Modern Colors defined locally for the redesign
val ModernBg = Color(0xFFF4F7FB)
val CardWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1E293B)
val TextMuted = Color(0xFF64748B)

val PrimaryIndigo = Color(0xFF4F46E5)
val PurpleAccent = Color(0xFF7C3AED)

val BlueLight = Color(0xFFEFF6FF)
val BlueText = Color(0xFF2563EB)

val GreenLight = Color(0xFFF0FDF4)
val GreenText = Color(0xFF16A34A)

val OrangeLight = Color(0xFFFFF7ED)
val OrangeText = Color(0xFFEA580C)

val RedLight = Color(0xFFFEF2F2)
val RedText = Color(0xFFDC2626)

@Composable
fun AdminDashboardScreen(
    totalEquipmentCount: Int,
    availableItemsCount: Int,
    lowStockCount: Int,
    pendingRequestsCount: Int,
    approvedRequestsCount: Int,
    returnedItemsCount: Int,
    onNotificationClick: () -> Unit,
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
    Surface(modifier = Modifier.fillMaxSize(), color = ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Modern Hero Section
            ModernHeroCard()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Dashboard Overview",
                style = MaterialTheme.typography.titleLarge,
                color = TextDark,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Modern Stats Grid
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                ModernStatCard("Total", totalEquipmentCount.toString(), Icons.Filled.Widgets, BlueLight, BlueText, Modifier.weight(1f))
                ModernStatCard("Available", availableItemsCount.toString(), Icons.Filled.CheckCircle, GreenLight, GreenText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                ModernStatCard("Low Stock", lowStockCount.toString(), Icons.Filled.TrendingDown, OrangeLight, OrangeText, Modifier.weight(1f))
                ModernStatCard("Pending", pendingRequestsCount.toString(), Icons.Filled.HourglassTop, OrangeLight, OrangeText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                ModernStatCard("Approved", approvedRequestsCount.toString(), Icons.Filled.ThumbUp, GreenLight, GreenText, Modifier.weight(1f))
                ModernStatCard("Overdue", overdueItemsCount.toString(), Icons.Filled.Warning, RedLight, RedText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Notifications
            if (pendingRequestsCount > 0) {
                NotificationCard(title = "Pending Requests", message = "$pendingRequestsCount request(s) need admin approval.", type = "warning")
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (lowStockCount > 0) {
                NotificationCard(title = "Low Stock Alert", message = "$lowStockCount equipment item(s) are low in stock.", type = "warning")
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (overdueItemsCount > 0) {
                NotificationCard(title = "Overdue Items", message = "$overdueItemsCount item(s) are overdue and need follow-up.", type = "error")
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                color = TextDark,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Modern Action List
            Column(
                modifier = Modifier
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                    .background(CardWhite, RoundedCornerShape(24.dp))
                    .padding(8.dp)
            ) {
                ModernActionRow("My Profile", "View your details", Icons.Filled.Person, BlueLight, BlueText, onProfileClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = ModernBg)
                ModernActionRow("Notifications", "System alerts", Icons.Filled.Notifications, OrangeLight, OrangeText, onNotificationClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = ModernBg)
                ModernActionRow("Add Equipment", "Create new item", Icons.Filled.AddCircle, GreenLight, GreenText, onAddEquipmentClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = ModernBg)
                ModernActionRow("Pending Requests", "Review & approve", Icons.Filled.HourglassEmpty, OrangeLight, OrangeText, onViewPendingRequestsClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = ModernBg)
                ModernActionRow("Approved Requests", "Track items", Icons.Filled.CheckCircleOutline, GreenLight, GreenText, onViewApprovedRequestsClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = ModernBg)
                ModernActionRow("Manage Equipment", "Stock & condition", Icons.Filled.Inventory, BlueLight, BlueText, onManageEquipmentClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = ModernBg)
                ModernActionRow("Manage Lab PCs", "Monitor computers", Icons.Filled.Computer, PurpleAccent.copy(alpha=0.1f), PurpleAccent, onManageLabComputersClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = ModernBg)
                ModernActionRow("Issue Reports", "Software problems", Icons.Filled.ReportProblem, RedLight, RedText, onViewSoftwareReportsClick)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Modern Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = RedText.copy(alpha = 0.3f)),
                colors = ButtonDefaults.buttonColors(containerColor = RedLight, contentColor = RedText),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout Account", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ModernHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = PrimaryIndigo.copy(alpha = 0.4f))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(PrimaryIndigo, PurpleAccent)
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Admin Panel",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Icon(
                    Icons.Filled.Dashboard,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Equipment Borrowing and Software Monitoring System",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Monitor equipment, manage requests, and control lab resources efficiently.",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f
            )
        }
    }
}

@Composable
private fun ModernStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    bgColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
                }
                Text(
                    text = value,
                    color = TextDark,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
            }

            Text(
                text = title,
                color = TextMuted,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ModernActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextDark,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Icon(
            Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Go",
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(28.dp)
        )
    }
}