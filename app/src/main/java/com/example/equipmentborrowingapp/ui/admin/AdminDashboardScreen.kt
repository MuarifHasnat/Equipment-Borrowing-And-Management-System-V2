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
    totalRoomsCount: Int = 0,
    totalEquipmentCount: Int = 0,
    availableItemsCount: Int = 0,
    lowStockCount: Int = 0,
    pendingRequestsCount: Int = 0,
    approvedRequestsCount: Int = 0,
    issuedItemsCount: Int = 0,
    returnedItemsCount: Int = 0,
    overdueItemsCount: Int = 0,
    pendingStudentsCount: Int = 0,
    verifiedStudentsCount: Int = 0,
    totalLabComputersCount: Int = 0,
    openSoftwareIssuesCount: Int = 0,
    onManageRoomsClick: () -> Unit,
    onVerifyStudentsClick: () -> Unit,
    onAddEquipmentClick: () -> Unit,
    onViewPendingRequestsClick: () -> Unit,
    onViewApprovedRequestsClick: () -> Unit,
    onManageEquipmentClick: () -> Unit,
    onManageLabComputersClick: () -> Unit,
    onViewSoftwareReportsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            ModernHeroCard()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Dashboard Overview",
                style = MaterialTheme.typography.titleLarge,
                color = TextDark,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernStatCard(
                    title = "Rooms",
                    value = totalRoomsCount.toString(),
                    icon = Icons.Filled.MeetingRoom,
                    bgColor = BlueLight,
                    contentColor = BlueText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Equipment",
                    value = totalEquipmentCount.toString(),
                    icon = Icons.Filled.Widgets,
                    bgColor = BlueLight,
                    contentColor = BlueText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernStatCard(
                    title = "Available",
                    value = availableItemsCount.toString(),
                    icon = Icons.Filled.CheckCircle,
                    bgColor = GreenLight,
                    contentColor = GreenText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Low Stock",
                    value = lowStockCount.toString(),
                    icon = Icons.Filled.TrendingDown,
                    bgColor = OrangeLight,
                    contentColor = OrangeText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernStatCard(
                    title = "Pending",
                    value = pendingRequestsCount.toString(),
                    icon = Icons.Filled.HourglassTop,
                    bgColor = OrangeLight,
                    contentColor = OrangeText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Approved",
                    value = approvedRequestsCount.toString(),
                    icon = Icons.Filled.ThumbUp,
                    bgColor = GreenLight,
                    contentColor = GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernStatCard(
                    title = "Issued",
                    value = issuedItemsCount.toString(),
                    icon = Icons.Filled.AssignmentTurnedIn,
                    bgColor = PurpleAccent.copy(alpha = 0.10f),
                    contentColor = PurpleAccent,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Overdue",
                    value = overdueItemsCount.toString(),
                    icon = Icons.Filled.Warning,
                    bgColor = RedLight,
                    contentColor = RedText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernStatCard(
                    title = "Students",
                    value = verifiedStudentsCount.toString(),
                    icon = Icons.Filled.VerifiedUser,
                    bgColor = GreenLight,
                    contentColor = GreenText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Lab PCs",
                    value = totalLabComputersCount.toString(),
                    icon = Icons.Filled.Computer,
                    bgColor = PurpleAccent.copy(alpha = 0.10f),
                    contentColor = PurpleAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (pendingRequestsCount > 0) {
                NotificationCard(
                    title = "Pending Requests",
                    message = "$pendingRequestsCount request(s) need admin approval.",
                    type = "warning"
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (lowStockCount > 0) {
                NotificationCard(
                    title = "Low Stock Alert",
                    message = "$lowStockCount equipment item(s) are low in stock.",
                    type = "warning"
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (overdueItemsCount > 0) {
                NotificationCard(
                    title = "Overdue Items",
                    message = "$overdueItemsCount item(s) are overdue and need follow-up.",
                    type = "error"
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (pendingStudentsCount > 0) {
                NotificationCard(
                    title = "Pending Students",
                    message = "$pendingStudentsCount student account(s) need verification.",
                    type = "warning"
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (openSoftwareIssuesCount > 0) {
                NotificationCard(
                    title = "Software Issues",
                    message = "$openSoftwareIssuesCount software issue(s) are still open.",
                    type = "error"
                )

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

            Column(
                modifier = Modifier
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(CardWhite, RoundedCornerShape(24.dp))
                    .padding(8.dp)
            ) {
                ModernActionRow(
                    title = "My Profile",
                    subtitle = "View your details",
                    icon = Icons.Filled.Person,
                    iconBgColor = BlueLight,
                    iconColor = BlueText,
                    onClick = onProfileClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Notifications",
                    subtitle = "System alerts",
                    icon = Icons.Filled.Notifications,
                    iconBgColor = OrangeLight,
                    iconColor = OrangeText,
                    onClick = onNotificationClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Manage Rooms / Labs",
                    subtitle = "Create and organize labs",
                    icon = Icons.Filled.MeetingRoom,
                    iconBgColor = BlueLight,
                    iconColor = BlueText,
                    onClick = onManageRoomsClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Manage Students",
                    subtitle = "Pending, verified, rejected and suspended students",
                    icon = Icons.Filled.VerifiedUser,
                    iconBgColor = GreenLight,
                    iconColor = GreenText,
                    onClick = onVerifyStudentsClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Add Equipment",
                    subtitle = "Create new item",
                    icon = Icons.Filled.AddCircle,
                    iconBgColor = GreenLight,
                    iconColor = GreenText,
                    onClick = onAddEquipmentClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Pending Requests",
                    subtitle = "Review and approve",
                    icon = Icons.Filled.HourglassEmpty,
                    iconBgColor = OrangeLight,
                    iconColor = OrangeText,
                    onClick = onViewPendingRequestsClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Active Borrow Requests",
                    subtitle = "Approved, issued, overdue and return flow",
                    icon = Icons.Filled.CheckCircleOutline,
                    iconBgColor = GreenLight,
                    iconColor = GreenText,
                    onClick = onViewApprovedRequestsClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Manage Equipment",
                    subtitle = "Stock and condition",
                    icon = Icons.Filled.Inventory,
                    iconBgColor = BlueLight,
                    iconColor = BlueText,
                    onClick = onManageEquipmentClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Manage Lab PCs",
                    subtitle = "Monitor computers",
                    icon = Icons.Filled.Computer,
                    iconBgColor = PurpleAccent.copy(alpha = 0.1f),
                    iconColor = PurpleAccent,
                    onClick = onManageLabComputersClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ModernBg
                )

                ModernActionRow(
                    title = "Issue Reports",
                    subtitle = "Software problems",
                    icon = Icons.Filled.ReportProblem,
                    iconBgColor = RedLight,
                    iconColor = RedText,
                    onClick = onViewSoftwareReportsClick
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = RedText.copy(alpha = 0.3f)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedLight,
                    contentColor = RedText
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Logout Account",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
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
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = PrimaryIndigo.copy(alpha = 0.4f)
            )
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
                    shape = RoundedCornerShape(50.dp)
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
                    imageVector = Icons.Filled.Dashboard,
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
                style = MaterialTheme.typography.bodyMedium
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
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
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
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
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Go",
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(28.dp)
        )
    }
}