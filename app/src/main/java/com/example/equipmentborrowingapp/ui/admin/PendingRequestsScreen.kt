package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

private object PendingReqColors {
    val Bg = Color(0xFFF4F7FB)
    val Card = Color.White
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val Primary = Color(0xFF4F46E5)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)

    val GrayLight = Color(0xFFF1F5F9)
}

@Composable
fun PendingRequestsScreen(
    requestList: List<BorrowRequest>,
    onApproveClick: (BorrowRequest) -> Unit,
    onRejectClick: (BorrowRequest) -> Unit,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedRequest by remember { mutableStateOf<BorrowRequest?>(null) }
    var selectedAction by remember { mutableStateOf("") }

    val categoryList = remember(requestList) {
        listOf("All") + requestList
            .map { it.equipmentCategory.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    val filteredRequests = requestList
        .filter { request ->
            val query = searchText.trim().lowercase()

            val matchesSearch =
                query.isBlank() ||
                        request.userName.lowercase().contains(query) ||
                        request.userEmail.lowercase().contains(query) ||
                        request.studentId.lowercase().contains(query) ||
                        request.department.lowercase().contains(query) ||
                        request.equipmentName.lowercase().contains(query) ||
                        request.equipmentCategory.lowercase().contains(query) ||
                        request.borrowDate.lowercase().contains(query) ||
                        request.dueDate.lowercase().contains(query)

            val matchesCategory =
                selectedCategory == "All" ||
                        request.equipmentCategory.equals(selectedCategory, ignoreCase = true)

            matchesSearch && matchesCategory
        }
        .sortedByDescending { it.requestTimestamp }

    selectedRequest?.let { request ->
        PendingRequestConfirmDialog(
            request = request,
            action = selectedAction,
            onDismiss = {
                selectedRequest = null
                selectedAction = ""
            },
            onConfirm = {
                if (selectedAction == "approve") {
                    onApproveClick(request)
                } else {
                    onRejectClick(request)
                }
                selectedRequest = null
                selectedAction = ""
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PendingReqColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            PendingRequestTopBar(
                totalCount = requestList.size,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PendingMiniStat(
                    title = "Pending",
                    value = requestList.size.toString(),
                    bgColor = PendingReqColors.OrangeLight,
                    textColor = PendingReqColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )

                PendingMiniStat(
                    title = "Showing",
                    value = filteredRequests.size.toString(),
                    bgColor = PendingReqColors.BlueLight,
                    textColor = PendingReqColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                PendingMiniStat(
                    title = "Category",
                    value = (categoryList.size - 1).coerceAtLeast(0).toString(),
                    bgColor = PendingReqColors.PurpleLight,
                    textColor = PendingReqColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = PendingReqColors.TextMuted
                    )
                },
                placeholder = {
                    Text(
                        text = "Search request",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryList.forEach { category ->
                    PendingFilterChip(
                        text = category,
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Showing ${filteredRequests.size} of ${requestList.size} request(s)",
                color = PendingReqColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (filteredRequests.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PendingReqColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No pending request found.",
                            color = PendingReqColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(filteredRequests) { request ->
                        PendingRequestCard(
                            request = request,
                            onApproveClick = {
                                selectedRequest = request
                                selectedAction = "approve"
                            },
                            onRejectClick = {
                                selectedRequest = request
                                selectedAction = "reject"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PendingRequestTopBar(
    totalCount: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(PendingReqColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = PendingReqColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(PendingReqColors.OrangeLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Inventory2,
                contentDescription = null,
                tint = PendingReqColors.OrangeText,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Pending Requests",
                color = PendingReqColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "$totalCount request(s) waiting for approval",
                color = PendingReqColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PendingMiniStat(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(52.dp),
        color = bgColor,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
        ) {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PendingFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PendingReqColors.Primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text = text, maxLines = 1)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text = text, maxLines = 1)
        }
    }
}

@Composable
private fun PendingRequestCard(
    request: BorrowRequest,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(request.equipmentImageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(request.equipmentImageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(request.equipmentImageUrl)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PendingReqColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PendingReqColors.GrayLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasImageUrl) {
                        AsyncImage(
                            model = safeImageUrl,
                            contentDescription = request.equipmentName,
                            placeholder = painterResource(id = fallbackImageResId),
                            error = painterResource(id = fallbackImageResId),
                            fallback = painterResource(id = fallbackImageResId),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = fallbackImageResId),
                            contentDescription = request.equipmentName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(9.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = request.equipmentName.ifBlank { "Unknown Equipment" },
                            modifier = Modifier.weight(1f),
                            color = PendingReqColors.TextDark,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Pending",
                            modifier = Modifier
                                .background(PendingReqColors.OrangeLight, RoundedCornerShape(50.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = PendingReqColors.OrangeText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = buildString {
                            append(request.userName.ifBlank { "Unknown Student" })
                            if (request.studentId.isNotBlank()) append(" • ID: ${request.studentId}")
                            append(" • Qty: ${request.quantity}")
                        },
                        color = PendingReqColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${request.equipmentCategory.ifBlank { "General" }} • ${request.department.ifBlank { "Department N/A" }}",
                        color = PendingReqColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Borrow: ${request.borrowDate.ifBlank { "N/A" }} • Due: ${request.dueDate.ifBlank { "N/A" }}",
                        color = PendingReqColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = PendingReqColors.Bg)
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onApproveClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PendingReqColors.GreenText,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text("Approve", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PendingReqColors.RedText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text("Reject", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PendingRequestConfirmDialog(
    request: BorrowRequest,
    action: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isApprove = action == "approve"

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = PendingReqColors.Card,
        title = {
            Text(
                text = if (isApprove) "Approve Request" else "Reject Request",
                color = PendingReqColors.TextDark,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = if (isApprove) {
                        "Approve this borrow request?"
                    } else {
                        "Reject this borrow request?"
                    },
                    color = PendingReqColors.TextMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = PendingReqColors.Bg,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = request.equipmentName.ifBlank { "Unknown Equipment" },
                            color = PendingReqColors.TextDark,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${request.userName.ifBlank { "Unknown Student" }} • Qty: ${request.quantity}",
                            color = PendingReqColors.TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isApprove) PendingReqColors.GreenText else PendingReqColors.RedText,
                    contentColor = Color.White
                )
            ) {
                Text(if (isApprove) "Approve" else "Reject")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = PendingReqColors.TextMuted)
            }
        }
    )
}
