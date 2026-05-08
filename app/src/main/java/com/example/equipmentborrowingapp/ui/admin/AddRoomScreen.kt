package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddRoomScreen(
    onAddClick: (
        name: String,
        building: String,
        floor: String,
        roomType: String,
        department: String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var building by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var roomType by remember { mutableStateOf("Lab") }
    var department by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Add Room / Lab",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Room / Lab Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = building,
            onValueChange = { building = it },
            label = { Text("Building") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = floor,
            onValueChange = { floor = it },
            label = { Text("Floor") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = roomType,
            onValueChange = { roomType = it },
            label = { Text("Room Type") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                onAddClick(name, building, floor, roomType, department)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Room / Lab")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}