package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.Room

@Composable
fun AddEquipmentScreen(
    roomList: List<Room>,
    onAddClick: (
        name: String,
        description: String,
        condition: String,
        totalQuantity: String,
        availableQuantity: String,
        category: String,
        imageName: String,
        imageUrl: String,
        isBorrowable: Boolean,
        roomId: String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var totalQuantity by remember { mutableStateOf("") }
    var availableQuantity by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageName by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isBorrowable by remember { mutableStateOf(true) }

    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var roomMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Add Equipment",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Select Room / Lab",
            style = MaterialTheme.typography.titleSmall
        )

        Box {
            OutlinedButton(
                onClick = {
                    roomMenuExpanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedRoom?.name ?: "Choose Room / Lab"
                )
            }

            DropdownMenu(
                expanded = roomMenuExpanded,
                onDismissRequest = {
                    roomMenuExpanded = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                roomList.forEach { room ->
                    DropdownMenuItem(
                        text = {
                            Text("${room.name} - ${room.roomType}")
                        },
                        onClick = {
                            selectedRoom = room
                            roomMenuExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Equipment Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = condition,
            onValueChange = { condition = it },
            label = { Text("Condition") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = totalQuantity,
            onValueChange = { totalQuantity = it },
            label = { Text("Total Quantity") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = availableQuantity,
            onValueChange = { availableQuantity = it },
            label = { Text("Available Quantity") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imageName,
            onValueChange = { imageName = it },
            label = { Text("Image Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Borrowable",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = isBorrowable,
                onCheckedChange = {
                    isBorrowable = it
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onAddClick(
                    name,
                    description,
                    condition,
                    totalQuantity,
                    availableQuantity,
                    category,
                    imageName,
                    imageUrl,
                    isBorrowable,
                    selectedRoom?.id ?: ""
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Equipment")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}