package com.example.equipmentborrowingapp.ui.common

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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.AppNotification

// Modern Colors
private object NotifColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val PurpleLight = Color(0xFFEEF2FF)
    val PurpleText = Color(0xFF4F46E5)

    val UnreadBg = Color(0xFFF8FAFC)
}

@Composable
fun NotificationScreen(
    notifications: List<AppNotification>,
    onMarkReadClick: (AppNotification) -> Unit,
    onDeleteClick: (AppNotification) -> Unit,
    onNotificationClick: (AppNotification) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = NotifColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(NotifColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = NotifColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        color = NotifColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Updates & Alerts",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NotifColors.TextMuted
                    )
                }
            }

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(NotifColors.CardWhite, CircleShape)
                                .shadow(2.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.NotificationsNone,
                                contentDescription = null,
                                tint = NotifColors.TextMuted.copy(alpha = 0.5f),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No notifications yet",
                            color = NotifColors.TextDark,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "You have no new updates right now.",
                            color = NotifColors.TextMuted,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(notifications, key = { it.id }) { notification ->
                        ModernNotificationCard(
                            notification = notification,
                            onMarkReadClick = { onMarkReadClick(notification) },
                            onDeleteClick = { onDeleteClick(notification) },
                            onNotificationClick = { onNotificationClick(notification) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernNotificationCard(
    notification: AppNotification,
    onMarkReadClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val (bgColor, iconColor, icon) = when (notification.type.lowercase()) {
        "success",
        "request_approved",
        "request_issued",
        "request_returned",
        "fine_paid",
        "fine_waived",
        "software_issue_solved" -> Triple(NotifColors.GreenLight, NotifColors.GreenText, Icons.Rounded.CheckCircle)

        "warning",
        "new_borrow_request",
        "student_registered",
        "low_stock",
        "overdue_request" -> Triple(NotifColors.OrangeLight, NotifColors.OrangeText, Icons.Rounded.WarningAmber)

        "error",
        "request_rejected",
        "request_lost",
        "request_damaged",
        "lost_damaged",
        "software_issue_rejected" -> Triple(NotifColors.RedLight, NotifColors.RedText, Icons.Rounded.ErrorOutline)

        else -> Triple(NotifColors.BlueLight, NotifColors.BlueText, Icons.Rounded.Info)
    }

    val cardBackgroundColor = if (notification.read) NotifColors.CardWhite else NotifColors.UnreadBg

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNotificationClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.read) 1.dp else 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        color = NotifColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (notification.read) FontWeight.SemiBold else FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "Delete",
                            tint = NotifColors.RedText.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = notification.message,
                    color = NotifColors.TextMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = bgColor,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = getNotificationTypeLabel(
                                type = notification.type,
                                title = notification.title,
                                message = notification.message
                            ),
                            color = iconColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    if (!notification.read) {
                        Surface(
                            onClick = onMarkReadClick,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                NotifColors.PrimaryIndigo.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = NotifColors.PrimaryIndigo,
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "Mark as read",
                                    color = NotifColors.PrimaryIndigo,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getNotificationTypeLabel(
    type: String,
    title: String,
    message: String
): String {
    val cleanType = type.trim().lowercase()
    val cleanTitle = title.trim().lowercase()
    val cleanMessage = message.trim().lowercase()
    val allText = "$cleanType $cleanTitle $cleanMessage"

    return when {
        cleanType in listOf(
            "request_approved",
            "request_rejected",
            "request_issued",
            "request_returned",
            "request_lost",
            "request_damaged",
            "new_borrow_request",
            "overdue_request"
        ) || allText.contains("borrow request") ||
                allText.contains("request approved") ||
                allText.contains("request rejected") ||
                allText.contains("issued") ||
                allText.contains("returned") ||
                allText.contains("overdue") -> "Request"

        cleanType in listOf("fine_paid", "fine_waived") ||
                allText.contains("fine paid") ||
                allText.contains("fine waived") ||
                allText.contains("fine") -> "Fine"

        cleanType in listOf("software_issue", "software_issue_solved", "software_issue_rejected") ||
                allText.contains("software issue") ||
                allText.contains("issue solved") ||
                allText.contains("issue rejected") -> "Issue"

        cleanType == "student_registered" ||
                allText.contains("student registered") ||
                allText.contains("new student") -> "Student"

        cleanType == "low_stock" ||
                allText.contains("low stock") -> "Stock"

        cleanType == "lost_damaged" ||
                allText.contains("lost/damaged") ||
                allText.contains("lost damaged") ||
                allText.contains("lost") ||
                allText.contains("damaged") -> "Lost/Damaged"

        else -> "General"
    }
}
