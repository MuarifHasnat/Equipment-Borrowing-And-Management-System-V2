package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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

// Helper to get matching Badge Colors (Background to Text Color)
private fun getStockBadgeColors(available: Int, total: Int): Pair<Color, Color> {
    return when {
        total <= 0 -> Pair(Color(0xFFF5F5F5), Color(0xFF757575)) // Gray
        available <= 0 -> Pair(Color(0xFFFFEBEE), Color(0xFFD32F2F)) // Red
        available <= 2 -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17)) // Orange
        else -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))           // Green
    }
}

@Composable
private fun StatusBadge(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun EquipmentCardImage(imageName: String, imageUrl: String, contentDescription: String) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val hasImageUrl = imageUrl.trim().isNotBlank()

    val modifier = Modifier
        .size(100.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(Color(0xFFF3F4F8))

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
            modifier = modifier.padding(8.dp) // Slight padding for local icons
        )
    }
}

@Composable
fun ManageEquipmentScreen(
    equipmentList: List<Equipment>,
    onEditClick: (Equipment) -> Unit,
    onBackClick: () -> Unit
) {
    // Premium Color Palette
    val bgColor = Color(0xFFF8F7FA)
    val cardBg = Color.White
    val gradientStart = Color(0xFFE040FB) // Vibrant Pink
    val gradientEnd = Color(0xFF8A2BE2)   // Deep Purple
    val darkText = Color(0xFF1A1A1A)
    val grayText = Color(0xFF888888)
    val dividerColor = Color(0xFFF0F0F0)

    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- Premium Header ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Inventory",
                        style = MaterialTheme.typography.titleMedium,
                        color = grayText,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Manage Equipment",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = darkText
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (equipmentList.isEmpty()) {
                // --- Empty State ---
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No equipment found.", fontSize = 16.sp, color = grayText, fontWeight = FontWeight.SemiBold)
                }
            } else {
                // --- Gorgeous List State ---
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(equipmentList, key = { it.id }) { equipment ->
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {

                                // Top Section: Image + Details
                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    EquipmentCardImage(equipment.imageName, equipment.imageUrl, equipment.name)

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = equipment.name,
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Black,
                                            color = darkText,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Subtle info text
                                        Text(
                                            text = "${equipment.category} • ${equipment.condition}",
                                            fontSize = 13.sp,
                                            color = grayText,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Beautiful Status Badges
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            val stockColors = getStockBadgeColors(equipment.availableQuantity, equipment.totalQuantity)
                                            StatusBadge(
                                                text = "Stock: ${equipment.availableQuantity}/${equipment.totalQuantity}",
                                                bgColor = stockColors.first,
                                                textColor = stockColors.second
                                            )

                                            val typeBg = if (equipment.isBorrowable) Color(0xFFF3E5F5) else Color(0xFFFFF3E0)
                                            val typeText = if (equipment.isBorrowable) Color(0xFF6A1B9A) else Color(0xFFE65100)
                                            StatusBadge(
                                                text = if (equipment.isBorrowable) "Borrowable" else "Lab Use",
                                                bgColor = typeBg,
                                                textColor = typeText
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))
                                HorizontalDivider(color = dividerColor, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(16.dp))

                                // Bottom Section: Action Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Gradient Edit Button
                                    Box(
                                        modifier = Modifier
                                            .weight(1.2f)
                                            .height(46.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Brush.horizontalGradient(listOf(gradientStart, gradientEnd)))
                                            .clickable { onEditClick(equipment) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Edit Equipment", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                                    }

                                    // Subtle Outlined Back Button
                                    Box(
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .height(46.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .border(1.5.dp, dividerColor, RoundedCornerShape(16.dp))
                                            .clickable { onBackClick() },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Back", color = darkText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Global Back Button (if empty)
            if (equipmentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.5.dp, Color(0xFFDCDCDC), RoundedCornerShape(20.dp))
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Go Back", color = darkText, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}