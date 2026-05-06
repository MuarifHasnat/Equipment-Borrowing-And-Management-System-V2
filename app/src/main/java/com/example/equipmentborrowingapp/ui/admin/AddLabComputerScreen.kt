package com.example.equipmentborrowingapp.ui.admin

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// কালারগুলোকে Object-এর ভেতর রাখা হয়েছে যেন অন্য ফাইলের সাথে Conflict না করে
private object LabColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)
    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLabComputerScreen(
    onAddClick: (
        String,
        String,
        String,
        String,
        String,
        String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var pcName by remember { mutableStateOf("") }
    var labRoom by remember { mutableStateOf("") }
    var locationNote by remember { mutableStateOf("") }
    var ipAddress by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Active") }
    var remarks by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var statusExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("Active", "Problematic", "Maintenance")

    Surface(modifier = Modifier.fillMaxSize(), color = LabColors.ModernBg) {
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
                        .background(LabColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = LabColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Add Lab Computer",
                        style = MaterialTheme.typography.titleLarge,
                        color = LabColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Lab Monitoring System",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabColors.TextMuted
                    )
                }
            }

            // Input Fields
            ModernTextField(
                value = pcName,
                onValueChange = {
                    pcName = it
                    errorMessage = ""
                },
                label = "PC Name",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                ModernTextField(
                    value = labRoom,
                    onValueChange = {
                        labRoom = it
                        errorMessage = ""
                    },
                    label = "Lab Room",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                ModernTextField(
                    value = ipAddress,
                    onValueChange = {
                        ipAddress = it
                        errorMessage = ""
                    },
                    label = "IP Address (Opt)",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
                )
            }

            ModernTextField(
                value = locationNote,
                onValueChange = {
                    locationNote = it
                    errorMessage = ""
                },
                label = "Location Note",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Modern Dropdown Menu for Status
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                ModernTextField(
                    value = status,
                    onValueChange = {},
                    label = "Status",
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false },
                    modifier = Modifier.background(LabColors.CardWhite)
                ) {
                    statusOptions.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item, color = LabColors.TextDark) },
                            onClick = {
                                status = item
                                statusExpanded = false
                                errorMessage = ""
                            }
                        )
                    }
                }
            }

            ModernTextField(
                value = remarks,
                onValueChange = {
                    remarks = it
                    errorMessage = ""
                },
                label = "Remarks",
                singleLine = false,
                modifier = Modifier.height(100.dp) // Taller box for remarks
            )

            // Error Message Display
            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LabColors.RedLight, RoundedCornerShape(12.dp))
                        .border(1.dp, LabColors.RedText.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Warning, contentDescription = "Error", tint = LabColors.RedText)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = errorMessage, color = LabColors.RedText, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = {
                    errorMessage = when {
                        pcName.isBlank() -> "PC name is required"
                        labRoom.isBlank() -> "Lab room is required"
                        else -> ""
                    }

                    if (errorMessage.isBlank()) {
                        onAddClick(
                            pcName.trim(),
                            labRoom.trim(),
                            locationNote.trim(),
                            ipAddress.trim(),
                            status.trim(),
                            remarks.trim()
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = LabColors.PrimaryIndigo.copy(alpha = 0.5f)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(listOf(LabColors.PrimaryIndigo, LabColors.PurpleAccent)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Lab Computer", color = Color.White, fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Reusable Modern TextField Composable
@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = LabColors.TextMuted) },
        singleLine = singleLine,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LabColors.CardWhite,
            unfocusedContainerColor = LabColors.CardWhite,
            focusedBorderColor = LabColors.PrimaryIndigo,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = LabColors.TextDark,
            unfocusedTextColor = LabColors.TextDark,
            cursorColor = LabColors.PrimaryIndigo
        )
    )
}