package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.AppStatusBadge
import com.example.equipmentborrowingapp.ui.common.GradientHeaderCard
import com.example.equipmentborrowingapp.ui.common.PrimaryActionButton
import com.example.equipmentborrowingapp.ui.common.SecondaryActionButton
import com.example.equipmentborrowingapp.ui.common.SectionTitle
import com.example.equipmentborrowingapp.ui.theme.AppBackground
import com.example.equipmentborrowingapp.ui.theme.InfoLight
import com.example.equipmentborrowingapp.ui.theme.SuccessLight
import com.example.equipmentborrowingapp.ui.theme.TextPrimary
import com.example.equipmentborrowingapp.ui.theme.WarningLight
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowRequestScreen(
    equipment: Equipment,
    onSubmitClick: (Int, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var quantityText by remember { mutableStateOf("1") }
    var borrowDate by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var showBorrowDatePicker by remember { mutableStateOf(false) }
    var showDueDatePicker by remember { mutableStateOf(false) }

    val fallbackImageResId = getFallbackImageRes(imageName = equipment.imageName)
    val hasImageUrl = equipment.imageUrl.trim().isNotBlank()
    val canSubmit = equipment.availableQuantity > 0 && equipment.isBorrowable

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            GradientHeaderCard(
                title = "Borrow Request",
                subtitle = "Request Equipment Access",
                description = "Review equipment details, choose quantity, and select borrowing dates before submitting."
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (hasImageUrl) {
                            AsyncImage(
                                model = equipment.imageUrl.trim(),
                                contentDescription = equipment.name,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = fallbackImageResId),
                                error = painterResource(id = fallbackImageResId),
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(androidx.compose.ui.graphics.Color(0xFFF5F5F5))
                            )
                        } else {
                            Image(
                                painter = painterResource(id = fallbackImageResId),
                                contentDescription = equipment.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(androidx.compose.ui.graphics.Color(0xFFF5F5F5))
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = formatEquipmentName(
                                    equipment.name.ifBlank { "Unknown Equipment" }
                                ),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                AppStatusBadge(
                                    text = formatCategory(equipment.category.ifBlank { "N/A" }),
                                    backgroundColor = InfoLight,
                                    textColor = TextPrimary
                                )

                                AppStatusBadge(
                                    text = if (equipment.isBorrowable) "Borrowable" else "Lab Use Only",
                                    backgroundColor = if (equipment.isBorrowable) SuccessLight else WarningLight,
                                    textColor = TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Condition: ${
                                    equipment.condition.ifBlank { "N/A" }
                                        .replaceFirstChar {
                                            if (it.isLowerCase()) {
                                                it.titlecase(Locale.getDefault())
                                            } else {
                                                it.toString()
                                            }
                                        }
                                }",
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color(0xFF333333)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Available: ${equipment.availableQuantity}/${equipment.totalQuantity}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color(0xFF333333)
                            )
                        }
                    }

                    if (!equipment.isBorrowable || equipment.availableQuantity <= 0) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = when {
                                !equipment.isBorrowable ->
                                    "This equipment is lab-use-only and cannot be borrowed."
                                equipment.availableQuantity <= 0 ->
                                    "This equipment is currently out of stock."
                                else -> ""
                            },
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(
                text = "Request Details",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = quantityText,
                onValueChange = {
                    quantityText = it.filter { ch -> ch.isDigit() }
                    errorMessage = ""
                },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enter the quantity you want to request.",
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBorrowDatePicker = true }
            ) {
                OutlinedTextField(
                    value = borrowDate,
                    onValueChange = {},
                    enabled = false,
                    readOnly = true,
                    label = { Text("Borrow Date") },
                    placeholder = { Text("Select date") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tap the field to select the borrow date.",
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDueDatePicker = true }
            ) {
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = {},
                    enabled = false,
                    readOnly = true,
                    label = { Text("Due Date") },
                    placeholder = { Text("Select date") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Maximum borrow period is 14 days.",
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color.Gray
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            PrimaryActionButton(
                text = "Submit Request",
                onClick = {
                    val quantity = quantityText.toIntOrNull()

                    val validationMessage = validateBorrowRequestInput(
                        quantity = quantity,
                        availableQuantity = equipment.availableQuantity,
                        isBorrowable = equipment.isBorrowable,
                        borrowDate = borrowDate,
                        dueDate = dueDate
                    )

                    if (validationMessage != null) {
                        errorMessage = validationMessage
                    } else {
                        errorMessage = ""
                        onSubmitClick(
                            quantity ?: 1,
                            borrowDate.trim(),
                            dueDate.trim()
                        )
                    }
                },
                enabled = canSubmit
            )

            Spacer(modifier = Modifier.height(8.dp))

            SecondaryActionButton(
                text = "Back",
                onClick = onBackClick
            )
        }
    }

    if (showBorrowDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDate ->
                borrowDate = selectedDate
                errorMessage = ""
            },
            onDismiss = {
                showBorrowDatePicker = false
            }
        )
    }

    if (showDueDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDate ->
                dueDate = selectedDate
                errorMessage = ""
            },
            onDismiss = {
                showDueDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val selectedDate = formatter.format(Date(selectedMillis))
                        onDateSelected(selectedDate)
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

private fun validateBorrowRequestInput(
    quantity: Int?,
    availableQuantity: Int,
    isBorrowable: Boolean,
    borrowDate: String,
    dueDate: String
): String? {
    if (!isBorrowable) {
        return "This equipment is lab-use-only"
    }

    if (availableQuantity <= 0) {
        return "This equipment is out of stock"
    }

    if (quantity == null) {
        return "Please enter a valid quantity"
    }

    if (quantity <= 0) {
        return "Quantity must be greater than 0"
    }

    if (quantity > availableQuantity) {
        return "Requested quantity exceeds available stock"
    }

    if (borrowDate.isBlank() || dueDate.isBlank()) {
        return "Please select borrow date and due date"
    }

    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.isLenient = false

    val borrowDateParsed = try {
        format.parse(borrowDate.trim())
    } catch (_: Exception) {
        null
    }

    val dueDateParsed = try {
        format.parse(dueDate.trim())
    } catch (_: Exception) {
        null
    }

    if (borrowDateParsed == null || dueDateParsed == null) {
        return "Invalid date selected"
    }

    val todayString = format.format(System.currentTimeMillis())
    val todayParsed = try {
        format.parse(todayString)
    } catch (_: Exception) {
        null
    }

    if (todayParsed != null && borrowDateParsed.before(todayParsed)) {
        return "Borrow date cannot be in the past"
    }

    if (dueDateParsed.before(borrowDateParsed)) {
        return "Due date cannot be before borrow date"
    }

    val diffMillis = dueDateParsed.time - borrowDateParsed.time
    val diffDays = diffMillis / (1000 * 60 * 60 * 24)

    if (diffDays > 14) {
        return "Borrow period cannot be more than 14 days"
    }

    return null
}

private fun getFallbackImageRes(imageName: String): Int {
    val mappedRes = EquipmentImageMapper.getImageRes(imageName.trim())
    return if (mappedRes != 0) mappedRes else R.drawable.ic_launcher_foreground
}

private fun formatEquipmentName(name: String): String {
    return name
        .replace("_", " ")
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { ch ->
                if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()) else ch.toString()
            }
        }
}

private fun formatCategory(category: String): String {
    return when (category.trim().lowercase()) {
        "cc" -> "Computer Components"
        "ece" -> "Electronics"
        "ee" -> "Electrical"
        else -> category.replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()) else ch.toString()
        }
    }
}