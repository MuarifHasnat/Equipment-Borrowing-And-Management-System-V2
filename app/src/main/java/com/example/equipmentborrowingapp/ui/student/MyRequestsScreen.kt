package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

private object MyReqColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF111827)
    val TextMuted = Color(0xFF6B7280)
    val PrimaryBlue = Color(0xFF2563EB)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF166534)

    val OrangeLight = Color(0xFFFFFBEB)
    val OrangeText = Color(0xFF92400E)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFB91C1C)

    val GrayLight = Color(0xFFF3F4F6)
    val GrayText = Color(0xFF374151)

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
}

@Composable
fun MyRequestsScreen(
    requestList: List<BorrowRequest>,
    onBackClick: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf("All") }
    var searchText by remember { mutableStateOf("") }

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
            val statusMatched =
                selectedStatus == "All" ||
                        request.status.equals(selectedStatus, ignoreCase = true)

            val query = searchText.trim().lowercase()

            val searchMatched =
                query.isBlank() ||
                        request.equipmentName.lowercase().contains(query) ||
                        request.equipmentCategory.lowercase().contains(query) ||
                        request.status.lowercase().contains(query) ||
                        request.borrowDate.lowercase().contains(query) ||
                        request.dueDate.lowercase().contains(query)

            statusMatched && searchMatched
        }
        .sortedByDescending { it.requestTimestamp }

    val pendingCount = requestList.count { it.status.equals("Pending", ignoreCase = true) }
    val activeCount = requestList.count { request ->
        val status = request.status.trim().lowercase()
        val fineStatus = request.fineStatus.trim().lowercase()

        val isActive =
            status == "approved" ||
                    status == "issued" ||
                    status == "overdue"

        val hasPendingFine =
            request.fineAmount > 0 &&
                    fineStatus != "paid" &&
                    fineStatus != "waived"

        val isLostOrDamagedWithPendingFine =
            (status == "lost" || status == "damaged") && hasPendingFine

        isActive || isLostOrDamagedWithPendingFine
    }
    val completedCount = requestList.count { it.status.equals("Returned", ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyReqColors.ModernBg)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(42.dp)
                    .background(MyReqColors.CardWhite, RoundedCornerShape(12.dp))
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MyReqColors.TextDark
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "My Requests",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MyReqColors.TextDark
                )

                Text(
                    text = "Track your equipment borrowing history",
                    style = MaterialTheme.typography.bodySmall,
                    color = MyReqColors.TextMuted
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MyRequestStatCard(
                title = "Pending",
                value = pendingCount.toString(),
                bgColor = MyReqColors.OrangeLight,
                textColor = MyReqColors.OrangeText,
                modifier = Modifier.weight(1f)
            )

            MyRequestStatCard(
                title = "Active",
                value = activeCount.toString(),
                bgColor = MyReqColors.BlueLight,
                textColor = MyReqColors.BlueText,
                modifier = Modifier.weight(1f)
            )

            MyRequestStatCard(
                title = "Returned",
                value = completedCount.toString(),
                bgColor = MyReqColors.GreenLight,
                textColor = MyReqColors.GreenText,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyRequestSearchBox(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1.45f)
            )

            MyRequestStatusDropdown(
                selectedStatus = selectedStatus,
                statusFilters = statusFilters,
                onStatusSelected = { selectedStatus = it },
                modifier = Modifier.weight(0.9f)
            )
        }

        Text(
            text = "Showing ${filteredRequests.size} request(s)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MyReqColors.TextMuted
        )

        if (filteredRequests.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MyReqColors.CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when {
                            requestList.isEmpty() -> "You have not made any borrowing requests yet."
                            selectedStatus != "All" -> "No $selectedStatus request found."
                            else -> "No request matches your search."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MyReqColors.TextMuted
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
    }
}

