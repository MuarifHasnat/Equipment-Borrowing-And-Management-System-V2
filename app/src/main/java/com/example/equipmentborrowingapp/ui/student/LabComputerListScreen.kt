package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer



private object StudentLabColors {
    val Background = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val Border = Color(0xFFE2E8F0)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PrimarySoft = Color(0xFFEEF2FF)

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
    softwareStatusList: List<ComputerSoftwareStatus> = emptyList(),
    onReportClick: (LabComputer) -> Unit,
    onInstallRequestClick: (LabComputer) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val statusOptions = remember(computerList) {
        listOf("All") + computerList.map { it.status.ifBlank { "Unknown" } }.distinct().sorted()
    }

    val filteredComputers = remember(computerList, softwareStatusList, searchQuery, selectedStatus) {
        computerList
            .filter { computer ->
                val matchesStatus = selectedStatus == "All" ||
                        computer.status.equals(selectedStatus, ignoreCase = true) ||
                        (selectedStatus == "Unknown" && computer.status.isBlank())

                val query = searchQuery.trim().lowercase()
                val matchesSearch = query.isBlank() ||
                        computer.pcName.lowercase().contains(query) ||
                        computer.labRoom.lowercase().contains(query) ||
                        computer.ipAddress.lowercase().contains(query) ||
                        computer.locationNote.lowercase().contains(query) ||
                        computer.remarks.lowercase().contains(query) ||
                        computer.status.lowercase().contains(query) ||
                        softwareStatusList.any { software ->
                            software.computerId == computer.id &&
                                    (
                                            software.softwareName.lowercase().contains(query) ||
                                                    software.version.lowercase().contains(query)
                                            )
                        }

                matchesStatus && matchesSearch
            }
            .sortedBy { it.pcName.lowercase() }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = StudentLabColors.Background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            LabComputerTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(14.dp))

