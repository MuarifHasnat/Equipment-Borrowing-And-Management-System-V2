package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.Room

@Composable
fun ManageRoomsScreen(
    roomList: List<Room>,
    onAddRoomClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Manage Rooms / Labs",
            style = MaterialTheme.typography.headlineSmall
        )

        Button(
            onClick = onAddRoomClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Room / Lab")
        }

        if (roomList.isEmpty()) {
            Text("No room or lab added yet.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(roomList) { room ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = room.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Type: ${room.roomType.ifBlank { "N/A" }}")
                            Text("Building: ${room.building.ifBlank { "N/A" }}")
                            Text("Floor: ${room.floor.ifBlank { "N/A" }}")
                            Text("Department: ${room.department.ifBlank { "N/A" }}")
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}