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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.MeetingRoom
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.data.model.Room

private object RoomWiseColors {
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
    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
}

private data class RoomGroupUi(
    val room: Room?,
    val roomId: String,
    val equipment: List<Equipment>
)

@Composable
fun RoomWiseEquipmentReportScreen(
    roomList: List<Room>,
    equipmentList: List<Equipment>,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStock by remember { mutableStateOf("All") }

    val roomById = remember(roomList) { roomList.associateBy { it.id } }

    val groups = equipmentList
        .groupBy { it.roomId }
        .map { entry ->
            RoomGroupUi(
                room = roomById[entry.key],
                roomId = entry.key,
                equipment = entry.value.sortedBy { it.name.lowercase() }
            )
        }
        .sortedBy { it.room?.name ?: "Unknown Room" }

    val filteredGroups = groups.mapNotNull { group ->
        val filteredEquipment = group.equipment.filter { equipment ->
            val query = searchText.trim().lowercase()
            val room = group.room

            val matchesSearch =
                query.isBlank() ||
                        equipment.name.lowercase().contains(query) ||
                        equipment.category.lowercase().contains(query) ||
                        equipment.condition.lowercase().contains(query) ||
                        room?.name.orEmpty().lowercase().contains(query) ||
                        room?.department.orEmpty().lowercase().contains(query) ||
                        room?.building.orEmpty().lowercase().contains(query)

            val matchesStock = when (selectedStock) {
                "Available" -> equipment.availableQuantity > 2
                "Low Stock" -> equipment.availableQuantity in 1..2
                "Out of Stock" -> equipment.availableQuantity <= 0
                else -> true
            }

            matchesSearch && matchesStock
        }

        if (filteredEquipment.isEmpty()) null else group.copy(equipment = filteredEquipment)
    }

    val lowStockCount = equipmentList.count { it.availableQuantity in 1..2 }
    val availableQty = equipmentList.sumOf { it.availableQuantity }

    Surface(modifier = Modifier.fillMaxSize(), color = RoomWiseColors.Bg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            RoomWiseTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoomWiseMiniStat("Rooms", roomList.size.toString(), RoomWiseColors.BlueLight, RoomWiseColors.BlueText, Modifier.weight(1f))
                RoomWiseMiniStat("Items", equipmentList.size.toString(), RoomWiseColors.PurpleLight, RoomWiseColors.PurpleText, Modifier.weight(1f))
                RoomWiseMiniStat("Low", lowStockCount.toString(), RoomWiseColors.OrangeLight, RoomWiseColors.OrangeText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            RoomWiseMiniStat("Available Quantity", availableQty.toString(), RoomWiseColors.GreenLight, RoomWiseColors.GreenText, Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = RoomWiseColors.TextMuted)
                },
                placeholder = {
                    Text(
                        text = "Search room/equipment",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Available", "Low", "Out").forEach { filter ->
                    val actualFilter = when (filter) {
                        "Low" -> "Low Stock"
                        "Out" -> "Out of Stock"
                        else -> filter
                    }

                    RoomWiseFilterChip(
                        text = filter,
                        selected = selectedStock == actualFilter,
                        onClick = {
                            selectedStock = actualFilter
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Showing ${filteredGroups.size} room group(s)",
                color = RoomWiseColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredGroups.isEmpty()) {
                RoomWiseEmptyCard("No room-wise equipment found.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(filteredGroups) { group ->
                        RoomWiseGroupCard(group)
                    }
                }
            }
        }
    }
}

@Composable
private fun RoomWiseTopBar(onBackClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(RoomWiseColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = RoomWiseColors.TextDark)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(RoomWiseColors.BlueLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.MeetingRoom, contentDescription = null, tint = RoomWiseColors.BlueText, modifier = Modifier.size(23.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Room Equipment",
                color = RoomWiseColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Grouped by room",
                color = RoomWiseColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RoomWiseGroupCard(group: RoomGroupUi) {
    val roomName = group.room?.name ?: "Unknown Room"
    val department = group.room?.department ?: "Department N/A"
    val totalQty = group.equipment.sumOf { it.totalQuantity }
    val availableQty = group.equipment.sumOf { it.availableQuantity }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RoomWiseColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(RoomWiseColors.BlueLight, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.MeetingRoom, contentDescription = null, tint = RoomWiseColors.BlueText, modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(roomName, color = RoomWiseColors.TextDark, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(department, color = RoomWiseColors.TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                }

                Text(
                    text = "${group.equipment.size} item(s)",
                    modifier = Modifier
                        .background(RoomWiseColors.PurpleLight, RoundedCornerShape(50.dp))
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    color = RoomWiseColors.PurpleText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = RoomWiseColors.Bg)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RoomWiseInfoPill("Total Qty", totalQty.toString(), RoomWiseColors.BlueLight, RoomWiseColors.BlueText, Modifier.weight(1f))
                RoomWiseInfoPill("Available", availableQty.toString(), RoomWiseColors.GreenLight, RoomWiseColors.GreenText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            group.equipment.take(3).forEach { equipment ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Inventory2, contentDescription = null, tint = RoomWiseColors.TextMuted, modifier = Modifier.size(16.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = equipment.name.ifBlank { "Unknown Equipment" },
                        modifier = Modifier.weight(1f),
                        color = RoomWiseColors.TextDark,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${equipment.availableQuantity}/${equipment.totalQuantity}",
                        color = if (equipment.availableQuantity <= 2) RoomWiseColors.OrangeText else RoomWiseColors.GreenText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Fixed logic to match take(3)
            if (group.equipment.size > 3) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("+ ${group.equipment.size - 3} more item(s)", color = RoomWiseColors.TextMuted, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RoomWiseMiniStat(title: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.height(52.dp), color = bgColor, shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = textColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(value, color = textColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1)
        }
    }
}

@Composable
private fun RoomWiseInfoPill(title: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    // Fixed: Removed hardcoded height(38.dp) so it can wrap contents dynamically
    Surface(modifier = modifier, color = bgColor, shape = RoundedCornerShape(14.dp)) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = textColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = textColor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun RoomWiseFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoomWiseColors.Primary, contentColor = Color.White),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
private fun RoomWiseEmptyCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RoomWiseColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(message, color = RoomWiseColors.TextMuted, fontWeight = FontWeight.SemiBold)
        }
    }
}