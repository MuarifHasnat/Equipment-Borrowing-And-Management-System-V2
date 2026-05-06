package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
private object EqListColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PrimaryIndigoLight = Color(0xFFE0E7FF)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentListScreen(
    equipmentList: List<Equipment>,
    onViewDetailsClick: (Equipment) -> Unit,
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
            selectedCategory == "All" || equipment.category.equals(selectedCategory, ignoreCase = true)

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

    Surface(modifier = Modifier.fillMaxSize(), color = EqListColors.ModernBg) {
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
                        .background(EqListColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = EqListColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Equipment Browser",
                        style = MaterialTheme.typography.titleLarge,
                        color = EqListColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Find and request lab equipment",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EqListColors.TextMuted
                    )
                }
            }

            // Summary Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernStatCard("Total", totalCount.toString(), EqListColors.BlueLight, EqListColors.BlueText, Modifier.weight(1f))
                ModernStatCard("Available", availableCount.toString(), EqListColors.GreenLight, EqListColors.GreenText, Modifier.weight(1f))
                ModernStatCard("Borrowable", borrowableCount.toString(), EqListColors.OrangeLight, EqListColors.OrangeText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Modern Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(14.dp),
                placeholder = { Text("Search by name or description...", color = EqListColors.TextMuted) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = EqListColors.TextMuted) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = EqListColors.CardWhite,
                    unfocusedContainerColor = EqListColors.CardWhite,
                    focusedBorderColor = EqListColors.PrimaryIndigo,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = EqListColors.TextDark,
                    unfocusedTextColor = EqListColors.TextDark
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filters
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
                    ModernDropdownField("Category", selectedCategory, categoryExpanded, Modifier.menuAnchor())
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                        modifier = Modifier.background(EqListColors.CardWhite)
                    ) {
                        categoryOptions.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category, color = EqListColors.TextDark) },
                                onClick = { selectedCategory = category; categoryExpanded = false }
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
                    ModernDropdownField("Status", selectedBorrowableStatus, borrowableExpanded, Modifier.menuAnchor())
                    ExposedDropdownMenu(
                        expanded = borrowableExpanded,
                        onDismissRequest = { borrowableExpanded = false },
                        modifier = Modifier.background(EqListColors.CardWhite)
                    ) {
                        borrowableOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = EqListColors.TextDark) },
                                onClick = { selectedBorrowableStatus = option; borrowableExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Showing ${filteredEquipmentList.size} item(s)",
                style = MaterialTheme.typography.bodySmall,
                color = EqListColors.TextMuted,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Equipment List
            if (filteredEquipmentList.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No equipment found", color = EqListColors.TextMuted, style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(filteredEquipmentList, key = { it.id }) { equipment ->
                        ModernEquipmentCard(equipment, onViewDetailsClick = { onViewDetailsClick(equipment) })
                    }
                }
            }
        }
    }
}

// Helper Composables

@Composable
private fun ModernStatCard(title: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = EqListColors.CardWhite),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(bgColor).padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 24.sp, color = textColor, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, color = textColor.copy(alpha = 0.8f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernDropdownField(label: String, value: String, expanded: Boolean, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label, color = EqListColors.TextMuted, style = MaterialTheme.typography.bodySmall) },
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        modifier = modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = EqListColors.CardWhite, unfocusedContainerColor = EqListColors.CardWhite,
            focusedBorderColor = EqListColors.PrimaryIndigo, unfocusedBorderColor = Color.Transparent,
            focusedTextColor = EqListColors.TextDark, unfocusedTextColor = EqListColors.TextDark
        )
    )
}

@Composable
private fun ModernEquipmentCard(equipment: Equipment, onViewDetailsClick: () -> Unit) {
    val localImageResId = EquipmentImageMapper.getImageRes(equipment.imageName)
    val hasImageUrl = equipment.imageUrl.isNotBlank()
    val stockStatus = getStockStatus(equipment.availableQuantity, equipment.totalQuantity)

    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EqListColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // Image Box
                Box(
                    modifier = Modifier.size(90.dp).clip(RoundedCornerShape(14.dp)).background(EqListColors.ModernBg)
                ) {
                    if (hasImageUrl) {
                        AsyncImage(
                            model = equipment.imageUrl, contentDescription = equipment.name,
                            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = localImageResId), contentDescription = equipment.name,
                            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = equipment.name.ifBlank { "Unnamed Equipment" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = EqListColors.TextDark,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = equipment.description.ifBlank { "No description available" },
                        style = MaterialTheme.typography.bodySmall,
                        color = EqListColors.TextMuted, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Badges
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ModernEqBadge(text = stockStatus, isSuccess = stockStatus == "In Stock", isWarning = stockStatus == "Low Stock")
                        Spacer(modifier = Modifier.width(8.dp))
                        ModernEqBadge(text = if (equipment.isBorrowable) "Borrowable" else "Lab Use", isSuccess = equipment.isBorrowable, isWarning = false)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = EqListColors.ModernBg)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Available Qty", style = MaterialTheme.typography.labelSmall, color = EqListColors.TextMuted, fontWeight = FontWeight.Bold)
                    Text("${equipment.availableQuantity} / ${equipment.totalQuantity}", style = MaterialTheme.typography.bodyMedium, color = EqListColors.TextDark, fontWeight = FontWeight.ExtraBold)
                }
                Button(
                    onClick = onViewDetailsClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EqListColors.PrimaryIndigoLight, contentColor = EqListColors.PrimaryIndigo),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("View Details", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun ModernEqBadge(text: String, isSuccess: Boolean, isWarning: Boolean) {
    val bgColor = when {
        isSuccess -> EqListColors.GreenLight
        isWarning -> EqListColors.OrangeLight
        else -> EqListColors.RedLight
    }
    val textColor = when {
        isSuccess -> EqListColors.GreenText
        isWarning -> EqListColors.OrangeText
        else -> EqListColors.RedText
    }
    Surface(color = bgColor, shape = RoundedCornerShape(6.dp)) {
        Text(text = text, color = textColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
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