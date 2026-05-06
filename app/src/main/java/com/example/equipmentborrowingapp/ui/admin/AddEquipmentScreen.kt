package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper


private object AddEquipColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)
    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)
}

@Composable
fun AddEquipmentScreen(
    onAddClick: (
        String, String, String, String, String, String, String, String, Boolean
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var totalQuantity by remember { mutableStateOf("") }
    var availableQuantity by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageName by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isBorrowable by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = AddEquipColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Modern Header with Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(AddEquipColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack, // Deprecation Fixed
                        contentDescription = "Back",
                        tint = AddEquipColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Add New Equipment",
                        style = MaterialTheme.typography.titleLarge,
                        color = AddEquipColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Fill in the details below",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AddEquipColors.TextMuted
                    )
                }
            }

            // Input Fields
            ModernTextField(
                value = name,
                onValueChange = { name = it; errorMessage = "" },
                label = "Equipment Name"
            )

            ModernTextField(
                value = category,
                onValueChange = { category = it; errorMessage = "" },
                label = "Category (e.g., Arduino, Sensor)"
            )

            ModernTextField(
                value = condition,
                onValueChange = { condition = it; errorMessage = "" },
                label = "Condition (e.g., Good, New, Damaged)"
            )

            // Side-by-side quantities
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                ModernTextField(
                    value = totalQuantity,
                    onValueChange = { totalQuantity = it; errorMessage = "" },
                    label = "Total Qty",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                ModernTextField(
                    value = availableQuantity,
                    onValueChange = { availableQuantity = it; errorMessage = "" },
                    label = "Available Qty",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            ModernTextField(
                value = description,
                onValueChange = { description = it; errorMessage = "" },
                label = "Description",
                modifier = Modifier.height(100.dp),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = AddEquipColors.TextMuted.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(20.dp))

            // Image Section
            Text(
                text = "Media & Images",
                style = MaterialTheme.typography.titleMedium,
                color = AddEquipColors.TextDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            ModernTextField(
                value = imageName,
                onValueChange = { imageName = it; errorMessage = "" },
                label = "Local Image Name (Optional Fallback)"
            )

            ModernTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it; errorMessage = "" },
                label = "Image URL (Optional Online Source)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            EquipmentPreviewSection(
                imageName = imageName,
                imageUrl = imageUrl,
                equipmentName = name.ifBlank { "Preview" }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Modern Toggle Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                colors = CardDefaults.cardColors(containerColor = AddEquipColors.CardWhite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Borrowable Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AddEquipColors.TextDark
                        )
                        Text(
                            text = if (isBorrowable) "Students can borrow this item" else "Strictly for Lab Use only",
                            style = MaterialTheme.typography.bodySmall,
                            color = AddEquipColors.TextMuted
                        )
                    }
                    Switch(
                        checked = isBorrowable,
                        onCheckedChange = { isBorrowable = it; errorMessage = "" },
                        colors = SwitchDefaults.colors(checkedTrackColor = AddEquipColors.PrimaryIndigo)
                    )
                }
            }

            // Error Message Display
            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AddEquipColors.RedLight, RoundedCornerShape(12.dp))
                        .border(1.dp, AddEquipColors.RedText.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Warning, contentDescription = "Error", tint = AddEquipColors.RedText)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = errorMessage, color = AddEquipColors.RedText, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = {
                    val total = totalQuantity.toIntOrNull()
                    val available = availableQuantity.toIntOrNull()

                    errorMessage = when {
                        name.isBlank() -> "Equipment name is required"
                        description.isBlank() -> "Description is required"
                        category.isBlank() -> "Category is required"
                        condition.isBlank() -> "Condition is required"
                        total == null || total <= 0 -> "Enter a valid total quantity"
                        available == null || available < 0 -> "Enter a valid available quantity"
                        available > total -> "Available quantity cannot exceed total"
                        imageName.isBlank() && imageUrl.isBlank() -> "Provide image name or image URL"
                        else -> ""
                    }

                    if (errorMessage.isBlank()) {
                        onAddClick(
                            name.trim(), description.trim(), condition.trim(),
                            totalQuantity.trim(), availableQuantity.trim(),
                            category.trim(), imageName.trim(), imageUrl.trim(), isBorrowable
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = AddEquipColors.PrimaryIndigo.copy(alpha = 0.5f)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(listOf(AddEquipColors.PrimaryIndigo, AddEquipColors.PurpleAccent)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Equipment", color = Color.White, fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Modifier parameter moved to the first optional position to fix the warning
@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = AddEquipColors.TextMuted) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AddEquipColors.CardWhite,
            unfocusedContainerColor = AddEquipColors.CardWhite,
            focusedBorderColor = AddEquipColors.PrimaryIndigo,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = AddEquipColors.TextDark,
            unfocusedTextColor = AddEquipColors.TextDark,
            cursorColor = AddEquipColors.PrimaryIndigo
        )
    )
}

@Composable
private fun EquipmentPreviewSection(
    imageName: String,
    imageUrl: String,
    equipmentName: String
) {
    val trimmedImageName = imageName.trim()
    val trimmedImageUrl = imageUrl.trim()
    val fallbackResId = EquipmentImageMapper.getImageRes(trimmedImageName)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.08f)),
        colors = CardDefaults.cardColors(containerColor = AddEquipColors.CardWhite),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AddEquipColors.ModernBg),
                contentAlignment = Alignment.Center
            ) {
                when {
                    trimmedImageUrl.isNotBlank() -> {
                        AsyncImage(
                            model = trimmedImageUrl,
                            contentDescription = equipmentName,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = fallbackResId),
                            error = painterResource(id = fallbackResId),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    trimmedImageName.isNotBlank() -> {
                        Image(
                            painter = painterResource(id = fallbackResId),
                            contentDescription = equipmentName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        Text(
                            text = "Image Preview",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AddEquipColors.TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = when {
                    trimmedImageUrl.isNotBlank() && trimmedImageName.isNotBlank() -> "Using URL image. Local acts as fallback."
                    trimmedImageUrl.isNotBlank() -> "Using online URL image."
                    trimmedImageName.isNotBlank() -> "Using local fallback image."
                    else -> "Add image name or URL to see preview."
                },
                style = MaterialTheme.typography.bodySmall,
                color = AddEquipColors.TextMuted,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}