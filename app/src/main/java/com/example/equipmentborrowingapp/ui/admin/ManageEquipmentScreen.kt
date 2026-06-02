package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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

private object ManageEquipColors {
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
fun ManageEquipmentScreen(
    equipmentList: List<Equipment>,
    onEditClick: (Equipment) -> Unit,
    onBackClick: () -> Unit,
    roomList: List<Room> = emptyList()
) {
    var searchText by remember { mutableStateOf("") }
    var selectedRoomId by remember { mutableStateOf("All") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedStock by remember { mutableStateOf("All") }
    var selectedBorrowType by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Name A-Z") }

    val roomById = remember(roomList) {
        roomList.associateBy { it.id }
    }

    val categoryList = remember(equipmentList) {
        listOf("All") + equipmentList
            .map { it.category.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    val roomFilterList = remember(roomList, equipmentList) {
        val usedRoomIds = equipmentList.map { it.roomId }.filter { it.isNotBlank() }.distinct()
        listOf("All") + roomList.filter { it.id in usedRoomIds }.map { it.id }
    }
    val filteredEquipment = filterAndSortAdminEquipment(
        equipmentList = equipmentList,
        roomById = roomById,
        searchText = searchText,
        selectedRoomId = selectedRoomId,
        selectedCategory = selectedCategory,
        selectedStock = selectedStock,
        selectedBorrowType = selectedBorrowType,
        selectedSort = selectedSort
    )


    val availableCount = equipmentList.count { it.availableQuantity > 0 }
    val lowStockCount = equipmentList.count { it.availableQuantity in 1..2 }
    val outOfStockCount = equipmentList.count { it.availableQuantity <= 0 }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ManageEquipColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            ManageEquipmentTopBar(
                totalCount = equipmentList.size,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EquipmentMiniStat(
                    title = "Available",
                    value = availableCount.toString(),
                    bgColor = ManageEquipColors.GreenLight,
                    textColor = ManageEquipColors.GreenText,
                    modifier = Modifier.weight(1f)
                )

                EquipmentMiniStat(
                    title = "Low",
                    value = lowStockCount.toString(),
                    bgColor = ManageEquipColors.OrangeLight,
                    textColor = ManageEquipColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )

                EquipmentMiniStat(
                    title = "Out",
                    value = outOfStockCount.toString(),
                    bgColor = ManageEquipColors.RedLight,
                    textColor = ManageEquipColors.RedText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompactEquipmentSearchBox(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1.35f)
                )

                CompactEquipmentDropdown(
                    label = "Stock",
                    selectedText = selectedStock,
                    options = listOf("All", "Available", "Low Stock", "Out of Stock"),
                    onOptionSelected = { selectedStock = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompactEquipmentDropdown(
                    label = "Category",
                    selectedText = selectedCategory,
                    options = categoryList,
                    onOptionSelected = { selectedCategory = it },
                    modifier = Modifier.weight(1f)
                )

                CompactEquipmentDropdown(
                    label = "Room",
                    selectedText = if (selectedRoomId == "All") "All Rooms" else roomById[selectedRoomId]?.name ?: "Unknown",
                    options = roomFilterList.map { roomId ->
                        if (roomId == "All") "All Rooms" else roomById[roomId]?.name ?: "Unknown Room"
                    },
                    onOptionSelected = { selectedLabel ->
                        selectedRoomId = if (selectedLabel == "All Rooms") {
                            "All"
                        } else {
                            roomFilterList.firstOrNull { roomId -> roomById[roomId]?.name == selectedLabel } ?: "All"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompactEquipmentDropdown(
                    label = "Type",
                    selectedText = selectedBorrowType,
                    options = listOf("All", "Borrowable", "Lab-use-only"),
                    onOptionSelected = { selectedBorrowType = it },
                    modifier = Modifier.weight(1f)
                )

                CompactEquipmentDropdown(
                    label = "Sort",
                    selectedText = selectedSort,
                    options = listOf("Name A-Z", "Name Z-A", "Category A-Z", "Stock Low-High", "Stock High-Low"),
                    onOptionSelected = { selectedSort = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Showing ${filteredEquipment.size} of ${equipmentList.size} item(s)",
                color = ManageEquipColors.TextDark,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (filteredEquipment.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ManageEquipColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No equipment found.",
                            color = ManageEquipColors.TextMuted,
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
                    items(filteredEquipment) { equipment ->
                        EquipmentCard(
                            equipment = equipment,
                            room = roomById[equipment.roomId],
                            onEditClick = { onEditClick(equipment) }
                        )
                    }
                }
            }
        }
    }
}
private fun filterAndSortAdminEquipment(
    equipmentList: List<Equipment>,
    roomById: Map<String, Room>,
    searchText: String,
    selectedRoomId: String,
    selectedCategory: String,
    selectedStock: String,
    selectedBorrowType: String,
    selectedSort: String
): List<Equipment> {
    val query = searchText.trim().lowercase()

    val filteredList = equipmentList.filter { equipment ->
        val room = roomById[equipment.roomId]

        val matchesSearch =
            query.isBlank() ||
                    equipment.name.lowercase().contains(query) ||
                    equipment.category.lowercase().contains(query) ||
                    equipment.condition.lowercase().contains(query) ||
                    room?.name.orEmpty().lowercase().contains(query) ||
                    room?.department.orEmpty().lowercase().contains(query) ||
                    room?.building.orEmpty().lowercase().contains(query)

        val matchesRoom =
            selectedRoomId == "All" || equipment.roomId == selectedRoomId

        val matchesCategory =
            selectedCategory == "All" ||
                    equipment.category.equals(selectedCategory, ignoreCase = true)

        val matchesStock = when (selectedStock) {
            "Available" -> equipment.availableQuantity > 2
            "Low Stock" -> equipment.availableQuantity in 1..2
            "Out of Stock" -> equipment.availableQuantity <= 0
            else -> true
        }

        val isActuallyBorrowable =
            equipment.isBorrowable && equipment.borrowType != "LabUseOnly"

        val matchesBorrowType = when (selectedBorrowType) {
            "Borrowable" -> isActuallyBorrowable
            "Lab-use-only" -> !isActuallyBorrowable
            else -> true
        }

        matchesSearch && matchesRoom && matchesCategory && matchesStock && matchesBorrowType
    }

    return when (selectedSort) {
        "Name Z-A" -> filteredList.sortedByDescending { equipment ->
            equipment.name.lowercase()
        }

        "Category A-Z" -> filteredList.sortedBy { equipment ->
            equipment.category.lowercase()
        }

        "Stock Low-High" -> filteredList.sortedBy { equipment ->
            equipment.availableQuantity
        }

        "Stock High-Low" -> filteredList.sortedByDescending { equipment ->
            equipment.availableQuantity
        }

        else -> filteredList.sortedBy { equipment ->
            equipment.name.lowercase()
        }
    }
}
@Composable
private fun ManageEquipmentTopBar(
    totalCount: Int,
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
                .background(ManageEquipColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = ManageEquipColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(ManageEquipColors.PurpleLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Inventory2,
                contentDescription = null,
                tint = ManageEquipColors.Primary,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Manage Equipment",
                color = ManageEquipColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "$totalCount item(s) in inventory",
                color = ManageEquipColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun EquipmentMiniStat(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(52.dp),
        color = bgColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
        ) {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun CompactEquipmentSearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(42.dp),
        color = ManageEquipColors.Card,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = ManageEquipColors.TextMuted,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = ManageEquipColors.TextDark,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            text = "Search",
                            color = ManageEquipColors.TextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun CompactEquipmentDropdown(
    label: String,
    selectedText: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clickable { expanded = true },
            color = ManageEquipColors.Card,
            shape = RoundedCornerShape(14.dp),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        color = ManageEquipColors.TextMuted,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Text(
                        text = selectedText.ifBlank { "All" },
                        color = ManageEquipColors.TextDark,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "▼",
                    color = ManageEquipColors.TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.distinct().forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun HorizontalFilterRow(
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
private fun FilterChipButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(34.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ManageEquipColors.Primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text = text, maxLines = 1)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(34.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text = text, maxLines = 1)
        }
    }
}

@Composable
private fun EquipmentCard(
    equipment: Equipment,
    room: Room?,
    onEditClick: () -> Unit
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(equipment.imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(equipment.imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(equipment.imageUrl)

    val stockBg: Color
    val stockText: Color
    val stockLabel: String

    when {
        equipment.availableQuantity <= 0 -> {
            stockBg = ManageEquipColors.RedLight
            stockText = ManageEquipColors.RedText
            stockLabel = "Out"
        }
        equipment.availableQuantity <= 2 -> {
            stockBg = ManageEquipColors.OrangeLight
            stockText = ManageEquipColors.OrangeText
            stockLabel = "Low"
        }
        else -> {
            stockBg = ManageEquipColors.GreenLight
            stockText = ManageEquipColors.GreenText
            stockLabel = "Available"
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ManageEquipColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ManageEquipColors.GrayLight),
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
                                .padding(9.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = equipment.name.ifBlank { "Unknown Equipment" },
                            modifier = Modifier.weight(1f),
                            color = ManageEquipColors.TextDark,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = stockLabel,
                            modifier = Modifier
                                .background(stockBg, RoundedCornerShape(50.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = stockText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${equipment.category.ifBlank { "General" }} • ${equipment.condition.ifBlank { "N/A" }}",
                        color = ManageEquipColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Room: ${room?.name ?: "N/A"}",
                        color = ManageEquipColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Stock ${equipment.availableQuantity}/${equipment.totalQuantity}",
                            modifier = Modifier
                                .background(ManageEquipColors.BlueLight, RoundedCornerShape(50.dp))
                                .padding(horizontal = 9.dp, vertical = 5.dp),
                            color = ManageEquipColors.BlueText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )

                        val isActuallyBorrowable =
                            equipment.isBorrowable && equipment.borrowType != "LabUseOnly"

                        Text(
                            text = if (isActuallyBorrowable) "Borrowable" else "Lab-use-only",
                            modifier = Modifier
                                .background(
                                    if (isActuallyBorrowable) ManageEquipColors.PurpleLight else ManageEquipColors.GrayLight,
                                    RoundedCornerShape(50.dp)
                                )
                                .padding(horizontal = 9.dp, vertical = 5.dp),
                            color = if (isActuallyBorrowable) ManageEquipColors.PurpleText else ManageEquipColors.GrayText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = ManageEquipColors.Bg)
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ManageEquipColors.Primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "Edit Equipment", fontWeight = FontWeight.Bold)
            }
        }
    }
}
