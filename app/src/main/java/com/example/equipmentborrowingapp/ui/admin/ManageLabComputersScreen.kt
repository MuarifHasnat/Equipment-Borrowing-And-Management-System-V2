package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.LabComputer

// Modern Colors
private object LabManageColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

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
fun ManageLabComputersScreen(
    computerList: List<LabComputer>,
    onAddComputerClick: () -> Unit,
    onEditComputerClick: (LabComputer) -> Unit,
    onOpenSoftwareClick: (LabComputer) -> Unit,
    onViewReportsClick: (LabComputer) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val statusFilters = listOf("All", "Active", "Maintenance", "Problematic")

    val filteredList = computerList.filter { computer ->
        val matchesSearch =
            searchQuery.isBlank() ||
                    computer.pcName.contains(searchQuery, ignoreCase = true) ||
                    computer.labRoom.contains(searchQuery, ignoreCase = true) ||
                    computer.locationNote.contains(searchQuery, ignoreCase = true)

        val matchesStatus =
            selectedStatus == "All" ||
                    computer.status.equals(selectedStatus, ignoreCase = true)

        matchesSearch && matchesStatus
    }

    val totalCount = computerList.size
    val activeCount = computerList.count { it.status.equals("Active", ignoreCase = true) }
    val problematicCount = computerList.count { it.status.equals("Problematic", ignoreCase = true) }

    Surface(modifier = Modifier.fillMaxSize(), color = LabManageColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 🔙 Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(LabManageColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = LabManageColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Manage Lab PCs",
                        style = MaterialTheme.typography.titleLarge,
                        color = LabManageColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Lab Monitoring System",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabManageColors.TextMuted
                    )
                }
            }

            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernSummaryCard(
                    title = "Total",
                    value = totalCount.toString(),
                    bgColor = LabManageColors.BlueLight,
                    textColor = LabManageColors.BlueText,
                    modifier = Modifier.weight(1f)
                )
                ModernSummaryCard(
                    title = "Active",
                    value = activeCount.toString(),
                    bgColor = LabManageColors.GreenLight,
                    textColor = LabManageColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
                ModernSummaryCard(
                    title = "Issues",
                    value = problematicCount.toString(),
                    bgColor = LabManageColors.RedLight,
                    textColor = LabManageColors.RedText,
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
                placeholder = { Text("Search PC, room or location", color = LabManageColors.TextMuted) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = LabManageColors.TextMuted) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LabManageColors.CardWhite,
                    unfocusedContainerColor = LabManageColors.CardWhite,
                    focusedBorderColor = LabManageColors.PrimaryIndigo,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = LabManageColors.TextDark,
                    unfocusedTextColor = LabManageColors.TextDark
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(statusFilters) { filter ->
                    val isSelected = selectedStatus == filter
                    Surface(
                        onClick = { selectedStatus = filter },
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) LabManageColors.PrimaryIndigo else LabManageColors.CardWhite,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, LabManageColors.TextMuted.copy(alpha = 0.2f)),
                        modifier = Modifier.shadow(if (isSelected) 4.dp else 0.dp, RoundedCornerShape(50), spotColor = LabManageColors.PrimaryIndigo.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else LabManageColors.TextMuted,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Add PC Button
            Button(
                onClick = onAddComputerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = LabManageColors.PrimaryIndigo.copy(alpha = 0.4f)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(listOf(LabManageColors.PrimaryIndigo, LabManageColors.PurpleAccent)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.AddCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Lab Computer", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PC List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No matching lab computers found",
                        color = LabManageColors.TextMuted,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(filteredList, key = { it.id }) { computer ->
                        ModernComputerCard(
                            computer = computer,
                            onEdit = onEditComputerClick,
                            onSoftware = onOpenSoftwareClick,
                            onReports = onViewReportsClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernSummaryCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LabManageColors.CardWhite),
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
                fontSize = 26.sp,
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
private fun ModernComputerCard(
    computer: LabComputer,
    onEdit: (LabComputer) -> Unit,
    onSoftware: (LabComputer) -> Unit,
    onReports: (LabComputer) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LabManageColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LabManageColors.ModernBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Computer, contentDescription = null, tint = LabManageColors.PrimaryIndigo)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = computer.pcName.ifBlank { "Unnamed PC" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LabManageColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Room: ${computer.labRoom.ifBlank { "N/A" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = LabManageColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                ModernStatusBadge(text = computer.status.ifBlank { "Unknown" })
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details
            if (computer.locationNote.isNotBlank() || computer.ipAddress.isNotBlank()) {
                Surface(
                    color = LabManageColors.ModernBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (computer.locationNote.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = LabManageColors.TextMuted)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = computer.locationNote, style = MaterialTheme.typography.bodySmall, color = LabManageColors.TextDark)
                            }
                        }
                        if (computer.ipAddress.isNotBlank()) {
                            if (computer.locationNote.isNotBlank()) Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Dns, contentDescription = null, modifier = Modifier.size(14.dp), tint = LabManageColors.TextMuted)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = computer.ipAddress, style = MaterialTheme.typography.bodySmall, color = LabManageColors.TextDark)
                            }
                        }
                    }
                }
            }

            if (computer.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Remarks: ${computer.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = LabManageColors.TextDark,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = LabManageColors.ModernBg)
            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModernActionBtn(
                    text = "Edit",
                    modifier = Modifier.weight(1f),
                    isPrimary = true,
                    onClick = { onEdit(computer) }
                )
                ModernActionBtn(
                    text = "Software",
                    modifier = Modifier.weight(1f),
                    isPrimary = false,
                    onClick = { onSoftware(computer) }
                )
                ModernActionBtn(
                    text = "Reports",
                    modifier = Modifier.weight(1f),
                    isPrimary = false,
                    onClick = { onReports(computer) }
                )
            }
        }
    }
}

@Composable
private fun ModernStatusBadge(text: String) {
    val (bgColor, textColor) = when (text.trim().lowercase()) {
        "active" -> Pair(LabManageColors.GreenLight, LabManageColors.GreenText)
        "problematic" -> Pair(LabManageColors.RedLight, LabManageColors.RedText)
        "maintenance" -> Pair(LabManageColors.OrangeLight, LabManageColors.OrangeText)
        else -> Pair(Color(0xFFF1F5F9), Color(0xFF64748B))
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
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ModernActionBtn(
    text: String,
    modifier: Modifier = Modifier,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = modifier.height(38.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LabManageColors.PrimaryIndigo),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(38.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = LabManageColors.TextDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, LabManageColors.ModernBg),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}