@Composable
private fun MyRequestSearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MyReqColors.TextDark
        ),
        modifier = modifier
            .height(42.dp)
            .background(MyReqColors.CardWhite, RoundedCornerShape(14.dp))
            .shadow(1.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.04f))
            .padding(horizontal = 11.dp),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = MyReqColors.TextMuted,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isBlank()) {
                        Text(
                            text = "Search requests...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MyReqColors.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun MyRequestStatusDropdown(
    selectedStatus: String,
    statusFilters: List<String>,
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(MyReqColors.CardWhite, RoundedCornerShape(14.dp))
                .shadow(1.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.04f))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedStatus,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MyReqColors.TextDark,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = MyReqColors.TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            statusFilters.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = status,
                            fontWeight = if (status == selectedStatus) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun MyRequestStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MyReqColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MyReqColors.TextMuted,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                modifier = Modifier
                    .background(bgColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 9.dp, vertical = 3.dp),
                style = MaterialTheme.typography.titleSmall,
                color = textColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun StudentRequestHistoryCard(
    request: BorrowRequest
) {
    val status = request.status.ifBlank { "Pending" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MyReqColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                StudentRequestImage(
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = request.equipmentName.ifBlank { "Unknown Equipment" },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MyReqColors.TextDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = request.equipmentCategory.ifBlank { "No category" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MyReqColors.TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        StudentRequestStatusBadge(status = status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Inventory2,
                            contentDescription = null,
                            tint = MyReqColors.TextMuted,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Quantity: ${request.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyReqColors.TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            tint = MyReqColors.TextMuted,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Due: ${request.dueDate.ifBlank { "N/A" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (status.equals("Overdue", ignoreCase = true)) {
                                MyReqColors.RedText
                            } else {
                                MyReqColors.TextMuted
                            },
                            fontWeight = if (status.equals("Overdue", ignoreCase = true)) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    }
                }
            }

            HorizontalDivider(color = MyReqColors.ModernBg)

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

            RequestStatusMessage(
                status = status,
                request = request
            )
            if (request.fineAmount > 0) {
                RequestMessageBox(
                    message = "Fine: ${request.fineAmount} taka • Status: ${
                        request.fineStatus.ifBlank { "Pending" }
                            .replaceFirstChar { it.uppercase() }
                    }",
                    backgroundColor = MyReqColors.RedLight,
                    textColor = MyReqColors.RedText
                )

                if (request.fineReason.isNotBlank()) {
                    RequestMessageBox(
                        message = "Reason: ${request.fineReason}",
                        backgroundColor = Color(0xFFFFF7ED),
                        textColor = Color(0xFFEA580C)
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
private fun StudentRequestImage(
    imageName: String,
    imageUrl: String,
    contentDescription: String
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(imageUrl)

    val imageModifier = Modifier
        .size(84.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(MyReqColors.ModernBg)

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
            contentScale = ContentScale.Crop,
            modifier = imageModifier.padding(8.dp)
        )
    }
}

@Composable
private fun RequestStatusMessage(
    status: String,
    request: BorrowRequest
) {
    when {
        status.equals("Pending", ignoreCase = true) -> {
            RequestMessageBox(
                message = "Your request is waiting for admin approval.",
                backgroundColor = MyReqColors.OrangeLight,
                textColor = MyReqColors.OrangeText
            )
        }

        status.equals("Approved", ignoreCase = true) -> {
            RequestMessageBox(
                message = "Your request is approved. Please collect the equipment from admin.",
                backgroundColor = MyReqColors.BlueLight,
                textColor = MyReqColors.BlueText
            )
        }

        status.equals("Issued", ignoreCase = true) -> {
            RequestMessageBox(
                message = "Equipment has been issued to you. Return it before the due date.",
                backgroundColor = MyReqColors.GreenLight,
                textColor = MyReqColors.GreenText
            )
        }

        status.equals("Overdue", ignoreCase = true) -> {
            RequestMessageBox(
                message = "⚠ This request is overdue. Please return the equipment immediately.",
                backgroundColor = MyReqColors.RedLight,
                textColor = MyReqColors.RedText
            )
        }

        status.equals("Returned", ignoreCase = true) -> {
            RequestMessageBox(
                message = "This equipment has been returned successfully.",
                backgroundColor = MyReqColors.GreenLight,
                textColor = MyReqColors.GreenText
            )
        }

        status.equals("Rejected", ignoreCase = true) -> {
            RequestMessageBox(
                message = request.rejectedReason.ifBlank {
                    "Your request has been rejected by admin."
                },
                backgroundColor = MyReqColors.RedLight,
                textColor = MyReqColors.RedText
            )
        }

        status.equals("Lost", ignoreCase = true) -> {
            RequestMessageBox(
                message = "This equipment has been marked as lost. Please contact admin.",
                backgroundColor = MyReqColors.RedLight,
                textColor = MyReqColors.RedText
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
                backgroundColor = MyReqColors.GrayLight,
                textColor = MyReqColors.GrayText
            )
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
            color = MyReqColors.TextMuted
        )

        Text(
            text = value.ifBlank { "N/A" },
            modifier = Modifier.weight(0.58f),
            style = MaterialTheme.typography.bodyMedium,
            color = MyReqColors.TextDark,
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
                containerColor = MyReqColors.PrimaryBlue,
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