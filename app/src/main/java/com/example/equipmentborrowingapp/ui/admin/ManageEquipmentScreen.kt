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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.data.model.Room
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

private object ManageColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PrimaryIndigoLight = Color(0xFFE0E7FF)

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

private fun getStockBadgeColors(
    available: Int,
    total: Int
): Pair<Color, Color> {
    return when {
        total <= 0 -> Pair(Color(0xFFF1F5F9), Color(0xFF64748B))
        available <= 0 -> Pair(ManageColors.RedLight, ManageColors.RedText)
        available <= 2 -> Pair(ManageColors.OrangeLight, ManageColors.OrangeText)
        else -> Pair(ManageColors.GreenLight, ManageColors.GreenText)
    }
}

private fun getStockText(
    available: Int,
    total: Int
): String {
    return when {
        total <= 0 -> "No Stock Data"
        available <= 0 -> "Out of Stock"
        available <= 2 -> "Low Stock"
        else -> "Available"
    }
}

@Composable
private fun StatusBadge(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(end = 8.dp)
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
private fun EquipmentCardImage(
    imageName: String,
    imageUrl: String,
    contentDescription: String
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val hasImageUrl = imageUrl.trim().isNotBlank()

    val modifier = Modifier
        .size(90.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(ManageColors.ModernBg)

    if (hasImageUrl) {
        AsyncImage(
            model = imageUrl.trim(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = fallbackImageResId),
            error = painterResource(id = fallbackImageResId),
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = fallbackImageResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier.padding(8.dp)
        )
    }
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
        val knownRooms = roomList
            .filter { it.id.isNotBlank() }
            .sortedBy { it.name.lowercase() }

        val unknownRoomIds = equipmentList
            .map { it.roomId }
            .filter { it.isNotBlank() && roomById[it] == null }
            .distinct()
            .sorted()

        listOf("All" to "All Rooms") +
                knownRooms.map { it.id to it.name.ifBlank { "Unnamed Room" } } +
                unknownRoomIds.map { it to "Unknown Room: $it" }
    }

    val filteredEquipment = equipmentList
        .filter { equipment ->
            val query = searchText.trim().lowercase()
            val room = roomById[equipment.roomId]

            val matchesSearch =
                query.isBlank() ||
                        equipment.name.lowercase().contains(query) ||
                        equipment.category.lowercase().contains(query) ||
                        equipment.condition.lowercase().contains(query) ||
                        equipment.description.lowercase().contains(query) ||
                        equipment.assetTag.lowercase().contains(query) ||
                        equipment.serialNumber.lowercase().contains(query) ||
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
                "Out of Stock" -> equipment.availableQuantity == 0
                else -> true
            }

            val matchesBorrowType = when (selectedBorrowType) {
                "Borrowable" -> equipment.isBorrowable
                "Lab-use-only" -> !equipment.isBorrowable
                else -> true
            }

            matchesSearch &&
                    matchesRoom &&
                    matchesCategory &&
                    matchesStock &&
                    matchesBorrowType
        }
        .let { list ->
            when (selectedSort) {
                "Name Z-A" -> list.sortedByDescending { it.name.lowercase() }
                "Available Low-High" -> list.sortedWith(
                    compareBy<Equipment> { it.availableQuantity }
                        .thenBy { it.name.lowercase() }
                )

                "Available High-Low" -> list.sortedWith(
                    compareByDescending<Equipment> { it.availableQuantity }
                        .thenBy { it.name.lowercase() }
                )

                "Total Qty High-Low" -> list.sortedWith(
                    compareByDescending<Equipment> { it.totalQuantity }
                        .thenBy { it.name.lowercase() }
                )

                else -> list.sortedBy { it.name.lowercase() }
            }
        }

    val totalEquipment = equipmentList.size
    val availableCount = equipmentList.count { it.availableQuantity > 0 }
    val lowStockCount = equipmentList.count { it.availableQuantity in 1..2 }
    val outOfStockCount = equipmentList.count { it.availableQuantity == 0 }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ManageColors.ModernBg
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
                        .background(ManageColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        )
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = ManageColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Manage Equipment",
                        style = MaterialTheme.typography.titleLarge,
                        color = ManageColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Search, filter, sort and edit inventory",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ManageColors.TextMuted
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SmallInventoryStatCard(
                    title = "Total",
                    value = totalEquipment.toString(),
                    bgColor = ManageColors.BlueLight,
                    textColor = ManageColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                SmallInventoryStatCard(
                    title = "Available",
                    value = availableCount.toString(),
                    bgColor = ManageColors.GreenLight,
                    textColor = ManageColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SmallInventoryStatCard(
                    title = "Low Stock",
                    value = lowStockCount.toString(),
                    bgColor = ManageColors.OrangeLight,
                    textColor = ManageColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )

                SmallInventoryStatCard(
                    title = "Out Stock",
                    value = outOfStockCount.toString(),
                    bgColor = ManageColors.RedLight,
                    textColor = ManageColors.RedText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text("Search equipment, category, room, asset or serial")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilterSectionTitle("Room / Lab")

            HorizontalFilterRow {
                roomFilterList.forEach { roomFilter ->
                    InventoryFilterChip(
                        text = roomFilter.second,
                        selected = selectedRoomId == roomFilter.first,
                        onClick = {
                            selectedRoomId = roomFilter.first
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            FilterSectionTitle("Category")

            HorizontalFilterRow {
                categoryList.forEach { category ->
                    InventoryFilterChip(
                        text = category,
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            FilterSectionTitle("Stock")

            HorizontalFilterRow {
                listOf(
                    "All",
                    "Available",
                    "Low Stock",
                    "Out of Stock"
                ).forEach { stock ->
                    InventoryFilterChip(
                        text = stock,
                        selected = selectedStock == stock,
                        onClick = {
                            selectedStock = stock
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            FilterSectionTitle("Borrow Type")

            HorizontalFilterRow {
                listOf(
                    "All",
                    "Borrowable",
                    "Lab-use-only"
                ).forEach { borrowType ->
                    InventoryFilterChip(
                        text = borrowType,
                        selected = selectedBorrowType == borrowType,
                        onClick = {
                            selectedBorrowType = borrowType
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            FilterSectionTitle("Sort")

            HorizontalFilterRow {
                listOf(
                    "Name A-Z",
                    "Name Z-A",
                    "Available Low-High",
                    "Available High-Low",
                    "Total Qty High-Low"
                ).forEach { sort ->
                    InventoryFilterChip(
                        text = sort,
                        selected = selectedSort == sort,
                        onClick = {
                            selectedSort = sort
                        }
                    )
                }
            }

            if (
                searchText.isNotBlank() ||
                selectedRoomId != "All" ||
                selectedCategory != "All" ||
                selectedStock != "All" ||
                selectedBorrowType != "All" ||
                selectedSort != "Name A-Z"
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        searchText = ""
                        selectedRoomId = "All"
                        selectedCategory = "All"
                        selectedStock = "All"
                        selectedBorrowType = "All"
                        selectedSort = "Name A-Z"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Clear Search, Filters and Sort")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Showing ${filteredEquipment.size} of ${equipmentList.size} equipment",
                style = MaterialTheme.typography.titleMedium,
                color = ManageColors.TextDark,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredEquipment.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (equipmentList.isEmpty()) {
                            "No equipment found."
                        } else {
                            "No equipment matches your search/filter."
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = ManageColors.TextMuted
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredEquipment) { equipment ->
                        ManageEquipmentCard(
                            equipment = equipment,
                            room = roomById[equipment.roomId],
                            onEditClick = {
                                onEditClick(equipment)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallInventoryStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ManageColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = ManageColors.TextMuted,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                modifier = Modifier
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun FilterSectionTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = ManageColors.TextMuted,
        fontWeight = FontWeight.Bold
    )
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
private fun InventoryFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ManageColors.PrimaryIndigo,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
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
private fun ManageEquipmentCard(
    equipment: Equipment,
    room: Room?,
    onEditClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ManageColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                EquipmentCardImage(
                    imageName = equipment.imageName,
                    imageUrl = equipment.imageUrl,
                    contentDescription = equipment.name
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = equipment.name.ifBlank { "Unnamed Equipment" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ManageColors.TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${equipment.category.ifBlank { "No category" }} • ${equipment.condition.ifBlank { "No condition" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ManageColors.TextMuted,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Room: ${
                            room?.name?.ifBlank { "Unnamed Room" }
                                ?: equipment.roomId.ifBlank { "No room assigned" }
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = ManageColors.TextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        val stockColors = getStockBadgeColors(
                            available = equipment.availableQuantity,
                            total = equipment.totalQuantity
                        )

                        StatusBadge(
                            text = "${getStockText(equipment.availableQuantity, equipment.totalQuantity)}: ${equipment.availableQuantity}/${equipment.totalQuantity}",
                            bgColor = stockColors.first,
                            textColor = stockColors.second
                        )

                        StatusBadge(
                            text = if (equipment.isBorrowable) {
                                "Borrowable"
                            } else {
                                "Lab-use-only"
                            },
                            bgColor = if (equipment.isBorrowable) {
                                ManageColors.PurpleLight
                            } else {
                                ManageColors.OrangeLight
                            },
                            textColor = if (equipment.isBorrowable) {
                                ManageColors.PurpleText
                            } else {
                                ManageColors.OrangeText
                            }
                        )
                    }
                }
            }

            if (equipment.assetTag.isNotBlank() || equipment.serialNumber.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

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
                    color = ManageColors.TextMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = ManageColors.ModernBg)
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ManageColors.PrimaryIndigoLight,
                    contentColor = ManageColors.PrimaryIndigo
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Edit Equipment",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}