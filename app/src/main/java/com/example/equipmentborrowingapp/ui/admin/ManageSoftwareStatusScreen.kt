package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer

// Modern Colors
private object SoftwareManageColors {
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

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)
}

@Composable
fun ManageSoftwareStatusScreen(
    computer: LabComputer,
    softwareList: List<ComputerSoftwareStatus>,
    onAddSoftwareClick: (
        String, String, Boolean, Boolean, Boolean, Boolean, String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var softwareName by remember { mutableStateOf("") }
    var version by remember { mutableStateOf("") }
    var installed by remember { mutableStateOf(true) }
    var launchesProperly by remember { mutableStateOf(false) }
    var compileWorks by remember { mutableStateOf(false) }
    var runWorks by remember { mutableStateOf(false) }
    var remarks by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = SoftwareManageColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                        .background(SoftwareManageColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = SoftwareManageColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Software Tracking",
                        style = MaterialTheme.typography.titleLarge,
                        color = SoftwareManageColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = computer.pcName.ifBlank { "Lab PC" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = SoftwareManageColors.TextMuted
                    )
                }
            }

            // Scrollable Content
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                // 🔥 ADD SOFTWARE FORM CARD
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftwareManageColors.CardWhite)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Add Software Status",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SoftwareManageColors.TextDark
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            ModernTextField(
                                value = softwareName,
                                onValueChange = { softwareName = it; errorMessage = "" },
                                label = "Software Name"
                            )

                            ModernTextField(
                                value = version,
                                onValueChange = { version = it; errorMessage = "" },
                                label = "Version (e.g., v1.4)"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            ModernSwitchRow(
                                title = "Is Installed?",
                                checked = installed,
                                onCheckedChange = {
                                    installed = it
                                    if (!it) {
                                        launchesProperly = false
                                        compileWorks = false
                                        runWorks = false
                                    }
                                }
                            )

                            ModernSwitchRow("Launches Properly", launchesProperly, installed) { launchesProperly = it }
                            ModernSwitchRow("Compile Works", compileWorks, installed) { compileWorks = it }
                            ModernSwitchRow("Run Works", runWorks, installed) { runWorks = it }

                            Spacer(modifier = Modifier.height(12.dp))

                            ModernTextField(
                                value = remarks,
                                onValueChange = { remarks = it; errorMessage = "" },
                                label = "Remarks / Notes",
                                singleLine = false,
                                modifier = Modifier.height(80.dp)
                            )

                            if (errorMessage.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(SoftwareManageColors.RedLight, RoundedCornerShape(12.dp))
                                        .border(1.dp, SoftwareManageColors.RedText.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Rounded.Warning, contentDescription = "Error", tint = SoftwareManageColors.RedText)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = errorMessage, color = SoftwareManageColors.RedText, style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    errorMessage = when {
                                        computer.id.isBlank() -> "Invalid computer selected"
                                        softwareName.isBlank() -> "Software name is required"
                                        else -> ""
                                    }

                                    if (errorMessage.isBlank()) {
                                        onAddSoftwareClick(
                                            softwareName.trim(), version.trim(), installed,
                                            launchesProperly, compileWorks, runWorks, remarks.trim()
                                        )

                                        softwareName = ""
                                        version = ""
                                        installed = true
                                        launchesProperly = false
                                        compileWorks = false
                                        runWorks = false
                                        remarks = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SoftwareManageColors.PrimaryIndigo),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Rounded.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Status", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 🔥 TRACKED SOFTWARE LIST
                item {
                    Text(
                        text = "Tracked Software",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SoftwareManageColors.TextDark,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }

                if (softwareList.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), contentAlignment = Alignment.Center) {
                            Text("No software status found", color = SoftwareManageColors.TextMuted, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                } else {
                    items(softwareList) { item ->
                        ModernSoftwareCard(item)
                    }
                }
            }
        }
    }
}

// Helper Composables

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = SoftwareManageColors.TextMuted) },
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SoftwareManageColors.ModernBg,
            unfocusedContainerColor = SoftwareManageColors.ModernBg,
            focusedBorderColor = SoftwareManageColors.PrimaryIndigo,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = SoftwareManageColors.TextDark,
            unfocusedTextColor = SoftwareManageColors.TextDark
        )
    )
}

@Composable
private fun ModernSwitchRow(
    title: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (enabled) SoftwareManageColors.TextDark else SoftwareManageColors.TextMuted
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(checkedTrackColor = SoftwareManageColors.PrimaryIndigo)
        )
    }
}

@Composable
private fun ModernSoftwareCard(item: ComputerSoftwareStatus) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareManageColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.softwareName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SoftwareManageColors.TextDark
                )
                if (item.version.isNotBlank()) {
                    Text(
                        text = "v${item.version}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftwareManageColors.TextMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Badges Grids
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModernStatusBadge(
                        text = if (item.installed) "Installed" else "Not Installed",
                        isSuccess = item.installed
                    )
                    ModernStatusBadge(
                        text = if (item.launchesProperly) "Launch OK" else "Launch Issue",
                        isSuccess = item.launchesProperly,
                        isWarning = !item.launchesProperly && item.installed
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModernStatusBadge(
                        text = if (item.compileWorks) "Compile OK" else "Compile Issue",
                        isSuccess = item.compileWorks,
                        isWarning = !item.compileWorks && item.installed
                    )
                    ModernStatusBadge(
                        text = if (item.runWorks) "Run OK" else "Run Issue",
                        isSuccess = item.runWorks,
                        isWarning = !item.runWorks && item.installed
                    )
                }
            }

            if (item.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = SoftwareManageColors.ModernBg,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Remarks: ${item.remarks}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftwareManageColors.TextDark,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernStatusBadge(
    text: String,
    isSuccess: Boolean,
    isWarning: Boolean = false
) {
    val bgColor = when {
        isSuccess -> SoftwareManageColors.GreenLight
        isWarning -> SoftwareManageColors.OrangeLight
        else -> SoftwareManageColors.RedLight
    }

    val textColor = when {
        isSuccess -> SoftwareManageColors.GreenText
        isWarning -> SoftwareManageColors.OrangeText
        else -> SoftwareManageColors.RedText
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}