package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

// Modern Colors
private object ManageColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PrimaryIndigoLight = Color(0xFFE0E7FF)
}

// Helper to get matching Badge Colors (Background to Text Color)
private fun getStockBadgeColors(available: Int, total: Int): Pair<Color, Color> {
    return when {
        total <= 0 -> Pair(Color(0xFFF1F5F9), Color(0xFF64748B)) // Gray
        available <= 0 -> Pair(Color(0xFFFEF2F2), Color(0xFFDC2626)) // Red
        available <= 2 -> Pair(Color(0xFFFFF7ED), Color(0xFFEA580C)) // Orange
        else -> Pair(Color(0xFFF0FDF4), Color(0xFF16A34A))           // Green
    }
}

@Composable
private fun StatusBadge(text: String, bgColor: Color, textColor: Color) {
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
private fun EquipmentCardImage(imageName: String, imageUrl: String, contentDescription: String) {
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
    onBackClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = ManageColors.ModernBg) {
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
                        .background(ManageColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
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
                        text = "Inventory Overview",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ManageColors.TextMuted
                    )
                }
            }

            if (equipmentList.isEmpty()) {
                // --- Empty State ---
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No equipment found.",
                        style = MaterialTheme.typography.titleMedium,
                        color = ManageColors.TextMuted
                    )
                }
            } else {
                // --- Gorgeous List State ---
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(equipmentList, key = { it.id }) { equipment ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ManageColors.CardWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                // Top Section: Image + Details
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    EquipmentCardImage(equipment.imageName, equipment.imageUrl, equipment.name)

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = equipment.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ManageColors.TextDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "${equipment.category} • ${equipment.condition}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ManageColors.TextMuted,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Beautiful Status Badges
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            val stockColors = getStockBadgeColors(equipment.availableQuantity, equipment.totalQuantity)
                                            StatusBadge(
                                                text = "Stock: ${equipment.availableQuantity}/${equipment.totalQuantity}",
                                                bgColor = stockColors.first,
                                                textColor = stockColors.second
                                            )

                                            val typeBg = if (equipment.isBorrowable) Color(0xFFF3E8FF) else Color(0xFFFEF3C7)
                                            val typeText = if (equipment.isBorrowable) Color(0xFF7E22CE) else Color(0xFFD97706)
                                            StatusBadge(
                                                text = if (equipment.isBorrowable) "Borrowable" else "Lab Use",
                                                bgColor = typeBg,
                                                textColor = typeText
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = ManageColors.ModernBg)
                                Spacer(modifier = Modifier.height(12.dp))

                                // Bottom Section: Action Button Only
                                Button(
                                    onClick = { onEditClick(equipment) },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
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
                                    Text("Edit Equipment", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}