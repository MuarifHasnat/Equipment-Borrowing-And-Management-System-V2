package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

private object EditEquipmentColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val RedText = Color(0xFFDC2626)
    val RedLight = Color(0xFFFEF2F2)
    val GreenText = Color(0xFF16A34A)
    val GreenLight = Color(0xFFF0FDF4)
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
    var condition by remember { mutableStateOf(equipment.condition) }
    var category by remember { mutableStateOf(equipment.category) }
    var totalQuantity by remember { mutableStateOf(equipment.totalQuantity.toString()) }
    var availableQuantity by remember { mutableStateOf(equipment.availableQuantity.toString()) }
    var imageName by remember { mutableStateOf(equipment.imageName) }
    var imageUrl by remember { mutableStateOf(equipment.imageUrl) }
    var assetTag by remember { mutableStateOf(equipment.assetTag) }
    var serialNumber by remember { mutableStateOf(equipment.serialNumber) }
    var isBorrowable by remember { mutableStateOf(equipment.isBorrowable) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(imageUrl)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = EditEquipmentColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(EditEquipmentColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = EditEquipmentColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.size(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Edit Equipment",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = EditEquipmentColors.TextDark
                    )

                    Text(
                        text = "Update equipment information",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EditEquipmentColors.TextMuted
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditEquipmentColors.CardWhite)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Image Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EditEquipmentColors.TextDark
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(EditEquipmentColors.ModernBg),
                        contentAlignment = Alignment.Center
                    ) {
                        if (hasImageUrl) {
                            AsyncImage(
                                model = safeImageUrl,
                                contentDescription = name.ifBlank { "Equipment image" },
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(id = fallbackImageResId),
                                error = painterResource(id = fallbackImageResId),
                                fallback = painterResource(id = fallbackImageResId),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = fallbackImageResId),
                                contentDescription = name.ifBlank { "Equipment image" },
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = imageName,
                        onValueChange = { imageName = it },
                        label = { Text("Image Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Image,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Image URL") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditEquipmentColors.CardWhite)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Equipment Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EditEquipmentColors.TextDark
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Equipment Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        minLines = 3
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = condition,
                        onValueChange = { condition = it },
                        label = { Text("Condition") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = totalQuantity,
                            onValueChange = { totalQuantity = it },
                            label = { Text("Total Qty") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = availableQuantity,
                            onValueChange = { availableQuantity = it },
                            label = { Text("Available") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    OutlinedTextField(
                        value = assetTag,
                        onValueChange = { assetTag = it },
                        label = { Text("Asset Tag") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = serialNumber,
                        onValueChange = { serialNumber = it },
                        label = { Text("Serial Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (isBorrowable) {
                                    EditEquipmentColors.GreenLight
                                } else {
                                    EditEquipmentColors.ModernBg
                                },
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Borrowable",
                                color = EditEquipmentColors.TextDark,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = if (isBorrowable) {
                                    "Students can request this equipment"
                                } else {
                                    "This item is lab-use-only"
                                },
                                color = EditEquipmentColors.TextMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Switch(
                            checked = isBorrowable,
                            onCheckedChange = { isBorrowable = it }
                        )
                    }

                    if (errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            color = EditEquipmentColors.RedText,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = {
                            val totalQty = totalQuantity.toIntOrNull()
                            val availableQty = availableQuantity.toIntOrNull()

                            errorMessage = when {
                                name.isBlank() -> "Equipment name is required"
                                description.isBlank() -> "Description is required"
                                category.isBlank() -> "Category is required"
                                condition.isBlank() -> "Condition is required"
                                totalQty == null || totalQty <= 0 -> "Enter a valid total quantity"
                                availableQty == null || availableQty < 0 -> "Enter a valid available quantity"
                                availableQty > totalQty -> "Available quantity cannot be greater than total quantity"
                                imageName.isBlank() && imageUrl.isBlank() -> "Image name or image URL is required"
                                else -> ""
                            }

                            if (errorMessage.isBlank()) {
                                onSaveClick(
                                    equipment.copy(
                                        name = name.trim(),
                                        description = description.trim(),
                                        condition = condition.trim(),
                                        category = category.trim(),
                                        totalQuantity = totalQty ?: equipment.totalQuantity,
                                        availableQuantity = availableQty ?: equipment.availableQuantity,
                                        imageName = imageName.trim(),
                                        imageUrl = imageUrl.trim(),
                                        assetTag = assetTag.trim(),
                                        serialNumber = serialNumber.trim(),
                                        isBorrowable = isBorrowable,
                                        borrowType = if (isBorrowable) "OutsideLab" else "LabUseOnly",
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EditEquipmentColors.PrimaryIndigo
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = "Save Changes",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            showDeleteDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = EditEquipmentColors.RedText
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = "Delete Equipment",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = EditEquipmentColors.CardWhite,
            title = {
                Text(
                    text = "Delete Equipment?",
                    color = EditEquipmentColors.TextDark,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete ${name.ifBlank { "this equipment" }}? This action cannot be undone.",
                    color = EditEquipmentColors.TextMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick(equipment)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditEquipmentColors.RedText
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}