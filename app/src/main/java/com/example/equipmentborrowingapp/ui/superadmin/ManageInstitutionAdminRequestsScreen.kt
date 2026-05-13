
package com.example.equipmentborrowingapp.ui.superadmin
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Approve the request first. Then manually create the admin account and mark it as created.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ManualAdminWorkflowInstructionCard()
            }

            if (requestList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "No admin request found.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            } else {
                items(requestList) { request ->
                    InstitutionAdminRequestCard(
                        request = request,
                        onApproveClick = onApproveClick,
                        onRejectClick = onRejectClick,
                        onMarkCreatedClick = onMarkCreatedClick
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun ManualAdminWorkflowInstructionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Manual Admin Account Creation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E40AF)
            )

            Text(
                text = "Step 1: Approve request",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1F2937)
            )

            Text(
                text = "Step 2: Create admin in Firebase Authentication",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1F2937)
            )

            Text(
                text = "Step 3: Create Firestore users/{uid} document manually",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1F2937)
            )

            Text(
                text = "Step 4: Press \"I Created Admin Manually\"",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1F2937)
            )
        }
    }
}

@Composable
fun AdminRequestStatusBadge(status: String) {
    val normalizedStatus = status.trim().ifBlank { "Unknown" }

    val backgroundColor = when (normalizedStatus.lowercase()) {
        "pending" -> Color(0xFFFEF3C7)
        "approved" -> Color(0xFFE0F2FE)
        "rejected" -> Color(0xFFFEE2E2)
        "created" -> Color(0xFFDCFCE7)
        else -> Color(0xFFE5E7EB)
    }

    val textColor = when (normalizedStatus.lowercase()) {
        "pending" -> Color(0xFF92400E)
        "approved" -> Color(0xFF0369A1)
        "rejected" -> Color(0xFFB91C1C)
        "created" -> Color(0xFF166534)
        else -> Color(0xFF374151)
    }

    Text(
        text = normalizedStatus,
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = textColor
    )
}

@Composable
fun InstitutionAdminRequestCard(
    request: InstitutionAdminRequest,
    onApproveClick: (InstitutionAdminRequest) -> Unit,
    onRejectClick: (InstitutionAdminRequest) -> Unit,
    onMarkCreatedClick: (InstitutionAdminRequest) -> Unit
) {
    val normalizedStatus = request.status.trim().lowercase()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.adminName.ifBlank { "Unnamed Admin" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.weight(1f)
                )

                AdminRequestStatusBadge(
                    status = request.status.ifBlank { "Unknown" }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RequestInfoRow(
                    label = "Admin Name",
                    value = request.adminName.ifBlank { "N/A" }
                )

                RequestInfoRow(
                    label = "Admin Email",
                    value = request.adminEmail.ifBlank { "N/A" }
                )

                RequestInfoRow(
                    label = "Institution Name",
                    value = request.institutionName.ifBlank { "N/A" }
                )

                RequestInfoRow(
                    label = "Institution ID",
                    value = request.institutionId.ifBlank { "N/A" }
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            when (normalizedStatus) {
                "pending" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onRejectClick(request)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFFDC2626)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFDC2626)
                            )
                        ) {
                            Text("Reject")
                        }

                        Button(
                            onClick = {
                                onApproveClick(request)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2563EB),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Approve")
                        }
                    }
                }

                "approved" -> {
                    Text(
                        text = "Now create this admin manually in Firebase Authentication and Firestore users/{uid}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )

                    Button(
                        onClick = {
                            onMarkCreatedClick(request)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF16A34A),
                            contentColor = Color.White
                        )
                    ) {
                        Text("I Created Admin Manually")
                    }
                }

                "rejected" -> {
                    Text(
                        text = "This request has been rejected.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB91C1C)
                    )
                }

                "created" -> {
                    Text(
                        text = "Admin account has been manually created.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF166534)
                    )
                }

                else -> {
                    Text(
                        text = "No action available for this request status.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
fun RequestInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6B7280),
            modifier = Modifier.weight(0.42f)
        )

        Text(
            text = value.ifBlank { "N/A" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (
                label == "Admin Email" ||
                label == "Institution ID"
            ) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = Color(0xFF111827),
            modifier = Modifier.weight(0.58f)
        )
    }
}

