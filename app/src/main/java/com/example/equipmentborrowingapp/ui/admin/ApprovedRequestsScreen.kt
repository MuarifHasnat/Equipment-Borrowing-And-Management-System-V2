package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.WarningAmber
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

private object ApprovedColors {
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
    val GrayText = Color(0xFF475569)
}

@Composable
private fun RequestCardImage(
    imageName: String,
    imageUrl: String,
    contentDescription: String
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(imageUrl)

    val imageModifier = Modifier
        .size(88.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(ApprovedColors.GrayLight)

    if (hasImageUrl) {
        AsyncImage(
            model = safeImageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = fallbackImageResId),
            error = painterResource(id = fallbackImageResId),
            fallback = painterResource(id = fallbackImageResId),
            modifier = imageModifier
        )
    } else {
        Image(
            painter = painterResource(id = fallbackImageResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = imageModifier.padding(8.dp)
        )
    }
}

@Composable
fun ApprovedRequestsScreen(
    requestList: List<BorrowRequest>,
    onIssuedClick: (BorrowRequest) -> Unit,
    onReturnedClick: (BorrowRequest) -> Unit,
    onLostClick: (BorrowRequest, Int, String) -> Unit,
    onDamagedClick: (BorrowRequest, Int, String) -> Unit,
    onFinePaidClick: (BorrowRequest) -> Unit,
    onFineWaivedClick: (BorrowRequest) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedRequest by remember { mutableStateOf<BorrowRequest?>(null) }
    var selectedAction by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                        request.status.lowercase().contains(query) ||
                        request.fineReason.lowercase().contains(query)

            val matchesStatus =
                selectedStatus == "All" ||
                        request.status.equals(selectedStatus, ignoreCase = true)

            matchesSearch && matchesStatus
        }
        .sortedWith(
            compareBy<BorrowRequest> { activeStatusOrder(it.status) }
                .thenByDescending { it.requestTimestamp }
        )

    val approvedCount = requestList.count { it.status.equals("Approved", ignoreCase = true) }
    val issuedCount = requestList.count { it.status.equals("Issued", ignoreCase = true) }
    val overdueCount = requestList.count { it.status.equals("Overdue", ignoreCase = true) }
    val pendingFineCount = requestList.count {
        it.fineAmount > 0 &&
                !it.fineStatus.equals("Paid", ignoreCase = true) &&
                !it.fineStatus.equals("Waived", ignoreCase = true)
    }

    selectedRequest?.let { request ->
        ConfirmLifecycleActionDialog(
            request = request,
            action = selectedAction,
            onDismiss = {
                selectedRequest = null
                selectedAction = ""
            },
            onConfirm = { fineAmount, fineReason ->
                when (selectedAction) {
                    "issue" -> onIssuedClick(request)
                    "return" -> onReturnedClick(request)
                    "lost" -> onLostClick(request, fineAmount, fineReason)
                    "damaged" -> onDamagedClick(request, fineAmount, fineReason)
                }

                val message = when (selectedAction) {
                    "issue" -> "Request marked as issued"
                    "return" -> "Item marked as returned"
                    "lost" -> "Request marked as lost"
                    "damaged" -> "Request marked as damaged"
                    else -> "Request updated"
                }

                selectedRequest = null
                selectedAction = ""

                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ApprovedColors.Bg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            ActiveRequestTopBar(
                totalCount = requestList.size,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActiveMiniStat("Approved", approvedCount.toString(), ApprovedColors.BlueLight, ApprovedColors.BlueText, Modifier.weight(1f))
                ActiveMiniStat("Issued", issuedCount.toString(), ApprovedColors.PurpleLight, ApprovedColors.PurpleText, Modifier.weight(1f))
                ActiveMiniStat("Overdue", overdueCount.toString(), ApprovedColors.RedLight, ApprovedColors.RedText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (pendingFineCount > 0) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    color = ApprovedColors.OrangeLight,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WarningAmber,
                            contentDescription = null,
                            tint = ApprovedColors.OrangeText,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "$pendingFineCount pending fine request(s)",
                            color = ApprovedColors.OrangeText,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

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
                        tint = ApprovedColors.TextMuted
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Approved", "Issued", "Overdue", "Lost", "Damaged").forEach { status ->
                    ActiveFilterChip(
                        text = status,
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Showing ${filteredRequests.size} of ${requestList.size} request(s)",
                style = MaterialTheme.typography.titleSmall,
                color = ApprovedColors.TextDark,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredRequests.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ApprovedColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No active borrow request found.",
                            color = ApprovedColors.TextMuted,
                            style = MaterialTheme.typography.bodyMedium,
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
                        BorrowLifecycleRequestCard(
                            request = request,
                            onIssueClick = {
                                selectedRequest = request
                                selectedAction = "issue"
                            },
                            onReturnClick = {
                                selectedRequest = request
                                selectedAction = "return"
                            },
                            onLostClick = {
                                selectedRequest = request
                                selectedAction = "lost"
                            },
                            onDamagedClick = {
                                selectedRequest = request
                                selectedAction = "damaged"
                            },
                            onFinePaidClick = {
                                onFinePaidClick(request)
                            },
                            onFineWaivedClick = {
                                onFineWaivedClick(request)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveRequestTopBar(
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
                .background(ApprovedColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = ApprovedColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(ApprovedColors.PurpleLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Inventory2,
                contentDescription = null,
                tint = ApprovedColors.Primary,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Active Requests",
                color = ApprovedColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "$totalCount request(s) need tracking",
                color = ApprovedColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ActiveMiniStat(
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
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun ActiveFilterChip(
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
                containerColor = ApprovedColors.Primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
private fun BorrowLifecycleRequestCard(
    request: BorrowRequest,
    onIssueClick: () -> Unit,
    onReturnClick: () -> Unit,
    onLostClick: () -> Unit,
    onDamagedClick: () -> Unit,
    onFinePaidClick: () -> Unit,
    onFineWaivedClick: () -> Unit
) {
    val status = request.status.ifBlank { "Approved" }
    val statusColor = statusTextColor(status)
    val statusBg = statusBackgroundColor(status)

    val fineStatus = request.fineStatus.trim().ifBlank { "Pending" }
    val hasPendingFine = request.fineAmount > 0 &&
            !fineStatus.equals("Paid", ignoreCase = true) &&
            !fineStatus.equals("Waived", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ApprovedColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                RequestCardImage(
                    imageName = request.equipmentImageName,
                    imageUrl = request.equipmentImageUrl,
                    contentDescription = request.equipmentName
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = request.equipmentName.ifBlank { "Unknown Equipment" },
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ApprovedColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = status,
                            modifier = Modifier
                                .background(statusBg, RoundedCornerShape(50.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = statusColor,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${request.userName.ifBlank { "Unknown Student" }} • Qty: ${request.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ApprovedColors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Borrow: ${request.borrowDate.ifBlank { "N/A" }} • Due: ${request.dueDate.ifBlank { "N/A" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (status.equals("Overdue", ignoreCase = true)) ApprovedColors.RedText else ApprovedColors.TextMuted,
                        fontWeight = if (status.equals("Overdue", ignoreCase = true)) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (request.studentId.isNotBlank() || request.department.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildString {
                                if (request.studentId.isNotBlank()) append("ID: ${request.studentId}")
                                if (request.department.isNotBlank()) {
                                    if (isNotBlank()) append(" • ")
                                    append(request.department)
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = ApprovedColors.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (status.equals("Overdue", ignoreCase = true)) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Overdue item. Return is required.",
                            modifier = Modifier
                                .background(ApprovedColors.RedLight, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            color = ApprovedColors.RedText,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (request.fineAmount > 0) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Fine: ${request.fineAmount} taka • $fineStatus",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            when (fineStatus.lowercase()) {
                                "paid" -> ApprovedColors.GreenLight
                                "waived" -> ApprovedColors.BlueLight
                                else -> ApprovedColors.RedLight
                            },
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    color = when (fineStatus.lowercase()) {
                        "paid" -> ApprovedColors.GreenText
                        "waived" -> ApprovedColors.BlueText
                        else -> ApprovedColors.RedText
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )

                if (request.fineReason.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Reason: ${request.fineReason}",
                        color = ApprovedColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (hasPendingFine) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onFinePaidClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ApprovedColors.GreenText,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Paid", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onFineWaivedClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ApprovedColors.BlueText
                            )
                        ) {
                            Text("Waive", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = ApprovedColors.Bg)
            Spacer(modifier = Modifier.height(8.dp))

            when {
                status.equals("Approved", ignoreCase = true) -> {
                    Button(
                        onClick = onIssueClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ApprovedColors.BlueText,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Mark as Issued", fontWeight = FontWeight.Bold)
                    }
                }

                status.equals("Issued", ignoreCase = true) ||
                        status.equals("Overdue", ignoreCase = true) -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onReturnClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ApprovedColors.GreenText,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Mark as Returned", fontWeight = FontWeight.Bold)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = onDamagedClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = ApprovedColors.OrangeText
                                )
                            ) {
                                Text("Damaged", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onLostClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = ApprovedColors.RedText
                                )
                            ) {
                                Text("Lost", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmLifecycleActionDialog(
    request: BorrowRequest,
    action: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, String) -> Unit
) {
    var fineAmountText by remember { mutableStateOf("") }
    var fineReason by remember { mutableStateOf("") }

    val isPenaltyAction = action.equals("lost", ignoreCase = true) ||
            action.equals("damaged", ignoreCase = true)

    val title = when (action) {
        "issue" -> "Confirm Issue"
        "return" -> "Confirm Return"
        "lost" -> "Confirm Lost"
        "damaged" -> "Confirm Damaged"
        else -> "Confirm Action"
    }

    val message = when (action) {
        "issue" -> "Mark this approved request as issued?"
        "return" -> "Mark this item as returned?"
        "lost" -> "Mark this item as lost? Stock will not increase."
        "damaged" -> "Mark this item as damaged? Stock will not increase."
        else -> "Update this request?"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = ApprovedColors.Card,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = ApprovedColors.TextDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = message,
                    color = ApprovedColors.TextMuted
                )

                Surface(
                    color = ApprovedColors.Bg,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = request.equipmentName.ifBlank { "Unknown Equipment" },
                            fontWeight = FontWeight.Bold,
                            color = ApprovedColors.TextDark
                        )

                        Text(
                            text = "${request.userName.ifBlank { "Unknown Student" }} • Qty: ${request.quantity}",
                            color = ApprovedColors.TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                if (isPenaltyAction) {
                    OutlinedTextField(
                        value = fineAmountText,
                        onValueChange = { value -> fineAmountText = value.filter { it.isDigit() } },
                        label = { Text("Fine Amount (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = fineReason,
                        onValueChange = { fineReason = it },
                        label = { Text(if (action == "lost") "Lost Note / Reason" else "Damage Note / Reason") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fineAmount = fineAmountText.toIntOrNull() ?: 0
                    val finalReason = fineReason.trim().ifBlank {
                        when (action) {
                            "lost" -> "Item marked as lost"
                            "damaged" -> "Item marked as damaged"
                            else -> ""
                        }
                    }
                    onConfirm(fineAmount, finalReason)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (action) {
                        "lost" -> ApprovedColors.RedText
                        "damaged" -> ApprovedColors.OrangeText
                        "return" -> ApprovedColors.GreenText
                        else -> ApprovedColors.Primary
                    },
                    contentColor = Color.White
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = ApprovedColors.TextMuted)
            }
        }
    )
}

private fun activeStatusOrder(status: String): Int {
    return when (status.trim().lowercase()) {
        "approved" -> 0
        "issued" -> 1
        "overdue" -> 2
        "damaged" -> 3
        "lost" -> 4
        else -> 5
    }
}

private fun statusTextColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "approved" -> ApprovedColors.BlueText
        "issued" -> ApprovedColors.PurpleText
        "overdue" -> ApprovedColors.RedText
        "returned" -> ApprovedColors.GreenText
        "lost" -> ApprovedColors.RedText
        "damaged" -> ApprovedColors.OrangeText
        else -> ApprovedColors.GrayText
    }
}

private fun statusBackgroundColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "approved" -> ApprovedColors.BlueLight
        "issued" -> ApprovedColors.PurpleLight
        "overdue" -> ApprovedColors.RedLight
        "returned" -> ApprovedColors.GreenLight
        "lost" -> ApprovedColors.RedLight
        "damaged" -> ApprovedColors.OrangeLight
        else -> ApprovedColors.GrayLight
    }
}
