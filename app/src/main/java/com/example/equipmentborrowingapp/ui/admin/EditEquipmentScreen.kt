package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

@Composable
private fun EquipmentPreviewCard(
    imageName: String,
    imageUrl: String,
    equipmentName: String
) {
    val trimmedImageName = imageName.trim()
    val trimmedImageUrl = imageUrl.trim()
    val fallbackImageResId = EquipmentImageMapper.getImageRes(trimmedImageName)
    val hasImageUrl = trimmedImageUrl.isNotBlank()
    val hasAnyImageInput = trimmedImageName.isNotBlank() || trimmedImageUrl.isNotBlank()

    Column {
        Text(
            text = "Image Preview",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFF3F4F8)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    hasImageUrl -> {
                        AsyncImage(
                            model = trimmedImageUrl,
                            contentDescription = equipmentName,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = fallbackImageResId),
                            error = painterResource(id = fallbackImageResId),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    hasAnyImageInput -> {
                        Image(
                            painter = painterResource(id = fallbackImageResId),
                            contentDescription = equipmentName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    else -> {
                        Text(
                            text = "Preview will appear here",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF888888)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when {
                hasImageUrl && trimmedImageName.isNotBlank() -> "Using URL image. Local image name stays as fallback."
                hasImageUrl -> "Using URL image."
                hasAnyImageInput -> "Using local fallback image."
                else -> "Add image name or image URL to preview."
            },
            fontSize = 13.sp,
            color = Color(0xFF888888),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun EditEquipmentScreen(
    equipment: Equipment,
    onSaveClick: (Equipment) -> Unit,
    onDeleteClick: (Equipment) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf(equipment.name) }
    var description by remember { mutableStateOf(equipment.description) }
    var category by remember { mutableStateOf(equipment.category) }
    var condition by remember { mutableStateOf(equipment.condition) }
    var totalQty by remember { mutableStateOf(equipment.totalQuantity.toString()) }
    var availableQty by remember { mutableStateOf(equipment.availableQuantity.toString()) }
    var imageName by remember { mutableStateOf(equipment.imageName) }
    var imageUrl by remember { mutableStateOf(equipment.imageUrl) }
    var isBorrowable by remember { mutableStateOf(equipment.isBorrowable) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Premium Color Palette
    val bgColor = Color(0xFFF8F7FA)
    val cardBg = Color.White
    val borderColor = Color(0xFFD7D7D7)
    val grayText = Color(0xFF8A8A8A) // Fixed the variable name here!
    val darkText = Color(0xFF1A1A1A)
    val gradientStart = Color(0xFFE040FB)
    val gradientEnd = Color(0xFF8A2BE2)
    val redDanger = Color(0xFFD32F2F)

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Equipment", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete ${equipment.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick(equipment)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = redDanger)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = darkText, fontWeight = FontWeight.SemiBold)
                }
            },
            containerColor = cardBg
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = bgColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {

            // --- Premium Header ---
            Text(
                text = "Inventory",
                style = MaterialTheme.typography.titleMedium,
                color = grayText,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Edit Equipment",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = darkText
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Form Fields ---
            val fieldModifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(12.dp))

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardBg, unfocusedContainerColor = cardBg,
                focusedBorderColor = borderColor, unfocusedBorderColor = borderColor,
                focusedTextColor = darkText, unfocusedTextColor = darkText,
                focusedLabelColor = grayText, unfocusedLabelColor = grayText
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = "" },
                label = { Text("Name", fontWeight = FontWeight.SemiBold) },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it; errorMessage = "" },
                label = { Text("Description", fontWeight = FontWeight.SemiBold) },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it; errorMessage = "" },
                    label = { Text("Category", fontWeight = FontWeight.SemiBold) },
                    modifier = fieldModifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true
                )

                OutlinedTextField(
                    value = condition,
                    onValueChange = { condition = it; errorMessage = "" },
                    label = { Text("Condition", fontWeight = FontWeight.SemiBold) },
                    modifier = fieldModifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = totalQty,
                    onValueChange = { totalQty = it; errorMessage = "" },
                    label = { Text("Total Qty", fontWeight = FontWeight.SemiBold) },
                    modifier = fieldModifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true
                )

                OutlinedTextField(
                    value = availableQty,
                    onValueChange = { availableQty = it; errorMessage = "" },
                    label = { Text("Available Qty", fontWeight = FontWeight.SemiBold) },
                    modifier = fieldModifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = imageName,
                onValueChange = { imageName = it; errorMessage = "" },
                label = { Text("Local Image Name (Optional Fallback)", fontWeight = FontWeight.SemiBold) },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it; errorMessage = "" },
                label = { Text("Image URL (Optional)", fontWeight = FontWeight.SemiBold) },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Image Preview ---
            EquipmentPreviewCard(
                imageName = imageName,
                imageUrl = imageUrl,
                equipmentName = name.ifBlank { "Equipment Preview" }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Checkbox ---
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).clickable { isBorrowable = !isBorrowable }
                ) {
                    Checkbox(
                        checked = isBorrowable,
                        onCheckedChange = { isBorrowable = it; errorMessage = "" },
                        colors = CheckboxDefaults.colors(checkedColor = gradientEnd)
                    )
                    Text(
                        text = if (isBorrowable) "Type: Borrowable" else "Type: Lab Use Only",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = darkText
                    )
                }
            }

            // Error Message
            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = redDanger,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Action Buttons ---

            // Save Button (Gradient)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(gradientStart, gradientEnd)), RoundedCornerShape(16.dp))
                    .clickable {
                        val total = totalQty.toIntOrNull()
                        val available = availableQty.toIntOrNull()

                        errorMessage = when {
                            name.isBlank() -> "Equipment name is required"
                            description.isBlank() -> "Description is required"
                            category.isBlank() -> "Category is required"
                            condition.isBlank() -> "Condition is required"
                            total == null -> "Enter a valid total quantity"
                            available == null -> "Enter a valid available quantity"
                            total < 0 -> "Total quantity cannot be negative"
                            available < 0 -> "Available quantity cannot be negative"
                            available > total -> "Available quantity cannot exceed total quantity"
                            imageName.isBlank() && imageUrl.isBlank() -> "Provide either image name or image URL"
                            else -> ""
                        }

                        if (errorMessage.isBlank()) {
                            val updated = equipment.copy(
                                name = name.trim(), description = description.trim(), category = category.trim(),
                                condition = condition.trim(), totalQuantity = total ?: 0, availableQuantity = available ?: 0,
                                imageName = imageName.trim(), imageUrl = imageUrl.trim(), isBorrowable = isBorrowable
                            )
                            onSaveClick(updated)
                        }
                    }
            ) {
                Text("Save Changes", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                // Back Button (Outlined)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, Color(0xFFDCDCDC), RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .clickable { onBackClick() }
                ) {
                    Text("Go Back", color = darkText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Delete Button (Red Outline)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, redDanger.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF0F0))
                        .clickable { showDeleteDialog = true }
                ) {
                    Text("Delete", color = redDanger, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}