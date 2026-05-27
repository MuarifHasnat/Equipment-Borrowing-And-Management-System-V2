package com.example.equipmentborrowingapp.ui.admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
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

    val isLostDamagedReport = fixedStatus.equals("Lost/Damaged", ignoreCase = true)

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
            fixedStatus.equals("Lost/Damaged", ignoreCase = true) ->
                request.status.equals("Lost", ignoreCase = true) ||
                        request.status.equals("Damaged", ignoreCase = true)

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
                    request.status.lowercase().contains(query) ||
                    request.fineReason.lowercase().contains(query)

        val matchesStatus =
            fixedStatus != "All" ||
                    selectedStatus == "All" ||
                    request.status.equals(selectedStatus, ignoreCase = true)

        matchesSearch && matchesStatus
    }.sortedByDescending { it.requestTimestamp }

    val totalQty = filteredList.sumOf { it.quantity }
    val totalFineAmount = filteredList.sumOf { it.fineAmount }
    val pendingFineAmount = filteredList
        .filter {
            it.fineAmount > 0 &&
                    !it.fineStatus.equals("Paid", ignoreCase = true) &&
                    !it.fineStatus.equals("Waived", ignoreCase = true)
        }
        .sumOf { it.fineAmount }

    val issuedCount = filteredList.count { it.status.equals("Issued", ignoreCase = true) }
    val overdueCount = filteredList.count { it.status.equals("Overdue", ignoreCase = true) }
    val lostCount = filteredList.count { it.status.equals("Lost", ignoreCase = true) }
    val damagedCount = filteredList.count { it.status.equals("Damaged", ignoreCase = true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = RequestReportColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            BorrowReportHeroCard(
                title = title,
                subtitle = subtitle,
                totalRecords = filteredList.size,
                totalFineAmount = totalFineAmount,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            CompactSummaryGrid(
                records = filteredList.size,
                totalQty = totalQty,
                firstTitle = if (isLostDamagedReport) "Lost" else "Issued",
                firstValue = if (isLostDamagedReport) lostCount.toString() else issuedCount.toString(),
                firstBg = if (isLostDamagedReport) RequestReportColors.RedLight else RequestReportColors.PurpleLight,
                firstText = if (isLostDamagedReport) RequestReportColors.RedText else RequestReportColors.PurpleText,
                secondTitle = if (isLostDamagedReport) "Damaged" else "Overdue",
                secondValue = if (isLostDamagedReport) damagedCount.toString() else overdueCount.toString(),
                secondBg = if (isLostDamagedReport) RequestReportColors.OrangeLight else RequestReportColors.RedLight,
                secondText = if (isLostDamagedReport) RequestReportColors.OrangeText else RequestReportColors.RedText,
                totalFine = totalFineAmount,
                pendingFine = pendingFineAmount
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text("Search")
                }
            )

            if (fixedStatus.equals("All", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(8.dp))

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
                            onClick = { selectedStatus = status }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = RequestReportColors.TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${filteredList.size} record(s)",
                    color = RequestReportColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredList.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RequestReportColors.CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No data found",
                            color = RequestReportColors.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
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
        }
    }
}

