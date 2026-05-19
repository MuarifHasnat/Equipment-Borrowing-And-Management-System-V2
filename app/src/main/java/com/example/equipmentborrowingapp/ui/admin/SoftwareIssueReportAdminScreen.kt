package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private object SoftwareIssueReportColors {
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

    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun SoftwareIssueReportAdminScreen(
    reportList: List<SoftwareIssueReport>,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedSeverity by remember { mutableStateOf("All") }

    val statusFilters = listOf(
        "All",
        "Open",
        "In Progress",
        "Resolved",
        "Solved",
        "Rejected"
    )

    val severityFilters = listOf(
        "All",
        "Low",
        "Medium",
        "High"
    )

    val filteredReports = reportList.filter { report ->
        val query = searchText.trim().lowercase()

        val matchesSearch =
            query.isBlank() ||
                    report.softwareName.lowercase().contains(query) ||
                    report.computerName.lowercase().contains(query) ||
                    report.reportedByUserName.lowercase().contains(query) ||
                    report.issueType.lowercase().contains(query) ||
                    report.description.lowercase().contains(query) ||
                    report.status.lowercase().contains(query) ||
                    report.severity.lowercase().contains(query)

        val matchesStatus =
            selectedStatus == "All" ||
                    report.status.equals(selectedStatus, ignoreCase = true)

        val matchesSeverity =
            selectedSeverity == "All" ||
                    report.severity.equals(selectedSeverity, ignoreCase = true)

        matchesSearch && matchesStatus && matchesSeverity
    }.sortedWith(
        compareBy<SoftwareIssueReport> { issueStatusOrder(it.status) }
            .thenBy { severityOrder(it.severity) }
            .thenByDescending { it.timestamp }
    )

    val totalIssues = reportList.size
    val openIssues = reportList.count {
        it.status.equals("Open", ignoreCase = true)
    }
    val inProgressIssues = reportList.count {
        it.status.equals("In Progress", ignoreCase = true)
    }
    val solvedIssues = reportList.count {
        it.status.equals("Solved", ignoreCase = true) ||
                it.status.equals("Resolved", ignoreCase = true)
    }
    val highSeverityIssues = reportList.count {
        it.severity.equals("High", ignoreCase = true)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SoftwareIssueReportColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            SoftwareIssueHeroCard(
                totalIssues = totalIssues,
                openIssues = openIssues
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SoftwareIssueStatCard(
                    title = "Total",
                    value = totalIssues.toString(),
                    bgColor = SoftwareIssueReportColors.BlueLight,
                    textColor = SoftwareIssueReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                SoftwareIssueStatCard(
                    title = "Open",
                    value = openIssues.toString(),
                    bgColor = SoftwareIssueReportColors.RedLight,
                    textColor = SoftwareIssueReportColors.RedText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SoftwareIssueStatCard(
                    title = "In Progress",
                    value = inProgressIssues.toString(),
                    bgColor = SoftwareIssueReportColors.OrangeLight,
                    textColor = SoftwareIssueReportColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )

                SoftwareIssueStatCard(
                    title = "High",
                    value = highSeverityIssues.toString(),
                    bgColor = SoftwareIssueReportColors.PurpleLight,
                    textColor = SoftwareIssueReportColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text("Search software, PC, student, issue type or description")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Status",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SoftwareIssueReportColors.TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statusFilters.forEach { status ->
                    SoftwareIssueFilterChip(
                        text = status,
                        selected = selectedStatus == status,
                        onClick = {
                            selectedStatus = status
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Severity",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SoftwareIssueReportColors.TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                severityFilters.forEach { severity ->
                    SoftwareIssueFilterChip(
                        text = severity,
                        selected = selectedSeverity == severity,
                        onClick = {
                            selectedSeverity = severity
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Software Issue Report",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = SoftwareIssueReportColors.TextDark
            )

            Text(
                text = "Showing ${filteredReports.size} issue record(s)",
                style = MaterialTheme.typography.bodySmall,
                color = SoftwareIssueReportColors.TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredReports.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SoftwareIssueReportColors.CardWhite
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No software issue found for selected search/filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoftwareIssueReportColors.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredReports) { report ->
                        SoftwareIssueReportCard(report = report)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Back to Reports",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SoftwareIssueHeroCard(
    totalIssues: Int,
    openIssues: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        SoftwareIssueReportColors.PrimaryIndigo,
                        SoftwareIssueReportColors.PurpleAccent
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(22.dp)
    ) {
        Column {
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Software Report",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Lab Software Issue Report",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$totalIssues total issue(s), $openIssues open issue(s)",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SoftwareIssueStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftwareIssueReportColors.CardWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = SoftwareIssueReportColors.TextMuted,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = value,
                modifier = Modifier
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.titleLarge,
                color = textColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun SoftwareIssueReportCard(
    report: SoftwareIssueReport
) {
    val statusColor = issueStatusTextColor(report.status)
    val statusBg = issueStatusBackgroundColor(report.status)

    val severityColor = issueSeverityTextColor(report.severity)
    val severityBg = issueSeverityBackgroundColor(report.severity)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftwareIssueReportColors.CardWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.softwareName.ifBlank { "Unknown Software" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = SoftwareIssueReportColors.TextDark
                    )

                    Text(
                        text = report.computerName.ifBlank { "Unknown Computer" },
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftwareIssueReportColors.TextMuted
                    )
                }

                Text(
                    text = report.status.ifBlank { "Open" },
                    modifier = Modifier
                        .background(
                            color = statusBg,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            HorizontalDivider(color = SoftwareIssueReportColors.Background)

            SoftwareIssueInfoRow(
                label = "Reported By",
                value = report.reportedByUserName.ifBlank { "Unknown Student" }
            )

            SoftwareIssueInfoRow(
                label = "Issue Type",
                value = report.issueType.ifBlank { "N/A" }
            )

            SoftwareIssueInfoRow(
                label = "Date",
                value = formatTimestamp(report.timestamp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SoftwareIssueSmallBadge(
                    text = "Severity: ${report.severity.ifBlank { "Medium" }}",
                    bgColor = severityBg,
                    textColor = severityColor,
                    modifier = Modifier.weight(1f)
                )

                SoftwareIssueSmallBadge(
                    text = "PC: ${report.computerName.ifBlank { "N/A" }}",
                    bgColor = SoftwareIssueReportColors.BlueLight,
                    textColor = SoftwareIssueReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )
            }

            if (report.description.isNotBlank()) {
                Text(
                    text = report.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = SoftwareIssueReportColors.GrayLight,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(10.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftwareIssueReportColors.GrayText
                )
            }
        }
    }
}

@Composable
private fun SoftwareIssueInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.weight(0.38f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = SoftwareIssueReportColors.TextMuted
        )

        Text(
            text = value.ifBlank { "N/A" },
            modifier = Modifier.weight(0.62f),
            style = MaterialTheme.typography.bodySmall,
            color = SoftwareIssueReportColors.TextDark,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SoftwareIssueSmallBadge(
    text: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(
                color = bgColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = textColor
    )
}

@Composable
private fun SoftwareIssueFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftwareIssueReportColors.BlueText,
                contentColor = Color.White
            )
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(text)
        }
    }
}

private fun issueStatusOrder(status: String): Int {
    return when (status.trim().lowercase()) {
        "open" -> 0
        "in progress" -> 1
        "resolved" -> 2
        "solved" -> 2
        "rejected" -> 3
        else -> 4
    }
}

private fun severityOrder(severity: String): Int {
    return when (severity.trim().lowercase()) {
        "high" -> 0
        "medium" -> 1
        "low" -> 2
        else -> 3
    }
}

private fun issueStatusTextColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "open" -> SoftwareIssueReportColors.RedText
        "in progress" -> SoftwareIssueReportColors.OrangeText
        "resolved" -> SoftwareIssueReportColors.GreenText
        "solved" -> SoftwareIssueReportColors.GreenText
        "rejected" -> SoftwareIssueReportColors.GrayText
        else -> SoftwareIssueReportColors.GrayText
    }
}

private fun issueStatusBackgroundColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "open" -> SoftwareIssueReportColors.RedLight
        "in progress" -> SoftwareIssueReportColors.OrangeLight
        "resolved" -> SoftwareIssueReportColors.GreenLight
        "solved" -> SoftwareIssueReportColors.GreenLight
        "rejected" -> SoftwareIssueReportColors.GrayLight
        else -> SoftwareIssueReportColors.GrayLight
    }
}

private fun issueSeverityTextColor(severity: String): Color {
    return when (severity.trim().lowercase()) {
        "high" -> SoftwareIssueReportColors.RedText
        "medium" -> SoftwareIssueReportColors.OrangeText
        "low" -> SoftwareIssueReportColors.GreenText
        else -> SoftwareIssueReportColors.GrayText
    }
}

private fun issueSeverityBackgroundColor(severity: String): Color {
    return when (severity.trim().lowercase()) {
        "high" -> SoftwareIssueReportColors.RedLight
        "medium" -> SoftwareIssueReportColors.OrangeLight
        "low" -> SoftwareIssueReportColors.GreenLight
        else -> SoftwareIssueReportColors.GrayLight
    }
}

private fun formatTimestamp(timestamp: Long): String {
    return try {
        if (timestamp <= 0L) {
            "N/A"
        } else {
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(Date(timestamp))
        }
    } catch (_: Exception) {
        "N/A"
    }
}