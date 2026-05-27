package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
private object MyIssueColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val BorderSoft = Color(0xFFE2E8F0)

    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun MySoftwareIssuesScreen(
    issueList: List<SoftwareIssueReport>,
    onRefreshClick: () -> Unit,
    onFeedbackSubmitClick: (SoftwareIssueReport, String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedReport by remember { mutableStateOf<SoftwareIssueReport?>(null) }
    var feedbackText by remember { mutableStateOf("") }
    var feedbackError by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val statusOptions = listOf("All", "Open", "In Progress", "Solved", "Rejected")
    val filteredIssues = remember(issueList, selectedStatus) {
        if (selectedStatus == "All") {
            issueList
        } else {
            issueList.filter { report ->
                val status = report.status.trim().lowercase()
                when (selectedStatus) {
                    "Open" -> status.isBlank() || status == "open" || status == "pending"
                    "Solved" -> status == "solved" || status == "resolved"
                    else -> report.status.equals(selectedStatus, ignoreCase = true)
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MyIssueColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            MySoftwareIssueTopBar(
                onBackClick = onBackClick,
                onRefreshClick = onRefreshClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            MySoftwareIssueSummaryCard(issueList = issueList)

            Spacer(modifier = Modifier.height(8.dp))

            StatusFilterCard(
                statusOptions = statusOptions,
                selectedStatus = selectedStatus,
                onStatusSelected = { selectedStatus = it },
                filteredCount = filteredIssues.size
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredIssues.isEmpty()) {
                EmptySoftwareIssueState(
                    title = if (issueList.isEmpty()) "No software issues yet" else "No $selectedStatus issues",
                    subtitle = if (issueList.isEmpty()) {
                        "Your submitted software issue reports will appear here."
                    } else {
                        "Try another status filter to see your submitted reports."
                    }
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 28.dp)
                ) {
                    items(filteredIssues, key = { it.id }) { report ->
                        StudentSoftwareIssueCard(
                            report = report,
                            onFeedbackClick = {
                                selectedReport = report
                                feedbackText = report.studentFeedback
                                feedbackError = ""
                            }
                        )
                    }
                }
            }
        }
    }

    selectedReport?.let { report ->
        AlertDialog(
            onDismissRequest = {
                selectedReport = null
                feedbackText = ""
                feedbackError = ""
            },
            title = {
                Text(
                    text = "Submit Feedback",
                    fontWeight = FontWeight.Bold,
                    color = MyIssueColors.TextDark
                )
            },
            text = {
                Column {
                    Text(
                        text = "Software: ${report.softwareName.ifBlank { "Unknown Software" }}",
                        color = MyIssueColors.TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = {
                            feedbackText = it
                            feedbackError = ""
                        },
                        label = { Text("Your feedback") },
                        placeholder = { Text("Example: Problem solved / Still facing issue") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MyIssueColors.PrimaryIndigo,
                            focusedLabelColor = MyIssueColors.PrimaryIndigo,
                            cursorColor = MyIssueColors.PrimaryIndigo
                        )
                    )

                    if (feedbackError.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = feedbackError,
                            color = MyIssueColors.RedText,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (feedbackText.isBlank()) {
                            feedbackError = "Feedback is required"
                        } else {
                            onFeedbackSubmitClick(report, feedbackText.trim())
                            selectedReport = null
                            feedbackText = ""
                            feedbackError = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyIssueColors.PrimaryIndigo
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Send,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedReport = null
                        feedbackText = ""
                        feedbackError = ""
                    }
                ) {
                    Text("Cancel", color = MyIssueColors.TextMuted)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MyIssueColors.CardWhite
        )
    }
}

@Composable
private fun MySoftwareIssueTopBar(
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .shadow(2.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                .background(MyIssueColors.CardWhite, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = MyIssueColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "My Software Issues",
                style = MaterialTheme.typography.titleLarge,
                color = MyIssueColors.TextDark,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Track status and admin response",
                style = MaterialTheme.typography.bodySmall,
                color = MyIssueColors.TextMuted
            )
        }

        IconButton(
            onClick = onRefreshClick,
            modifier = Modifier
                .size(42.dp)
                .shadow(2.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                .background(MyIssueColors.CardWhite, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Refresh",
                tint = MyIssueColors.PrimaryIndigo
            )
        }
    }
}

@Composable
private fun MySoftwareIssueSummaryCard(issueList: List<SoftwareIssueReport>) {
    val openCount = issueList.count {
        val status = it.status.trim().lowercase()
        status.isBlank() || status == "open" || status == "pending"
    }
    val progressCount = issueList.count { it.status.equals("In Progress", ignoreCase = true) }
    val solvedCount = issueList.count {
        it.status.equals("Solved", ignoreCase = true) ||
                it.status.equals("Resolved", ignoreCase = true)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MiniCountChip(
            label = "Open",
            count = openCount,
            bgColor = MyIssueColors.BlueLight,
            textColor = MyIssueColors.BlueText,
            modifier = Modifier.weight(1f)
        )
        MiniCountChip(
            label = "Progress",
            count = progressCount,
            bgColor = MyIssueColors.OrangeLight,
            textColor = MyIssueColors.OrangeText,
            modifier = Modifier.weight(1f)
        )
        MiniCountChip(
            label = "Solved",
            count = solvedCount,
            bgColor = MyIssueColors.GreenLight,
            textColor = MyIssueColors.GreenText,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MiniCountChip(
    label: String,
    count: Int,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(52.dp)
            .background(bgColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count.toString(),
            color = textColor,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatusFilterCard(
    statusOptions: List<String>,
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    filteredCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompactStatusDropdown(
            options = statusOptions,
            selectedStatus = selectedStatus,
            onStatusSelected = onStatusSelected,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .height(42.dp)
                .width(104.dp)
                .background(MyIssueColors.CardWhite, RoundedCornerShape(15.dp))
                .border(1.dp, MyIssueColors.BorderSoft.copy(alpha = 0.7f), RoundedCornerShape(15.dp))
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$filteredCount shown",
                color = MyIssueColors.TextMuted,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CompactStatusDropdown(
    options: List<String>,
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(MyIssueColors.CardWhite, RoundedCornerShape(15.dp))
                .border(1.dp, MyIssueColors.BorderSoft.copy(alpha = 0.7f), RoundedCornerShape(15.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Status",
                    color = MyIssueColors.TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = selectedStatus,
                    color = MyIssueColors.TextDark,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "▾",
                color = MyIssueColors.TextMuted,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MyIssueColors.CardWhite
        ) {
            options.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = status,
                            color = MyIssueColors.TextDark,
                            fontWeight = if (selectedStatus == status) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun StudentSoftwareIssueCard(
    report: SoftwareIssueReport,
    onFeedbackClick: () -> Unit
) {
    val statusStyle = issueStatusStyle(report.status)
    val severityStyle = severityStyle(report.severity)
    val canGiveFeedback = report.status.equals("Solved", ignoreCase = true) ||
            report.status.equals("Resolved", ignoreCase = true)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MyIssueColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MyIssueColors.BlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (report.computerImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = report.computerImageUrl.trim(),
                            contentDescription = report.computerName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = MyIssueColors.BlueText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.softwareName.ifBlank { "Unknown Software" },
                        color = MyIssueColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = report.computerName.ifBlank { "Unknown Computer" },
                        color = MyIssueColors.TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IssueBadge(
                    text = report.status.ifBlank { "Open" },
                    icon = statusStyle.icon,
                    bgColor = statusStyle.bgColor,
                    textColor = statusStyle.textColor
                )

                IssueBadge(
                    text = report.severity.ifBlank { "Medium" },
                    icon = severityStyle.icon,
                    bgColor = severityStyle.bgColor,
                    textColor = severityStyle.textColor
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MyIssueColors.GrayLight.copy(alpha = 0.65f), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                IssueInfoRow(label = "Issue Type", value = report.issueType.ifBlank { "N/A" })
                IssueInfoRow(label = "Description", value = report.description.ifBlank { "No description" })
                IssueInfoRow(label = "Submitted", value = formatIssueDate(report.timestamp))

                if (report.adminComment.isNotBlank()) {
                    IssueInfoRow(label = "Admin Comment", value = report.adminComment)
                }

                if (report.resolvedAt > 0L) {
                    IssueInfoRow(label = "Resolved At", value = formatIssueDate(report.resolvedAt))
                }
            }

            if (report.studentFeedback.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                FeedbackBox(feedback = report.studentFeedback)
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (canGiveFeedback) {
                Button(
                    onClick = onFeedbackClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyIssueColors.PrimaryIndigo
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Comment,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (report.studentFeedback.isBlank()) "Submit Feedback" else "Update Feedback",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                OutlinedButton(
                    onClick = { },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MyIssueColors.BorderSoft),
                    colors = ButtonDefaults.outlinedButtonColors(
                        disabledContentColor = MyIssueColors.TextMuted
                    )
                ) {
                    Text(
                        text = "Feedback available after issue is solved",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun IssueInfoRow(
    label: String,
    value: String
) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Text(
            text = label,
            color = MyIssueColors.TextMuted,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = MyIssueColors.TextDark,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun FeedbackBox(feedback: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MyIssueColors.GreenLight, RoundedCornerShape(14.dp))
            .border(1.dp, MyIssueColors.GreenText.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = MyIssueColors.GreenText,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Your Feedback",
                color = MyIssueColors.GreenText,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = feedback,
            color = MyIssueColors.TextDark,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun IssueBadge(
    text: String,
    icon: ImageVector,
    bgColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptySoftwareIssueState(
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MyIssueColors.CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 34.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(MyIssueColors.GrayLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = MyIssueColors.GrayText,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                color = MyIssueColors.TextDark,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                color = MyIssueColors.TextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private data class IssueVisualStyle(
    val icon: ImageVector,
    val bgColor: Color,
    val textColor: Color
)

private fun issueStatusStyle(status: String): IssueVisualStyle {
    return when (status.lowercase()) {
        "open", "pending", "" -> IssueVisualStyle(
            icon = Icons.Rounded.Pending,
            bgColor = MyIssueColors.BlueLight,
            textColor = MyIssueColors.BlueText
        )

        "in progress" -> IssueVisualStyle(
            icon = Icons.Rounded.HourglassTop,
            bgColor = MyIssueColors.OrangeLight,
            textColor = MyIssueColors.OrangeText
        )

        "solved", "resolved" -> IssueVisualStyle(
            icon = Icons.Rounded.CheckCircle,
            bgColor = MyIssueColors.GreenLight,
            textColor = MyIssueColors.GreenText
        )

        "rejected" -> IssueVisualStyle(
            icon = Icons.Rounded.Error,
            bgColor = MyIssueColors.RedLight,
            textColor = MyIssueColors.RedText
        )

        else -> IssueVisualStyle(
            icon = Icons.Rounded.Info,
            bgColor = MyIssueColors.GrayLight,
            textColor = MyIssueColors.GrayText
        )
    }
}

private fun severityStyle(severity: String): IssueVisualStyle {
    return when (severity.lowercase()) {
        "high" -> IssueVisualStyle(
            icon = Icons.Rounded.Warning,
            bgColor = MyIssueColors.RedLight,
            textColor = MyIssueColors.RedText
        )

        "medium" -> IssueVisualStyle(
            icon = Icons.Rounded.Warning,
            bgColor = MyIssueColors.OrangeLight,
            textColor = MyIssueColors.OrangeText
        )

        "low" -> IssueVisualStyle(
            icon = Icons.Rounded.Warning,
            bgColor = MyIssueColors.GreenLight,
            textColor = MyIssueColors.GreenText
        )

        else -> IssueVisualStyle(
            icon = Icons.Rounded.Warning,
            bgColor = MyIssueColors.GrayLight,
            textColor = MyIssueColors.GrayText
        )
    }
}

private fun formatIssueDate(time: Long): String {
    if (time <= 0L) return "N/A"

    return try {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(time))
    } catch (e: Exception) {
        "N/A"
    }
}
