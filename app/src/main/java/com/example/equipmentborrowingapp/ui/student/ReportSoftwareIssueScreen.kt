package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.ui.common.*
import com.example.equipmentborrowingapp.ui.theme.*

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // 🔥 HEADER
        GradientHeaderCard(
            title = "Report Issue",
            subtitle = computer.pcName.ifBlank { "Lab PC" },
            description = "Submit software issues quickly for admin review."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                SectionTitle("Issue Details")

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

                // 🔥 ISSUE TYPE
                OutlinedTextField(
                    value = issueType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Issue Type") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { issueExpanded = true }
                )

                DropdownMenu(
                    expanded = issueExpanded,
                    onDismissRequest = { issueExpanded = false }
                ) {
                    issueTypes.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                issueType = it
                                issueExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 🔥 SEVERITY
                OutlinedTextField(
                    value = severity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Severity") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { severityExpanded = true }
                )

                DropdownMenu(
                    expanded = severityExpanded,
                    onDismissRequest = { severityExpanded = false }
                ) {
                    severityOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                severity = it
                                severityExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        errorMessage = ""
                    },
                    label = { Text("Issue Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryActionButton(
                    text = "Submit Issue Report",
                    onClick = {
                        errorMessage = when {
                            computer.id.isBlank() -> "Invalid computer selected"
                            softwareName.isBlank() -> "Software name is required"
                            issueType.isBlank() -> "Issue type is required"
                            description.isBlank() -> "Issue description is required"
                            severity.isBlank() -> "Severity is required"
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
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                SecondaryActionButton(
                    text = "Back",
                    onClick = onBackClick
                )
            }
        }
    }
}