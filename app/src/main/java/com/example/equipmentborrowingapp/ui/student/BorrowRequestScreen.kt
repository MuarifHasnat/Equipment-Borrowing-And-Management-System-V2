package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import com.example.equipmentborrowingapp.ui.theme.*
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
    var purpose by remember { mutableStateOf("") }
    var borrowDate by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var showBorrowDatePicker by remember { mutableStateOf(false) }
    var showDueDatePicker by remember { mutableStateOf(false) }

    val fallbackImageResId = getFallbackImageRes(equipment.imageName)
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
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Borrow Equipment",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Fill the details to borrow",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(22.dp))

            EquipmentPreviewCard(
                equipment = equipment,
                hasImageUrl = hasImageUrl,
                fallbackImageResId = fallbackImageResId
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Borrow Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            DetailsPanel {
                QuantityInputCard(
                    value = quantityText,
                    onValueChange = {
                        quantityText = it.filter { ch -> ch.isDigit() }
                        errorMessage = ""
                    }
                )

                BorrowInfoCard(
                    icon = Icons.Filled.Assignment,
                    title = "Purpose",
                    value = purpose,
                    placeholder = "Enter purpose",
                    onValueChange = {
                        purpose = it
                        errorMessage = ""
                    }
                )

                DateInfoCard(
                    icon = Icons.Filled.CalendarMonth,
                    title = "Borrow Date",
                    value = borrowDate,
                    placeholder = "Select date",
                    onClick = { showBorrowDatePicker = true }
                )

                DateInfoCard(
                    icon = Icons.Filled.EventAvailable,
                    title = "Return Date",
                    value = dueDate,
                    placeholder = "Select date",
                    onClick = { showDueDatePicker = true }
                )

                BorrowInfoCard(
                    icon = Icons.Filled.NoteAlt,
                    title = "Notes (Optional)",
                    value = notes,
                    placeholder = "Add any notes",
                    onValueChange = { notes = it }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Guidelines",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            GuidelinesPanel()

            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = Error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    val quantity = quantityText.toIntOrNull()
                    val validationMessage = validateBorrowRequestInput(
                        quantity = quantity,
                        availableQuantity = equipment.availableQuantity,
                        isBorrowable = equipment.isBorrowable,
                        borrowDate = borrowDate,
                        dueDate = dueDate
                    )

                    if (purpose.isBlank()) {
                        errorMessage = "Please enter purpose"
                    } else if (validationMessage != null) {
                        errorMessage = validationMessage
                    } else {
                        errorMessage = ""
                        onSubmitClick(quantity ?: 1, borrowDate.trim(), dueDate.trim())
                    }
                },
                enabled = canSubmit,
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
                    text = "Submit Request",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showBorrowDatePicker) {
        DatePickerModal(
            onDateSelected = {
                borrowDate = it
                errorMessage = ""
            },
            onDismiss = { showBorrowDatePicker = false }
        )
    }

    if (showDueDatePicker) {
        DatePickerModal(
            onDateSelected = {
                dueDate = it
                errorMessage = ""
            },
            onDismiss = { showDueDatePicker = false }
        )
    }
}

@Composable
private fun EquipmentPreviewCard(
    equipment: Equipment,
    hasImageUrl: Boolean,
    fallbackImageResId: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasImageUrl) {
            AsyncImage(
                model = equipment.imageUrl.trim(),
                contentDescription = equipment.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = fallbackImageResId),
                error = painterResource(id = fallbackImageResId),
                modifier = Modifier
                    .size(112.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
            )
        } else {
            Image(
                painter = painterResource(id = fallbackImageResId),
                contentDescription = equipment.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(112.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formatEquipmentName(equipment.name.ifBlank { "Unknown Equipment" }),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Available: ${equipment.availableQuantity}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Secondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = if (equipment.availableQuantity > 0) Success else Error,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (equipment.availableQuantity > 0) "In Stock" else "Out of Stock",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (equipment.availableQuantity > 0) Success else Error
                )
            }
        }
    }
}

@Composable
private fun DetailsPanel(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            content = content
        )
    }
}

@Composable
private fun QuantityInputCard(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground, RoundedCornerShape(20.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCircle(Icons.Filled.Inventory2)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Quantity",
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Enter quantity") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = DividerLight,
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }
}

@Composable
private fun BorrowInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground, RoundedCornerShape(20.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCircle(icon)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                placeholder = { Text(placeholder) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = DividerLight,
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }
}

@Composable
private fun DateInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    placeholder: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCircle(icon)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value.ifBlank { placeholder },
                color = if (value.isBlank()) TextSecondary else TextPrimary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = null,
            tint = TextPrimary
        )
    }
}

@Composable
private fun IconCircle(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(PrimaryLight, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(25.dp)
        )
    }
}

@Composable
private fun GuidelinesPanel() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            GuidelineRow(
                icon = Icons.Filled.Security,
                title = "Handle with Care",
                subtitle = "Keep the equipment safe"
            )
            GuidelineRow(
                icon = Icons.Filled.Schedule,
                title = "Return on Time",
                subtitle = "Return before the due date"
            )
            GuidelineRow(
                icon = Icons.Filled.Assignment,
                title = "Use Responsibly",
                subtitle = "Use only for learning & projects"
            )
        }
    }
}

@Composable
private fun GuidelineRow(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground, RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextLight,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
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
                        onDateSelected(formatter.format(Date(selectedMillis)))
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
    if (!isBorrowable) return "This equipment is lab-use-only"
    if (availableQuantity <= 0) return "This equipment is out of stock"
    if (quantity == null) return "Please enter a valid quantity"
    if (quantity <= 0) return "Quantity must be greater than 0"
    if (quantity > availableQuantity) return "Requested quantity exceeds available stock"
    if (borrowDate.isBlank() || dueDate.isBlank()) return "Please select borrow date and due date"

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

    if (borrowDateParsed == null || dueDateParsed == null) return "Invalid date selected"

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

    if (diffDays > 14) return "Borrow period cannot be more than 14 days"

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