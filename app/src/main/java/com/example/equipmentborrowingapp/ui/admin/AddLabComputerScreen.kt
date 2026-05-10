package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.Room

@Composable
fun AddLabComputerScreen(
    roomList: List<Room>,
    onAddClick: (
        roomId: String,
        pcName: String,
        labRoom: String,
        locationNote: String,
        ipAddress: String,
        status: String,
        remarks: String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var roomMenuExpanded by remember { mutableStateOf(false) }

    var pcName by remember { mutableStateOf("") }
    var labRoom by remember { mutableStateOf("") }
    var locationNote by remember { mutableStateOf("") }
    var ipAddress by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Working") }
    var remarks by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Add Lab Computer",
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
                }
            ) {
                roomList.forEach { room ->
                    DropdownMenuItem(
                        text = {
                            Text("${room.name} - ${room.roomType}")
                        },
                        onClick = {
                            selectedRoom = room
                            labRoom = room.name
                            roomMenuExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = pcName,
            onValueChange = {
                pcName = it
            },
            label = {
                Text("PC Name")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = labRoom,
            onValueChange = {
                labRoom = it
            },
            label = {
                Text("Lab Room")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = locationNote,
            onValueChange = {
                locationNote = it
            },
            label = {
                Text("Location Note")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ipAddress,
            onValueChange = {
                ipAddress = it
            },
            label = {
                Text("IP Address")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = status,
            onValueChange = {
                status = it
            },
            label = {
                Text("Status")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = remarks,
            onValueChange = {
                remarks = it
            },
            label = {
                Text("Remarks")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onAddClick(
                    selectedRoom?.id ?: "",
                    pcName,
                    labRoom,
                    locationNote,
                    ipAddress,
                    status,
                    remarks
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Computer")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}