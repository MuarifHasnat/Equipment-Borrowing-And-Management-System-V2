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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.AppUser
import com.example.equipmentborrowingapp.data.model.BorrowRequest

private object HistoryColors {
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

private data class StudentHistoryGroup(
    val student: AppUser?,
    val userId: String,
    val requests: List<BorrowRequest>
)

@Composable
fun StudentBorrowHistoryReportScreen(
    studentList: List<AppUser>,
    requestList: List<BorrowRequest>,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val studentById = remember(studentList) { studentList.associateBy { it.uid } }

    val groups = requestList
        .groupBy { it.userId }
        .map { entry ->
            StudentHistoryGroup(
                student = studentById[entry.key],
                userId = entry.key,
                requests = entry.value.sortedByDescending { it.requestTimestamp }
            )
        }
        .sortedBy { it.student?.name ?: it.requests.firstOrNull()?.userName ?: "Unknown Student" }

    val filteredGroups = groups.mapNotNull { group ->
        val filteredRequests = group.requests.filter { request ->
            val query = searchText.trim().lowercase()
            val student = group.student

            val matchesSearch =
                query.isBlank() ||
                        request.userName.lowercase().contains(query) ||
                        request.userEmail.lowercase().contains(query) ||
                        request.studentId.lowercase().contains(query) ||
                        request.department.lowercase().contains(query) ||
                        request.equipmentName.lowercase().contains(query) ||
                        student?.name.orEmpty().lowercase().contains(query)

            val matchesStatus =
                selectedStatus == "All" ||
                        request.status.equals(selectedStatus, ignoreCase = true)

            matchesSearch && matchesStatus
        }

        if (filteredRequests.isEmpty()) null else group.copy(requests = filteredRequests)
    }

    val returnedCount = requestList.count { it.status.equals("Returned", ignoreCase = true) }
    val overdueCount = requestList.count { it.status.equals("Overdue", ignoreCase = true) }

    Surface(modifier = Modifier.fillMaxSize(), color = HistoryColors.Bg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            StudentHistoryTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HistoryMiniStat("Students", studentList.size.toString(), HistoryColors.BlueLight, HistoryColors.BlueText, Modifier.weight(1f))
                HistoryMiniStat("Requests", requestList.size.toString(), HistoryColors.PurpleLight, HistoryColors.PurpleText, Modifier.weight(1f))
                HistoryMiniStat("Returned", returnedCount.toString(), HistoryColors.GreenLight, HistoryColors.GreenText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            HistoryMiniStat("Overdue Requests", overdueCount.toString(), HistoryColors.RedLight, HistoryColors.RedText, Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = HistoryColors.TextMuted)
                },
                placeholder = {
                    Text(
                        text = "Search student/equipment",
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
                listOf("All", "Pending", "Approved", "Issued", "Returned", "Late", "Lost", "Damage").forEach { status ->
                    HistoryFilterChip(
                        text = status,
                        selected = selectedStatus == when (status) {
                            "Late" -> "Overdue"
                            "Damage" -> "Damaged"
                            else -> status
                        },
                        onClick = {
                            selectedStatus = when (status) {
                                "Late" -> "Overdue"
                                "Damage" -> "Damaged"
                                else -> status
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Showing ${filteredGroups.size} student history group(s)",
                color = HistoryColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (filteredGroups.isEmpty()) {
                EmptyHistoryCard()
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(filteredGroups) { group ->
                        StudentHistoryCard(group)
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentHistoryTopBar(onBackClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(HistoryColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = HistoryColors.TextDark)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(HistoryColors.PurpleLight, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.History, contentDescription = null, tint = HistoryColors.Primary, modifier = Modifier.size(23.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Borrow History",
                color = HistoryColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Student-wise records",
                color = HistoryColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StudentHistoryCard(group: StudentHistoryGroup) {
    val studentName = group.student?.name ?: group.requests.firstOrNull()?.userName ?: "Unknown Student"
    val email = group.student?.email ?: group.requests.firstOrNull()?.userEmail ?: "No email"
    val returned = group.requests.count { it.status.equals("Returned", ignoreCase = true) }
    val active = group.requests.count {
        it.status.equals("Approved", ignoreCase = true) ||
                it.status.equals("Issued", ignoreCase = true) ||
                it.status.equals("Overdue", ignoreCase = true)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = HistoryColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(HistoryColors.BlueLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Person, contentDescription = null, tint = HistoryColors.BlueText, modifier = Modifier.size(22.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(studentName, color = HistoryColors.TextDark, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(email, color = HistoryColors.TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Text(
                    text = "${group.requests.size} request(s)",
                    modifier = Modifier
                        .background(HistoryColors.PurpleLight, RoundedCornerShape(50.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = HistoryColors.PurpleText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = HistoryColors.Bg)
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HistoryInfoPill("Returned", returned.toString(), HistoryColors.GreenLight, HistoryColors.GreenText, Modifier.weight(1f))
                HistoryInfoPill("Active", active.toString(), HistoryColors.OrangeLight, HistoryColors.OrangeText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            group.requests.take(3).forEach { request ->
                RequestLine(request)
            }

            if (group.requests.size > 3)  {
                Spacer(modifier = Modifier.height(4.dp))
                Text("+ ${group.requests.size - 3} more record(s)", color = HistoryColors.TextMuted, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RequestLine(request: BorrowRequest) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = request.equipmentName.ifBlank { "Unknown Equipment" },
            modifier = Modifier.weight(1f),
            color = HistoryColors.TextDark,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = when (request.status.trim().lowercase()) {
                "damaged" -> "Damage"
                "overdue" -> "Late"
                else -> request.status.ifBlank { "N/A" }
            },
            modifier = Modifier
                .background(historyStatusBg(request.status), RoundedCornerShape(50.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = historyStatusText(request.status),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HistoryMiniStat(title: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.height(52.dp), color = bgColor, shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = textColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(value, color = textColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1)
        }
    }
}

@Composable
private fun HistoryInfoPill(title: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.height(42.dp), color = bgColor, shape = RoundedCornerShape(14.dp)) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = textColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(value, color = textColor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun HistoryFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HistoryColors.Primary, contentColor = Color.White),
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
private fun EmptyHistoryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = HistoryColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No student borrow history found.", color = HistoryColors.TextMuted, fontWeight = FontWeight.SemiBold)
        }
    }
}

private fun historyStatusText(status: String): Color {
    return when (status.lowercase()) {
        "returned" -> HistoryColors.GreenText
        "overdue", "lost" -> HistoryColors.RedText
        "issued" -> HistoryColors.PurpleText
        "approved" -> HistoryColors.BlueText
        "damaged", "pending" -> HistoryColors.OrangeText
        else -> HistoryColors.GrayText
    }
}

private fun historyStatusBg(status: String): Color {
    return when (status.lowercase()) {
        "returned" -> HistoryColors.GreenLight
        "overdue", "lost" -> HistoryColors.RedLight
        "issued" -> HistoryColors.PurpleLight
        "approved" -> HistoryColors.BlueLight
        "damaged", "pending" -> HistoryColors.OrangeLight
        else -> HistoryColors.GrayLight
    }
}
