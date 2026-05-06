package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Send
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.LabComputer

// Modern Colors
private object ReportColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSoftwareIssueScreen(
    computer: LabComputer,
    onSubmitClick: (String, String, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var softwareName by remember { mutableStateOf("") }
    var issueType by remember { mutableStateOf("Not Opening") }
    var description by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf("Medium") }
    var errorMessage by remember { mutableStateOf("") }

    var issueExpanded by remember { mutableStateOf(false) }
    var severityExpanded by remember { mutableStateOf(false) }

    val issueTypes = listOf(
        "Not Installed",
        "Not Opening",
        "Compile Error",
        "Runtime Error",
        "Missing Compiler",
        "Other"
    )

    val severityOptions = listOf("Low", "Medium", "High")

    Surface(modifier = Modifier.fillMaxSize(), color = ReportColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 🔙 Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(ReportColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = ReportColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Report Issue",
                        style = MaterialTheme.typography.titleLarge,
                        color = ReportColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Submit software problems for review",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ReportColors.TextMuted
                    )
                }
            }

            // 🔥 Target PC Info Card
            Surface(
                color = ReportColors.BlueLight,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Computer,
                        contentDescription = null,
                        tint = ReportColors.BlueText,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Target Lab PC",
                            color = ReportColors.BlueText.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = computer.pcName.ifBlank { "Unknown PC" },
                            color = ReportColors.BlueText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // 🔥 Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ReportColors.CardWhite)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Issue Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ReportColors.TextDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ModernTextField(
                        value = softwareName,
                        onValueChange = { softwareName = it; errorMessage = "" },
                        label = "Software Name (e.g. Android Studio)"
                    )

                    // 🔥 Issue Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = issueExpanded,
                        onExpandedChange = { issueExpanded = !issueExpanded }
                    ) {
                        ModernTextField(
                            value = issueType,
                            onValueChange = {},
                            label = "Issue Type",
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = issueExpanded) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = issueExpanded,
                            onDismissRequest = { issueExpanded = false },
                            modifier = Modifier.background(ReportColors.CardWhite)
                        ) {
                            issueTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type, color = ReportColors.TextDark) },
                                    onClick = {
                                        issueType = type
                                        issueExpanded = false
                                        errorMessage = ""
                                    }
                                )
                            }
                        }
                    }

                    // 🔥 Severity Dropdown
                    ExposedDropdownMenuBox(
                        expanded = severityExpanded,
                        onExpandedChange = { severityExpanded = !severityExpanded }
                    ) {
                        ModernTextField(
                            value = severity,
                            onValueChange = {},
                            label = "Severity Level",
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = severityExpanded) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = severityExpanded,
                            onDismissRequest = { severityExpanded = false },
                            modifier = Modifier.background(ReportColors.CardWhite)
                        ) {
                            severityOptions.forEach { level ->
                                DropdownMenuItem(
                                    text = { Text(level, color = ReportColors.TextDark) },
                                    onClick = {
                                        severity = level
                                        severityExpanded = false
                                        errorMessage = ""
                                    }
                                )
                            }
                        }
                    }

                    ModernTextField(
                        value = description,
                        onValueChange = { description = it; errorMessage = "" },
                        label = "Detailed Description",
                        singleLine = false,
                        modifier = Modifier.height(120.dp)
                    )

                    // Error Message
                    if (errorMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ReportColors.RedLight, RoundedCornerShape(12.dp))
                                .border(1.dp, ReportColors.RedText.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.Warning, contentDescription = "Error", tint = ReportColors.RedText)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = errorMessage, color = ReportColors.RedText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🔥 Submit Button
                    Button(
                        onClick = {
                            errorMessage = when {
                                computer.id.isBlank() -> "Invalid computer selected"
                                softwareName.isBlank() -> "Software name is required"
                                issueType.isBlank() -> "Issue type is required"
                                description.isBlank() -> "Detailed description is required"
                                severity.isBlank() -> "Severity level is required"
                                else -> ""
                            }

                            if (errorMessage.isBlank()) {
                                onSubmitClick(
                                    softwareName.trim(),
                                    issueType.trim(),
                                    description.trim(),
                                    severity.trim()
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = ReportColors.PrimaryIndigo.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(listOf(ReportColors.PrimaryIndigo, ReportColors.PurpleAccent)),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Submit Issue Report", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
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
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = ReportColors.TextMuted) },
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
            focusedContainerColor = ReportColors.ModernBg,
            unfocusedContainerColor = ReportColors.ModernBg,
            focusedBorderColor = ReportColors.PrimaryIndigo,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = ReportColors.TextDark,
            unfocusedTextColor = ReportColors.TextDark,
            cursorColor = ReportColors.PrimaryIndigo
        )
    )
}