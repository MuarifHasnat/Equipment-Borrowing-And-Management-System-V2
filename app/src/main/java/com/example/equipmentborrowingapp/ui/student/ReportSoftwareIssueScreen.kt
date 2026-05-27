package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer

private object ReportIssueColors {
    val Background = Color(0xFFF4F7FB)
    val Card = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val Border = Color(0xFFE2E8F0)

    val Primary = Color(0xFF4F46E5)
    val PrimarySoft = Color(0xFFEEF2FF)
    val Purple = Color(0xFF7C3AED)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)
    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)
    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)
    val GrayLight = Color(0xFFF1F5F9)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSoftwareIssueScreen(
    computer: LabComputer,
    softwareList: List<ComputerSoftwareStatus> = emptyList(),
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ReportIssueColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            ReportTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(18.dp))

            TargetComputerCard(computer = computer)

            Spacer(modifier = Modifier.height(10.dp))

            InstalledSoftwareForReportCard(
                softwareList = softwareList,
                onSoftwareSelected = { software ->
                    softwareName = software.softwareName
                    errorMessage = ""
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = ReportIssueColors.Card),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Issue Information",
                        color = ReportIssueColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Fill in the software problem details clearly so admin can review it faster.",
                        color = ReportIssueColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    StudentReportTextField(
                        value = softwareName,
                        onValueChange = {
                            softwareName = it
                            errorMessage = ""
                        },
                        label = "Software Name",
                        placeholder = "Example: Android Studio, VS Code"
                    )

                    ExposedDropdownMenuBox(
                        expanded = issueExpanded,
                        onExpandedChange = { issueExpanded = !issueExpanded }
                    ) {
                        StudentReportTextField(
                            value = issueType,
                            onValueChange = {},
                            label = "Issue Type",
                            placeholder = "Select issue type",
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = issueExpanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = issueExpanded,
                            onDismissRequest = { issueExpanded = false },
                            modifier = Modifier.background(ReportIssueColors.Card)
                        ) {
                            issueTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = type,
                                            color = ReportIssueColors.TextDark
                                        )
                                    },
                                    onClick = {
                                        issueType = type
                                        issueExpanded = false
                                        errorMessage = ""
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = severityExpanded,
                        onExpandedChange = { severityExpanded = !severityExpanded }
                    ) {
                        StudentReportTextField(
                            value = severity,
                            onValueChange = {},
                            label = "Severity Level",
                            placeholder = "Select severity",
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = severityExpanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = severityExpanded,
                            onDismissRequest = { severityExpanded = false },
                            modifier = Modifier.background(ReportIssueColors.Card)
                        ) {
                            severityOptions.forEach { level ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            SeverityDot(severity = level)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = level,
                                                color = ReportIssueColors.TextDark
                                            )
                                        }
                                    },
                                    onClick = {
                                        severity = level
                                        severityExpanded = false
                                        errorMessage = ""
                                    }
                                )
                            }
                        }
                    }

                    SeverityPreview(severity = severity)

                    Spacer(modifier = Modifier.height(10.dp))

                    StudentReportTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            errorMessage = ""
                        },
                        label = "Detailed Description",
                        placeholder = "Write what happens, when it happens, and any error message shown.",
                        singleLine = false,
                        modifier = Modifier.heightIn(min = 130.dp)
                    )

                    if (errorMessage.isNotBlank()) {
                        ErrorBox(message = errorMessage)
                    }

                    Spacer(modifier = Modifier.height(22.dp))

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
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ReportIssueColors.Primary,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit Issue Report",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ReportTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .background(ReportIssueColors.Card, RoundedCornerShape(14.dp))
                .border(1.dp, ReportIssueColors.Border, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = ReportIssueColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Report Software Issue",
                color = ReportIssueColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Send a clear issue report to admin",
                color = ReportIssueColors.TextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun TargetComputerCard(
    computer: LabComputer
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ReportIssueColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(ReportIssueColors.PrimarySoft, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (computer.computerImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = computer.computerImageUrl.trim(),
                            contentDescription = computer.pcName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = ReportIssueColors.Primary,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Selected Lab PC",
                        color = ReportIssueColors.TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = computer.pcName.ifBlank { "Unknown PC" },
                        color = ReportIssueColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                StatusBadge(status = computer.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoChip(
                    title = "Lab",
                    value = computer.labRoom.ifBlank { "Not set" },
                    modifier = Modifier.weight(1f)
                )
                InfoChip(
                    title = "IP",
                    value = computer.ipAddress.ifBlank { "Not set" },
                    modifier = Modifier.weight(1f)
                )
            }

            if (computer.locationNote.isNotBlank() || computer.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                InfoChip(
                    title = "Note",
                    value = when {
                        computer.locationNote.isNotBlank() -> computer.locationNote
                        else -> computer.remarks
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
private fun InstalledSoftwareForReportCard(
    softwareList: List<ComputerSoftwareStatus>,
    onSoftwareSelected: (ComputerSoftwareStatus) -> Unit
) {
    val installedSoftware = softwareList
        .filter { it.installed }
        .sortedBy { it.softwareName.lowercase() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ReportIssueColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Installed Software on this PC",
                color = ReportIssueColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Tap a software name to report an issue for it.",
                color = ReportIssueColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(9.dp))

            if (installedSoftware.isEmpty()) {
                Text(
                    text = "No installed software status added by admin yet.",
                    color = ReportIssueColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    installedSoftware.forEach { software ->
                        Button(
                            onClick = { onSoftwareSelected(software) },
                            modifier = Modifier.height(42.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ReportIssueColors.PrimarySoft,
                                contentColor = ReportIssueColors.Primary
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.55f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (software.softwareLogoUrl.isNotBlank()) {
                                    AsyncImage(
                                        model = software.softwareLogoUrl.trim(),
                                        contentDescription = software.softwareName,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(3.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.Code,
                                        contentDescription = null,
                                        tint = ReportIssueColors.Primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(7.dp))

                            Column {
                                Text(
                                    text = software.softwareName.ifBlank { "Unknown" },
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "v${software.version.ifBlank { "N/A" }}",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoChip(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(ReportIssueColors.GrayLight, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            color = ReportIssueColors.TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = ReportIssueColors.TextDark,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusBadge(status: String) {
    val normalizedStatus = status.lowercase()
    val background = when {
        normalizedStatus.contains("active") -> ReportIssueColors.GreenLight
        normalizedStatus.contains("maintenance") -> ReportIssueColors.OrangeLight
        normalizedStatus.contains("inactive") -> ReportIssueColors.RedLight
        else -> ReportIssueColors.GrayLight
    }
    val textColor = when {
        normalizedStatus.contains("active") -> ReportIssueColors.GreenText
        normalizedStatus.contains("maintenance") -> ReportIssueColors.OrangeText
        normalizedStatus.contains("inactive") -> ReportIssueColors.RedText
        else -> ReportIssueColors.TextMuted
    }

    Text(
        text = status.ifBlank { "Unknown" },
        color = textColor,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(background, RoundedCornerShape(50.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
private fun SeverityPreview(severity: String) {
    val background = when (severity) {
        "High" -> ReportIssueColors.RedLight
        "Low" -> ReportIssueColors.GreenLight
        else -> ReportIssueColors.OrangeLight
    }
    val textColor = when (severity) {
        "High" -> ReportIssueColors.RedText
        "Low" -> ReportIssueColors.GreenText
        else -> ReportIssueColors.OrangeText
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SeverityDot(severity = severity)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$severity priority selected",
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SeverityDot(severity: String) {
    val dotColor = when (severity) {
        "High" -> ReportIssueColors.RedText
        "Low" -> ReportIssueColors.GreenText
        else -> ReportIssueColors.OrangeText
    }

    Box(
        modifier = Modifier
            .size(10.dp)
            .background(dotColor, RoundedCornerShape(50.dp))
    )
}

@Composable
private fun ErrorBox(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ReportIssueColors.RedLight, RoundedCornerShape(14.dp))
            .border(1.dp, ReportIssueColors.RedText.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = null,
            tint = ReportIssueColors.RedText,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message,
            color = ReportIssueColors.RedText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun StudentReportTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = ReportIssueColors.TextMuted,
                fontWeight = FontWeight.SemiBold
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = ReportIssueColors.TextMuted.copy(alpha = 0.75f)
            )
        },
        singleLine = singleLine,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ReportIssueColors.Background,
            unfocusedContainerColor = ReportIssueColors.Background,
            focusedBorderColor = ReportIssueColors.Primary,
            unfocusedBorderColor = ReportIssueColors.Border,
            focusedTextColor = ReportIssueColors.TextDark,
            unfocusedTextColor = ReportIssueColors.TextDark,
            cursorColor = ReportIssueColors.Primary
        )
    )
}
