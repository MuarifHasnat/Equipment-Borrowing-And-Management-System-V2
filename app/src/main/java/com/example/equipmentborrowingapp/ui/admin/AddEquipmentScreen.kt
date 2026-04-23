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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

@Composable
private fun EquipmentPreviewSection(
    imageName: String,
    imageUrl: String,
    equipmentName: String
) {
    val trimmedImageName = imageName.trim()
    val trimmedImageUrl = imageUrl.trim()
    val fallbackResId = EquipmentImageMapper.getImageRes(trimmedImageName)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Image Preview",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFF5F5F5)),
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
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    trimmedImageName.isNotBlank() -> {
                        Image(
                            painter = painterResource(id = fallbackResId),
                            contentDescription = equipmentName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    else -> {
                        Text(
                            text = "Preview will appear here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = when {
                trimmedImageUrl.isNotBlank() && trimmedImageName.isNotBlank() ->
                    "Using URL image. Local image name stays as fallback."
                trimmedImageUrl.isNotBlank() ->
                    "Using URL image."
                trimmedImageName.isNotBlank() ->
                    "Using local fallback image."
                else ->
                    "Add image name or image URL to preview."
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AddEquipmentScreen(
    onAddClick: (
        String,
        String,
        String,
        String,
        String,
        String,
        String,
        String,
        Boolean
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Equipment Borrowing App",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add New Equipment",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                errorMessage = ""
            },
            label = { Text("Equipment Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                errorMessage = ""
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = category,
            onValueChange = {
                category = it
                errorMessage = ""
            },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = condition,
            onValueChange = {
                condition = it
                errorMessage = ""
            },
            label = { Text("Condition") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = totalQuantity,
            onValueChange = {
                totalQuantity = it
                errorMessage = ""
            },
            label = { Text("Total Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = availableQuantity,
            onValueChange = {
                availableQuantity = it
                errorMessage = ""
            },
            label = { Text("Available Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = imageName,
            onValueChange = {
                imageName = it
                errorMessage = ""
            },
            label = { Text("Local Image Name (optional fallback)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = {
                imageUrl = it
                errorMessage = ""
            },
            label = { Text("Image URL (optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Use imageName for local fallback. Use imageUrl for online image.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(14.dp))

        EquipmentPreviewSection(
            imageName = imageName,
            imageUrl = imageUrl,
            equipmentName = name.ifBlank { "Equipment Preview" }
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isBorrowable,
                onCheckedChange = {
                    isBorrowable = it
                    errorMessage = ""
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBorrowable) "Borrowable" else "Lab Use Only"
            )
        }

        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                    available > total -> "Available quantity cannot exceed total quantity"
                    imageName.isBlank() && imageUrl.isBlank() ->
                        "Provide image name or image URL"
                    else -> ""
                }

                if (errorMessage.isBlank()) {
                    onAddClick(
                        name.trim(),
                        description.trim(),
                        condition.trim(),
                        totalQuantity.trim(),
                        availableQuantity.trim(),
                        category.trim(),
                        imageName.trim(),
                        imageUrl.trim(),
                        isBorrowable
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Equipment")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}