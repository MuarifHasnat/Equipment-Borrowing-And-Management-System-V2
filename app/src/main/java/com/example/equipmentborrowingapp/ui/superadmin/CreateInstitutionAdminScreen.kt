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
import com.example.equipmentborrowingapp.data.model.Institution

@Composable
fun CreateInstitutionAdminScreen(
    institutionList: List<Institution>,
    onCreateRequestClick: (
        institutionId: String,
        institutionName: String,
        adminName: String,
        adminEmail: String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedInstitution by remember { mutableStateOf<Institution?>(null) }
    var institutionMenuExpanded by remember { mutableStateOf(false) }

    var adminName by remember { mutableStateOf("") }
    var adminEmail by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Create Institution Admin",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Select Institution",
            style = MaterialTheme.typography.titleSmall
        )

        Box {
            OutlinedButton(
                onClick = {
                    institutionMenuExpanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedInstitution?.let {
                        "${it.name} (${it.id})"
                    } ?: "Choose Institution"
                )
            }

            DropdownMenu(
                expanded = institutionMenuExpanded,
                onDismissRequest = {
                    institutionMenuExpanded = false
                }
            ) {
                institutionList.forEach { institution ->
                    DropdownMenuItem(
                        text = {
                            Text("${institution.name} (${institution.id})")
                        },
                        onClick = {
                            selectedInstitution = institution
                            institutionMenuExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = adminName,
            onValueChange = {
                adminName = it
            },
            label = {
                Text("Admin Name")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = adminEmail,
            onValueChange = {
                adminEmail = it
            },
            label = {
                Text("Admin Email")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                val institution = selectedInstitution

                onCreateRequestClick(
                    institution?.id ?: "",
                    institution?.name ?: "",
                    adminName,
                    adminEmail
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Admin Request")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}