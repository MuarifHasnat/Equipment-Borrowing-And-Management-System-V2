package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport

// Modern Colors
private object SoftwareReportColors {
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
}

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

    Surface(modifier = Modifier.fillMaxSize(), color = SoftwareReportColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            //  Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(SoftwareReportColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = SoftwareReportColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Software Reports",
                        style = MaterialTheme.typography.titleLarge,
                        color = SoftwareReportColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Issue Monitoring & Resolution",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SoftwareReportColors.TextMuted
                    )
                }
            }

            // Summary Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernStatCard(
                    title = "Total",
                    value = reportList.size.toString(),
                    bgColor = SoftwareReportColors.BlueLight,
                    textColor = SoftwareReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Open",
                    value = reportList.count { it.status == "Open" }.toString(),
                    bgColor = SoftwareReportColors.RedLight,
                    textColor = SoftwareReportColors.RedText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "High",
                    value = reportList.count { it.severity == "High" }.toString(),
                    bgColor = SoftwareReportColors.OrangeLight,
                    textColor = SoftwareReportColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Modern Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(14.dp),
                placeholder = { Text("Search software, PC, or user...", color = SoftwareReportColors.TextMuted) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = SoftwareReportColors.TextMuted) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SoftwareReportColors.CardWhite,
                    unfocusedContainerColor = SoftwareReportColors.CardWhite,
                    focusedBorderColor = SoftwareReportColors.PrimaryIndigo,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = SoftwareReportColors.TextDark,
                    unfocusedTextColor = SoftwareReportColors.TextDark
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filters
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Status:", style = MaterialTheme.typography.labelLarge, color = SoftwareReportColors.TextMuted, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(statusFilters) { filter ->
                        ModernFilterChip(
                            text = filter,
                            isSelected = selectedStatus == filter,
                            onClick = { selectedStatus = filter }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Severity:", style = MaterialTheme.typography.labelLarge, color = SoftwareReportColors.TextMuted, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(severityFilters) { filter ->
                        ModernFilterChip(
                            text = filter,
                            isSelected = selectedSeverity == filter,
                            onClick = { selectedSeverity = filter }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // List
            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No matching reports found", color = SoftwareReportColors.TextMuted, style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(filteredList) { report ->
                        ModernReportCard(report, onStatusUpdateClick)
                    }
                }
            }
        }
    }
}

// Helper Composables

@Composable
private fun ModernStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareReportColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                color = textColor,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = textColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ModernFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (isSelected) SoftwareReportColors.PrimaryIndigo else SoftwareReportColors.CardWhite,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, SoftwareReportColors.TextMuted.copy(alpha = 0.2f)),
        modifier = Modifier.shadow(if (isSelected) 4.dp else 0.dp, RoundedCornerShape(50), spotColor = SoftwareReportColors.PrimaryIndigo.copy(alpha = 0.5f))
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else SoftwareReportColors.TextMuted,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ModernReportCard(
    report: SoftwareIssueReport,
    onStatusUpdateClick: (SoftwareIssueReport, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareReportColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = report.softwareName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SoftwareReportColors.TextDark
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModernStatusBadge(
                        text = report.severity,
                        isHigh = report.severity == "High",
                        isMedium = report.severity == "Medium"
                    )
                    ModernStatusBadge(
                        text = report.status,
                        isHigh = report.status == "Open",
                        isMedium = report.status == "In Progress"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details
            Surface(
                color = SoftwareReportColors.ModernBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Computer, contentDescription = null, modifier = Modifier.size(16.dp), tint = SoftwareReportColors.TextMuted)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = report.computerName, style = MaterialTheme.typography.bodySmall, color = SoftwareReportColors.TextDark, fontWeight = FontWeight.SemiBold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = SoftwareReportColors.TextMuted)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = report.reportedByUserName, style = MaterialTheme.typography.bodySmall, color = SoftwareReportColors.TextDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = report.description,
                style = MaterialTheme.typography.bodyMedium,
                color = SoftwareReportColors.TextMuted,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = SoftwareReportColors.ModernBg)
            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Text("Update Status:", style = MaterialTheme.typography.labelSmall, color = SoftwareReportColors.TextMuted, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernStatusButton(
                    text = "Open",
                    isSelected = report.status == "Open",
                    onClick = { onStatusUpdateClick(report, "Open") },
                    modifier = Modifier.weight(1f)
                )
                ModernStatusButton(
                    text = "Progress",
                    isSelected = report.status == "In Progress",
                    onClick = { onStatusUpdateClick(report, "In Progress") },
                    modifier = Modifier.weight(1f)
                )
                ModernStatusButton(
                    text = "Resolved",
                    isSelected = report.status == "Resolved",
                    onClick = { onStatusUpdateClick(report, "Resolved") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ModernStatusBadge(
    text: String,
    isHigh: Boolean,
    isMedium: Boolean
) {
    val bgColor = when {
        isHigh -> SoftwareReportColors.RedLight
        isMedium -> SoftwareReportColors.OrangeLight
        else -> SoftwareReportColors.GreenLight
    }

    val textColor = when {
        isHigh -> SoftwareReportColors.RedText
        isMedium -> SoftwareReportColors.OrangeText
        else -> SoftwareReportColors.GreenText
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ModernStatusButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Button(
            onClick = onClick,
            modifier = modifier.height(36.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SoftwareReportColors.PrimaryIndigo),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(36.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftwareReportColors.TextDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, SoftwareReportColors.ModernBg),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}