package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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

private object LowStockColors {
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

    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun LowStockReportScreen(
    roomList: List<Room>,
    equipmentList: List<Equipment>,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Low Stock") }

    val roomById = remember(roomList) {
        roomList.associateBy { it.id }
    }

    val reportList = equipmentList.filter { equipment ->
        val query = searchText.trim().lowercase()
        val room = roomById[equipment.roomId]

        val matchesSearch =
            query.isBlank() ||
                    equipment.name.lowercase().contains(query) ||
                    equipment.category.lowercase().contains(query) ||
                    equipment.condition.lowercase().contains(query) ||
                    room?.name.orEmpty().lowercase().contains(query) ||
                    room?.department.orEmpty().lowercase().contains(query) ||
                    room?.building.orEmpty().lowercase().contains(query)

        val matchesFilter = when (selectedFilter) {
            "Low Stock" -> equipment.availableQuantity in 1..2
            "Out of Stock" -> equipment.availableQuantity == 0
            "Available" -> equipment.availableQuantity > 0
            "Borrowable" -> equipment.isBorrowable
            "Lab-use-only" -> !equipment.isBorrowable
            else -> true
        }

        matchesSearch && matchesFilter
    }.sortedWith(
        compareBy<Equipment> { it.availableQuantity }
            .thenBy { it.name.lowercase() }
    )

    val totalEquipment = equipmentList.size
    val lowStockCount = equipmentList.count { it.availableQuantity in 1..2 }
    val outOfStockCount = equipmentList.count { it.availableQuantity == 0 }
    val totalAvailableQuantity = equipmentList.sumOf { it.availableQuantity }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LowStockColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            LowStockHeroCard(
                lowStockCount = lowStockCount,
                outOfStockCount = outOfStockCount
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LowStockStatCard(
                    title = "Equipment",
                    value = totalEquipment.toString(),
                    bgColor = LowStockColors.BlueLight,
                    textColor = LowStockColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                LowStockStatCard(
                    title = "Available Qty",
                    value = totalAvailableQuantity.toString(),
                    bgColor = LowStockColors.GreenLight,
                    textColor = LowStockColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LowStockStatCard(
                    title = "Low Stock",
                    value = lowStockCount.toString(),
                    bgColor = LowStockColors.OrangeLight,
                    textColor = LowStockColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )

                LowStockStatCard(
                    title = "Out Stock",
                    value = outOfStockCount.toString(),
                    bgColor = LowStockColors.RedLight,
                    textColor = LowStockColors.RedText,
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
                    Text("Search equipment, category, condition or room")
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
                    "Low Stock",
                    "Out of Stock",
                    "Available",
                    "Borrowable",
                    "Lab-use-only",
                    "All"
                ).forEach { filter ->
                    LowStockFilterChip(
                        text = filter,
                        selected = selectedFilter == filter,
                        onClick = {
                            selectedFilter = filter
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Low Stock Equipment Report",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = LowStockColors.TextDark
            )

            Text(
                text = "Showing ${reportList.size} equipment record(s)",
                style = MaterialTheme.typography.bodySmall,
                color = LowStockColors.TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (reportList.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = LowStockColors.CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No equipment found for selected search/filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LowStockColors.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reportList) { equipment ->
                        LowStockEquipmentCard(
                            equipment = equipment,
                            room = roomById[equipment.roomId]
                        )
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
private fun LowStockHeroCard(
    lowStockCount: Int,
    outOfStockCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        LowStockColors.PrimaryIndigo,
                        LowStockColors.PurpleAccent
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
                    text = "Inventory Report",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Low Stock Equipment",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$lowStockCount low stock item(s), $outOfStockCount out of stock item(s)",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun LowStockStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LowStockColors.CardWhite),
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
                color = LowStockColors.TextMuted,
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
private fun LowStockEquipmentCard(
    equipment: Equipment,
    room: Room?
) {
    val statusText = when {
        equipment.availableQuantity == 0 -> "Out of Stock"
        equipment.availableQuantity in 1..2 -> "Low Stock"
        else -> "Available"
    }

    val statusBg = when {
        equipment.availableQuantity == 0 -> LowStockColors.RedLight
        equipment.availableQuantity in 1..2 -> LowStockColors.OrangeLight
        else -> LowStockColors.GreenLight
    }

    val statusColor = when {
        equipment.availableQuantity == 0 -> LowStockColors.RedText
        equipment.availableQuantity in 1..2 -> LowStockColors.OrangeText
        else -> LowStockColors.GreenText
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = LowStockColors.CardWhite),
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
                        text = equipment.name.ifBlank { "Unnamed Equipment" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = LowStockColors.TextDark
                    )

                    Text(
                        text = equipment.category.ifBlank { "No category" },
                        style = MaterialTheme.typography.bodySmall,
                        color = LowStockColors.TextMuted
                    )
                }

                Text(
                    text = statusText,
                    modifier = Modifier
                        .background(
                            color = statusBg,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            HorizontalDivider(color = LowStockColors.Background)

            LowStockInfoRow(
                label = "Room/Lab",
                value = room?.name?.ifBlank { "Unnamed Room" } ?: "No room assigned"
            )

            LowStockInfoRow(
                label = "Department",
                value = room?.department?.ifBlank { "N/A" } ?: "N/A"
            )

            LowStockInfoRow(
                label = "Condition",
                value = equipment.condition.ifBlank { "N/A" }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LowStockSmallBadge(
                    text = "Total: ${equipment.totalQuantity}",
                    bgColor = LowStockColors.BlueLight,
                    textColor = LowStockColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                LowStockSmallBadge(
                    text = "Available: ${equipment.availableQuantity}",
                    bgColor = statusBg,
                    textColor = statusColor,
                    modifier = Modifier.weight(1f)
                )
            }

            LowStockSmallBadge(
                text = if (equipment.isBorrowable) {
                    "Borrowable Item"
                } else {
                    "Lab-use-only Item"
                },
                bgColor = if (equipment.isBorrowable) {
                    LowStockColors.GreenLight
                } else {
                    LowStockColors.PurpleLight
                },
                textColor = if (equipment.isBorrowable) {
                    LowStockColors.GreenText
                } else {
                    LowStockColors.PurpleText
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LowStockInfoRow(
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
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = LowStockColors.TextMuted
        )

        Text(
            text = value.ifBlank { "N/A" },
            modifier = Modifier.weight(0.62f),
            style = MaterialTheme.typography.bodySmall,
            color = LowStockColors.TextDark,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LowStockSmallBadge(
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
private fun LowStockFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LowStockColors.BlueText,
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