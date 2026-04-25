package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import kotlinx.coroutines.launch
import com.example.equipmentborrowingapp.ui.theme.SurfaceWhite
import com.example.equipmentborrowingapp.ui.common.ProfessionalStatusBadge
private fun getRequestStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "pending" -> Color(0xFFFF9800)
        "approved" -> Color(0xFF2E7D32)
        "rejected" -> Color(0xFFC62828)
        "returned" -> Color(0xFF1565C0)
        "overdue" -> Color(0xFF8E24AA)
        else -> Color.Gray
    }
}

@Composable
private fun RequestCardImage(
    imageName: String,
    imageUrl: String,
    contentDescription: String
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val hasImageUrl = imageUrl.trim().isNotBlank()

    if (hasImageUrl) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(fallbackImageResId),
            error = painterResource(fallbackImageResId),
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    } else {
        Image(
            painter = painterResource(fallbackImageResId),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun PendingRequestsScreen(
    requestList: List<BorrowRequest>,
    onApproveClick: (BorrowRequest) -> Unit,
    onRejectClick: (BorrowRequest) -> Unit,
    onBackClick: () -> Unit
) {

    var selectedRequest by remember { mutableStateOf<BorrowRequest?>(null) }
    var dialogType by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

    // 🔥 DIALOG
    selectedRequest?.let { request ->
        val isApprove = dialogType == "approve"

        AlertDialog(
            onDismissRequest = {
                selectedRequest = null
                dialogType = ""
            },
            title = {
                Text(if (isApprove) "Confirm Approval" else "Confirm Rejection")
            },
            text = {
                Column {
                    Text(
                        if (isApprove)
                            "Approve this request?"
                        else
                            "Reject this request?"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Student: ${request.userName}")
                    Text("Equipment: ${request.equipmentName}")
                    Text("Qty: ${request.quantity}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isLoading = true

                        if (isApprove) {
                            onApproveClick(request)
                        } else {
                            onRejectClick(request)
                        }

                        selectedRequest = null
                        dialogType = ""

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (isApprove) "Approved successfully" else "Rejected successfully"
                            )
                            isLoading = false
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedRequest = null
                        dialogType = ""
                    }
                ) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                Text(
                    "Pending Requests",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (requestList.isEmpty()) {
                    Text("No pending requests found")
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(requestList) { request ->

                            Card(
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(5.dp),
                                colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {

                                    Row {

                                        RequestCardImage(
                                            request.equipmentImageName,
                                            request.equipmentImageUrl,
                                            request.equipmentName
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {

                                            Text(
                                                request.equipmentName,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Text("User: ${request.userName}")
                                            Text("Qty: ${request.quantity}")
                                            Text("Borrow: ${request.borrowDate}")
                                            Text("Due: ${request.dueDate}")

                                            ProfessionalStatusBadge(
                                                text = request.status,
                                                type = request.status
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {

                                        Button(
                                            onClick = {
                                                selectedRequest = request
                                                dialogType = "approve"
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Approve Request")
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                selectedRequest = request
                                                dialogType = "reject"
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Reject Request")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
            }
        }
    }
}