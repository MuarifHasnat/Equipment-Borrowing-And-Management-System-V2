package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.data.model.Room
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

private object LowStockColors {
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
            "Low" -> equipment.availableQuantity in 1..2
            "Out" -> equipment.availableQuantity == 0
            "Available" -> equipment.availableQuantity > 0
            "Borrow" -> equipment.isBorrowable
            "Lab Only" -> !equipment.isBorrowable
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
        color = LowStockColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            LowStockTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LowMiniStat("Total", totalEquipment.toString(), LowStockColors.BlueLight, LowStockColors.BlueText, Modifier.weight(1f))
                LowMiniStat("Low", lowStockCount.toString(), LowStockColors.OrangeLight, LowStockColors.OrangeText, Modifier.weight(1f))
                LowMiniStat("Out", outOfStockCount.toString(), LowStockColors.RedLight, LowStockColors.RedText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            LowMiniStat(
                title = "Available Quantity",
                value = totalAvailableQuantity.toString(),
                bgColor = LowStockColors.GreenLight,
                textColor = LowStockColors.GreenText,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = LowStockColors.TextMuted
                    )
                },
                placeholder = {
                    Text(
                        text = "Search equipment",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Low Stock", "Out of Stock", "Available", "Borrowable", "Lab-use-only", "All").forEach { filter ->
                    LowFilterChip(
                        text = filter,
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Showing ${reportList.size} item(s)",
                color = LowStockColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (reportList.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LowStockColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No equipment found.",
                            color = LowStockColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(reportList) { equipment ->
                        LowStockItemCard(
                            equipment = equipment,
                            room = roomById[equipment.roomId]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LowStockTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(LowStockColors.Card, RoundedCornerShape(15.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = LowStockColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(LowStockColors.OrangeLight, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.WarningAmber,
                contentDescription = null,
                tint = LowStockColors.OrangeText,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Low Stock Report",
                color = LowStockColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "Review stock status",
                color = LowStockColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LowMiniStat(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(52.dp),
        color = bgColor,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun LowFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LowStockColors.Primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text = text, maxLines = 1)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text = text, maxLines = 1)
        }
    }
}

@Composable
private fun LowStockItemCard(
    equipment: Equipment,
    room: Room?
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(equipment.imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(equipment.imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(equipment.imageUrl)

    val badgeBg = when {
        equipment.availableQuantity <= 0 -> LowStockColors.RedLight
        equipment.availableQuantity <= 2 -> LowStockColors.OrangeLight
        else -> LowStockColors.GreenLight
    }

    val badgeText = when {
        equipment.availableQuantity <= 0 -> LowStockColors.RedText
        equipment.availableQuantity <= 2 -> LowStockColors.OrangeText
        else -> LowStockColors.GreenText
    }

    val badgeLabel = when {
        equipment.availableQuantity <= 0 -> "Out"
        equipment.availableQuantity <= 2 -> "Low"
        else -> "Available"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = LowStockColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LowStockColors.GrayLight),
                contentAlignment = Alignment.Center
            ) {
                if (hasImageUrl) {
                    AsyncImage(
                        model = safeImageUrl,
                        contentDescription = equipment.name,
                        placeholder = painterResource(id = fallbackImageResId),
                        error = painterResource(id = fallbackImageResId),
                        fallback = painterResource(id = fallbackImageResId),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = fallbackImageResId),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(7.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = equipment.name.ifBlank { "Unknown Equipment" },
                        modifier = Modifier.weight(1f),
                        color = LowStockColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = badgeLabel,
                        modifier = Modifier
                            .background(badgeBg, RoundedCornerShape(50.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "${equipment.category.ifBlank { "General" }} • ${equipment.condition.ifBlank { "N/A" }}",
                    color = LowStockColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Room: ${room?.name ?: "N/A"}",
                    color = LowStockColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Stock ${equipment.availableQuantity}/${equipment.totalQuantity}",
                        modifier = Modifier
                            .background(LowStockColors.BlueLight, RoundedCornerShape(50.dp))
                            .padding(horizontal = 9.dp, vertical = 5.dp),
                        color = LowStockColors.BlueText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (equipment.isBorrowable) "Borrowable" else "Lab-use-only",
                        modifier = Modifier
                            .background(
                                if (equipment.isBorrowable) LowStockColors.PurpleLight else LowStockColors.GrayLight,
                                RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 5.dp),
                        color = if (equipment.isBorrowable) LowStockColors.PurpleText else LowStockColors.GrayText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
