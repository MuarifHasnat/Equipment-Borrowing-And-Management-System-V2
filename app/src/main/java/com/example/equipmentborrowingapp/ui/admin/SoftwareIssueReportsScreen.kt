package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport
import com.example.equipmentborrowingapp.ui.common.*
import com.example.equipmentborrowingapp.ui.theme.*

@Composable
fun SoftwareIssueReportsScreen(
    reportList: List<SoftwareIssueReport>,
    onStatusUpdateClick: (SoftwareIssueReport, String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedSeverity by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val statusFilters = listOf("All", "Open", "In Progress", "Resolved")
    val severityFilters = listOf("All", "Low", "Medium", "High")

    val filteredList = reportList.filter { report ->
        val statusMatch =
            selectedStatus == "All" || report.status.equals(selectedStatus, true)

        val severityMatch =
            selectedSeverity == "All" || report.severity.equals(selectedSeverity, true)

        val searchMatch =
            searchQuery.isBlank() ||
                    report.softwareName.contains(searchQuery, true) ||
                    report.computerName.contains(searchQuery, true) ||
                    report.reportedByUserName.contains(searchQuery, true)

        statusMatch && severityMatch && searchMatch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔥 HEADER
        GradientHeaderCard(
            title = "Software Reports",
            subtitle = "Issue Monitoring",
            description = "Track, filter, and resolve software-related problems efficiently."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 SUMMARY
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryStatCard(
                title = "Total",
                value = reportList.size.toString(),
                bgColor = InfoLight,
                modifier = Modifier.weight(1f)
            )

            SummaryStatCard(
                title = "Open",
                value = reportList.count { it.status == "Open" }.toString(),
                bgColor = ErrorLight,
                modifier = Modifier.weight(1f)
            )

            SummaryStatCard(
                title = "High",
                value = reportList.count { it.severity == "High" }.toString(),
                bgColor = WarningLight,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 SEARCH
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("Search...") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔥 STATUS FILTER
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(statusFilters) { filter ->
                CompactActionBox(
                    text = filter,
                    filled = selectedStatus == filter,
                    onClick = { selectedStatus = filter }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 🔥 SEVERITY FILTER
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(severityFilters) { severity ->
                CompactActionBox(
                    text = severity,
                    filled = selectedSeverity == severity,
                    onClick = { selectedSeverity = severity }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredList.isEmpty()) {
            EmptyStateText("No matching reports found", Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { report ->
                    ModernReportCard(report, onStatusUpdateClick)
                }
            }
        }

        SecondaryActionButton(
            text = "Back",
            onClick = onBackClick
        )
    }
}

@Composable
private fun ModernReportCard(
    report: SoftwareIssueReport,
    onStatusUpdateClick: (SoftwareIssueReport, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Text(
                text = report.softwareName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            TwoColumnInfoRow(
                leftTitle = "PC",
                leftValue = report.computerName,
                rightTitle = "User",
                rightValue = report.reportedByUserName
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                AppStatusBadge(
                    text = report.severity,
                    backgroundColor = when (report.severity) {
                        "High" -> ErrorLight
                        "Medium" -> WarningLight
                        else -> SuccessLight
                    },
                    textColor = TextPrimary
                )

                AppStatusBadge(
                    text = report.status,
                    backgroundColor = when (report.status) {
                        "Open" -> ErrorLight
                        "In Progress" -> WarningLight
                        else -> SuccessLight
                    },
                    textColor = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = report.description)

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                CompactActionBox(
                    text = "Open",
                    filled = report.status == "Open",
                    onClick = { onStatusUpdateClick(report, "Open") },
                    modifier = Modifier.weight(1f)
                )

                CompactActionBox(
                    text = "Progress",
                    filled = report.status == "In Progress",
                    onClick = { onStatusUpdateClick(report, "In Progress") },
                    modifier = Modifier.weight(1f)
                )

                CompactActionBox(
                    text = "Resolved",
                    filled = report.status == "Resolved",
                    onClick = { onStatusUpdateClick(report, "Resolved") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}