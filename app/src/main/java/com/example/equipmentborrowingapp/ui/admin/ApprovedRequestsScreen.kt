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

// Modern Colors
private object ApprovedColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)
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
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    } else {
        Image(
            painter = painterResource(fallback),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(12.dp))
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

    // 🔥 Modern DIALOG
    selectedRequest?.let { request ->
        AlertDialog(
            onDismissRequest = { selectedRequest = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = ApprovedColors.CardWhite,
            title = {
                Text("Confirm Return", fontWeight = FontWeight.Bold, color = ApprovedColors.TextDark)
            },
            text = {
                Column {
                    Text("Are you sure you want to mark this item as returned?", color = ApprovedColors.TextMuted)
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = ApprovedColors.ModernBg,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Student: ${request.userName}", fontWeight = FontWeight.SemiBold, color = ApprovedColors.TextDark)
                            Text("Equipment: ${request.equipmentName}", color = ApprovedColors.TextDark)
                            Text("Qty: ${request.quantity}", color = ApprovedColors.TextDark)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isLoading = true
                        onReturnedClick(request)
                        selectedRequest = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Item marked as returned successfully")
                            isLoading = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ApprovedColors.PrimaryIndigo)
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedRequest = null }) {
                    Text("Cancel", color = ApprovedColors.TextMuted)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ApprovedColors.ModernBg
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ApprovedColors.PrimaryIndigo)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // 🔙 Modern Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(ApprovedColors.CardWhite, RoundedCornerShape(12.dp))
                            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = ApprovedColors.TextDark
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Approved Requests",
                            style = MaterialTheme.typography.titleLarge,
                            color = ApprovedColors.TextDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Track borrowed items & returns",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ApprovedColors.TextMuted
                        )
                    }
                }

                if (requestList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No approved requests found", color = ApprovedColors.TextMuted)
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
                                colors = CardDefaults.cardColors(containerColor = ApprovedColors.CardWhite),
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
                                                    color = ApprovedColors.TextDark,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                ProfessionalStatusBadge(
                                                    text = request.status,
                                                    type = request.status
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Rounded.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = ApprovedColors.TextMuted)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${request.userName} • Qty: ${request.quantity}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = ApprovedColors.TextMuted
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Rounded.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = ApprovedColors.TextMuted)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Due: ${request.dueDate}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = ApprovedColors.TextMuted
                                                )
                                            }

                                            if (request.status.equals("Overdue", true)) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "⚠ Overdue item. Return is required.",
                                                    color = ApprovedColors.RedText,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }
                                    }

                                    // Action Button
                                    if (request.status.equals("Approved", true) || request.status.equals("Overdue", true)) {
                                        Spacer(modifier = Modifier.height(14.dp))
                                        HorizontalDivider(color = ApprovedColors.ModernBg)
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Button(
                                            onClick = { selectedRequest = request },
                                            modifier = Modifier.fillMaxWidth().height(42.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ApprovedColors.PrimaryIndigo.copy(alpha = 0.1f),
                                                contentColor = ApprovedColors.PrimaryIndigo
                                            )
                                        ) {
                                            Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Mark as Returned", fontWeight = FontWeight.Bold)
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