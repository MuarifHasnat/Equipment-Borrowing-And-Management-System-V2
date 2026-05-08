package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Power
import androidx.compose.material.icons.rounded.SettingsInputComponent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

// Modern Colors
private object EqDetailsColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val GrayLight = Color(0xFFF1F5F9)
}

@Composable
fun EquipmentDetailsScreen(
    equipment: Equipment,
    onBorrowClick: (Equipment) -> Unit,
    onBackClick: () -> Unit
) {
    val fallbackImageResId = getFallbackImageRes(equipment.imageName)
    val hasImageUrl = equipment.imageUrl.trim().isNotBlank()
    val inStock = equipment.availableQuantity > 0

    Surface(modifier = Modifier.fillMaxSize(), color = EqDetailsColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(EqDetailsColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = EqDetailsColors.TextDark)
                }

                IconButton(
                    onClick = { /* TODO: Bookmark feature */ },
                    modifier = Modifier
                        .background(EqDetailsColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(Icons.Rounded.BookmarkBorder, contentDescription = "Save", tint = EqDetailsColors.TextDark)
                }
            }

            //  Title & Availability
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = equipment.name.ifBlank { "Unknown Equipment" },
                    style = MaterialTheme.typography.headlineMedium,
                    color = EqDetailsColors.TextDark,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Available: ${equipment.availableQuantity} / ${equipment.totalQuantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EqDetailsColors.TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Surface(
                        color = if (inStock) EqDetailsColors.GreenLight else EqDetailsColors.RedLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (inStock) Icons.Rounded.CheckCircle else Icons.Rounded.ErrorOutline,
                                contentDescription = null,
                                tint = if (inStock) EqDetailsColors.GreenText else EqDetailsColors.RedText,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (inStock) "In Stock" else "Out of Stock",
                                color = if (inStock) EqDetailsColors.GreenText else EqDetailsColors.RedText,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  Modern Image Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(EqDetailsColors.CardWhite)
                    .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                if (hasImageUrl) {
                    AsyncImage(
                        model = equipment.imageUrl.trim(),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = fallbackImageResId),
                        error = painterResource(id = fallbackImageResId),
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = fallbackImageResId),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ModernFeatureChip(icon = Icons.Rounded.Memory, title = equipment.category.ifBlank { "General" }, modifier = Modifier.weight(1f))
                ModernFeatureChip(icon = Icons.Rounded.Power, title = equipment.condition.ifBlank { "Good" }, modifier = Modifier.weight(1f))
                ModernFeatureChip(icon = Icons.Rounded.SettingsInputComponent, title = if (equipment.isBorrowable) "Borrowable" else "Lab Only", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  Description Section
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                color = EqDetailsColors.TextDark,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = equipment.description.ifBlank {
                    "${equipment.name} is useful for lab work, learning, and project development."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = EqDetailsColors.TextMuted,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            //  Premium Borrow Button
            Button(
                onClick = { onBorrowClick(equipment) },
                enabled = equipment.isBorrowable && equipment.availableQuantity > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = if (equipment.isBorrowable && equipment.availableQuantity > 0) 8.dp else 0.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = EqDetailsColors.PrimaryIndigo.copy(alpha = 0.5f)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (equipment.isBorrowable && equipment.availableQuantity > 0) {
                                Brush.horizontalGradient(listOf(EqDetailsColors.PrimaryIndigo, EqDetailsColors.PurpleAccent))
                            } else {
                                Brush.horizontalGradient(listOf(EqDetailsColors.GrayLight, EqDetailsColors.GrayLight))
                            },
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (!equipment.isBorrowable) "For Lab Use Only" else if (!inStock) "Currently Unavailable" else "Borrow Equipment",
                        color = if (equipment.isBorrowable && equipment.availableQuantity > 0) Color.White else EqDetailsColors.TextMuted,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ModernFeatureChip(icon: ImageVector, title: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        color = EqDetailsColors.CardWhite,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EqDetailsColors.PrimaryIndigo,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = title,
                color = EqDetailsColors.TextDark,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun getFallbackImageRes(imageName: String): Int {
    val mappedRes = EquipmentImageMapper.getImageRes(imageName.trim())
    return if (mappedRes != 0) mappedRes else R.drawable.ic_launcher_foreground
}