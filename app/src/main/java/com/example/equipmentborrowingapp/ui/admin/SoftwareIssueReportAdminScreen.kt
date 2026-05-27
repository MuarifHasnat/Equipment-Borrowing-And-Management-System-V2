package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BugReport
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport

private object SoftwareAdminColors {
    val Bg = Color(0xFFF4F7FB)
    val Card = Color.White
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)
    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)
    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)
    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)
}

@Composable
fun SoftwareIssueReportAdminScreen(
    reportList: List<SoftwareIssueReport>,
    onBackClick: () -> Unit
) {
    val solvedCount = reportList.count {
        it.status.equals("Solved", ignoreCase = true) ||
                it.status.equals("Resolved", ignoreCase = true)
    }
    val openCount = reportList.size - solvedCount

    Surface(modifier = Modifier.fillMaxSize(), color = SoftwareAdminColors.Bg) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
            SoftwareAdminTopBar(onBackClick)

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminIssueStat("Total", reportList.size.toString(), SoftwareAdminColors.BlueLight, SoftwareAdminColors.BlueText, Modifier.weight(1f))
                AdminIssueStat("Open", openCount.toString(), SoftwareAdminColors.OrangeLight, SoftwareAdminColors.OrangeText, Modifier.weight(1f))
                AdminIssueStat("Solved", solvedCount.toString(), SoftwareAdminColors.GreenLight, SoftwareAdminColors.GreenText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (reportList.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth().height(180.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = SoftwareAdminColors.Card), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No software issue found.", color = SoftwareAdminColors.TextMuted, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 6.dp)) {
                    items(reportList.sortedByDescending { it.timestamp }) { report ->
                        AdminIssueCard(report)
                    }
                }
            }
        }
    }
}

@Composable
private fun SoftwareAdminTopBar(onBackClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBackClick, modifier = Modifier.size(42.dp).background(SoftwareAdminColors.Card, RoundedCornerShape(14.dp))) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = SoftwareAdminColors.TextDark)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(modifier = Modifier.size(42.dp).background(SoftwareAdminColors.RedLight, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.BugReport, contentDescription = null, tint = SoftwareAdminColors.RedText, modifier = Modifier.size(23.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("Software Issue Report", color = SoftwareAdminColors.TextDark, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("Summary of lab software issues", color = SoftwareAdminColors.TextMuted, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AdminIssueCard(report: SoftwareIssueReport) {
    val solved = report.status.equals("Solved", ignoreCase = true) || report.status.equals("Resolved", ignoreCase = true)

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = SoftwareAdminColors.Card), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(64.dp).background(SoftwareAdminColors.BlueLight, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
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
                    Text(report.softwareName.ifBlank { "Unknown Software" }, color = SoftwareAdminColors.TextDark, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(report.computerName.ifBlank { "Unknown Computer" }, color = SoftwareAdminColors.TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = report.status.ifBlank { "Open" },
                        modifier = Modifier.background(if (solved) SoftwareAdminColors.GreenLight else SoftwareAdminColors.OrangeLight, RoundedCornerShape(50.dp)).padding(horizontal = 8.dp, vertical = 3.dp),
                        color = if (solved) SoftwareAdminColors.GreenText else SoftwareAdminColors.OrangeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = SoftwareAdminColors.Bg)
            Spacer(modifier = Modifier.height(6.dp))

            Text("Issue: ${report.issueType.ifBlank { "N/A" }} • Severity: ${report.severity.ifBlank { "N/A" }}", color = SoftwareAdminColors.TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(report.description.ifBlank { "No description provided" }, color = SoftwareAdminColors.TextDark, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildString {
                    append("Reported by: ${report.reportedByUserName.ifBlank { "N/A" }}")
                    if (report.reportedByStudentId.isNotBlank()) append(" • ID: ${report.reportedByStudentId}")
                    if (report.reportedByDepartment.isNotBlank()) append(" • ${report.reportedByDepartment}")
                },
                color = SoftwareAdminColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AdminIssueStat(title: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.height(52.dp), color = bgColor, shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = textColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(value, color = textColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1)
        }
    }
}
