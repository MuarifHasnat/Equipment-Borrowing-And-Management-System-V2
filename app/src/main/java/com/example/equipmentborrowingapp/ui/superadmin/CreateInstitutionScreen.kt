package com.example.equipmentborrowingapp.ui.superadmin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateInstitutionScreen(
    onCreateClick: (
        institutionId: String,
        name: String,
        shortName: String,
        emailDomain: String,
        type: String,
        status: String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var institutionId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var shortName by remember { mutableStateOf("") }
    var emailDomain by remember { mutableStateOf("") }

    var selectedType by remember { mutableStateOf("university") }
    var selectedStatus by remember { mutableStateOf("Approved") }

    var typeExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val typeOptions = listOf("university", "college", "institute")
    val statusOptions = listOf("Approved", "Pending", "Rejected", "Suspended")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Create Institution",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = institutionId,
            onValueChange = { institutionId = it },
            label = { Text("Institution ID / Code") },
            placeholder = { Text("Example: 1090") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Institution Name") },
            placeholder = { Text("Example: Test University") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = shortName,
            onValueChange = { shortName = it },
            label = { Text("Short Name") },
            placeholder = { Text("Example: TU") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = emailDomain,
            onValueChange = { emailDomain = it },
            label = { Text("Email Domain") },
            placeholder = { Text("Example: tu.edu") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(
            text = "Institution Type",
            style = MaterialTheme.typography.titleSmall
        )

        Box {
            OutlinedButton(
                onClick = { typeExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedType)
            }

            DropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false }
            ) {
                typeOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedType = option
                            typeExpanded = false
                        }
                    )
                }
            }
        }

        Text(
            text = "Status",
            style = MaterialTheme.typography.titleSmall
        )

        Box {
            OutlinedButton(
                onClick = { statusExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedStatus)
            }

            DropdownMenu(
                expanded = statusExpanded,
                onDismissRequest = { statusExpanded = false }
            ) {
                statusOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedStatus = option
                            statusExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                onCreateClick(
                    institutionId,
                    name,
                    shortName,
                    emailDomain,
                    selectedType,
                    selectedStatus
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Institution")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}