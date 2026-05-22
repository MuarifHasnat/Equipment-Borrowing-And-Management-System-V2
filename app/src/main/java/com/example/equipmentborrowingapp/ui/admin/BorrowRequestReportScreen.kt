package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.BorrowRequest

private object RequestReportColors {
    val Background = Color(0xFFF4F7FB)
    val CardWhite = Color.White
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)

    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

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
fun BorrowRequestReportScreen(
    title: String,
    subtitle: String,
    requestList: List<BorrowRequest>,
    fixedStatus: String = "All",
    onFinePaidClick: (BorrowRequest) -> Unit = {},
    onFineWaivedClick: (BorrowRequest) -> Unit = {},
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(fixedStatus) }

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

    val baseList = requestList.filter { request ->
        when {
            fixedStatus.equals("All", ignoreCase = true) -> true

            fixedStatus.equals("Lost/Damaged", ignoreCase = true) -> {
                request.status.equals("Lost", ignoreCase = true) ||
                        request.status.equals("Damaged", ignoreCase = true)
            }

            else -> request.status.equals(fixedStatus, ignoreCase = true)
        }
    }

    val filteredList = baseList.filter { request ->
        val query = searchText.trim().lowercase()

        val matchesSearch =
            query.isBlank() ||
                    request.userName.lowercase().contains(query) ||
                    request.userEmail.lowercase().contains(query) ||
                    request.studentId.lowercase().contains(query) ||
                    request.department.lowercase().contains(query) ||
                    request.equipmentName.lowercase().contains(query) ||
                    request.equipmentCategory.lowercase().contains(query) ||
                    request.status.lowercase().contains(query)

        val matchesStatus =
            fixedStatus != "All" ||
                    selectedStatus == "All" ||
                    request.status.equals(selectedStatus, ignoreCase = true)

        matchesSearch && matchesStatus
    }.sortedByDescending { it.requestTimestamp }

    val totalQty = filteredList.sumOf { it.quantity }
    val totalFineAmount = filteredList.sumOf { it.fineAmount }

    val pendingFineAmount = filteredList
        .filter { it.fineStatus.equals("Pending", ignoreCase = true) }
        .sumOf { it.fineAmount }

    val paidFineAmount = filteredList
        .filter { it.fineStatus.equals("Paid", ignoreCase = true) }
        .sumOf { it.fineAmount }

    val finedRequestCount = filteredList.count { it.fineAmount > 0 }
    val pendingCount = requestList.count { it.status.equals("Pending", ignoreCase = true) }
    val approvedCount = requestList.count { it.status.equals("Approved", ignoreCase = true) }
    val issuedCount = requestList.count { it.status.equals("Issued", ignoreCase = true) }
    val returnedCount = requestList.count { it.status.equals("Returned", ignoreCase = true) }
    val overdueCount = requestList.count { it.status.equals("Overdue", ignoreCase = true) }
    val lostDamagedCount = requestList.count {
        it.status.equals("Lost", ignoreCase = true) ||
                it.status.equals("Damaged", ignoreCase = true)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = RequestReportColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            BorrowReportHeroCard(
                title = title,
                subtitle = subtitle,
                totalRecords = filteredList.size
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BorrowReportStatCard(
                    title = "Records",
                    value = filteredList.size.toString(),
                    bgColor = RequestReportColors.BlueLight,
                    textColor = RequestReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                BorrowReportStatCard(
                    title = "Total Qty",
                    value = totalQty.toString(),
                    bgColor = RequestReportColors.PurpleLight,
                    textColor = RequestReportColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BorrowReportStatCard(
                    title = "Issued",
                    value = issuedCount.toString(),
                    bgColor = RequestReportColors.PurpleLight,
                    textColor = RequestReportColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )

                BorrowReportStatCard(
                    title = "Overdue",
                    value = overdueCount.toString(),
                    bgColor = RequestReportColors.RedLight,
                    textColor = RequestReportColors.RedText,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BorrowReportStatCard(
                    title = "Total Fine",
                    value = "$totalFineAmount Tk",
                    bgColor = RequestReportColors.RedLight,
                    textColor = RequestReportColors.RedText,
                    modifier = Modifier.weight(1f)
                )

                BorrowReportStatCard(
                    title = "Pending Fine",
                    value = "$pendingFineAmount Tk",
                    bgColor = RequestReportColors.OrangeLight,
                    textColor = RequestReportColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text("Search student, ID, department, equipment or status")
                }
            )

            if (fixedStatus == "All") {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statusFilters.forEach { status ->
                        BorrowReportFilterChip(
                            text = status,
                            selected = selectedStatus == status,
                            onClick = {
                                selectedStatus = status
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = RequestReportColors.TextDark
            )

            Text(
                text = "Showing ${filteredList.size} record(s)",
                style = MaterialTheme.typography.bodySmall,
                color = RequestReportColors.TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredList.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = RequestReportColors.CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No request report found for selected search/filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RequestReportColors.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { request ->
                        BorrowRequestReportCard(
                            request = request,
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Back to Reports",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BorrowReportHeroCard(
    title: String,
    subtitle: String,
    totalRecords: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        RequestReportColors.PrimaryIndigo,
                        RequestReportColors.PurpleAccent
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(22.dp)
    ) {
        Column {
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Borrow Report",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$subtitle • $totalRecords record(s)",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun BorrowReportStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = RequestReportColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = RequestReportColors.TextMuted,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = value,
                modifier = Modifier
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.titleLarge,
                color = textColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun BorrowRequestReportCard(
    request: BorrowRequest,
    onFinePaidClick: () -> Unit,
    onFineWaivedClick: () -> Unit
) {
    val statusColor = statusTextColor(request.status)
    val statusBg = statusBackgroundColor(request.status)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RequestReportColors.CardWhite),
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
                        fontWeight = FontWeight.ExtraBold,
                        color = RequestReportColors.TextDark
                    )

                    Text(
                        text = request.equipmentCategory.ifBlank { "No category" },
                        style = MaterialTheme.typography.bodySmall,
                        color = RequestReportColors.TextMuted
                    )
                }

                Text(
                    text = request.status.ifBlank { "Pending" },
                    modifier = Modifier
                        .background(
                            color = statusBg,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            HorizontalDivider(color = RequestReportColors.Background)

            BorrowReportInfoRow(
                label = "Student",
                value = request.userName.ifBlank { "Unknown Student" }
            )

            BorrowReportInfoRow(
                label = "Email",
                value = request.userEmail.ifBlank { "N/A" }
            )

            BorrowReportInfoRow(
                label = "Student ID",
                value = request.studentId.ifBlank { "N/A" }
            )

            BorrowReportInfoRow(
                label = "Department",
                value = request.department.ifBlank { "N/A" }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BorrowSmallBadge(
                    text = "Qty: ${request.quantity}",
                    bgColor = RequestReportColors.BlueLight,
                    textColor = RequestReportColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                BorrowSmallBadge(
                    text = "Due: ${request.dueDate.ifBlank { "N/A" }}",
                    bgColor = if (request.status.equals("Overdue", ignoreCase = true)) {
                        RequestReportColors.RedLight
                    } else {
                        RequestReportColors.GrayLight
                    },
                    textColor = if (request.status.equals("Overdue", ignoreCase = true)) {
                        RequestReportColors.RedText
                    } else {
                        RequestReportColors.GrayText
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            BorrowReportInfoRow(
                label = "Borrow Date",
                value = request.borrowDate.ifBlank { "N/A" }
            )

            if (request.returnedDate.isNotBlank()) {
                BorrowReportInfoRow(
                    label = "Returned Date",
                    value = request.returnedDate
                )
            }

            if (request.rejectedReason.isNotBlank()) {
                BorrowReportMessageBox(
                    message = "Rejected Reason: ${request.rejectedReason}",
                    bgColor = RequestReportColors.RedLight,
                    textColor = RequestReportColors.RedText
                )
            }

            if (request.returnCondition.isNotBlank()) {
                BorrowReportMessageBox(
                    message = "Return Condition: ${request.returnCondition}",
                    bgColor = RequestReportColors.GreenLight,
                    textColor = RequestReportColors.GreenText
                )
            }
            if (request.fineAmount > 0) {
                val fineStatus = request.fineStatus.ifBlank { "Pending" }

                BorrowReportMessageBox(
                    message = "Fine: ${request.fineAmount} Tk • Status: $fineStatus",
                    bgColor = when (fineStatus.lowercase()) {
                        "paid" -> RequestReportColors.GreenLight
                        "waived" -> RequestReportColors.BlueLight
                        else -> RequestReportColors.RedLight
                    },
                    textColor = when (fineStatus.lowercase()) {
                        "paid" -> RequestReportColors.GreenText
                        "waived" -> RequestReportColors.BlueText
                        else -> RequestReportColors.RedText
                    }
                )

                if (request.fineReason.isNotBlank()) {
                    BorrowReportMessageBox(
                        message = "Fine Reason: ${request.fineReason}",
                        bgColor = RequestReportColors.OrangeLight,
                        textColor = RequestReportColors.OrangeText
                    )
                }

                if (fineStatus.equals("Pending", ignoreCase = true)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onFinePaidClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = RequestReportColors.GreenText
                            )
                        ) {
                            Text(
                                text = "Mark Paid",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onFineWaivedClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = RequestReportColors.BlueText
                            )
                        ) {
                            Text(
                                text = "Waive",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            if (request.adminNote.isNotBlank()) {
                BorrowReportMessageBox(
                    message = "Admin Note: ${request.adminNote}",
                    bgColor = RequestReportColors.GrayLight,
                    textColor = RequestReportColors.GrayText
                )
            }
        }
    }
}

@Composable
private fun BorrowReportInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.weight(0.38f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = RequestReportColors.TextMuted
        )

        Text(
            text = value.ifBlank { "N/A" },
            modifier = Modifier.weight(0.62f),
            style = MaterialTheme.typography.bodySmall,
            color = RequestReportColors.TextDark,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BorrowSmallBadge(
    text: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(
                color = bgColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = textColor
    )
}

@Composable
private fun BorrowReportMessageBox(
    message: String,
    bgColor: Color,
    textColor: Color
) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = textColor
    )
}

@Composable
private fun BorrowReportFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RequestReportColors.BlueText,
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

private fun statusTextColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "pending" -> RequestReportColors.OrangeText
        "approved" -> RequestReportColors.BlueText
        "issued" -> RequestReportColors.PurpleText
        "returned" -> RequestReportColors.GreenText
        "rejected" -> RequestReportColors.RedText
        "overdue" -> RequestReportColors.RedText
        "lost" -> RequestReportColors.RedText
        "damaged" -> RequestReportColors.OrangeText
        "cancelled" -> RequestReportColors.GrayText
        else -> RequestReportColors.GrayText
    }
}

private fun statusBackgroundColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "pending" -> RequestReportColors.OrangeLight
        "approved" -> RequestReportColors.BlueLight
        "issued" -> RequestReportColors.PurpleLight
        "returned" -> RequestReportColors.GreenLight
        "rejected" -> RequestReportColors.RedLight
        "overdue" -> RequestReportColors.RedLight
        "lost" -> RequestReportColors.RedLight
        "damaged" -> RequestReportColors.OrangeLight
        "cancelled" -> RequestReportColors.GrayLight
        else -> RequestReportColors.GrayLight
    }
}