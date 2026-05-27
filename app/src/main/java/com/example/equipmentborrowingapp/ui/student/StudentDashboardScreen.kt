package com.example.equipmentborrowingapp.ui.student
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import androidx.compose.ui.text.style.TextAlign
private object StudentColors {
    val Bg = Color(0xFFF4F7FB)
    val Card = Color.White
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)

    val Primary = Color(0xFF4F46E5)

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

    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun StudentDashboardScreen(
    recentRequests: List<BorrowRequest> = emptyList(),
    onViewEquipmentClick: () -> Unit,
    onMyRequestsClick: () -> Unit,
    onLabComputersClick: () -> Unit,
    onMySoftwareIssuesClick: () -> Unit,
    hasUnreadNotifications: Boolean = false,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit
) {
    val pendingCount = recentRequests.count { it.status.equals("Pending", ignoreCase = true) }
    val activeCount = recentRequests.count {
        it.status.equals("Approved", ignoreCase = true) ||
                it.status.equals("Issued", ignoreCase = true) ||
                it.status.equals("Overdue", ignoreCase = true)
    }
    val completedCount = recentRequests.count {
        it.status.equals("Returned", ignoreCase = true)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StudentColors.Bg
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 14.dp)
                    .padding(bottom = 96.dp)
            ) {
            StudentDashboardTopBar(
                hasUnreadNotifications = hasUnreadNotifications,
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick,
                onLogout = onLogout
            )

            Spacer(modifier = Modifier.height(14.dp))

            StudentWelcomeCard(
                pendingCount = pendingCount,
                activeCount = activeCount,
                completedCount = completedCount,
                onBrowseClick = onViewEquipmentClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StudentMiniStat(
                    title = "Pending",
                    value = pendingCount.toString(),
                    bgColor = StudentColors.OrangeLight,
                    textColor = StudentColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )

                StudentMiniStat(
                    title = "Active",
                    value = activeCount.toString(),
                    bgColor = StudentColors.BlueLight,
                    textColor = StudentColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                StudentMiniStat(
                    title = "Returned",
                    value = completedCount.toString(),
                    bgColor = StudentColors.GreenLight,
                    textColor = StudentColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            StudentSectionTitle(
                title = "Quick Actions",
                actionText = "Browse",
                onActionClick = onViewEquipmentClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StudentActionCard(
                        title = "Equipment",
                        subtitle = "Borrow items",
                        icon = Icons.Rounded.Inventory2,
                        iconBg = StudentColors.BlueLight,
                        iconColor = StudentColors.BlueText,
                        modifier = Modifier.weight(1f),
                        onClick = onViewEquipmentClick
                    )

                    StudentActionCard(
                        title = "My Req.",
                        subtitle = "Track status",
                        icon = Icons.Rounded.ListAlt,
                        iconBg = StudentColors.PurpleLight,
                        iconColor = StudentColors.PurpleText,
                        modifier = Modifier.weight(1f),
                        onClick = onMyRequestsClick
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StudentActionCard(
                        title = "Lab PCs",
                        subtitle = "Check PCs",
                        icon = Icons.Rounded.Computer,
                        iconBg = StudentColors.GreenLight,
                        iconColor = StudentColors.GreenText,
                        modifier = Modifier.weight(1f),
                        onClick = onLabComputersClick
                    )

                    StudentActionCard(
                        title = "Issues",
                        subtitle = "Report or view",
                        icon = Icons.Rounded.BugReport,
                        iconBg = StudentColors.OrangeLight,
                        iconColor = StudentColors.OrangeText,
                        modifier = Modifier.weight(1f),
                        onClick = onMySoftwareIssuesClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            StudentSectionTitle(
                title = "Recent Requests",
                actionText = "View all",
                onActionClick = onMyRequestsClick
            )

            Spacer(modifier = Modifier.height(10.dp))

                RecentRequestsPanel(
                    recentRequests = recentRequests.take(2),
                    onMyRequestsClick = onMyRequestsClick,
                    modifier = Modifier.height(176.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                StudentBottomBar(
                    onHomeClick = {},
                    onEquipmentClick = onViewEquipmentClick,
                    onRequestsClick = onMyRequestsClick,
                    onProfileClick = onProfileClick
                )
            }
        }
    }
}
@Composable
private fun StudentDashboardTopBar(
    hasUnreadNotifications: Boolean,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                tint = StudentColors.Primary,
                modifier = Modifier.size(25.dp)
            )

        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Student Portal",
                color = StudentColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )

            Text(
                text = "Borrow equipment easily",
                color = StudentColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(StudentColors.Card, CircleShape)
                .clickable { onNotificationClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Notifications",
                tint = StudentColors.TextDark,
                modifier = Modifier.size(22.dp)
            )
            if (hasUnreadNotifications) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .align(Alignment.TopEnd)
                        .background(StudentColors.RedText, CircleShape)
                )
            }
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
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(StudentColors.RedLight, CircleShape)
                .clickable { onLogout() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Logout,
                contentDescription = "Logout",
                tint = StudentColors.RedText,
                modifier = Modifier.size(21.dp)
            )
        }
    }
}