@Composable
private fun BorrowReportHeroCard(
    title: String,
    subtitle: String,
    totalRecords: Int,
    totalFineAmount: Int,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        RequestReportColors.PrimaryIndigo,
                        RequestReportColors.PurpleAccent
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.18f),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    modifier = Modifier.height(42.dp)
                ) {
                    Text(
                        text = "Back",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$totalRecords records",
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(50.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "$subtitle • Fine: $totalFineAmount Tk",
                color = Color.White.copy(alpha = 0.86f),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompactSummaryGrid(
    records: Int,
    totalQty: Int,
    firstTitle: String,
    firstValue: String,
    firstBg: Color,
    firstText: Color,
    secondTitle: String,
    secondValue: String,
    secondBg: Color,
    secondText: Color,
    totalFine: Int,
    pendingFine: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RequestReportColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TinyStat("Records", records.toString(), RequestReportColors.BlueLight, RequestReportColors.BlueText, Modifier.weight(1f))
                TinyStat("Qty", totalQty.toString(), RequestReportColors.PurpleLight, RequestReportColors.PurpleText, Modifier.weight(1f))
                TinyStat(firstTitle, firstValue, firstBg, firstText, Modifier.weight(1f))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TinyStat(secondTitle, secondValue, secondBg, secondText, Modifier.weight(1f))
                TinyStat("Fine", "$totalFine Tk", RequestReportColors.RedLight, RequestReportColors.RedText, Modifier.weight(1f))
                TinyStat("Pending", "$pendingFine Tk", RequestReportColors.OrangeLight, RequestReportColors.OrangeText, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TinyStat(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(42.dp),
        color = bgColor,
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.75f),
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
    val status = request.status.ifBlank { "Pending" }
    val statusColor = statusTextColor(status)
    val statusBg = statusBackgroundColor(status)
    val fineStatus = request.fineStatus.trim().ifBlank { "Pending" }
    val hasPendingFine = request.fineAmount > 0 &&
            !fineStatus.equals("Paid", ignoreCase = true) &&
            !fineStatus.equals("Waived", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = RequestReportColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                val fallbackImageResId = EquipmentImageMapper.getImageRes(request.equipmentImageName)
                val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(request.equipmentImageUrl)
                val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(request.equipmentImageUrl)

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(RequestReportColors.GrayLight, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasImageUrl) {
                        AsyncImage(
                            model = safeImageUrl,
                            contentDescription = request.equipmentName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentScale = ContentScale.Fit,
                            error = painterResource(id = fallbackImageResId),
                            placeholder = painterResource(id = fallbackImageResId)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = fallbackImageResId),
                            contentDescription = request.equipmentName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.equipmentName.ifBlank { "Unknown Equipment" },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = RequestReportColors.TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${request.userName.ifBlank { "Unknown Student" }} • Qty: ${request.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = RequestReportColors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = status,
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(50.dp))
                        .padding(horizontal = 9.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SmallInfoBox(
                    title = "Borrow",
                    value = request.borrowDate.ifBlank { "N/A" },
                    modifier = Modifier.weight(1f)
                )
                SmallInfoBox(
                    title = "Due",
                    value = request.dueDate.ifBlank { "N/A" },
                    modifier = Modifier.weight(1f)
                )
            }

            if (request.returnCondition.isNotBlank()) {
                MessagePill(
                    text = "Return: ${request.returnCondition}",
                    bgColor = when (request.returnCondition.lowercase()) {
                        "damaged" -> RequestReportColors.OrangeLight
                        "lost" -> RequestReportColors.RedLight
                        else -> RequestReportColors.GreenLight
                    },
                    textColor = when (request.returnCondition.lowercase()) {
                        "damaged" -> RequestReportColors.OrangeText
                        "lost" -> RequestReportColors.RedText
                        else -> RequestReportColors.GreenText
                    }
                )
            }

            if (request.fineAmount > 0) {
                MessagePill(
                    text = "Fine: ${request.fineAmount} Tk • $fineStatus",
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
                    Text(
                        text = "Reason: ${request.fineReason}",
                        style = MaterialTheme.typography.bodySmall,
                        color = RequestReportColors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (hasPendingFine) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onFinePaidClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RequestReportColors.GreenText,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Paid",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onFineWaivedClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(12.dp),
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
        }
    }
}

@Composable
private fun SmallInfoBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(40.dp),
        color = RequestReportColors.GrayLight,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = RequestReportColors.TextMuted,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = RequestReportColors.TextDark,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MessagePill(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
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
                containerColor = RequestReportColors.PrimaryIndigo,
                contentColor = Color.White
            ),
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
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
