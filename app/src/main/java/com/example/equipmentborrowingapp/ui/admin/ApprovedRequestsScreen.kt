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

private fun getRequestStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "approved" -> Color(0xFF4CAF50)
        "returned" -> Color(0xFF2196F3)
        "overdue" -> Color(0xFFF44336)
        else -> Color.Gray
    }
}

@Composable
private fun RequestCardImage(
    imageName: String,
    imageUrl: String,
    contentDescription: String
) {
    val fallback = EquipmentImageMapper.getImageRes(imageName)

    if (imageUrl.isNotBlank()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(fallback),
            error = painterResource(fallback),
            modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp))
        )
    } else {
        Image(
            painter = painterResource(fallback),
            contentDescription = contentDescription,
            modifier = Modifier.size(90.dp)
        )
    }
}

@Composable
fun ApprovedRequestsScreen(
    requestList: List<BorrowRequest>,
    onReturnedClick: (BorrowRequest) -> Unit,
    onBackClick: () -> Unit
) {

    var selectedRequest by remember { mutableStateOf<BorrowRequest?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

    // 🔥 DIALOG
    selectedRequest?.let { request ->
        AlertDialog(
            onDismissRequest = { selectedRequest = null },
            title = { Text("Confirm Return") },
            text = {
                Column {
                    Text("Mark this item as returned?")

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
                        onReturnedClick(request)
                        selectedRequest = null

                        scope.launch {
                            snackbarHostState.showSnackbar("Marked as returned")
                            isLoading = false
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { selectedRequest = null }
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
                    "Approved Requests",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (requestList.isEmpty()) {
                    Text("No approved requests found")
                } else {

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(requestList) { request ->

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
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

                                            if (request.returnedDate.isNotBlank()) {
                                                Text("Returned: ${request.returnedDate}")
                                            }

                                            Text(
                                                "Status: ${request.status}",
                                                color = getRequestStatusColor(request.status)
                                            )

                                            if (request.status.equals("Overdue", true)) {
                                                Text(
                                                    "⚠ Overdue",
                                                    color = Color.Red
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {

                                        Button(
                                            onClick = {
                                                selectedRequest = request
                                            },
                                            enabled = request.status.equals("Approved", true)
                                                    || request.status.equals("Overdue", true),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Mark Returned")
                                        }

                                        OutlinedButton(
                                            onClick = onBackClick,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Back")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (requestList.isEmpty()) {
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
}