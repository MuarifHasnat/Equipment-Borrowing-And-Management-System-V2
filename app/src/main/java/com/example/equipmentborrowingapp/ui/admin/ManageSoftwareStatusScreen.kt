package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer

private object SoftwareStatusColors {
    val Bg = Color(0xFFF4F7FB)
    val Card = Color.White
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val Primary = Color(0xFF4F46E5)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)

    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun ManageSoftwareStatusScreen(
    computer: LabComputer,
    softwareList: List<ComputerSoftwareStatus>,
    onAddSoftwareClick: (
        String, String, String, Boolean, Boolean, Boolean, Boolean, String
    ) -> Unit,
    onUpdateSoftwareClick: (ComputerSoftwareStatus) -> Unit = {},
    onDeleteSoftwareClick: (ComputerSoftwareStatus) -> Unit = {},
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingSoftware by remember { mutableStateOf<ComputerSoftwareStatus?>(null) }
    var deletingSoftware by remember { mutableStateOf<ComputerSoftwareStatus?>(null) }

    val filteredList = softwareList.filter { software ->
        val query = searchText.trim().lowercase()

        val matchesSearch =
            query.isBlank() ||
                    software.softwareName.lowercase().contains(query) ||
                    software.version.lowercase().contains(query) ||
                    software.remarks.lowercase().contains(query)

        val isOk = software.installed &&
                software.launchesProperly &&
                software.compileWorks &&
                software.runWorks

        val matchesFilter = when (selectedFilter) {
            "Installed" -> software.installed
            "Problem" -> !isOk
            "All OK" -> isOk
            else -> true
        }

        matchesSearch && matchesFilter
    }.sortedBy { it.softwareName.lowercase() }

    val installedCount = softwareList.count { it.installed }
    val okCount = softwareList.count {
        it.installed && it.launchesProperly && it.compileWorks && it.runWorks
    }
    val problemCount = softwareList.size - okCount

    if (showAddDialog) {
        SoftwareEditDialog(
            title = "Add Software",
            initial = null,
            onDismiss = { showAddDialog = false },
            onSave = { name, version, logoUrl, installed, launches, compile, run, remarks ->
                onAddSoftwareClick(name, version, logoUrl, installed, launches, compile, run, remarks)
                showAddDialog = false
            }
        )
    }

    editingSoftware?.let { software ->
        SoftwareEditDialog(
            title = "Edit Software",
            initial = software,
            onDismiss = { editingSoftware = null },
            onSave = { name, version, logoUrl, installed, launches, compile, run, remarks ->
                onUpdateSoftwareClick(
                    software.copy(
                        softwareName = name,
                        version = version,
                        softwareLogoUrl = logoUrl,
                        installed = installed,
                        launchesProperly = launches,
                        compileWorks = compile,
                        runWorks = run,
                        remarks = remarks,
                        checkedAt = System.currentTimeMillis()
                    )
                )
                editingSoftware = null
            }
        )
    }

    deletingSoftware?.let { software ->
        AlertDialog(
            onDismissRequest = { deletingSoftware = null },
            shape = RoundedCornerShape(22.dp),
            containerColor = SoftwareStatusColors.Card,
            title = {
                Text(
                    text = "Delete Software",
                    fontWeight = FontWeight.Bold,
                    color = SoftwareStatusColors.TextDark
                )
            },
            text = {
                Text(
                    text = "Delete ${software.softwareName.ifBlank { "this software" }} from this computer?",
                    color = SoftwareStatusColors.TextMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteSoftwareClick(software)
                        deletingSoftware = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftwareStatusColors.RedText,
                        contentColor = Color.White
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingSoftware = null }) {
                    Text("Cancel", color = SoftwareStatusColors.TextMuted)
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SoftwareStatusColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            SoftwareStatusTopBar(
                computerName = computer.pcName.ifBlank { "Lab Computer" },
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SoftwareMiniStat("Total", softwareList.size.toString(), SoftwareStatusColors.BlueLight, SoftwareStatusColors.BlueText, Modifier.weight(1f))
                SoftwareMiniStat("Installed", installedCount.toString(), SoftwareStatusColors.PurpleLight, SoftwareStatusColors.PurpleText, Modifier.weight(1f))
                SoftwareMiniStat("Problem", problemCount.toString(), SoftwareStatusColors.RedLight, SoftwareStatusColors.RedText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftwareStatusColors.Primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Add Software", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = SoftwareStatusColors.TextMuted
                    )
                },
                label = { Text("Search software") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Installed", "All OK", "Problem").forEach { filter ->
                    SoftwareStatusFilterChip(
                        text = filter,
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Showing ${filteredList.size} of ${softwareList.size} software",
                color = SoftwareStatusColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredList.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftwareStatusColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No software status found.",
                            color = SoftwareStatusColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(filteredList) { software ->
                        SoftwareStatusCard(
                            software = software,
                            onEditClick = { editingSoftware = software },
                            onDeleteClick = { deletingSoftware = software }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SoftwareStatusTopBar(
    computerName: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .background(SoftwareStatusColors.Card, RoundedCornerShape(15.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = SoftwareStatusColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(46.dp)
                .background(SoftwareStatusColors.PurpleLight, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Code,
                contentDescription = null,
                tint = SoftwareStatusColors.Primary,
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Software Status",
                color = SoftwareStatusColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = computerName,
                color = SoftwareStatusColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SoftwareStatusCard(
    software: ComputerSoftwareStatus,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val allOk = software.installed &&
            software.launchesProperly &&
            software.compileWorks &&
            software.runWorks

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareStatusColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(SoftwareStatusColors.PurpleLight, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (software.softwareLogoUrl.isNotBlank()) {
                        AsyncImage(
                            model = software.softwareLogoUrl.trim(),
                            contentDescription = software.softwareName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Code,
                            contentDescription = null,
                            tint = SoftwareStatusColors.Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = software.softwareName.ifBlank { "Unknown Software" },
                        color = SoftwareStatusColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Version: ${software.version.ifBlank { "N/A" }}",
                        color = SoftwareStatusColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }

                Text(
                    text = if (allOk) "OK" else "Check",
                    modifier = Modifier
                        .background(
                            if (allOk) SoftwareStatusColors.GreenLight else SoftwareStatusColors.OrangeLight,
                            RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = if (allOk) SoftwareStatusColors.GreenText else SoftwareStatusColors.OrangeText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = SoftwareStatusColors.Bg)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SoftwareFlag("Installed", software.installed, Modifier.weight(1f))
                SoftwareFlag("Launch", software.launchesProperly, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SoftwareFlag("Compile", software.compileWorks, Modifier.weight(1f))
                SoftwareFlag("Run", software.runWorks, Modifier.weight(1f))
            }

            if (software.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = software.remarks,
                    color = SoftwareStatusColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftwareStatusColors.Primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("Edit", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SoftwareStatusColors.RedText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SoftwareFlag(
    title: String,
    checked: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(38.dp),
        color = if (checked) SoftwareStatusColors.GreenLight else SoftwareStatusColors.RedLight,
        shape = RoundedCornerShape(13.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (checked) Icons.Rounded.CheckCircle else Icons.Rounded.Close,
                contentDescription = null,
                tint = if (checked) SoftwareStatusColors.GreenText else SoftwareStatusColors.RedText,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = title,
                color = if (checked) SoftwareStatusColors.GreenText else SoftwareStatusColors.RedText,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SoftwareMiniStat(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(58.dp),
        color = bgColor,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SoftwareStatusFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(34.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftwareStatusColors.Primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(34.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
private fun SoftwareEditDialog(
    title: String,
    initial: ComputerSoftwareStatus?,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        version: String,
        logoUrl: String,
        installed: Boolean,
        launches: Boolean,
        compile: Boolean,
        run: Boolean,
        remarks: String
    ) -> Unit
) {
    var name by remember(initial) { mutableStateOf(initial?.softwareName ?: "") }
    var version by remember(initial) { mutableStateOf(initial?.version ?: "") }
    var logoUrl by remember(initial) { mutableStateOf(initial?.softwareLogoUrl ?: "") }
    var installed by remember(initial) { mutableStateOf(initial?.installed ?: true) }
    var launches by remember(initial) { mutableStateOf(initial?.launchesProperly ?: true) }
    var compile by remember(initial) { mutableStateOf(initial?.compileWorks ?: true) }
    var run by remember(initial) { mutableStateOf(initial?.runWorks ?: true) }
    var remarks by remember(initial) { mutableStateOf(initial?.remarks ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = SoftwareStatusColors.Card,
        title = {
            Text(
                text = title,
                color = SoftwareStatusColors.TextDark,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Software Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = version,
                    onValueChange = { version = it },
                    label = { Text("Version") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = logoUrl,
                    onValueChange = { logoUrl = it },
                    label = { Text("Software Logo URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                StatusSwitchRow("Installed", installed) { installed = it }
                StatusSwitchRow("Launches Properly", launches) { launches = it }
                StatusSwitchRow("Compile Works", compile) { compile = it }
                StatusSwitchRow("Run Works", run) { run = it }

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Remarks") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(14.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        name.trim(),
                        version.trim(),
                        logoUrl.trim(),
                        installed,
                        launches,
                        compile,
                        run,
                        remarks.trim()
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftwareStatusColors.Primary,
                    contentColor = Color.White
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SoftwareStatusColors.TextMuted)
            }
        }
    )
}

@Composable
private fun StatusSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftwareStatusColors.Bg, RoundedCornerShape(13.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = SoftwareStatusColors.TextDark,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