@Composable
private fun StudentWelcomeCard(
    pendingCount: Int,
    activeCount: Int,
    completedCount: Int,
    onBrowseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(116.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome back 👋",
                    color = StudentColors.TextDark,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$pendingCount pending • $activeCount active • $completedCount returned",
                    color = StudentColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onBrowseClick,
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StudentColors.Primary,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Find Equipment",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(StudentColors.PurpleLight, RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Inventory2,
                    contentDescription = null,
                    tint = StudentColors.Primary,
                    modifier = Modifier.size(38.dp)
                )
            }
        }
    }
}

@Composable
private fun StudentMiniStat(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(60.dp),
        color = bgColor,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )  {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun StudentSectionTitle(
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
                color = StudentColors.Primary,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = StudentColors.Primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun StudentActionCard(
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
            .height(82.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.Card),
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
private fun RecentRequestsPanel(
    recentRequests: List<BorrowRequest>,
    onMyRequestsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        if (recentRequests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.ListAlt,
                        contentDescription = null,
                        tint = StudentColors.TextMuted,
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "No recent request yet",
                        color = StudentColors.TextDark,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your latest equipment requests will appear here.",
                        color = StudentColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onMyRequestsClick,
                        modifier = Modifier.height(38.dp),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                    ) {
                        Text("Open My Requests", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(recentRequests.take(2)) { request ->
                    RecentRequestRow(request = request)
                }
            }
        }
    }
}

@Composable
private fun RecentRequestRow(
    request: BorrowRequest
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(request.equipmentImageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(request.equipmentImageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(request.equipmentImageUrl)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(StudentColors.Bg, RoundedCornerShape(18.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(StudentColors.GrayLight),
            contentAlignment = Alignment.Center
        ) {
            if (hasImageUrl) {
                AsyncImage(
                    model = safeImageUrl,
                    contentDescription = request.equipmentName,
                    placeholder = painterResource(id = fallbackImageResId),
                    error = painterResource(id = fallbackImageResId),
                    fallback = painterResource(id = fallbackImageResId),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = fallbackImageResId),
                    contentDescription = request.equipmentName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(7.dp)
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

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Qty: ${request.quantity} • ${request.dueDate.ifBlank { "N/A" }}",
                color = StudentColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        RequestStatusBadge(status = request.status)
    }
}

@Composable
private fun RequestStatusBadge(
    status: String
) {
    val cleanStatus = status.ifBlank { "Pending" }

    val bgColor = when (cleanStatus.lowercase()) {
        "approved" -> StudentColors.BlueLight
        "issued" -> StudentColors.PurpleLight
        "returned" -> StudentColors.GreenLight
        "overdue", "lost", "rejected" -> StudentColors.RedLight
        "damaged", "pending" -> StudentColors.OrangeLight
        else -> StudentColors.GrayLight
    }

    val textColor = when (cleanStatus.lowercase()) {
        "approved" -> StudentColors.BlueText
        "issued" -> StudentColors.PurpleText
        "returned" -> StudentColors.GreenText
        "overdue", "lost", "rejected" -> StudentColors.RedText
        "damaged", "pending" -> StudentColors.OrangeText
        else -> StudentColors.GrayText
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
private fun StudentBottomBar(
    onHomeClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onRequestsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = StudentColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                title = "Home",
                icon = Icons.Rounded.Home,
                selected = true,
                onClick = onHomeClick
            )

            BottomNavItem(
                title = "Browse",
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
            .padding(horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (selected) StudentColors.Primary else StudentColors.TextMuted,
            modifier = Modifier.size(23.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = title,
            color = if (selected) StudentColors.Primary else StudentColors.TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
