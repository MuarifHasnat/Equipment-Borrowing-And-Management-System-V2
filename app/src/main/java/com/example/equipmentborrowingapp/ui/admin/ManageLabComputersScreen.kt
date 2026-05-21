package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.LocationOn
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.data.model.Room

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

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
}

@Composable
fun ManageLabComputersScreen(
    computerList: List<LabComputer>,
    onAddComputerClick: () -> Unit,
    onEditComputerClick: (LabComputer) -> Unit,
    onOpenSoftwareClick: (LabComputer) -> Unit,
    onViewReportsClick: (LabComputer) -> Unit,
    onBackClick: () -> Unit,
    roomList: List<Room> = emptyList()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedLabRoom by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("PC Name A-Z") }

    val roomById = remember(roomList) {
        roomList.associateBy { it.id }
    }

    val labRoomFilterList = remember(computerList, roomList) {
        val fromComputers = computerList
            .map { computer ->
                val roomName = roomById[computer.roomId]?.name.orEmpty()
                roomName.ifBlank { computer.labRoom }
            }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        listOf("All") + fromComputers
    }

    val statusFilters = listOf(
        "All",
        "Active",
        "Maintenance",
        "Problematic"
    )

    val sortFilters = listOf(
        "PC Name A-Z",
        "PC Name Z-A",
        "Room A-Z",
        "Status",
        "Last Checked Newest",
        "Last Checked Oldest"
    )

    val filteredList = computerList
        .filter { computer ->
            val query = searchQuery.trim().lowercase()
            val roomName = roomById[computer.roomId]?.name.orEmpty()
            val labRoomName = roomName.ifBlank { computer.labRoom }

            val matchesSearch =
                query.isBlank() ||
                        computer.pcName.lowercase().contains(query) ||
                        computer.labRoom.lowercase().contains(query) ||
                        roomName.lowercase().contains(query) ||
                        computer.locationNote.lowercase().contains(query) ||
                        computer.ipAddress.lowercase().contains(query) ||
                        computer.status.lowercase().contains(query) ||
                        computer.remarks.lowercase().contains(query)

            val matchesStatus =
                selectedStatus == "All" ||
                        computer.status.equals(selectedStatus, ignoreCase = true)

            val matchesLabRoom =
                selectedLabRoom == "All" ||
                        labRoomName.equals(selectedLabRoom, ignoreCase = true)

            matchesSearch && matchesStatus && matchesLabRoom
        }
        .let { list ->
            when (selectedSort) {
                "PC Name Z-A" -> list.sortedByDescending { it.pcName.lowercase() }

                "Room A-Z" -> list.sortedWith(
                    compareBy<LabComputer> {
                        val roomName = roomById[it.roomId]?.name.orEmpty()
                        roomName.ifBlank { it.labRoom }.lowercase()
                    }.thenBy { it.pcName.lowercase() }
                )

                "Status" -> list.sortedWith(
                    compareBy<LabComputer> { labStatusOrder(it.status) }
                        .thenBy { it.pcName.lowercase() }
                )

                "Last Checked Newest" -> list.sortedByDescending { it.lastCheckedAt }

                "Last Checked Oldest" -> list.sortedBy { it.lastCheckedAt }

                else -> list.sortedBy { it.pcName.lowercase() }
            }
        }

    val totalCount = computerList.size
    val activeCount = computerList.count { it.status.equals("Active", ignoreCase = true) }
    val maintenanceCount = computerList.count { it.status.equals("Maintenance", ignoreCase = true) }
    val problematicCount = computerList.count { it.status.equals("Problematic", ignoreCase = true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LabManageColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(
                            color = LabManageColors.CardWhite,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        )
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
                        text = "Search, filter, sort and manage computers",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabManageColors.TextMuted
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
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
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ModernSummaryCard(
                    title = "Maintenance",
                    value = maintenanceCount.toString(),
                    bgColor = LabManageColors.OrangeLight,
                    textColor = LabManageColors.OrangeText,
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

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                shape = RoundedCornerShape(14.dp),
                placeholder = {
                    Text(
                        text = "Search PC, room, IP, location, status or remarks",
                        color = LabManageColors.TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = LabManageColors.TextMuted
                    )
                },
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

            Spacer(modifier = Modifier.height(12.dp))

            LabFilterTitle("Status")

            LabHorizontalFilterRow {
                statusFilters.forEach { filter ->
                    LabFilterChip(
                        text = filter,
                        selected = selectedStatus == filter,
                        onClick = {
                            selectedStatus = filter
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LabFilterTitle("Room / Lab")

            LabHorizontalFilterRow {
                labRoomFilterList.forEach { labRoom ->
                    LabFilterChip(
                        text = labRoom,
                        selected = selectedLabRoom == labRoom,
                        onClick = {
                            selectedLabRoom = labRoom
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LabFilterTitle("Sort")

            LabHorizontalFilterRow {
                sortFilters.forEach { sort ->
                    LabFilterChip(
                        text = sort,
                        selected = selectedSort == sort,
                        onClick = {
                            selectedSort = sort
                        }
                    )
                }
            }

            if (
                searchQuery.isNotBlank() ||
                selectedStatus != "All" ||
                selectedLabRoom != "All" ||
                selectedSort != "PC Name A-Z"
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        searchQuery = ""
                        selectedStatus = "All"
                        selectedLabRoom = "All"
                        selectedSort = "PC Name A-Z"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Clear Search, Filters and Sort")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddComputerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = LabManageColors.PrimaryIndigo.copy(alpha = 0.4f)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    LabManageColors.PrimaryIndigo,
                                    LabManageColors.PurpleAccent
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.AddCircle,
                            contentDescription = null,
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Add New Lab Computer",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Showing ${filteredList.size} of ${computerList.size} lab PC(s)",
                style = MaterialTheme.typography.titleMedium,
                color = LabManageColors.TextDark,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (computerList.isEmpty()) {
                            "No lab computers found."
                        } else {
                            "No matching lab computers found."
                        },
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
                            room = roomById[computer.roomId],
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
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LabManageColors.CardWhite),
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
                fontSize = 22.sp,
                color = textColor,
                fontWeight = FontWeight.Black
            )

            Text(
                text = title,
                color = textColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun LabFilterTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = LabManageColors.TextMuted,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun LabHorizontalFilterRow(
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
private fun LabFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LabManageColors.PrimaryIndigo,
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

@Composable
private fun ModernComputerCard(
    computer: LabComputer,
    room: Room?,
    onEdit: (LabComputer) -> Unit,
    onSoftware: (LabComputer) -> Unit,
    onReports: (LabComputer) -> Unit
) {
    val displayRoomName = room?.name.orEmpty().ifBlank {
        computer.labRoom.ifBlank { "N/A" }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LabManageColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LabManageColors.ModernBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = LabManageColors.PrimaryIndigo
                        )
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
                            text = "Room: $displayRoomName",
                            style = MaterialTheme.typography.bodySmall,
                            color = LabManageColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                ModernStatusBadge(text = computer.status.ifBlank { "Unknown" })
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = LabManageColors.ModernBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = LabManageColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = computer.locationNote.ifBlank { "No location note" },
                            style = MaterialTheme.typography.bodySmall,
                            color = LabManageColors.TextDark
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Dns,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = LabManageColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = computer.ipAddress.ifBlank { "No IP address" },
                            style = MaterialTheme.typography.bodySmall,
                            color = LabManageColors.TextDark
                        )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModernActionBtn(
                    text = "Edit",
                    modifier = Modifier.weight(1f),
                    isPrimary = true,
                    onClick = {
                        onEdit(computer)
                    }
                )

                ModernActionBtn(
                    text = "Software",
                    modifier = Modifier.weight(1f),
                    isPrimary = false,
                    onClick = {
                        onSoftware(computer)
                    }
                )

                ModernActionBtn(
                    text = "Reports",
                    modifier = Modifier.weight(1f),
                    isPrimary = false,
                    onClick = {
                        onReports(computer)
                    }
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
            colors = ButtonDefaults.buttonColors(
                containerColor = LabManageColors.PrimaryIndigo
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(38.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = LabManageColors.TextDark
            ),
            border = BorderStroke(
                width = 1.dp,
                color = LabManageColors.ModernBg
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

private fun labStatusOrder(status: String): Int {
    return when (status.trim().lowercase()) {
        "active" -> 0
        "maintenance" -> 1
        "problematic" -> 2
        else -> 3
    }
}