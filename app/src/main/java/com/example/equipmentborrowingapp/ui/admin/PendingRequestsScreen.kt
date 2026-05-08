package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import com.example.equipmentborrowingapp.ui.common.ProfessionalStatusBadge
import kotlinx.coroutines.launch

// Modern Colors for Pending Requests
private object PendingColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)
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
                .size(85.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    } else {
        Image(
            painter = painterResource(fallbackImageResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(12.dp))
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

    //  Modern DIALOG
    selectedRequest?.let { request ->
        val isApprove = dialogType == "approve"

        AlertDialog(
            onDismissRequest = {
                selectedRequest = null
                dialogType = ""
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = PendingColors.CardWhite,
            title = {
                Text(
                    text = if (isApprove) "Confirm Approval" else "Confirm Rejection",
                    fontWeight = FontWeight.Bold,
                    color = PendingColors.TextDark
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isApprove) "Are you sure you want to approve this request?" else "Are you sure you want to reject this request?",
                        color = PendingColors.TextMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = PendingColors.ModernBg,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Student: ${request.userName}", fontWeight = FontWeight.SemiBold, color = PendingColors.TextDark)
                            Text("Equipment: ${request.equipmentName}", color = PendingColors.TextDark)
                            Text("Qty: ${request.quantity}", color = PendingColors.TextDark)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
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
                                if (isApprove) "Request approved successfully" else "Request rejected successfully"
                            )
                            isLoading = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isApprove) PendingColors.GreenText else PendingColors.RedText
                    )
                ) {
                    Text(if (isApprove) "Approve" else "Reject")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedRequest = null
                        dialogType = ""
                    }
                ) {
                    Text("Cancel", color = PendingColors.TextMuted)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = PendingColors.ModernBg
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PendingColors.PrimaryIndigo)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                //  Modern Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(PendingColors.CardWhite, RoundedCornerShape(12.dp))
                            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = PendingColors.TextDark
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Pending Requests",
                            style = MaterialTheme.typography.titleLarge,
                            color = PendingColors.TextDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Review and manage student requests",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PendingColors.TextMuted
                        )
                    }
                }

                if (requestList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No pending requests found", color = PendingColors.TextMuted)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(requestList) { request ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                colors = CardDefaults.cardColors(containerColor = PendingColors.CardWhite),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        RequestCardImage(
                                            imageName = request.equipmentImageName,
                                            imageUrl = request.equipmentImageUrl,
                                            contentDescription = request.equipmentName
                                        )

                                        Spacer(modifier = Modifier.width(14.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(
                                                    text = request.equipmentName,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = PendingColors.TextDark,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                ProfessionalStatusBadge(
                                                    text = request.status,
                                                    type = request.status
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Rounded.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = PendingColors.TextMuted)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${request.userName} • Qty: ${request.quantity}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = PendingColors.TextMuted
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Rounded.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = PendingColors.TextMuted)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Borrow: ${request.borrowDate}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = PendingColors.TextMuted
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(2.dp))

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Rounded.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = PendingColors.TextMuted)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Due: ${request.dueDate}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = PendingColors.TextMuted
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = PendingColors.ModernBg)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Action Buttons
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = {
                                                selectedRequest = request
                                                dialogType = "approve"
                                            },
                                            modifier = Modifier.weight(1f).height(42.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = PendingColors.GreenLight,
                                                contentColor = PendingColors.GreenText
                                            )
                                        ) {
                                            Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Approve", fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                selectedRequest = request
                                                dialogType = "reject"
                                            },
                                            modifier = Modifier.weight(1f).height(42.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PendingColors.RedText),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, PendingColors.RedText.copy(alpha = 0.3f))
                                        ) {
                                            Icon(Icons.Rounded.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Reject", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}