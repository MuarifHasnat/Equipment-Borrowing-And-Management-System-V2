package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import androidx.compose.ui.unit.sp

// Modern Premium Colors
private object StudentColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)

    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

    val WarningLight = Color(0xFFFFF7ED)
    val WarningText = Color(0xFFEA580C)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)
}

@Composable
fun StudentDashboardScreen(
    onViewEquipmentClick: () -> Unit,
    onMyRequestsClick: () -> Unit,
    onLabComputersClick: () -> Unit,
    onMySoftwareIssuesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StudentColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            //  Hero Card
            StudentHeroCard()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                color = StudentColors.TextDark,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StudentMiniStatCard(
                    title = "Equipment",
                    value = "Browse",
                    icon = Icons.Rounded.Inventory2,
                    modifier = Modifier.weight(1f)
                )

                StudentMiniStatCard(
                    title = "Requests",
                    value = "Track",
                    icon = Icons.Rounded.ListAlt,
                    modifier = Modifier.weight(1f)
                )

                StudentMiniStatCard(
                    title = "Lab PCs",
                    value = "Report",
                    icon = Icons.Rounded.Computer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            //  Modern Alert Card
            ModernAlertCard(
                title = "Reminder",
                message = "Check your request status regularly and return approved equipment before the due date.",
                icon = Icons.Rounded.WarningAmber
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                color = StudentColors.TextDark,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  Action List (Settings Style)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                    .background(StudentColors.CardWhite, RoundedCornerShape(24.dp))
                    .padding(8.dp)
            ) {
                ModernActionRow(
                    title = "My Profile",
                    subtitle = "View your account details",
                    icon = Icons.Rounded.Person,
                    iconBgColor = StudentColors.BlueLight,
                    iconColor = StudentColors.BlueText,
                    onClick = onProfileClick
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = StudentColors.ModernBg)

                ModernActionRow(
                    title = "Notifications",
                    subtitle = "View alerts and updates",
                    icon = Icons.Rounded.Notifications,
                    iconBgColor = StudentColors.WarningLight,
                    iconColor = StudentColors.WarningText,
                    onClick = onNotificationClick
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = StudentColors.ModernBg)

                ModernActionRow(
                    title = "View Equipment",
                    subtitle = "Browse and request items",
                    icon = Icons.Rounded.Inventory2,
                    iconBgColor = StudentColors.BlueLight,
                    iconColor = StudentColors.BlueText,
                    onClick = onViewEquipmentClick
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = StudentColors.ModernBg)

                ModernActionRow(
                    title = "My Requests",
                    subtitle = "Track your borrowed items",
                    icon = Icons.Rounded.ListAlt,
                    iconBgColor = StudentColors.PrimaryIndigo.copy(alpha = 0.1f),
                    iconColor = StudentColors.PrimaryIndigo,
                    onClick = onMyRequestsClick
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = StudentColors.ModernBg)

                ModernActionRow(
                    title = "Lab Computers",
                    subtitle = "Report software issues",
                    icon = Icons.Rounded.Computer,
                    iconBgColor = StudentColors.PurpleAccent.copy(alpha = 0.1f),
                    iconColor = StudentColors.PurpleAccent,
                    onClick = onLabComputersClick
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = StudentColors.ModernBg)

            ModernActionRow(
                title = "My Software Issues",
                subtitle = "Track issue status and submit feedback",
                icon = Icons.Rounded.BugReport,
                iconBgColor = StudentColors.BlueLight,
                iconColor = StudentColors.BlueText,
                onClick = onMySoftwareIssuesClick
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = StudentColors.RedText.copy(alpha = 0.3f)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StudentColors.RedLight,
                    contentColor = StudentColors.RedText
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Logout,
                    contentDescription = "Logout",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout Account", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StudentHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = StudentColors.PrimaryIndigo.copy(alpha = 0.4f))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(StudentColors.PrimaryIndigo, StudentColors.PurpleAccent)
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
                        text = "Student Panel",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Icon(
                    Icons.Rounded.School,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Lab & Equipment Access",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Borrow equipment, track requests, and report lab software issues easily.",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f
            )
        }
    }
}

@Composable
private fun StudentMiniStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = StudentColors.PrimaryIndigo,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = StudentColors.TextDark,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = title,
                color = StudentColors.TextMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ModernAlertCard(
    title: String,
    message: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.WarningLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = StudentColors.WarningText,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    color = StudentColors.WarningText,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    color = StudentColors.WarningText.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 18.sp
                )
            }
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
                color = StudentColors.TextDark,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = StudentColors.TextMuted,
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