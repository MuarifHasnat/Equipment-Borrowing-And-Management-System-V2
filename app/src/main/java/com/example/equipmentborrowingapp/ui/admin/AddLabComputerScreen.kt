package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F7FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Lab Monitoring",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )

            Text(
                text = "Add Lab Computer",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = pcName,
                onValueChange = {
                    pcName = it
                    errorMessage = ""
                },
                label = { Text("PC Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = labRoom,
                onValueChange = {
                    labRoom = it
                    errorMessage = ""
                },
                label = { Text("Lab Room") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = locationNote,
                onValueChange = {
                    locationNote = it
                    errorMessage = ""
                },
                label = { Text("Location Note") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = ipAddress,
                onValueChange = {
                    ipAddress = it
                    errorMessage = ""
                },
                label = { Text("IP Address (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { statusExpanded = true },
                    enabled = false
                )

                DropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOptions.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                status = item
                                statusExpanded = false
                                errorMessage = ""
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Lab Computer")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}