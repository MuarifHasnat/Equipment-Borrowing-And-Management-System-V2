package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.BorrowRequest

@Composable
fun MyRequestsScreen(
    requestList: List<BorrowRequest>,
    onBackClick: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf("All") }

    val statusFilters = listOf(
        "All",
        "Pending",
        "Approved",
        "Issued",
        "Returned",
        "Rejected",
        "Overdue",
        "Lost",
        "Damaged",
        "Cancelled"
    )

    val filteredRequests = requestList
        .filter { request ->
            selectedStatus == "All" ||
                    request.status.equals(selectedStatus, ignoreCase = true)
        }
        .sortedByDescending { it.requestTimestamp }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "My Requests",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Text(
            text = "Track your pending, approved, issued, returned, rejected and overdue requests.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            statusFilters.forEach { status ->
                RequestStatusFilterChip(
                    text = status,
                    selected = selectedStatus == status,
                    onClick = {
                        selectedStatus = status
                    }
                )
            }
        }

        Text(
            text = "Requests Found: ${filteredRequests.size}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        if (filteredRequests.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedStatus == "All") {
                            "You have not made any borrowing requests yet."
                        } else {
                            "No $selectedStatus request found."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredRequests) { request ->
                    StudentRequestHistoryCard(request = request)
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
private fun StudentRequestHistoryCard(
    request: BorrowRequest
) {
    val status = request.status.ifBlank { "Pending" }

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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.equipmentName.ifBlank { "Unknown Equipment" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Text(
                        text = request.equipmentCategory.ifBlank { "No category" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }

                StudentRequestStatusBadge(status = status)
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
                StudentRequestInfoRow(
                    label = "Quantity",
                    value = request.quantity.toString()
                )

                StudentRequestInfoRow(
                    label = "Borrow Date",
                    value = request.borrowDate.ifBlank { "N/A" }
                )

                StudentRequestInfoRow(
                    label = "Due Date",
                    value = request.dueDate.ifBlank { "N/A" }
                )

                if (request.returnedDate.isNotBlank()) {
                    StudentRequestInfoRow(
                        label = "Returned Date",
                        value = request.returnedDate
                    )
                }

                if (request.returnCondition.isNotBlank()) {
                    StudentRequestInfoRow(
                        label = "Return Condition",
                        value = request.returnCondition
                    )
                }
            }

            when {
                status.equals("Pending", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "Your request is waiting for admin approval.",
                        backgroundColor = Color(0xFFFFFBEB),
                        textColor = Color(0xFF92400E)
                    )
                }

                status.equals("Approved", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "Your request is approved. Please collect the equipment from admin.",
                        backgroundColor = Color(0xFFEFF6FF),
                        textColor = Color(0xFF2563EB)
                    )
                }

                status.equals("Issued", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "Equipment has been issued to you. Return it before the due date.",
                        backgroundColor = Color(0xFFF0FDF4),
                        textColor = Color(0xFF166534)
                    )
                }

                status.equals("Overdue", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "⚠ This request is overdue. Please return the equipment immediately.",
                        backgroundColor = Color(0xFFFEF2F2),
                        textColor = Color(0xFFB91C1C)
                    )
                }

                status.equals("Returned", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "This equipment has been returned successfully.",
                        backgroundColor = Color(0xFFF0FDF4),
                        textColor = Color(0xFF166534)
                    )
                }

                status.equals("Rejected", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = request.rejectedReason.ifBlank {
                            "Your request has been rejected by admin."
                        },
                        backgroundColor = Color(0xFFFEF2F2),
                        textColor = Color(0xFFB91C1C)
                    )
                }

                status.equals("Lost", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "This equipment has been marked as lost. Please contact admin.",
                        backgroundColor = Color(0xFFFEF2F2),
                        textColor = Color(0xFFB91C1C)
                    )
                }

                status.equals("Damaged", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "This equipment has been marked as damaged. Please contact admin.",
                        backgroundColor = Color(0xFFFFF7ED),
                        textColor = Color(0xFFEA580C)
                    )
                }

                status.equals("Cancelled", ignoreCase = true) -> {
                    RequestMessageBox(
                        message = "This request has been cancelled.",
                        backgroundColor = Color(0xFFF3F4F6),
                        textColor = Color(0xFF374151)
                    )
                }
            }

            if (request.adminNote.isNotBlank()) {
                RequestMessageBox(
                    message = "Admin Note: ${request.adminNote}",
                    backgroundColor = Color(0xFFF8FAFC),
                    textColor = Color(0xFF475569)
                )
            }
        }
    }
}

@Composable
private fun StudentRequestStatusBadge(
    status: String
) {
    val normalizedStatus = status.trim().ifBlank { "Pending" }

    val backgroundColor = when (normalizedStatus.lowercase()) {
        "pending" -> Color(0xFFFEF3C7)
        "approved" -> Color(0xFFEFF6FF)
        "issued" -> Color(0xFFDCFCE7)
        "returned" -> Color(0xFFF0FDF4)
        "rejected" -> Color(0xFFFEE2E2)
        "overdue" -> Color(0xFFFEE2E2)
        "lost" -> Color(0xFFFEE2E2)
        "damaged" -> Color(0xFFFFEDD5)
        "cancelled" -> Color(0xFFE5E7EB)
        else -> Color(0xFFE5E7EB)
    }

    val textColor = when (normalizedStatus.lowercase()) {
        "pending" -> Color(0xFF92400E)
        "approved" -> Color(0xFF2563EB)
        "issued" -> Color(0xFF166534)
        "returned" -> Color(0xFF15803D)
        "rejected" -> Color(0xFFB91C1C)
        "overdue" -> Color(0xFFB91C1C)
        "lost" -> Color(0xFFB91C1C)
        "damaged" -> Color(0xFFEA580C)
        "cancelled" -> Color(0xFF374151)
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
private fun StudentRequestInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.weight(0.42f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6B7280)
        )

        Text(
            text = value.ifBlank { "N/A" },
            modifier = Modifier.weight(0.58f),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF111827),
            fontWeight = if (label == "Due Date") {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}

@Composable
private fun RequestMessageBox(
    message: String,
    backgroundColor: Color,
    textColor: Color
) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = textColor
    )
}

@Composable
private fun RequestStatusFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White
            )
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(text)
        }
    }
}