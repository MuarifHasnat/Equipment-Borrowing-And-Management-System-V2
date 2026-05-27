package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport

private object SoftwareReportColors {
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
    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun SoftwareIssueReportsScreen(
    reportList: List<SoftwareIssueReport>,
    onUpdateStatusClick: (SoftwareIssueReport, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val filteredList = reportList.filter { report ->
        val query = searchText.trim().lowercase()

        val matchesSearch =
            query.isBlank() ||
                    report.softwareName.lowercase().contains(query) ||
                    report.computerName.lowercase().contains(query) ||
                    report.issueType.lowercase().contains(query) ||
                    report.description.lowercase().contains(query) ||
                    report.reportedByUserName.lowercase().contains(query) ||
                    report.reportedByStudentId.lowercase().contains(query) ||
                    report.reportedByDepartment.lowercase().contains(query)

        val cleanStatus = report.status.trim().lowercase()

        val matchesStatus =
            selectedStatus == "All" ||
                    report.status.equals(selectedStatus, ignoreCase = true) ||
                    (
                            selectedStatus == "Open" &&
                                    (
                                            cleanStatus.isBlank() ||
                                                    cleanStatus == "open" ||
                                                    cleanStatus == "pending"
                                            )
                            )

        matchesSearch && matchesStatus
    }.sortedByDescending { it.timestamp }

    val solvedCount = reportList.count {
        it.status.equals("Solved", ignoreCase = true) ||
                it.status.equals("Resolved", ignoreCase = true)
    }

    val openCount = reportList.count {
        val status = it.status.trim().lowercase()
        status.isBlank() ||
                status == "open" ||
                status == "pending" ||
                status == "in progress"
    }

    Surface(modifier = Modifier.fillMaxSize(), color = SoftwareReportColors.Bg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            SoftwareReportsTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SoftwareMiniStat("Total", reportList.size.toString(), SoftwareReportColors.BlueLight, SoftwareReportColors.BlueText, Modifier.weight(1f))
                SoftwareMiniStat("Open", openCount.toString(), SoftwareReportColors.OrangeLight, SoftwareReportColors.OrangeText, Modifier.weight(1f))
                SoftwareMiniStat("Solved", solvedCount.toString(), SoftwareReportColors.GreenLight, SoftwareReportColors.GreenText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = SoftwareReportColors.TextMuted)
                },
                placeholder = { Text("Search issue") }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Open", "Progress", "Solved", "Rejected").forEach { status ->
                    SoftwareFilterChip(
                        text = status,
                        selected = selectedStatus == if (status == "Progress") "In Progress" else status,
                        onClick = { selectedStatus = if (status == "Progress") "In Progress" else status }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Showing ${filteredList.size} of ${reportList.size} issue(s)",
                color = SoftwareReportColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (filteredList.isEmpty()) {
                EmptySoftwareCard()
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(filteredList) { report ->
                        SoftwareIssueCard(
                            report = report,
                            showActions = true,
                            onMarkProgress = {
                                onUpdateStatusClick(report, "In Progress", "Issue is being reviewed by admin")
                            },
                            onMarkSolved = {
                                onUpdateStatusClick(report, "Solved", "Issue has been solved")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SoftwareReportsTopBar(onBackClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(SoftwareReportColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = SoftwareReportColors.TextDark)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(SoftwareReportColors.RedLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.BugReport, contentDescription = null, tint = SoftwareReportColors.RedText, modifier = Modifier.size(23.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("Software Issues", color = SoftwareReportColors.TextDark, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("Review software reports", color = SoftwareReportColors.TextMuted, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun SoftwareIssueCard(
    report: SoftwareIssueReport,
    showActions: Boolean,
    onMarkProgress: () -> Unit,
    onMarkSolved: () -> Unit
) {
    val solved = report.status.equals("Solved", ignoreCase = true) ||
            report.status.equals("Resolved", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareReportColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(SoftwareReportColors.BlueLight, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (report.computerImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = report.computerImageUrl.trim(),
                            contentDescription = report.computerName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                            error = painterResource(id = android.R.drawable.ic_menu_gallery)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = report.computerName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(report.softwareName.ifBlank { "Unknown Software" }, color = SoftwareReportColors.TextDark, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(report.computerName.ifBlank { "Unknown Computer" }, color = SoftwareReportColors.TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = report.status.ifBlank { "Open" },
                        modifier = Modifier
                            .background(softwareStatusBg(report.status), RoundedCornerShape(50.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        color = softwareStatusText(report.status),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = SoftwareReportColors.Bg)
            Spacer(modifier = Modifier.height(6.dp))

            Text("Issue: ${report.issueType.ifBlank { "N/A" }} • Severity: ${report.severity.ifBlank { "N/A" }}", color = SoftwareReportColors.TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(4.dp))

            Text(report.description.ifBlank { "No description provided" }, color = SoftwareReportColors.TextDark, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "By: ${report.reportedByUserName.ifBlank { "N/A" }} • ${report.reportedByStudentId.ifBlank { "N/A" }} • ${report.reportedByDepartment.ifBlank { "N/A" }}",
                color = SoftwareReportColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (report.adminComment.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Admin: ${report.adminComment}", color = SoftwareReportColors.TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }

            if (showActions && !solved) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onMarkProgress,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftwareReportColors.OrangeText)
                    ) {
                        Icon(Icons.Rounded.Schedule, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Progress", fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Button(
                        onClick = onMarkSolved,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftwareReportColors.GreenText, contentColor = Color.White)
                    ) {
                        Icon(Icons.Rounded.Done, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Solve", fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
private fun SoftwareMiniStat(title: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.height(52.dp), color = bgColor, shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = textColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(value, color = textColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1)
        }
    }
}

@Composable
private fun SoftwareFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SoftwareReportColors.Primary, contentColor = Color.White),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
private fun EmptySoftwareCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareReportColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No software issue found.", color = SoftwareReportColors.TextMuted, fontWeight = FontWeight.SemiBold)
        }
    }
}

private fun softwareStatusText(status: String): Color {
    return when (status.lowercase()) {
        "solved", "resolved" -> SoftwareReportColors.GreenText
        "in progress" -> SoftwareReportColors.OrangeText
        "rejected" -> SoftwareReportColors.RedText
        else -> SoftwareReportColors.BlueText
    }
}

private fun softwareStatusBg(status: String): Color {
    return when (status.lowercase()) {
        "solved", "resolved" -> SoftwareReportColors.GreenLight
        "in progress" -> SoftwareReportColors.OrangeLight
        "rejected" -> SoftwareReportColors.RedLight
        else -> SoftwareReportColors.BlueLight
    }
}
