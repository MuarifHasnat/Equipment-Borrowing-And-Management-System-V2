package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage

import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.*
import com.example.equipmentborrowingapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentListScreen(
    equipmentList: List<Equipment>,
    onBorrowClick: (Equipment) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedBorrowableStatus by remember { mutableStateOf("All") }

    val categoryOptions = remember(equipmentList) {
        listOf("All") + equipmentList
            .map { it.category.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    val borrowableOptions = listOf("All", "Borrowable", "Lab Use Only")

    val filteredEquipmentList = equipmentList.filter { equipment ->
        val matchesSearch =
            searchQuery.isBlank() ||
                    equipment.name.trim().contains(searchQuery.trim(), ignoreCase = true) ||
                    equipment.description.trim().contains(searchQuery.trim(), ignoreCase = true)

        val matchesCategory =
            selectedCategory == "All" ||
                    equipment.category.equals(selectedCategory, ignoreCase = true)

        val matchesBorrowableStatus = when (selectedBorrowableStatus) {
            "Borrowable" -> equipment.isBorrowable
            "Lab Use Only" -> !equipment.isBorrowable
            else -> true
        }

        matchesSearch && matchesCategory && matchesBorrowableStatus
    }

    val totalCount = equipmentList.size
    val availableCount = equipmentList.count { it.availableQuantity > 0 }
    val borrowableCount = equipmentList.count { it.isBorrowable }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            GradientHeaderCard(
                title = "Equipment Browser",
                subtitle = "Find Lab Equipment",
                description = "Search equipment, check availability, and submit borrow requests for eligible items."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryStatCard(
                    title = "Total",
                    value = totalCount.toString(),
                    bgColor = PrimaryLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Available",
                    value = availableCount.toString(),
                    bgColor = SuccessLight,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatCard(
                    title = "Borrowable",
                    value = borrowableCount.toString(),
                    bgColor = InfoLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle(
                text = "Filter Equipment",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search by name or description") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var categoryExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categoryOptions.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                var borrowableExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = borrowableExpanded,
                    onExpandedChange = { borrowableExpanded = !borrowableExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedBorrowableStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = borrowableExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = borrowableExpanded,
                        onDismissRequest = { borrowableExpanded = false }
                    ) {
                        borrowableOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedBorrowableStatus = option
                                    borrowableExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Showing ${filteredEquipmentList.size} item(s)",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredEquipmentList.isEmpty()) {
                EmptyStateText(
                    text = "No equipment found",
                    modifier = Modifier
                        .height(320.dp)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.height(520.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredEquipmentList, key = { it.id }) { equipment ->
                        ModernEquipmentCard(
                            equipment = equipment,
                            onBorrowClick = { onBorrowClick(equipment) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryActionButton(
                text = "Back",
                onClick = onBackClick
            )
        }
    }
}

@Composable
private fun ModernEquipmentCard(
    equipment: Equipment,
    onBorrowClick: () -> Unit
) {
    val localImageResId = EquipmentImageMapper.getImageRes(equipment.imageName)
    val stockStatus = getStockStatus(equipment.availableQuantity, equipment.totalQuantity)
    val stockColors = getStockBadgeColors(stockStatus)
    val canBorrow = equipment.isBorrowable && equipment.availableQuantity > 0
    val hasImageUrl = equipment.imageUrl.isNotBlank()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (hasImageUrl) {
                    AsyncImage(
                        model = equipment.imageUrl,
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = localImageResId),
                        placeholder = painterResource(id = localImageResId),
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF5F5F5))
                    )
                } else {
                    Image(
                        painter = painterResource(id = localImageResId),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF5F5F5))
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = equipment.name.ifBlank { "Unnamed Equipment" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = equipment.description.ifBlank { "No description available" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppStatusBadge(
                            text = equipment.category.ifBlank { "General Lab" },
                            backgroundColor = InfoLight,
                            textColor = TextPrimary
                        )

                        AppStatusBadge(
                            text = if (equipment.isBorrowable) "Borrowable" else "Lab Use Only",
                            backgroundColor = if (equipment.isBorrowable) SuccessLight else WarningLight,
                            textColor = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    AppStatusBadge(
                        text = stockStatus,
                        backgroundColor = stockColors.first,
                        textColor = stockColors.second
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFEAEAEA))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabeledValue(
                    title = "Condition",
                    value = equipment.condition.ifBlank { "N/A" }
                )

                LabeledValue(
                    title = "Available",
                    value = "${equipment.availableQuantity}/${equipment.totalQuantity}"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            PrimaryActionButton(
                text = when {
                    !equipment.isBorrowable -> "Lab Use Only"
                    equipment.availableQuantity <= 0 -> "Out of Stock"
                    else -> "Borrow Request"
                },
                onClick = onBorrowClick,
                enabled = canBorrow
            )
        }
    }
}

private fun getStockStatus(available: Int, total: Int): String {
    return when {
        available <= 0 -> "Out of Stock"
        available <= 2 -> "Low Stock"
        total > 0 && available == total -> "In Stock"
        else -> "Available"
    }
}

private fun getStockBadgeColors(status: String): Pair<Color, Color> {
    return when (status) {
        "Out of Stock" -> Pair(ErrorLight, TextPrimary)
        "Low Stock" -> Pair(WarningLight, TextPrimary)
        "In Stock" -> Pair(SuccessLight, TextPrimary)
        else -> Pair(InfoLight, TextPrimary)
    }
}