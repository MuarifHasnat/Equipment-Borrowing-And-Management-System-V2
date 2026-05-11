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
import com.example.equipmentborrowingapp.data.model.Institution

@Composable
fun ManageInstitutionsScreen(
    institutionList: List<Institution>,
    onCreateInstitutionClick: () -> Unit,
    onApproveClick: (Institution) -> Unit,
    onRejectClick: (Institution) -> Unit,
    onSuspendClick: (Institution) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Manage Institutions",
            style = MaterialTheme.typography.headlineSmall
        )

        Button(
            onClick = onCreateInstitutionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create New Institution")
        }

        if (institutionList.isEmpty()) {
            Text(
                text = "No institution found.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(institutionList) { institution ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = institution.name.ifBlank { "Unnamed Institution" },
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("ID: ${institution.id.ifBlank { "N/A" }}")
                            Text("Short Name: ${institution.shortName.ifBlank { "N/A" }}")
                            Text("Email Domain: ${institution.emailDomain.ifBlank { "N/A" }}")
                            Text("Type: ${institution.type.ifBlank { "N/A" }}")
                            Text("Status: ${institution.status.ifBlank { "N/A" }}")

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onApproveClick(institution)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Approve")
                                }

                                OutlinedButton(
                                    onClick = {
                                        onRejectClick(institution)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reject")
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    onSuspendClick(institution)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Suspend")
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