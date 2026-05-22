package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private object AdminDashColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)

    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)
    val DeepBlue = Color(0xFF1D4ED8)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
}

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
    onReportsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AdminDashColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            AdminTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            AdminHeroCard(
                pendingRequestsCount = pendingRequestsCount,
                overdueItemsCount = overdueItemsCount,
                openSoftwareIssuesCount = openSoftwareIssuesCount
            )

            Spacer(modifier = Modifier.height(18.dp))

            SectionHeader(
                title = "Overview",
                actionText = "Reports",
                onActionClick = onReportsClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CompactStatCard(
                    title = "Equipment",
                    value = totalEquipmentCount.toString(),
                    subtitle = "$availableItemsCount available",
                    icon = Icons.Filled.Widgets,
                    bgColor = AdminDashColors.BlueLight,
                    textColor = AdminDashColors.BlueText,
                    modifier = Modifier.weight(1f),
                    onClick = onManageEquipmentClick
                )

                CompactStatCard(
                    title = "Requests",
                    value = pendingRequestsCount.toString(),
                    subtitle = "Pending",
                    icon = Icons.Filled.Assignment,
                    bgColor = AdminDashColors.PurpleLight,
                    textColor = AdminDashColors.PurpleText,
                    modifier = Modifier.weight(1f),
                    onClick = onViewPendingRequestsClick
                )

                CompactStatCard(
                    title = "Overdue",
                    value = overdueItemsCount.toString(),
                    subtitle = "Need action",
                    icon = Icons.Filled.Warning,
                    bgColor = AdminDashColors.RedLight,
                    textColor = AdminDashColors.RedText,
                    modifier = Modifier.weight(1f),
                    onClick = onViewApprovedRequestsClick
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionHeader(
                title = "Quick Actions",
                actionText = "View reports",
                onActionClick = onReportsClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminActionCard(
                        title = "Rooms",
                        subtitle = "$totalRoomsCount lab rooms",
                        icon = Icons.Filled.MeetingRoom,
                        iconBg = AdminDashColors.BlueLight,
                        iconColor = AdminDashColors.BlueText,
                        modifier = Modifier.weight(1f),
                        onClick = onManageRoomsClick
                    )

                    AdminActionCard(
                        title = "Add Item",
                        subtitle = "New equipment",
                        icon = Icons.Filled.AddBox,
                        iconBg = AdminDashColors.GreenLight,
                        iconColor = AdminDashColors.GreenText,
                        modifier = Modifier.weight(1f),
                        onClick = onAddEquipmentClick
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminActionCard(
                        title = "Equipment",
                        subtitle = "Manage stock",
                        icon = Icons.Filled.Inventory,
                        iconBg = AdminDashColors.PurpleLight,
                        iconColor = AdminDashColors.PurpleText,
                        modifier = Modifier.weight(1f),
                        onClick = onManageEquipmentClick
                    )

                    AdminActionCard(
                        title = "Pending",
                        subtitle = "$pendingRequestsCount requests",
                        icon = Icons.Filled.Assignment,
                        iconBg = AdminDashColors.OrangeLight,
                        iconColor = AdminDashColors.OrangeText,
                        modifier = Modifier.weight(1f),
                        onClick = onViewPendingRequestsClick
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminActionCard(
                        title = "Active",
                        subtitle = "$approvedRequestsCount approved",
                        icon = Icons.Filled.CheckCircle,
                        iconBg = AdminDashColors.GreenLight,
                        iconColor = AdminDashColors.GreenText,
                        modifier = Modifier.weight(1f),
                        onClick = onViewApprovedRequestsClick
                    )

                    AdminActionCard(
                        title = "Students",
                        subtitle = "$pendingStudentsCount pending",
                        icon = Icons.Filled.VerifiedUser,
                        iconBg = AdminDashColors.BlueLight,
                        iconColor = AdminDashColors.BlueText,
                        modifier = Modifier.weight(1f),
                        onClick = onVerifyStudentsClick
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminActionCard(
                        title = "Lab PCs",
                        subtitle = "$totalLabComputersCount computers",
                        icon = Icons.Filled.Computer,
                        iconBg = AdminDashColors.PurpleLight,
                        iconColor = AdminDashColors.PurpleText,
                        modifier = Modifier.weight(1f),
                        onClick = onManageLabComputersClick
                    )

                    AdminActionCard(
                        title = "Issues",
                        subtitle = "$openSoftwareIssuesCount open",
                        icon = Icons.Filled.BugReport,
                        iconBg = AdminDashColors.OrangeLight,
                        iconColor = AdminDashColors.OrangeText,
                        modifier = Modifier.weight(1f),
                        onClick = onViewSoftwareReportsClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionHeader(
                title = "System Summary",
                actionText = "Details",
                onActionClick = onReportsClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            SystemSummaryCard(
                issuedItemsCount = issuedItemsCount,
                returnedItemsCount = returnedItemsCount,
                lowStockCount = lowStockCount,
                verifiedStudentsCount = verifiedStudentsCount,
                onReportsClick = onReportsClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            AdminBottomBar(
                onHomeClick = {},
                onEquipmentClick = onManageEquipmentClick,
                onReportsClick = onReportsClick,
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AdminDashColors.RedLight,
                    contentColor = AdminDashColors.RedText
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout",
                    modifier = Modifier.size(19.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Logout Account",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AdminTopBar(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(AdminDashColors.PurpleLight, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Assessment,
                    contentDescription = null,
                    tint = AdminDashColors.PrimaryIndigo,
                    modifier = Modifier.size(27.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "LabMate",
                    color = AdminDashColors.TextDark,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "Admin Portal",
                    color = AdminDashColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clickable { onNotificationClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = AdminDashColors.TextDark,
                modifier = Modifier.size(25.dp)
            )

            Box(
                modifier = Modifier
                    .size(9.dp)
                    .align(Alignment.TopEnd)
                    .background(AdminDashColors.RedText, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(AdminDashColors.BlueLight, CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = AdminDashColors.BlueText,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AdminHeroCard(
    pendingRequestsCount: Int,
    overdueItemsCount: Int,
    openSoftwareIssuesCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = AdminDashColors.PrimaryIndigo.copy(alpha = 0.25f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        AdminDashColors.DeepBlue,
                        AdminDashColors.PrimaryIndigo,
                        AdminDashColors.PurpleAccent
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(
                text = "Admin Dashboard 👋",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Manage equipment, requests,\nstudents and lab resources.",
                color = Color.White.copy(alpha = 0.88f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(50.dp))
                    .padding(horizontal = 11.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "$pendingRequestsCount pending • $overdueItemsCount overdue • $openSoftwareIssuesCount issues",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Icon(
            imageVector = Icons.Filled.Widgets,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.14f),
            modifier = Modifier
                .size(112.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = AdminDashColors.TextDark,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Row(
            modifier = Modifier.clickable { onActionClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = actionText,
                color = AdminDashColors.PrimaryIndigo,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = AdminDashColors.PrimaryIndigo,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun CompactStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(104.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AdminDashColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(11.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(bgColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(21.dp)
                )
            }

            Column {
                Text(
                    text = value,
                    color = AdminDashColors.TextDark,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )

                Text(
                    text = title,
                    color = AdminDashColors.TextDark,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = subtitle,
                    color = AdminDashColors.TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AdminActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(86.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AdminDashColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = AdminDashColors.TextDark,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subtitle,
                    color = AdminDashColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SystemSummaryCard(
    issuedItemsCount: Int,
    returnedItemsCount: Int,
    lowStockCount: Int,
    verifiedStudentsCount: Int,
    onReportsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReportsClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = AdminDashColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            SummaryRow(
                title = "Issued Items",
                value = issuedItemsCount.toString(),
                color = AdminDashColors.BlueText
            )

            SummaryRow(
                title = "Returned Items",
                value = returnedItemsCount.toString(),
                color = AdminDashColors.GreenText
            )

            SummaryRow(
                title = "Low Stock",
                value = lowStockCount.toString(),
                color = AdminDashColors.OrangeText
            )

            SummaryRow(
                title = "Verified Students",
                value = verifiedStudentsCount.toString(),
                color = AdminDashColors.PurpleText
            )
        }
    }
}

@Composable
private fun SummaryRow(
    title: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = AdminDashColors.TextDark,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = value,
            modifier = Modifier
                .background(color.copy(alpha = 0.10f), RoundedCornerShape(50.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            color = color,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AdminBottomBar(
    onHomeClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onReportsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = AdminDashColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem(
                title = "Home",
                icon = Icons.Filled.Assessment,
                selected = true,
                onClick = onHomeClick
            )

            BottomNavItem(
                title = "Equipment",
                icon = Icons.Filled.Widgets,
                selected = false,
                onClick = onEquipmentClick
            )

            BottomNavItem(
                title = "Reports",
                icon = Icons.Filled.Assessment,
                selected = false,
                onClick = onReportsClick
            )

            BottomNavItem(
                title = "Profile",
                icon = Icons.Filled.Person,
                selected = false,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (selected) AdminDashColors.PrimaryIndigo else AdminDashColors.TextMuted,
            modifier = Modifier.size(23.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = title,
            color = if (selected) AdminDashColors.PrimaryIndigo else AdminDashColors.TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}