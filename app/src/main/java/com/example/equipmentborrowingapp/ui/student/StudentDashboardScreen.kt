package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

private object StudentColors {
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
fun StudentDashboardScreen(
    recentRequests: List<BorrowRequest> = emptyList(),
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
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            DashboardTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            HeroCard()

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(
                title = "Quick Actions",
                actionText = "See all",
                onActionClick = onViewEquipmentClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Equipment",
                        subtitle = "Browse & reserve",
                        icon = Icons.Rounded.Inventory2,
                        iconBg = StudentColors.BlueLight,
                        iconColor = StudentColors.BlueText,
                        modifier = Modifier.weight(1f),
                        onClick = onViewEquipmentClick
                    )

                    QuickActionCard(
                        title = "Requests",
                        subtitle = "Track status",
                        icon = Icons.Rounded.ListAlt,
                        iconBg = StudentColors.GreenLight,
                        iconColor = StudentColors.GreenText,
                        modifier = Modifier.weight(1f),
                        onClick = onMyRequestsClick
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Lab PCs",
                        subtitle = "Computers",
                        icon = Icons.Rounded.Computer,
                        iconBg = StudentColors.PurpleLight,
                        iconColor = StudentColors.PurpleText,
                        modifier = Modifier.weight(1f),
                        onClick = onLabComputersClick
                    )

                    QuickActionCard(
                        title = "Issues",
                        subtitle = "Software help",
                        icon = Icons.Rounded.BugReport,
                        iconBg = StudentColors.OrangeLight,
                        iconColor = StudentColors.OrangeText,
                        modifier = Modifier.weight(1f),
                        onClick = onMySoftwareIssuesClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionHeader(
                title = "Recent Requests",
                actionText = "View all",
                onActionClick = onMyRequestsClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            RecentRequestsCard(
                requests = recentRequests,
                onClick = onMyRequestsClick
            )

            Spacer(modifier = Modifier.weight(1f))

            DashboardBottomBar(
                onHomeClick = { },
                onEquipmentClick = onViewEquipmentClick,
                onRequestsClick = onMyRequestsClick,
                onProfileClick = onProfileClick
            )
        }
    }
}

@Composable
private fun DashboardTopBar(
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
                    .background(StudentColors.PurpleLight, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = StudentColors.PrimaryIndigo,
                    modifier = Modifier.size(27.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "LabMate",
                    color = StudentColors.TextDark,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "Equipment Portal",
                    color = StudentColors.TextMuted,
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
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Notifications",
                tint = StudentColors.TextDark,
                modifier = Modifier.size(25.dp)
            )

            Box(
                modifier = Modifier
                    .size(9.dp)
                    .align(Alignment.TopEnd)
                    .background(StudentColors.RedText, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(StudentColors.BlueLight, CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Profile",
                tint = StudentColors.BlueText,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun HeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = StudentColors.PrimaryIndigo.copy(alpha = 0.25f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        StudentColors.DeepBlue,
                        StudentColors.PrimaryIndigo,
                        StudentColors.PurpleAccent
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
                text = "Welcome back 👋",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Manage lab equipment,\nrequests and software issues.",
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
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Smart Lab Dashboard",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Icon(
            imageVector = Icons.Rounded.Inventory2,
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
            color = StudentColors.TextDark,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Row(
            modifier = Modifier.clickable { onActionClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = actionText,
                color = StudentColors.PrimaryIndigo,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = StudentColors.PrimaryIndigo,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
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
        colors = CardDefaults.cardColors(containerColor = StudentColors.CardWhite),
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
                    color = StudentColors.TextDark,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subtitle,
                    color = StudentColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun RecentRequestsCard(
    requests: List<BorrowRequest>,
    onClick: () -> Unit
) {
    val limitedRequests = requests.take(4)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (limitedRequests.size >= 2) 132.dp else 94.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (limitedRequests.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(StudentColors.BlueLight, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Inventory2,
                        contentDescription = null,
                        tint = StudentColors.BlueText,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "No recent request",
                        color = StudentColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Your latest borrow request will appear here",
                        color = StudentColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                limitedRequests.forEach { request ->
                    RecentRequestRow(
                        request = request,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentRequestRow(
    request: BorrowRequest,
    onClick: () -> Unit
) {
    val imageName = request.equipmentImageName
    val imageUrl = request.equipmentImageUrl
    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(imageUrl)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(StudentColors.BlueLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (hasImageUrl) {
                AsyncImage(
                    model = safeImageUrl,
                    contentDescription = request.equipmentName,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = fallbackImageResId),
                    error = painterResource(id = fallbackImageResId),
                    fallback = painterResource(id = fallbackImageResId),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = fallbackImageResId),
                    contentDescription = request.equipmentName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = request.equipmentName.ifBlank { "Unknown Equipment" },
                color = StudentColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Qty: ${request.quantity} • Due: ${request.dueDate.ifBlank { "N/A" }}",
                color = StudentColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        RequestMiniStatusBadge(status = request.status.ifBlank { "Pending" })

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun RequestMiniStatusBadge(status: String) {
    val cleanStatus = status.trim().ifBlank { "Pending" }

    val bgColor = when (cleanStatus.lowercase()) {
        "approved" -> StudentColors.BlueLight
        "issued" -> StudentColors.PurpleLight
        "returned" -> StudentColors.GreenLight
        "rejected", "overdue", "lost" -> StudentColors.RedLight
        "damaged", "pending" -> StudentColors.OrangeLight
        else -> StudentColors.BlueLight
    }

    val textColor = when (cleanStatus.lowercase()) {
        "approved" -> StudentColors.BlueText
        "issued" -> StudentColors.PurpleText
        "returned" -> StudentColors.GreenText
        "rejected", "overdue", "lost" -> StudentColors.RedText
        "damaged", "pending" -> StudentColors.OrangeText
        else -> StudentColors.BlueText
    }

    Text(
        text = cleanStatus,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = textColor,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        maxLines = 1
    )
}

@Composable
private fun DashboardBottomBar(
    onHomeClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onRequestsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.CardWhite),
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
                icon = Icons.Rounded.School,
                selected = true,
                onClick = onHomeClick
            )

            BottomNavItem(
                title = "Equipment",
                icon = Icons.Rounded.Inventory2,
                selected = false,
                onClick = onEquipmentClick
            )

            BottomNavItem(
                title = "Requests",
                icon = Icons.Rounded.ListAlt,
                selected = false,
                onClick = onRequestsClick
            )

            BottomNavItem(
                title = "Profile",
                icon = Icons.Rounded.Person,
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
            tint = if (selected) StudentColors.PrimaryIndigo else StudentColors.TextMuted,
            modifier = Modifier.size(23.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = title,
            color = if (selected) StudentColors.PrimaryIndigo else StudentColors.TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}