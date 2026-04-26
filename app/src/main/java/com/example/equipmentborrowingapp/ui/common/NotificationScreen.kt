package com.example.equipmentborrowingapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.AppNotification
import com.example.equipmentborrowingapp.ui.theme.*

@Composable
fun NotificationScreen(
    notificationList: List<AppNotification>,
    onMarkReadClick: (AppNotification) -> Unit,
    onDeleteClick: (AppNotification) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            GradientHeaderCard(
                title = "Notifications",
                subtitle = "Updates & Alerts",
                description = "View your latest request, stock and system updates."
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (notificationList.isEmpty()) {
                EmptyStateView(
                    title = "No notifications",
                    subtitle = "You have no updates right now."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notificationList, key = { it.id }) { item ->
                        NotificationItemCard(
                            notification = item,
                            onMarkReadClick = { onMarkReadClick(item) },
                            onDeleteClick = { onDeleteClick(item) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryActionButton(
                text = "Back",
                onClick = onBackClick
            )
        }
    }
}

@Composable
private fun NotificationItemCard(
    notification: AppNotification,
    onMarkReadClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val bgColor = when (notification.type.lowercase()) {
        "success" -> SuccessLight
        "warning" -> WarningLight
        "error" -> ErrorLight
        else -> InfoLight
    }

    val iconColor = when (notification.type.lowercase()) {
        "success" -> Success
        "warning" -> Warning
        "error" -> Error
        else -> Info
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) SurfaceWhite else bgColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                tint = iconColor
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                if (!notification.isRead) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onMarkReadClick) {
                        Text("Mark as read")
                    }
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Error
                )
            }
        }
    }
}