            LabComputerSearchAndFilterCard(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                statusOptions = statusOptions,
                selectedStatus = selectedStatus,
                onStatusSelected = { selectedStatus = it },
                totalCount = computerList.size,
                showingCount = filteredComputers.size
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (computerList.isEmpty()) {
                LabComputerEmptyState(
                    title = "No lab computers found",
                    subtitle = "Lab computers will appear here when available."
                )
            } else if (filteredComputers.isEmpty()) {
                LabComputerEmptyState(
                    title = "No matching computer",
                    subtitle = "Try changing your search text or status filter."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        items = filteredComputers,
                        key = { it.id }
                    ) { computer ->
                        ModernLabComputerCard(
                            computer = computer,
                            softwareList = softwareStatusList.filter { it.computerId == computer.id },
                            onReportClick = { onReportClick(computer) },
                            onInstallRequestClick = { onInstallRequestClick(computer) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LabComputerTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(44.dp)
                .clickable { onBackClick() },
            shape = RoundedCornerShape(14.dp),
            color = StudentLabColors.CardWhite,
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, StudentLabColors.Border)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = StudentLabColors.TextDark
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Lab Computers",
                style = MaterialTheme.typography.titleLarge,
                color = StudentLabColors.TextDark,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "View PCs and report software issues",
                style = MaterialTheme.typography.bodyMedium,
                color = StudentLabColors.TextMuted
            )
        }
    }
}

@Composable
private fun LabComputerSearchAndFilterCard(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    statusOptions: List<String>,
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    totalCount: Int,
    showingCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = StudentLabColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, StudentLabColors.Border.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Available PCs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudentLabColors.TextDark
                    )
                    Text(
                        text = "Showing $showingCount of $totalCount computers",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudentLabColors.TextMuted
                    )
                }

                Surface(
                    color = StudentLabColors.PrimarySoft,
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = StudentLabColors.PrimaryIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$totalCount PCs",
                            color = StudentLabColors.PrimaryIndigo,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "Search PC, room, IP...",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = StudentLabColors.TextMuted
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudentLabColors.PrimaryIndigo,
                    unfocusedBorderColor = StudentLabColors.Border,
                    focusedContainerColor = StudentLabColors.Background,
                    unfocusedContainerColor = StudentLabColors.Background
                )
            )

            if (statusOptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statusOptions.forEach { status ->
                        val selected = selectedStatus == status
                        Surface(
                            modifier = Modifier.clickable { onStatusSelected(status) },
                            color = if (selected) StudentLabColors.PrimarySoft else StudentLabColors.CardWhite,
                            shape = RoundedCornerShape(999.dp),
                            border = BorderStroke(
                                1.dp,
                                if (selected) StudentLabColors.PrimaryIndigo.copy(alpha = 0.35f) else StudentLabColors.Border
                            )
                        ) {
                            Text(
                                text = status,
                                color = if (selected) StudentLabColors.PrimaryIndigo else StudentLabColors.TextMuted,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LabComputerEmptyState(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = StudentLabColors.CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, StudentLabColors.Border)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(StudentLabColors.PrimarySoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Computer,
                        contentDescription = null,
                        tint = StudentLabColors.PrimaryIndigo,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = title,
                    color = StudentLabColors.TextDark,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    color = StudentLabColors.TextMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ModernLabComputerCard(
    computer: LabComputer,
    softwareList: List<ComputerSoftwareStatus>,
    onReportClick: () -> Unit,
    onInstallRequestClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = StudentLabColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, StudentLabColors.Border.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(StudentLabColors.PrimarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        if (computer.computerImageUrl.isNotBlank()) {
                            AsyncImage(
                                model = computer.computerImageUrl.trim(),
                                contentDescription = computer.pcName,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Computer,
                                contentDescription = null,
                                tint = StudentLabColors.PrimaryIndigo,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(13.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = computer.pcName.ifBlank { "Unnamed PC" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudentLabColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = StudentLabColors.TextMuted,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = computer.labRoom.ifBlank { "Room not assigned" },
                                style = MaterialTheme.typography.bodySmall,
                                color = StudentLabColors.TextMuted,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
                ModernLabPcStatusBadge(status = computer.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoChip(
                    modifier = Modifier.weight(1f),
                    label = "Room",
                    value = computer.labRoom.ifBlank { "N/A" },
                    icon = Icons.Rounded.LocationOn
                )
                InfoChip(
                    modifier = Modifier.weight(1f),
                    label = "IP Address",
                    value = computer.ipAddress.ifBlank { "N/A" },
                    icon = Icons.Rounded.Dns
                )
            }

            if (computer.locationNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = StudentLabColors.Background,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = null,
                            tint = StudentLabColors.PrimaryIndigo,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = computer.locationNote,
                            style = MaterialTheme.typography.bodySmall,
                            color = StudentLabColors.TextDark,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            if (computer.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Remarks: ${computer.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = StudentLabColors.TextMuted,
                    modifier = Modifier.padding(horizontal = 2.dp),
                    lineHeight = 18.sp
                )
            }

            val installedSoftware = softwareList
                .filter { it.installed }
                .sortedBy { it.softwareName.lowercase() }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = StudentLabColors.Background,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Installed Software",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudentLabColors.TextDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (installedSoftware.isEmpty()) {
                        Text(
                            text = "No software list added by admin yet.",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudentLabColors.TextMuted
                        )
                    } else {
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            installedSoftware.forEach { software ->
                                StudentSoftwareChip(software = software)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = StudentLabColors.Border.copy(alpha = 0.75f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onReportClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(15.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StudentLabColors.PrimaryIndigo,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ReportProblem,
                        contentDescription = "Report",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Report",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                OutlinedButton(
                    onClick = onInstallRequestClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(15.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Request Install",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Install Req.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@Composable
private fun StudentSoftwareChip(
    software: ComputerSoftwareStatus
) {
    Surface(
        color = StudentLabColors.CardWhite,
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(1.dp, StudentLabColors.Border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(StudentLabColors.PrimarySoft),
                contentAlignment = Alignment.Center
            ) {
                if (software.softwareLogoUrl.isNotBlank()) {
                    AsyncImage(
                        model = software.softwareLogoUrl.trim(),
                        contentDescription = software.softwareName,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Code,
                        contentDescription = null,
                        tint = StudentLabColors.PrimaryIndigo,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = buildString {
                    append(software.softwareName.ifBlank { "Software" })
                    if (software.version.isNotBlank()) {
                        append(" • v")
                        append(software.version)
                    }
                },
                style = MaterialTheme.typography.labelMedium,
                color = StudentLabColors.TextDark,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun InfoChip(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = modifier,
        color = StudentLabColors.Background,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = StudentLabColors.TextMuted,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = StudentLabColors.TextMuted,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = StudentLabColors.TextDark,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ModernLabPcStatusBadge(status: String) {
    val cleanStatus = status.ifBlank { "Unknown" }
    val (bgColor, textColor) = when (cleanStatus.trim().lowercase()) {
        "active", "available", "working" -> Pair(StudentLabColors.GreenLight, StudentLabColors.GreenText)
        "problematic", "issue", "damaged", "broken" -> Pair(StudentLabColors.RedLight, StudentLabColors.RedText)
        "maintenance", "under maintenance", "repair" -> Pair(StudentLabColors.OrangeLight, StudentLabColors.OrangeText)
        else -> Pair(StudentLabColors.GrayLight, StudentLabColors.GrayText)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = cleanStatus,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            maxLines = 1
        )
    }
}
