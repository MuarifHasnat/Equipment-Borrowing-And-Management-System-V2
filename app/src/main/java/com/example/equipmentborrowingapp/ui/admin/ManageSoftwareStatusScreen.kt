package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.ui.common.*
import com.example.equipmentborrowingapp.ui.theme.*

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔥 HEADER
        GradientHeaderCard(
            title = "Software Tracking",
            subtitle = computer.pcName.ifBlank { "Lab PC" },
            description = "Manage and monitor software status for this computer."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 FORM CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                SectionTitle("Add Software Status")

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = softwareName,
                    onValueChange = {
                        softwareName = it
                        errorMessage = ""
                    },
                    label = { Text("Software Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = version,
                    onValueChange = {
                        version = it
                        errorMessage = ""
                    },
                    label = { Text("Version") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                StatusCheckboxRow("Installed", installed) {
                    installed = it
                    if (!it) {
                        launchesProperly = false
                        compileWorks = false
                        runWorks = false
                    }
                }

                StatusCheckboxRow("Launches Properly", launchesProperly, installed) {
                    launchesProperly = it
                }

                StatusCheckboxRow("Compile Works", compileWorks, installed) {
                    compileWorks = it
                }

                StatusCheckboxRow("Run Works", runWorks, installed) {
                    runWorks = it
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = remarks,
                    onValueChange = {
                        remarks = it
                        errorMessage = ""
                    },
                    label = { Text("Remarks") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(14.dp))

                PrimaryActionButton(
                    text = "Add Software Status",
                    onClick = {
                        errorMessage = when {
                            computer.id.isBlank() -> "Invalid computer selected"
                            softwareName.isBlank() -> "Software name is required"
                            else -> ""
                        }

                        if (errorMessage.isBlank()) {
                            onAddSoftwareClick(
                                softwareName.trim(),
                                version.trim(),
                                installed,
                                launchesProperly,
                                compileWorks,
                                runWorks,
                                remarks.trim()
                            )

                            softwareName = ""
                            version = ""
                            installed = true
                            launchesProperly = false
                            compileWorks = false
                            runWorks = false
                            remarks = ""
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        SectionTitle("Tracked Software")

        Spacer(modifier = Modifier.height(10.dp))

        if (softwareList.isEmpty()) {
            EmptyStateText("No software status found", Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(softwareList) { item ->
                    ModernSoftwareCard(item)
                }
            }
        }

        SecondaryActionButton(
            text = "Back",
            onClick = onBackClick
        )
    }
}

@Composable
private fun StatusCheckboxRow(
    text: String,
    checked: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onChange, enabled = enabled)
        Text(text = text)
    }
}

@Composable
private fun ModernSoftwareCard(item: ComputerSoftwareStatus) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Text(
                text = item.softwareName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (item.version.isNotBlank()) {
                Text(text = "Version: ${item.version}")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                AppStatusBadge(
                    text = if (item.installed) "Installed" else "Not Installed",
                    backgroundColor = if (item.installed) SuccessLight else ErrorLight,
                    textColor = TextPrimary
                )

                AppStatusBadge(
                    text = if (item.launchesProperly) "Launch OK" else "Issue",
                    backgroundColor = if (item.launchesProperly) InfoLight else WarningLight,
                    textColor = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                AppStatusBadge(
                    text = if (item.compileWorks) "Compile OK" else "Compile Issue",
                    backgroundColor = if (item.compileWorks) SuccessLight else ErrorLight,
                    textColor = TextPrimary
                )

                AppStatusBadge(
                    text = if (item.runWorks) "Run OK" else "Run Issue",
                    backgroundColor = if (item.runWorks) SuccessLight else ErrorLight,
                    textColor = TextPrimary
                )
            }

            if (item.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Remarks: ${item.remarks}")
            }
        }
    }
}