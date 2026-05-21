package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

private object MyIssueColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MyIssueColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            MySoftwareIssueTopBar(
                onBackClick = onBackClick,
                onRefreshClick = onRefreshClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            MySoftwareIssueHeroCard(totalIssues = issueList.size)

            Spacer(modifier = Modifier.height(18.dp))

            if (issueList.isEmpty()) {
                EmptySoftwareIssueState()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 28.dp)
                ) {
                    items(issueList, key = { it.id }) { report ->
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
                        singleLine = false
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
                    Text("Cancel")
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
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .background(MyIssueColors.CardWhite, RoundedCornerShape(12.dp))
                .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = MyIssueColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "My Software Issues",
                style = MaterialTheme.typography.titleLarge,
                color = MyIssueColors.TextDark,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Track issue status and admin response",
                style = MaterialTheme.typography.bodyMedium,
                color = MyIssueColors.TextMuted
            )
        }

        IconButton(
            onClick = onRefreshClick,
            modifier = Modifier
                .background(MyIssueColors.CardWhite, RoundedCornerShape(12.dp))
                .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
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
private fun MySoftwareIssueHeroCard(totalIssues: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = MyIssueColors.PrimaryIndigo.copy(alpha = 0.18f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MyIssueColors.CardWhite)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(MyIssueColors.PrimaryIndigo.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.BugReport,
                    contentDescription = null,
                    tint = MyIssueColors.PrimaryIndigo,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Submitted Reports",
                    color = MyIssueColors.TextMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$totalIssues issue${if (totalIssues == 1) "" else "s"} found",
                    color = MyIssueColors.TextDark,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MyIssueColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(MyIssueColors.BlueLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Computer,
                        contentDescription = null,
                        tint = MyIssueColors.BlueText,
                        modifier = Modifier.size(23.dp)
                    )
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
                    icon = Icons.Rounded.Warning,
                    bgColor = severityStyle.bgColor,
                    textColor = severityStyle.textColor
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            IssueInfoRow(label = "Issue Type", value = report.issueType.ifBlank { "N/A" })
            IssueInfoRow(label = "Description", value = report.description.ifBlank { "No description" })
            IssueInfoRow(label = "Submitted", value = formatIssueDate(report.timestamp))

            if (report.adminComment.isNotBlank()) {
                IssueInfoRow(label = "Admin Comment", value = report.adminComment)
            }

            if (report.resolvedAt > 0L) {
                IssueInfoRow(label = "Resolved At", value = formatIssueDate(report.resolvedAt))
            }

            if (report.studentFeedback.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                FeedbackBox(feedback = report.studentFeedback)
            }

            if (report.status.equals("Solved", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = onFeedbackClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MyIssueColors.PrimaryIndigo
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(MyIssueColors.PrimaryIndigo.copy(alpha = 0.45f))
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Comment,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (report.studentFeedback.isBlank()) "Add Feedback" else "Update Feedback",
                        fontWeight = FontWeight.Bold
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
private fun EmptySoftwareIssueState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
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
            text = "No software issues yet",
            color = MyIssueColors.TextDark,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your submitted software issue reports will appear here.",
            color = MyIssueColors.TextMuted,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private data class IssueVisualStyle(
    val icon: ImageVector,
    val bgColor: Color,
    val textColor: Color
)

private fun issueStatusStyle(status: String): IssueVisualStyle {
    return when (status.lowercase()) {
        "open" -> IssueVisualStyle(
            icon = Icons.Rounded.Pending,
            bgColor = MyIssueColors.BlueLight,
            textColor = MyIssueColors.BlueText
        )

        "in progress" -> IssueVisualStyle(
            icon = Icons.Rounded.HourglassTop,
            bgColor = MyIssueColors.OrangeLight,
            textColor = MyIssueColors.OrangeText
        )

        "solved" -> IssueVisualStyle(
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