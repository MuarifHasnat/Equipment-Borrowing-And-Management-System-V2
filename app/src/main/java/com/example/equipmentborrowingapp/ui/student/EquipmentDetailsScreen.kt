package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import com.example.equipmentborrowingapp.ui.theme.*

@Composable
fun EquipmentDetailsScreen(
    equipment: Equipment,
    onBorrowClick: (Equipment) -> Unit,
    onBackClick: () -> Unit
) {
    val fallbackImageResId = getFallbackImageRes(equipment.imageName)
    val hasImageUrl = equipment.imageUrl.trim().isNotBlank()
    val inStock = equipment.availableQuantity > 0

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            PrimaryLight.copy(alpha = 0.55f),
                            AppBackground,
                            PrimaryLight.copy(alpha = 0.35f)
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Primary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceWhite,
                    shadowElevation = 4.dp
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = "Save",
                        tint = TextPrimary,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = equipment.name.ifBlank { "Unknown Equipment" },
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Available: ${equipment.availableQuantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = if (inStock) Success else Error,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = if (inStock) "In Stock" else "Out of Stock",
                            color = if (inStock) Success else Error,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                if (hasImageUrl) {
                    AsyncImage(
                        model = equipment.imageUrl.trim(),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = fallbackImageResId),
                        error = painterResource(id = fallbackImageResId),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = fallbackImageResId),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Details",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = equipment.description.ifBlank {
                    "${equipment.name} is useful for lab work, learning, and project development."
                },
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeatureChip(
                    icon = Icons.Filled.Memory,
                    title = equipment.category.ifBlank { "General" },
                    modifier = Modifier.weight(1f)
                )
                FeatureChip(
                    icon = Icons.Filled.Power,
                    title = equipment.condition.ifBlank { "Good" },
                    modifier = Modifier.weight(1f)
                )
                FeatureChip(
                    icon = Icons.Filled.SettingsInputComponent,
                    title = if (equipment.isBorrowable) "Borrowable" else "Lab Only",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { onBorrowClick(equipment) },
                        enabled = equipment.isBorrowable && equipment.availableQuantity > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = TextLight,
                            disabledContainerColor = DividerLight,
                            disabledContentColor = TextSecondary
                        )
                    ) {
                        Text(
                            text = "Borrow Equipment",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(TextSecondary.copy(alpha = 0.35f))
                )
            }
        }
    }
}

@Composable
private fun FeatureChip(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
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
                tint = TextPrimary,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = title.take(14),
                color = TextPrimary,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

private fun getFallbackImageRes(imageName: String): Int {
    val mappedRes = EquipmentImageMapper.getImageRes(imageName.trim())
    return if (mappedRes != 0) mappedRes else R.drawable.ic_launcher_foreground
}