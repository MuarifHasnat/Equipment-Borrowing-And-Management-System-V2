package com.example.equipmentborrowingapp.ui.admin
import androidx.compose.foundation.layout.width
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private object ReportColors {
    val Background = Color(0xFFF4F7FB)
    val CardWhite = Color.White
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

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
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
    val totalActiveRequests = approvedRequestsCount + issuedItemsCount + overdueItemsCount
    val totalProblemItems = overdueItemsCount + lostDamagedItemsCount + lowStockCount + softwareIssuesCount

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ReportColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            ReportsTopBar(
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            ReportsHeroCard(
                totalActiveRequests = totalActiveRequests,
                totalProblemItems = totalProblemItems
            )

            Spacer(modifier = Modifier.height(24.dp))

            ReportsSectionTitle(
                title = "Report Overview",
                subtitle = "Quick summary before opening detailed reports"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ReportStatCard(
                    title = "Rooms",
                    value = totalRoomsCount.toString(),
                    bgColor = ReportColors.BlueLight,
                    contentColor = ReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                ReportStatCard(
                    title = "Equipment",
                    value = totalEquipmentCount.toString(),
                    bgColor = ReportColors.GreenLight,
                    contentColor = ReportColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ReportStatCard(
                    title = "Students",
                    value = totalStudentsCount.toString(),
                    bgColor = ReportColors.PurpleLight,
                    contentColor = ReportColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )

                ReportStatCard(
                    title = "Active",
                    value = totalActiveRequests.toString(),
                    bgColor = ReportColors.OrangeLight,
                    contentColor = ReportColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (totalProblemItems > 0) {
                ReportNoticeCard(
                    title = "Attention Needed",
                    message = "$totalProblemItems report item(s) need review: overdue, lost/damaged, low stock or software issues.",
                    bgColor = ReportColors.RedLight,
                    textColor = ReportColors.RedText
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            ReportsSectionTitle(
                title = "Inventory Reports",
                subtitle = "Room-wise and stock-related reports"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReportsGroupCard {
                ReportActionRow(
                    title = "Room-wise Equipment Report",
                    subtitle = "View equipment grouped by room or lab",
                    countText = "$totalRoomsCount rooms",
                    dotColor = ReportColors.BlueText,
                    dotBgColor = ReportColors.BlueLight,
                    onClick = onRoomWiseEquipmentReportClick
                )

                ReportDivider()

                ReportActionRow(
                    title = "Low Stock Equipment Report",
                    subtitle = "Equipment with low available quantity",
                    countText = "$lowStockCount items",
                    dotColor = ReportColors.OrangeText,
                    dotBgColor = ReportColors.OrangeLight,
                    onClick = onLowStockReportClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ReportsSectionTitle(
                title = "Borrow Request Reports",
                subtitle = "Track complete borrow request lifecycle"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReportsGroupCard {
                ReportActionRow(
                    title = "Pending Request Report",
                    subtitle = "Requests waiting for admin approval",
                    countText = "$pendingRequestsCount pending",
                    dotColor = ReportColors.OrangeText,
                    dotBgColor = ReportColors.OrangeLight,
                    onClick = onPendingRequestReportClick
                )

                ReportDivider()

                ReportActionRow(
                    title = "Approved Request Report",
                    subtitle = "Approved requests waiting to be issued",
                    countText = "$approvedRequestsCount approved",
                    dotColor = ReportColors.BlueText,
                    dotBgColor = ReportColors.BlueLight,
                    onClick = onApprovedRequestReportClick
                )

                ReportDivider()

                ReportActionRow(
                    title = "Issued Item Report",
                    subtitle = "Items currently issued to students",
                    countText = "$issuedItemsCount issued",
                    dotColor = ReportColors.PurpleText,
                    dotBgColor = ReportColors.PurpleLight,
                    onClick = onIssuedItemReportClick
                )

                ReportDivider()

                ReportActionRow(
                    title = "Returned Item Report",
                    subtitle = "Completed returned borrow records",
                    countText = "$returnedItemsCount returned",
                    dotColor = ReportColors.GreenText,
                    dotBgColor = ReportColors.GreenLight,
                    onClick = onReturnedItemReportClick
                )

                ReportDivider()

                ReportActionRow(
                    title = "Overdue Item Report",
                    subtitle = "Issued items past due date",
                    countText = "$overdueItemsCount overdue",
                    dotColor = ReportColors.RedText,
                    dotBgColor = ReportColors.RedLight,
                    onClick = onOverdueItemReportClick
                )

                ReportDivider()

                ReportActionRow(
                    title = "Lost / Damaged Report",
                    subtitle = "Items marked as lost or damaged",
                    countText = "$lostDamagedItemsCount records",
                    dotColor = ReportColors.RedText,
                    dotBgColor = ReportColors.RedLight,
                    onClick = onLostDamagedReportClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ReportsSectionTitle(
                title = "Student and Lab Reports",
                subtitle = "Student borrowing history and software issues"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReportsGroupCard {
                ReportActionRow(
                    title = "Student Borrow History",
                    subtitle = "View student-wise borrowing records",
                    countText = "$totalStudentsCount students",
                    dotColor = ReportColors.PurpleText,
                    dotBgColor = ReportColors.PurpleLight,
                    onClick = onStudentBorrowHistoryClick
                )

                ReportDivider()

                ReportActionRow(
                    title = "Software Issue Report",
                    subtitle = "Software problems reported from lab computers",
                    countText = "$softwareIssuesCount issues",
                    dotColor = ReportColors.RedText,
                    dotBgColor = ReportColors.RedLight,
                    onClick = onSoftwareIssueReportClick
                )
            }

            Spacer(modifier = Modifier.height(28.dp))


            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
private fun ReportsTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(46.dp)
                .background(
                    color = ReportColors.CardWhite,
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.Black.copy(alpha = 0.06f)
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = ReportColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(46.dp)
                .background(ReportColors.PurpleLight, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Assessment,
                contentDescription = null,
                tint = ReportColors.PrimaryIndigo,
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "Reports",
                color = ReportColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "Admin analytics center",
                color = ReportColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
@Composable
private fun ReportsHeroCard(
    totalActiveRequests: Int,
    totalProblemItems: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = ReportColors.PrimaryIndigo.copy(alpha = 0.25f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        ReportColors.PrimaryIndigo,
                        ReportColors.PurpleAccent
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Reports Center",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Admin Reports and History",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Review inventory, borrow lifecycle, student history and lab software issue reports.",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (totalProblemItems > 0) {
                    "$totalProblemItems issue-related item(s) need review. Active borrow records: $totalActiveRequests."
                } else {
                    "No urgent report issue found. Active borrow records: $totalActiveRequests."
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ReportsSectionTitle(
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = ReportColors.TextDark
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = ReportColors.TextMuted
        )
    }
}

@Composable
private fun ReportStatCard(
    title: String,
    value: String,
    bgColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ReportColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(contentColor)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = title,
                    color = ReportColors.TextMuted,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = value,
                    color = ReportColors.TextDark,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun ReportNoticeCard(
    title: String,
    message: String,
    bgColor: Color,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Composable
private fun ReportsGroupCard(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(
                color = ReportColors.CardWhite,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(8.dp)
    ) {
        content()
    }
}

@Composable
private fun ReportActionRow(
    title: String,
    subtitle: String,
    countText: String,
    dotColor: Color,
    dotBgColor: Color,
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
                .clip(RoundedCornerShape(14.dp))
                .background(dotBgColor)
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(dotColor)
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.padding(horizontal = 8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = ReportColors.TextDark,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = ReportColors.TextMuted,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = countText,
            modifier = Modifier
                .background(
                    color = dotBgColor,
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = dotColor
        )

        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

        Text(
            text = "›",
            color = Color(0xFFCBD5E1),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ReportDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = ReportColors.Background
    )
}