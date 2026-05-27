package com.example.equipmentborrowingapp.ui.admin

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private object ReportColors {
    val Background = Color(0xFFF4F7FB)
    val CardWhite = Color.White
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)

    val PrimaryIndigo = Color(0xFF4F46E5)

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

    val SlateLight = Color(0xFFF1F5F9)
    val SlateText = Color(0xFF475569)
}

@Composable
fun ReportsDashboardScreen(
    totalRoomsCount: Int = 0,
    totalEquipmentCount: Int = 0,
    totalStudentsCount: Int = 0,
    pendingRequestsCount: Int = 0,
    approvedRequestsCount: Int = 0,
    issuedItemsCount: Int = 0,
    returnedItemsCount: Int = 0,
    overdueItemsCount: Int = 0,
    lostDamagedItemsCount: Int = 0,
    lowStockCount: Int = 0,
    softwareIssuesCount: Int = 0,
    onRoomWiseEquipmentReportClick: () -> Unit,
    onStudentBorrowHistoryClick: () -> Unit,
    onPendingRequestReportClick: () -> Unit,
    onApprovedRequestReportClick: () -> Unit,
    onIssuedItemReportClick: () -> Unit,
    onReturnedItemReportClick: () -> Unit,
    onOverdueItemReportClick: () -> Unit,
    onLostDamagedReportClick: () -> Unit,
    onLowStockReportClick: () -> Unit,
    onSoftwareIssueReportClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val activeRequestsCount = approvedRequestsCount + issuedItemsCount + overdueItemsCount
    val attentionCount = overdueItemsCount + lostDamagedItemsCount + lowStockCount + softwareIssuesCount

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ReportColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            ReportsHeader(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(8.dp))

            ReportsSummaryCard(
                totalEquipmentCount = totalEquipmentCount,
                activeRequestsCount = activeRequestsCount,
                attentionCount = attentionCount,
                totalStudentsCount = totalStudentsCount
            )

            Spacer(modifier = Modifier.height(12.dp))

            ReportSectionTitle(
                title = "Inventory Reports",
                subtitle = "Rooms, equipment and stock overview"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReportGroupCard {
                ReportMenuRow(
                    title = "Room-wise Equipment",
                    subtitle = "$totalRoomsCount room(s) available",
                    countText = totalRoomsCount.toString(),
                    icon = Icons.Filled.Inventory,
                    iconBgColor = ReportColors.BlueLight,
                    iconColor = ReportColors.BlueText,
                    onClick = onRoomWiseEquipmentReportClick
                )

                ReportDivider()

                ReportMenuRow(
                    title = "Low Stock Equipment",
                    subtitle = "$lowStockCount item(s) need restock",
                    countText = lowStockCount.toString(),
                    icon = Icons.Filled.ReportProblem,
                    iconBgColor = ReportColors.OrangeLight,
                    iconColor = ReportColors.OrangeText,
                    onClick = onLowStockReportClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ReportSectionTitle(
                title = "Borrow Request Reports",
                subtitle = "Track request lifecycle and item condition"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReportGroupCard {
                ReportMenuRow(
                    title = "Pending Requests",
                    subtitle = "Waiting for admin approval",
                    countText = pendingRequestsCount.toString(),
                    icon = Icons.Filled.ListAlt,
                    iconBgColor = ReportColors.OrangeLight,
                    iconColor = ReportColors.OrangeText,
                    onClick = onPendingRequestReportClick
                )

                ReportDivider()

                ReportMenuRow(
                    title = "Approved Requests",
                    subtitle = "Approved but not fully completed",
                    countText = approvedRequestsCount.toString(),
                    icon = Icons.Filled.ListAlt,
                    iconBgColor = ReportColors.BlueLight,
                    iconColor = ReportColors.BlueText,
                    onClick = onApprovedRequestReportClick
                )

                ReportDivider()

                ReportMenuRow(
                    title = "Issued Items",
                    subtitle = "Items currently issued to students",
                    countText = issuedItemsCount.toString(),
                    icon = Icons.Filled.Widgets,
                    iconBgColor = ReportColors.PurpleLight,
                    iconColor = ReportColors.PurpleText,
                    onClick = onIssuedItemReportClick
                )

                ReportDivider()

                ReportMenuRow(
                    title = "Returned Items",
                    subtitle = "Completed borrow requests",
                    countText = returnedItemsCount.toString(),
                    icon = Icons.Filled.Widgets,
                    iconBgColor = ReportColors.GreenLight,
                    iconColor = ReportColors.GreenText,
                    onClick = onReturnedItemReportClick
                )

                ReportDivider()

                ReportMenuRow(
                    title = "Overdue Items",
                    subtitle = "Due date passed and not returned",
                    countText = overdueItemsCount.toString(),
                    icon = Icons.Filled.ReportProblem,
                    iconBgColor = ReportColors.RedLight,
                    iconColor = ReportColors.RedText,
                    onClick = onOverdueItemReportClick
                )

                ReportDivider()

                ReportMenuRow(
                    title = "Lost / Damaged Report",
                    subtitle = "Items marked as lost or damaged",
                    countText = lostDamagedItemsCount.toString(),
                    icon = Icons.Filled.ReportProblem,
                    iconBgColor = ReportColors.RedLight,
                    iconColor = ReportColors.RedText,
                    onClick = onLostDamagedReportClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ReportSectionTitle(
                title = "Student & Lab Reports",
                subtitle = "Student history and software issue records"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReportGroupCard {
                ReportMenuRow(
                    title = "Student Borrow History",
                    subtitle = "$totalStudentsCount student(s) in system",
                    countText = totalStudentsCount.toString(),
                    icon = Icons.Filled.History,
                    iconBgColor = ReportColors.PurpleLight,
                    iconColor = ReportColors.PurpleText,
                    onClick = onStudentBorrowHistoryClick
                )

                ReportDivider()

                ReportMenuRow(
                    title = "Software Issue Report",
                    subtitle = "$softwareIssuesCount issue(s) reported",
                    countText = softwareIssuesCount.toString(),
                    icon = Icons.Filled.Computer,
                    iconBgColor = ReportColors.RedLight,
                    iconColor = ReportColors.RedText,
                    onClick = onSoftwareIssueReportClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ReportsHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = ReportColors.CardWhite,
                    shape = RoundedCornerShape(14.dp)
                )
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(14.dp),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = ReportColors.TextDark,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(ReportColors.PurpleLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Assessment,
                contentDescription = null,
                tint = ReportColors.PrimaryIndigo,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Reports",
                color = ReportColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )

            Text(
                text = "Choose a report to review",
                color = ReportColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ReportsSummaryCard(
    totalEquipmentCount: Int,
    activeRequestsCount: Int,
    attentionCount: Int,
    totalStudentsCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ReportColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Report Overview",
                        color = ReportColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Quick summary of system activity",
                        color = ReportColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(ReportColors.BlueLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Assessment,
                        contentDescription = null,
                        tint = ReportColors.BlueText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryMiniCard(
                    title = "Equipment",
                    value = totalEquipmentCount.toString(),
                    bgColor = ReportColors.BlueLight,
                    textColor = ReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                SummaryMiniCard(
                    title = "Active",
                    value = activeRequestsCount.toString(),
                    bgColor = ReportColors.PurpleLight,
                    textColor = ReportColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryMiniCard(
                    title = "Need Review",
                    value = attentionCount.toString(),
                    bgColor = if (attentionCount > 0) ReportColors.RedLight else ReportColors.GreenLight,
                    textColor = if (attentionCount > 0) ReportColors.RedText else ReportColors.GreenText,
                    modifier = Modifier.weight(1f)
                )

                SummaryMiniCard(
                    title = "Students",
                    value = totalStudentsCount.toString(),
                    bgColor = ReportColors.GreenLight,
                    textColor = ReportColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryMiniCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(52.dp),
        color = bgColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ReportSectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = ReportColors.TextDark
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = ReportColors.TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ReportGroupCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ReportColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun ReportMenuRow(
    title: String,
    subtitle: String,
    countText: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(iconBgColor, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = ReportColors.TextDark,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                color = ReportColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = countText,
            modifier = Modifier
                .background(iconBgColor, RoundedCornerShape(50.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = iconColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Spacer(modifier = Modifier.width(6.dp))

        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ReportDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 12.dp),
        color = ReportColors.Background
    )
}
