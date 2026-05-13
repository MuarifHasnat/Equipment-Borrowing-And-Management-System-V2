package com.example.equipmentborrowingapp.ui.superadmin

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
import com.example.equipmentborrowingapp.data.model.InstitutionAdminRequest

@Composable
fun ManageInstitutionAdminRequestsScreen(
    requestList: List<InstitutionAdminRequest>,
    onApproveClick: (InstitutionAdminRequest) -> Unit,
    onRejectClick: (InstitutionAdminRequest) -> Unit,
    onMarkCreatedClick: (InstitutionAdminRequest) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Institution Admin Requests",
            style = MaterialTheme.typography.headlineSmall
        )

        if (requestList.isEmpty()) {
            Text(
                text = "No admin request found.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(requestList) { request ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = request.adminName.ifBlank { "Unnamed Admin" },
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Email: ${request.adminEmail.ifBlank { "N/A" }}")
                            Text("Institution: ${request.institutionName.ifBlank { "N/A" }}")
                            Text("Institution ID: ${request.institutionId.ifBlank { "N/A" }}")
                            Text("Status: ${request.status.ifBlank { "N/A" }}")

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onApproveClick(request)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Approve")
                                }

                                OutlinedButton(
                                    onClick = {
                                        onRejectClick(request)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reject")
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    onMarkCreatedClick(request)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Mark Admin Account Created")
                            }
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