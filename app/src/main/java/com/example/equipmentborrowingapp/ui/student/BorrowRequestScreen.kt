package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Security
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Modern Colors
private object BorrowColors {
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

    Surface(modifier = Modifier.fillMaxSize(), color = BorrowColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            //  Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(BorrowColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = BorrowColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Borrow Equipment",
                        style = MaterialTheme.typography.titleLarge,
                        color = BorrowColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Fill in the required details",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BorrowColors.TextMuted
                    )
                }
            }

            //  Equipment Preview Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BorrowColors.CardWhite)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(BorrowColors.ModernBg)
                    ) {
                        if (hasImageUrl) {
                            AsyncImage(
                                model = equipment.imageUrl.trim(),
                                contentDescription = equipment.name,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = fallbackImageResId),
                                error = painterResource(id = fallbackImageResId),
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = fallbackImageResId),
                                contentDescription = equipment.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = formatEquipmentName(equipment.name.ifBlank { "Unknown Equipment" }),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BorrowColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Available: ${equipment.availableQuantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = BorrowColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Status Badge
                        Surface(
                            color = if (canSubmit) BorrowColors.GreenLight else BorrowColors.RedLight,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = if (canSubmit) Icons.Rounded.CheckCircle else Icons.Rounded.ErrorOutline,
                                    contentDescription = null,
                                    tint = if (canSubmit) BorrowColors.GreenText else BorrowColors.RedText,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (canSubmit) "In Stock" else "Unavailable",
                                    color = if (canSubmit) BorrowColors.GreenText else BorrowColors.RedText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  Borrow Details Form
            Text(
                text = "Borrow Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BorrowColors.TextDark,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BorrowColors.CardWhite)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    ModernTextField(
                        value = quantityText,
                        onValueChange = {
                            quantityText = it.filter { ch -> ch.isDigit() }
                            errorMessage = ""
                        },
                        label = "Quantity",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                    )

                    ModernTextField(
                        value = purpose,
                        onValueChange = { purpose = it; errorMessage = "" },
                        label = "Purpose (e.g. Lab Project)",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    ModernDateField(
                        value = borrowDate,
                        placeholder = "Select Borrow Date",
                        onClick = { showBorrowDatePicker = true }
                    )

                    ModernDateField(
                        value = dueDate,
                        placeholder = "Select Return Date",
                        onClick = { showDueDatePicker = true }
                    )

                    ModernTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Notes (Optional)",
                        singleLine = false,
                        modifier = Modifier.height(80.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  Guidelines
            Text(
                text = "Important Guidelines",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BorrowColors.TextDark,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BorrowColors.CardWhite)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModernGuidelineRow(icon = Icons.Rounded.Security, title = "Handle with Care", subtitle = "Keep the equipment safe from damage")
                    ModernGuidelineRow(icon = Icons.Rounded.Schedule, title = "Return on Time", subtitle = "Must be returned before the due date")
                    ModernGuidelineRow(icon = Icons.Rounded.Assignment, title = "Use Responsibly", subtitle = "Use only for learning & lab projects")
                }
            }

            // Error Message Display
            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BorrowColors.RedLight, RoundedCornerShape(12.dp))
                        .border(1.dp, BorrowColors.RedText.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Warning, contentDescription = "Error", tint = BorrowColors.RedText)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = errorMessage, color = BorrowColors.RedText, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  Submit Button
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
                        errorMessage = "Please enter a purpose"
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
                    .height(56.dp)
                    .shadow(if (canSubmit) 6.dp else 0.dp, RoundedCornerShape(16.dp), spotColor = BorrowColors.PrimaryIndigo.copy(alpha = 0.5f)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = BorrowColors.GrayLight
                ),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (canSubmit) {
                                Brush.horizontalGradient(listOf(BorrowColors.PrimaryIndigo, BorrowColors.PurpleAccent))
                            } else {
                                Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                            },
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (canSubmit) "Submit Request" else "Currently Unavailable",
                        color = if (canSubmit) Color.White else BorrowColors.TextMuted,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Modals
    if (showBorrowDatePicker) {
        DatePickerModal(
            onDateSelected = { borrowDate = it; errorMessage = "" },
            onDismiss = { showBorrowDatePicker = false }
        )
    }

    if (showDueDatePicker) {
        DatePickerModal(
            onDateSelected = { dueDate = it; errorMessage = "" },
            onDismiss = { showDueDatePicker = false }
        )
    }
}

// Helper Composables

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
        label = { Text(label, color = BorrowColors.TextMuted) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = BorrowColors.ModernBg,
            unfocusedContainerColor = BorrowColors.ModernBg,
            focusedBorderColor = BorrowColors.PrimaryIndigo,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = BorrowColors.TextDark,
            unfocusedTextColor = BorrowColors.TextDark
        )
    )
}

@Composable
private fun ModernDateField(
    value: String,
    placeholder: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
            .background(BorrowColors.ModernBg, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelSmall,
                    color = BorrowColors.TextMuted
                )
                if (value.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BorrowColors.TextDark
                    )
                }
            }
            Icon(
                imageVector = Icons.Rounded.CalendarToday,
                contentDescription = "Select Date",
                tint = BorrowColors.PrimaryIndigo
            )
        }
    }
}

@Composable
private fun ModernGuidelineRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BorrowColors.ModernBg)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(BorrowColors.CardWhite, CircleShape)
                .shadow(1.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BorrowColors.PrimaryIndigo,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                color = BorrowColors.TextDark,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = subtitle,
                color = BorrowColors.TextMuted,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// Kept untouched to preserve existing logic
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