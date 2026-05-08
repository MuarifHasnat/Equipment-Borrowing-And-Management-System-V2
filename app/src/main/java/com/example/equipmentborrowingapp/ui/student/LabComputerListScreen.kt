package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.LabComputer

// Modern Premium Colors
private object StudentLabColors {
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

    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF64748B)
}

@Composable
fun LabComputerListScreen(
    computerList: List<LabComputer>,
    onReportClick: (LabComputer) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = StudentLabColors.ModernBg) {
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
                        .background(StudentLabColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = StudentLabColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Lab Computers",
                        style = MaterialTheme.typography.titleLarge,
                        color = StudentLabColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "View PCs and report issues",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StudentLabColors.TextMuted
                    )
                }
            }

            if (computerList.isEmpty()) {
                // --- Empty State ---
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = StudentLabColors.TextMuted.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No lab computers found",
                            color = StudentLabColors.TextMuted,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                // --- Computer List ---
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        items = computerList.sortedBy { it.pcName.lowercase() },
                        key = { it.id }
                    ) { computer ->
                        ModernLabComputerCard(
                            computer = computer,
                            onReportClick = { onReportClick(computer) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernLabComputerCard(
    computer: LabComputer,
    onReportClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StudentLabColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Icon + Name/Room + Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(StudentLabColors.ModernBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = StudentLabColors.PrimaryIndigo
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = computer.pcName.ifBlank { "Unnamed PC" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudentLabColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Room: ${computer.labRoom.ifBlank { "N/A" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudentLabColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                ModernLabPcStatusBadge(status = computer.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details Box
            if (computer.locationNote.isNotBlank() || computer.ipAddress.isNotBlank()) {
                Surface(
                    color = StudentLabColors.ModernBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (computer.locationNote.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = StudentLabColors.TextMuted)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = computer.locationNote, style = MaterialTheme.typography.bodySmall, color = StudentLabColors.TextDark)
                            }
                        }
                        if (computer.ipAddress.isNotBlank()) {
                            if (computer.locationNote.isNotBlank()) Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Dns, contentDescription = null, modifier = Modifier.size(14.dp), tint = StudentLabColors.TextMuted)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "IP: ${computer.ipAddress}", style = MaterialTheme.typography.bodySmall, color = StudentLabColors.TextDark)
                            }
                        }
                    }
                }
            }

            if (computer.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Remarks: ${computer.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = StudentLabColors.TextDark,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = StudentLabColors.ModernBg)
            Spacer(modifier = Modifier.height(12.dp))

            // Action Button
            Button(
                onClick = onReportClick,
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StudentLabColors.PrimaryIndigo.copy(alpha = 0.1f),
                    contentColor = StudentLabColors.PrimaryIndigo
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ReportProblem,
                    contentDescription = "Report",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Report Software Issue", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ModernLabPcStatusBadge(status: String) {
    val (bgColor, textColor) = when (status.trim().lowercase()) {
        "active" -> Pair(StudentLabColors.GreenLight, StudentLabColors.GreenText)
        "problematic" -> Pair(StudentLabColors.RedLight, StudentLabColors.RedText)
        "maintenance" -> Pair(StudentLabColors.OrangeLight, StudentLabColors.OrangeText)
        else -> Pair(StudentLabColors.GrayLight, StudentLabColors.GrayText)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status.ifBlank { "Unknown" },
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}