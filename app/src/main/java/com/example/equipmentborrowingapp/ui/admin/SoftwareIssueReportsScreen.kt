package com.example.equipmentborrowingapp.ui.admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private object IssueColors {
    val ModernBg = Color(0xFFF4F7FB)
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
fun SoftwareIssueReportsScreen(
    reportList: List<SoftwareIssueReport>,
    onUpdateStatusClick: (SoftwareIssueReport, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedSeverity by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Newest First") }

    var selectedReport by remember { mutableStateOf<SoftwareIssueReport?>(null) }
    var selectedNewStatus by remember { mutableStateOf("") }
    var adminComment by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val statusFilters = listOf(
        "All",
        "Open",
        "In Progress",
        "Solved",
        "Rejected"
    )

    val severityFilters = listOf(
        "All",
        "Low",
        "Medium",
        "High"
    )

    val filteredReports = reportList
        .filter { report ->
            val query = searchText.trim().lowercase()

            val matchesSearch =
                query.isBlank() ||
                        report.softwareName.lowercase().contains(query) ||
                        report.computerName.lowercase().contains(query) ||
                        report.reportedByUserName.lowercase().contains(query) ||
                        report.issueType.lowercase().contains(query) ||
                        report.description.lowercase().contains(query) ||
                        report.status.lowercase().contains(query) ||
                        report.severity.lowercase().contains(query) ||
                        report.adminComment.lowercase().contains(query)

            val matchesStatus =
                selectedStatus == "All" ||
                        report.status.equals(selectedStatus, ignoreCase = true) ||
                        (selectedStatus == "Solved" && report.status.equals("Resolved", ignoreCase = true))

            val matchesSeverity =
                selectedSeverity == "All" ||
                        report.severity.equals(selectedSeverity, ignoreCase = true)

            matchesSearch && matchesStatus && matchesSeverity
        }
        .let { list ->
            when (selectedSort) {
                "Oldest First" -> list.sortedBy { it.timestamp }
                "Status" -> list.sortedWith(
                    compareBy<SoftwareIssueReport> { issueStatusOrder(it.status) }
                        .thenByDescending { it.timestamp }
                )

                "Severity" -> list.sortedWith(
                    compareBy<SoftwareIssueReport> { issueSeverityOrder(it.severity) }
                        .thenBy { issueStatusOrder(it.status) }
                )

                "Software A-Z" -> list.sortedBy { it.softwareName.lowercase() }
                "Computer A-Z" -> list.sortedBy { it.computerName.lowercase() }
                else -> list.sortedByDescending { it.timestamp }
            }
        }

    val openCount = reportList.count { it.status.equals("Open", ignoreCase = true) }
    val inProgressCount = reportList.count { it.status.equals("In Progress", ignoreCase = true) }
    val solvedCount = reportList.count {
        it.status.equals("Solved", ignoreCase = true) ||
                it.status.equals("Resolved", ignoreCase = true)
    }
    val highCount = reportList.count { it.severity.equals("High", ignoreCase = true) }

    selectedReport?.let { report ->
        UpdateIssueStatusDialog(
            report = report,
            newStatus = selectedNewStatus,
            adminComment = adminComment,
            onCommentChange = {
                adminComment = it
            },
            onDismiss = {
                selectedReport = null
                selectedNewStatus = ""
                adminComment = ""
            },
            onConfirm = {
                onUpdateStatusClick(report, selectedNewStatus, adminComment.trim())

                scope.launch {
                    snackbarHostState.showSnackbar("Issue update requested")
                }

                selectedReport = null
                selectedNewStatus = ""
                adminComment = ""
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = IssueColors.ModernBg
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = IssueColors.ModernBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(
                                IssueColors.CardWhite,
                                RoundedCornerShape(12.dp)
                            )
                            .shadow(
                                2.dp,
                                RoundedCornerShape(12.dp),
                                spotColor = Color.Black.copy(alpha = 0.05f)
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = IssueColors.TextDark
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))

                    Column {
                        Text(
                            text = "Software Issue Reports",
                            style = MaterialTheme.typography.titleLarge,
                            color = IssueColors.TextDark,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Update issue status and add admin comments",
                            style = MaterialTheme.typography.bodyMedium,
                            color = IssueColors.TextMuted
                        )
                    }
                }

                IssueHeroCard(
                    total = reportList.size,
                    open = openCount
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IssueMiniStatCard(
                        title = "Open",
                        value = openCount.toString(),
                        bgColor = IssueColors.RedLight,
                        textColor = IssueColors.RedText,
                        modifier = Modifier.weight(1f)
                    )

                    IssueMiniStatCard(
                        title = "In Progress",
                        value = inProgressCount.toString(),
                        bgColor = IssueColors.OrangeLight,
                        textColor = IssueColors.OrangeText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IssueMiniStatCard(
                        title = "Solved",
                        value = solvedCount.toString(),
                        bgColor = IssueColors.GreenLight,
                        textColor = IssueColors.GreenText,
                        modifier = Modifier.weight(1f)
                    )

                    IssueMiniStatCard(
                        title = "High",
                        value = highCount.toString(),
                        bgColor = IssueColors.PurpleLight,
                        textColor = IssueColors.PurpleText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    label = {
                        Text("Search software, PC, student, issue or comment")
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = null,
                            tint = IssueColors.TextMuted
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = IssueColors.CardWhite,
                        unfocusedContainerColor = IssueColors.CardWhite,
                        focusedBorderColor = IssueColors.PrimaryIndigo,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                IssueFilterTitle("Status")

                IssueHorizontalFilterRow {
                    statusFilters.forEach { status ->
                        IssueFilterChip(
                            text = status,
                            selected = selectedStatus == status,
                            onClick = {
                                selectedStatus = status
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                IssueFilterTitle("Severity")

                IssueHorizontalFilterRow {
                    severityFilters.forEach { severity ->
                        IssueFilterChip(
                            text = severity,
                            selected = selectedSeverity == severity,
                            onClick = {
                                selectedSeverity = severity
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                IssueFilterTitle("Sort")

                IssueHorizontalFilterRow {
                    listOf(
                        "Newest First",
                        "Oldest First",
                        "Status",
                        "Severity",
                        "Software A-Z",
                        "Computer A-Z"
                    ).forEach { sort ->
                        IssueFilterChip(
                            text = sort,
                            selected = selectedSort == sort,
                            onClick = {
                                selectedSort = sort
                            }
                        )
                    }
                }

                if (
                    searchText.isNotBlank() ||
                    selectedStatus != "All" ||
                    selectedSeverity != "All" ||
                    selectedSort != "Newest First"
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            searchText = ""
                            selectedStatus = "All"
                            selectedSeverity = "All"
                            selectedSort = "Newest First"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Clear Search, Filters and Sort")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Showing ${filteredReports.size} of ${reportList.size} issue report(s)",
                    style = MaterialTheme.typography.titleMedium,
                    color = IssueColors.TextDark,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (filteredReports.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (reportList.isEmpty()) {
                                "No software issue reports found."
                            } else {
                                "No issue report matches your search/filter."
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = IssueColors.TextMuted
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(filteredReports, key = { it.id }) { report ->
                            IssueReportCard(
                                report = report,
                                onMarkInProgress = {
                                    selectedReport = report
                                    selectedNewStatus = "In Progress"
                                    adminComment = report.adminComment
                                },
                                onMarkSolved = {
                                    selectedReport = report
                                    selectedNewStatus = "Solved"
                                    adminComment = report.adminComment
                                },
                                onReject = {
                                    selectedReport = report
                                    selectedNewStatus = "Rejected"
                                    adminComment = report.adminComment
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IssueHeroCard(
    total: Int,
    open: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        IssueColors.PrimaryIndigo,
                        IssueColors.PurpleAccent
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Issue Tracking",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.18f),
                        RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Lab Software Issues",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$total total issue(s), $open open issue(s)",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun IssueMiniStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = IssueColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = textColor,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = title,
                color = textColor.copy(alpha = 0.85f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun IssueReportCard(
    report: SoftwareIssueReport,
    onMarkInProgress: () -> Unit,
    onMarkSolved: () -> Unit,
    onReject: () -> Unit
) {
    val status = report.status.ifBlank { "Open" }
    val statusBg = issueStatusBackgroundColor(status)
    val statusColor = issueStatusTextColor(status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                4.dp,
                RoundedCornerShape(18.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = IssueColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.softwareName.ifBlank { "Unknown Software" },
                        style = MaterialTheme.typography.titleMedium,
                        color = IssueColors.TextDark,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = report.computerName.ifBlank { "Unknown Computer" },
                        style = MaterialTheme.typography.bodySmall,
                        color = IssueColors.TextMuted
                    )
                }

                Text(
                    text = status,
                    modifier = Modifier
                        .background(
                            statusBg,
                            RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            IssueInfoRow(
                label = "Reported By",
                value = report.reportedByUserName.ifBlank { "Unknown Student" }
            )

            IssueInfoRow(
                label = "Issue Type",
                value = report.issueType.ifBlank { "N/A" }
            )

            IssueInfoRow(
                label = "Severity",
                value = report.severity.ifBlank { "Medium" }
            )

            IssueInfoRow(
                label = "Reported At",
                value = formatIssueTimestamp(report.timestamp)
            )

            if (report.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                MessageBox(
                    title = "Student Description",
                    message = report.description,
                    bgColor = IssueColors.GrayLight,
                    textColor = IssueColors.GrayText
                )
            }

            if (report.adminComment.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                MessageBox(
                    title = "Admin Comment",
                    message = report.adminComment,
                    bgColor = IssueColors.BlueLight,
                    textColor = IssueColors.BlueText
                )
            }

            if (report.studentFeedback.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                MessageBox(
                    title = "Student Feedback",
                    message = report.studentFeedback,
                    bgColor = IssueColors.GreenLight,
                    textColor = IssueColors.GreenText
                )
            }

            if (report.resolvedAt > 0L) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Resolved At: ${formatIssueTimestamp(report.resolvedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = IssueColors.GreenText,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = IssueColors.ModernBg)
            Spacer(modifier = Modifier.height(10.dp))

            when {
                status.equals("Open", ignoreCase = true) -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onMarkInProgress,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IssueColors.OrangeLight,
                                contentColor = IssueColors.OrangeText
                            )
                        ) {
                            Icon(
                                Icons.Rounded.PlayArrow,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 3.dp))

                            Text("In Progress", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = null,
                                tint = IssueColors.RedText
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 3.dp))

                            Text(
                                text = "Reject",
                                color = IssueColors.RedText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                status.equals("In Progress", ignoreCase = true) -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onMarkSolved,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IssueColors.GreenLight,
                                contentColor = IssueColors.GreenText
                            )
                        ) {
                            Icon(
                                Icons.Rounded.DoneAll,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 3.dp))

                            Text("Solved", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = null,
                                tint = IssueColors.RedText
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 3.dp))

                            Text(
                                text = "Reject",
                                color = IssueColors.RedText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                status.equals("Solved", ignoreCase = true) ||
                        status.equals("Resolved", ignoreCase = true) -> {
                    Text(
                        text = "This issue has been solved.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                IssueColors.GreenLight,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        color = IssueColors.GreenText,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                status.equals("Rejected", ignoreCase = true) -> {
                    Text(
                        text = "This issue has been rejected.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                IssueColors.RedLight,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        color = IssueColors.RedText,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    OutlinedButton(
                        onClick = onMarkInProgress,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Comment,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 3.dp))

                        Text("Update Status")
                    }
                }
            }
        }
    }
}

@Composable
private fun UpdateIssueStatusDialog(
    report: SoftwareIssueReport,
    newStatus: String,
    adminComment: String,
    onCommentChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val buttonColor = when (newStatus) {
        "In Progress" -> IssueColors.OrangeText
        "Solved" -> IssueColors.GreenText
        "Rejected" -> IssueColors.RedText
        else -> IssueColors.PrimaryIndigo
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = IssueColors.CardWhite,
        title = {
            Text(
                text = "Update Issue Status",
                fontWeight = FontWeight.Bold,
                color = IssueColors.TextDark
            )
        },
        text = {
            Column {
                Text(
                    text = "Status will be changed to: $newStatus",
                    color = IssueColors.TextDark,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = report.softwareName.ifBlank { "Unknown Software" },
                    color = IssueColors.TextMuted,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = adminComment,
                    onValueChange = onCommentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    label = {
                        Text("Admin Comment")
                    },
                    placeholder = {
                        Text("Write action note or solution details...")
                    },
                    shape = RoundedCornerShape(14.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                )
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = IssueColors.TextMuted
                )
            }
        }
    )
}

@Composable
private fun IssueInfoRow(
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
            color = IssueColors.TextMuted,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value.ifBlank { "N/A" },
            modifier = Modifier.weight(0.62f),
            color = IssueColors.TextDark,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MessageBox(
    title: String,
    message: String,
    bgColor: Color,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                bgColor,
                RoundedCornerShape(12.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            color = textColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = message,
            color = textColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun IssueFilterTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = IssueColors.TextMuted,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun IssueHorizontalFilterRow(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun IssueFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = IssueColors.PrimaryIndigo,
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
        "solved" -> 2
        "resolved" -> 2
        "rejected" -> 3
        else -> 4
    }
}

private fun issueSeverityOrder(severity: String): Int {
    return when (severity.trim().lowercase()) {
        "high" -> 0
        "medium" -> 1
        "low" -> 2
        else -> 3
    }
}

private fun issueStatusTextColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "open" -> IssueColors.RedText
        "in progress" -> IssueColors.OrangeText
        "solved" -> IssueColors.GreenText
        "resolved" -> IssueColors.GreenText
        "rejected" -> IssueColors.GrayText
        else -> IssueColors.GrayText
    }
}

private fun issueStatusBackgroundColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "open" -> IssueColors.RedLight
        "in progress" -> IssueColors.OrangeLight
        "solved" -> IssueColors.GreenLight
        "resolved" -> IssueColors.GreenLight
        "rejected" -> IssueColors.GrayLight
        else -> IssueColors.GrayLight
    }
}

private fun formatIssueTimestamp(timestamp: Long): String {
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