package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.horizontalScroll
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
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.data.model.Room

private object RoomReportColors {
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
}

private data class RoomEquipmentGroup(
    val room: Room?,
    val roomId: String,
    val equipmentList: List<Equipment>
)

@Composable
fun RoomWiseEquipmentReportScreen(
    roomList: List<Room>,
    equipmentList: List<Equipment>,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStockFilter by remember { mutableStateOf("All") }

    val roomById = remember(roomList) {
        roomList.associateBy { it.id }
    }

    val roomGroups = remember(roomList, equipmentList) {
        val equipmentByRoom = equipmentList.groupBy { it.roomId }

        val knownRoomGroups = roomList
            .sortedBy { it.name.lowercase() }
            .map { room ->
                RoomEquipmentGroup(
                    room = room,
                    roomId = room.id,
                    equipmentList = equipmentByRoom[room.id].orEmpty()
                )
            }

        val unknownRoomGroups = equipmentByRoom
            .filterKeys { roomId ->
                roomId.isBlank() || roomById[roomId] == null
            }
            .map { entry ->
                RoomEquipmentGroup(
                    room = null,
                    roomId = entry.key,
                    equipmentList = entry.value
                )
            }

        knownRoomGroups + unknownRoomGroups
    }

    val filteredGroups = roomGroups.mapNotNull { group ->
        val filteredEquipment = group.equipmentList.filter { equipment ->
            val query = searchText.trim().lowercase()

            val roomName = group.room?.name.orEmpty()
            val department = group.room?.department.orEmpty()
            val building = group.room?.building.orEmpty()

            val matchesSearch =
                query.isBlank() ||
                        equipment.name.lowercase().contains(query) ||
                        equipment.category.lowercase().contains(query) ||
                        equipment.condition.lowercase().contains(query) ||
                        roomName.lowercase().contains(query) ||
                        department.lowercase().contains(query) ||
                        building.lowercase().contains(query)

            val matchesStock = when (selectedStockFilter) {
                "Available" -> equipment.availableQuantity > 0
                "Out of Stock" -> equipment.availableQuantity == 0
                "Low Stock" -> equipment.availableQuantity in 1..2
                "Borrowable" -> equipment.isBorrowable
                "Lab-use-only" -> !equipment.isBorrowable
                else -> true
            }

            matchesSearch && matchesStock
        }

        val roomMatchesSearch =
            searchText.isBlank() ||
                    group.room?.name.orEmpty().lowercase().contains(searchText.lowercase()) ||
                    group.room?.department.orEmpty().lowercase().contains(searchText.lowercase()) ||
                    group.room?.building.orEmpty().lowercase().contains(searchText.lowercase())

        if (filteredEquipment.isNotEmpty() || roomMatchesSearch) {
            group.copy(equipmentList = filteredEquipment)
        } else {
            null
        }
    }

    val totalQuantity = equipmentList.sumOf { it.totalQuantity }
    val totalAvailable = equipmentList.sumOf { it.availableQuantity }
    val lowStockCount = equipmentList.count { it.availableQuantity in 1..2 }
    val outOfStockCount = equipmentList.count { it.availableQuantity == 0 }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = RoomReportColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            RoomReportHeroCard(
                totalRooms = roomList.size,
                totalEquipment = equipmentList.size
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoomReportStatCard(
                    title = "Total Qty",
                    value = totalQuantity.toString(),
                    bgColor = RoomReportColors.BlueLight,
                    textColor = RoomReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                RoomReportStatCard(
                    title = "Available",
                    value = totalAvailable.toString(),
                    bgColor = RoomReportColors.GreenLight,
                    textColor = RoomReportColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoomReportStatCard(
                    title = "Low Stock",
                    value = lowStockCount.toString(),
                    bgColor = RoomReportColors.OrangeLight,
                    textColor = RoomReportColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )

                RoomReportStatCard(
                    title = "Out Stock",
                    value = outOfStockCount.toString(),
                    bgColor = RoomReportColors.RedLight,
                    textColor = RoomReportColors.RedText,
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
                    Text("Search room, department, equipment or category")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "All",
                    "Available",
                    "Low Stock",
                    "Out of Stock",
                    "Borrowable",
                    "Lab-use-only"
                ).forEach { filter ->
                    RoomReportFilterChip(
                        text = filter,
                        selected = selectedStockFilter == filter,
                        onClick = {
                            selectedStockFilter = filter
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Room-wise Equipment List",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = RoomReportColors.TextDark
            )

            Text(
                text = "Showing ${filteredGroups.size} room group(s)",
                style = MaterialTheme.typography.bodySmall,
                color = RoomReportColors.TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredGroups.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = RoomReportColors.CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No equipment report found for selected search/filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RoomReportColors.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredGroups) { group ->
                        RoomEquipmentGroupCard(group = group)
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
private fun RoomReportHeroCard(
    totalRooms: Int,
    totalEquipment: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        RoomReportColors.PrimaryIndigo,
                        RoomReportColors.PurpleAccent
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
                    text = "Room-wise Report",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Equipment by Room / Lab",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$totalRooms room(s), $totalEquipment equipment record(s)",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun RoomReportStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = RoomReportColors.CardWhite),
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
                color = RoomReportColors.TextMuted,
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
private fun RoomEquipmentGroupCard(
    group: RoomEquipmentGroup
) {
    val room = group.room
    val roomTitle = room?.name?.ifBlank { "Unnamed Room" }
        ?: if (group.roomId.isBlank()) "No Room Assigned" else "Unknown Room"

    val totalQty = group.equipmentList.sumOf { it.totalQuantity }
    val availableQty = group.equipmentList.sumOf { it.availableQuantity }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RoomReportColors.CardWhite),
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
                        text = roomTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoomReportColors.TextDark
                    )

                    Text(
                        text = buildString {
                            append(room?.roomType?.ifBlank { "Room" } ?: "Room")
                            if (!room?.department.isNullOrBlank()) {
                                append(" • ${room?.department}")
                            }
                            if (!room?.building.isNullOrBlank()) {
                                append(" • ${room?.building}")
                            }
                            if (!room?.floor.isNullOrBlank()) {
                                append(" • Floor ${room?.floor}")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = RoomReportColors.TextMuted
                    )
                }

                Text(
                    text = "${group.equipmentList.size} item(s)",
                    modifier = Modifier
                        .background(
                            color = RoomReportColors.BlueLight,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = RoomReportColors.BlueText
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SmallSummaryBadge(
                    text = "Total Qty: $totalQty",
                    bgColor = RoomReportColors.PurpleLight,
                    textColor = RoomReportColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )

                SmallSummaryBadge(
                    text = "Available: $availableQty",
                    bgColor = RoomReportColors.GreenLight,
                    textColor = RoomReportColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(color = RoomReportColors.Background)

            if (group.equipmentList.isEmpty()) {
                Text(
                    text = "No equipment assigned to this room.",
                    style = MaterialTheme.typography.bodySmall,
                    color = RoomReportColors.TextMuted
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.equipmentList
                        .sortedBy { it.name.lowercase() }
                        .forEach { equipment ->
                            EquipmentReportRow(equipment = equipment)
                        }
                }
            }
        }
    }
}

@Composable
private fun EquipmentReportRow(
    equipment: Equipment
) {
    val stockColor = when {
        equipment.availableQuantity == 0 -> RoomReportColors.RedText
        equipment.availableQuantity in 1..2 -> RoomReportColors.OrangeText
        else -> RoomReportColors.GreenText
    }

    val stockBg = when {
        equipment.availableQuantity == 0 -> RoomReportColors.RedLight
        equipment.availableQuantity in 1..2 -> RoomReportColors.OrangeLight
        else -> RoomReportColors.GreenLight
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = RoomReportColors.Background,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = equipment.name.ifBlank { "Unnamed Equipment" },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = RoomReportColors.TextDark
                )

                Text(
                    text = equipment.category.ifBlank { "No category" },
                    style = MaterialTheme.typography.bodySmall,
                    color = RoomReportColors.TextMuted
                )
            }

            Text(
                text = if (equipment.isBorrowable) "Borrowable" else "Lab-use-only",
                modifier = Modifier
                    .background(
                        color = if (equipment.isBorrowable) RoomReportColors.GreenLight else RoomReportColors.PurpleLight,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (equipment.isBorrowable) RoomReportColors.GreenText else RoomReportColors.PurpleText
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SmallSummaryBadge(
                text = "Total: ${equipment.totalQuantity}",
                bgColor = RoomReportColors.BlueLight,
                textColor = RoomReportColors.BlueText,
                modifier = Modifier.weight(1f)
            )

            SmallSummaryBadge(
                text = "Available: ${equipment.availableQuantity}",
                bgColor = stockBg,
                textColor = stockColor,
                modifier = Modifier.weight(1f)
            )
        }

        if (equipment.condition.isNotBlank()) {
            Text(
                text = "Condition: ${equipment.condition}",
                style = MaterialTheme.typography.bodySmall,
                color = RoomReportColors.TextMuted
            )
        }

        if (equipment.assetTag.isNotBlank() || equipment.serialNumber.isNotBlank()) {
            Text(
                text = buildString {
                    if (equipment.assetTag.isNotBlank()) {
                        append("Asset: ${equipment.assetTag}")
                    }
                    if (equipment.serialNumber.isNotBlank()) {
                        if (isNotBlank()) append(" • ")
                        append("Serial: ${equipment.serialNumber}")
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = RoomReportColors.TextMuted
            )
        }
    }
}

@Composable
private fun SmallSummaryBadge(
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
private fun RoomReportFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RoomReportColors.BlueText,
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