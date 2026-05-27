package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
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
    val BorderSoft = Color(0xFFE2E8F0)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PrimaryPurple = Color(0xFF7C3AED)
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
        val query = searchQuery.trim()
        val matchesSearch =
            query.isBlank() ||
                    equipment.name.trim().contains(query, ignoreCase = true) ||
                    equipment.description.trim().contains(query, ignoreCase = true) ||
                    equipment.category.trim().contains(query, ignoreCase = true)

        val matchesCategory =
            selectedCategory == "All" ||
                    equipment.category.equals(selectedCategory, ignoreCase = true)

        val isActuallyBorrowable =
            equipment.isBorrowable && equipment.borrowType != "LabUseOnly"

        val matchesBorrowableStatus = when (selectedBorrowableStatus) {
            "Borrowable" -> isActuallyBorrowable
            "Lab Use Only" -> !isActuallyBorrowable
            else -> true
        }
        matchesSearch && matchesCategory && matchesBorrowableStatus
    }

    val totalCount = equipmentList.size
    val availableCount = equipmentList.count { it.availableQuantity > 0 }
    val borrowableCount = equipmentList.count {
        it.isBorrowable && it.borrowType != "LabUseOnly"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = EqListColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 9.dp)
        ) {
            EquipmentListTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(7.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModernStatCard(
                    title = "Total",
                    value = totalCount.toString(),
                    bgColor = EqListColors.BlueLight,
                    textColor = EqListColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Available",
                    value = availableCount.toString(),
                    bgColor = EqListColors.GreenLight,
                    textColor = EqListColors.GreenText,
                    modifier = Modifier.weight(1f)
                )

                ModernStatCard(
                    title = "Borrowable",
                    value = borrowableCount.toString(),
                    bgColor = EqListColors.PrimaryIndigoLight,
                    textColor = EqListColors.PrimaryIndigo,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            FilterSection(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                selectedCategory = selectedCategory,
                onCategoryChange = { selectedCategory = it },
                categoryOptions = categoryOptions,
                selectedBorrowableStatus = selectedBorrowableStatus,
                onBorrowableStatusChange = { selectedBorrowableStatus = it },
                borrowableOptions = borrowableOptions,
                resultCount = filteredEquipmentList.size
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredEquipmentList.isEmpty()) {
                EmptyEquipmentState(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(bottom = 14.dp)
                ) {
                    items(filteredEquipmentList, key = { it.id }) { equipment ->
                        ModernEquipmentCard(
                            equipment = equipment,
                            onViewDetailsClick = {
                                onViewDetailsClick(equipment)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EquipmentListTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(13.dp),
                    spotColor = Color.Black.copy(alpha = 0.06f)
                )
                .background(EqListColors.CardWhite, RoundedCornerShape(13.dp))
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = EqListColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Browse Equipment",
                style = MaterialTheme.typography.titleMedium,
                color = EqListColors.TextDark,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Search, filter and request lab items",
                style = MaterialTheme.typography.bodySmall,
                color = EqListColors.TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    categoryOptions: List<String>,
    selectedBorrowableStatus: String,
    onBorrowableStatusChange: (String) -> Unit,
    borrowableOptions: List<String>,
    resultCount: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ModernSearchField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var categoryExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded },
                modifier = Modifier.weight(1f)
            ) {
                ModernDropdownField(
                    label = "Category",
                    value = selectedCategory,
                    expanded = categoryExpanded,
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                    modifier = Modifier.background(EqListColors.CardWhite)
                ) {
                    categoryOptions.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category,
                                    color = EqListColors.TextDark
                                )
                            },
                            onClick = {
                                onCategoryChange(category)
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
                ModernDropdownField(
                    label = "Use Type",
                    value = selectedBorrowableStatus,
                    expanded = borrowableExpanded,
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = borrowableExpanded,
                    onDismissRequest = { borrowableExpanded = false },
                    modifier = Modifier.background(EqListColors.CardWhite)
                ) {
                    borrowableOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    color = EqListColors.TextDark
                                )
                            },
                            onClick = {
                                onBorrowableStatusChange(option)
                                borrowableExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Showing $resultCount item(s)",
                style = MaterialTheme.typography.labelSmall,
                color = EqListColors.TextMuted,
                fontWeight = FontWeight.SemiBold
            )

            if (searchQuery.isNotBlank() || selectedCategory != "All" || selectedBorrowableStatus != "All") {
                Text(
                    text = "Filters active",
                    style = MaterialTheme.typography.labelSmall,
                    color = EqListColors.PrimaryIndigo,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(EqListColors.PrimaryIndigoLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}



@Composable
private fun ModernSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp),
        shape = RoundedCornerShape(13.dp),
        color = EqListColors.ModernBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = EqListColors.TextMuted,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = EqListColors.TextDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isBlank()) {
                            Text(
                                text = "Search equipment...",
                                color = EqListColors.TextMuted,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
private fun ModernStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = EqListColors.CardWhite),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(vertical = 6.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = textColor,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = title,
                color = textColor.copy(alpha = 0.82f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernDropdownField(
    label: String,
    value: String,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp),
        shape = RoundedCornerShape(13.dp),
        color = EqListColors.ModernBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    color = EqListColors.TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = value,
                    color = EqListColors.TextDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }
    }
}

@Composable
private fun ModernEquipmentCard(
    equipment: Equipment,
    onViewDetailsClick: () -> Unit
) {
    val localImageResId = EquipmentImageMapper.getImageRes(equipment.imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(equipment.imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(equipment.imageUrl)
    val stockStatus = getStockStatus(equipment.availableQuantity, equipment.totalQuantity)
    val isActuallyBorrowable =
        equipment.isBorrowable && equipment.borrowType != "LabUseOnly"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = EqListColors.CardWhite),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(EqListColors.ModernBg)
                ) {
                    if (hasImageUrl) {
                        AsyncImage(
                            model = safeImageUrl,
                            contentDescription = equipment.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            error = painterResource(id = localImageResId),
                            fallback = painterResource(id = localImageResId)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = localImageResId),
                            contentDescription = equipment.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = equipment.name.ifBlank { "Unnamed Equipment" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = EqListColors.TextDark,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = equipment.category.ifBlank { "General" },
                        style = MaterialTheme.typography.labelMedium,
                        color = EqListColors.PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = equipment.description.ifBlank { "No description available" },
                        style = MaterialTheme.typography.bodySmall,
                        color = EqListColors.TextMuted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModernEqBadge(
                    text = stockStatus,
                    isSuccess = stockStatus == "In Stock" || stockStatus == "Available",
                    isWarning = stockStatus == "Low Stock"
                )

                ModernEqBadge(
                    text = if (isActuallyBorrowable) "Borrowable" else "Lab Use Only",
                    isSuccess = isActuallyBorrowable,
                    isWarning = false
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            HorizontalDivider(color = EqListColors.BorderSoft.copy(alpha = 0.8f))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Available Quantity",
                        style = MaterialTheme.typography.labelSmall,
                        color = EqListColors.TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${equipment.availableQuantity} / ${equipment.totalQuantity}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = EqListColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Button(
                    onClick = onViewDetailsClick,
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EqListColors.PrimaryIndigo,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "View Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernEqBadge(
    text: String,
    isSuccess: Boolean,
    isWarning: Boolean
) {
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

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EmptyEquipmentState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = EqListColors.CardWhite),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 34.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No equipment found",
                    color = EqListColors.TextDark,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Try changing your search text or filters.",
                    color = EqListColors.TextMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun getStockStatus(
    available: Int,
    total: Int
): String {
    return when {
        available <= 0 -> "Out of Stock"
        available <= 2 -> "Low Stock"
        total > 0 && available == total -> "In Stock"
        else -> "Available"
    }
}
