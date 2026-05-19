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
import com.example.equipmentborrowingapp.data.model.AppUser
import com.example.equipmentborrowingapp.data.model.BorrowRequest

private object StudentHistoryColors {
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

private data class StudentBorrowHistoryGroup(
    val student: AppUser?,
    val userId: String,
    val displayName: String,
    val displayEmail: String,
    val studentId: String,
    val department: String,
    val semester: String,
    val requestList: List<BorrowRequest>
)

@Composable
fun StudentBorrowHistoryReportScreen(
    studentList: List<AppUser>,
    requestList: List<BorrowRequest>,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val studentByUid = remember(studentList) {
        studentList.associateBy { it.uid }
    }

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

    val filteredRequests = requestList.filter { request ->
        val query = searchText.trim().lowercase()

        val student = studentByUid[request.userId]

        val studentName = student?.name.orEmpty().ifBlank { request.userName }
        val studentEmail = student?.email.orEmpty().ifBlank { request.userEmail }
        val sid = student?.studentId.orEmpty().ifBlank { request.studentId }
        val dept = student?.department.orEmpty().ifBlank { request.department }

        val matchesSearch =
            query.isBlank() ||
                    studentName.lowercase().contains(query) ||
                    studentEmail.lowercase().contains(query) ||
                    sid.lowercase().contains(query) ||
                    dept.lowercase().contains(query) ||
                    request.equipmentName.lowercase().contains(query) ||
                    request.equipmentCategory.lowercase().contains(query) ||
                    request.status.lowercase().contains(query)

        val matchesStatus =
            selectedStatus == "All" ||
                    request.status.equals(selectedStatus, ignoreCase = true)

        matchesSearch && matchesStatus
    }

    val historyGroups = filteredRequests
        .groupBy { it.userId.ifBlank { it.userName.ifBlank { "unknown" } } }
        .map { entry ->
            val userId = entry.key
            val requests = entry.value.sortedByDescending { it.requestTimestamp }
            val firstRequest = requests.firstOrNull()
            val student = studentByUid[userId]

            StudentBorrowHistoryGroup(
                student = student,
                userId = userId,
                displayName = student?.name.orEmpty()
                    .ifBlank { firstRequest?.userName.orEmpty() }
                    .ifBlank { "Unknown Student" },
                displayEmail = student?.email.orEmpty()
                    .ifBlank { firstRequest?.userEmail.orEmpty() }
                    .ifBlank { "No email found" },
                studentId = student?.studentId.orEmpty()
                    .ifBlank { firstRequest?.studentId.orEmpty() }
                    .ifBlank { "N/A" },
                department = student?.department.orEmpty()
                    .ifBlank { firstRequest?.department.orEmpty() }
                    .ifBlank { "N/A" },
                semester = student?.semester.orEmpty().ifBlank { "N/A" },
                requestList = requests
            )
        }
        .sortedBy { it.displayName.lowercase() }

    val totalRequests = requestList.size
    val activeRequests = requestList.count {
        it.status.equals("Approved", ignoreCase = true) ||
                it.status.equals("Issued", ignoreCase = true) ||
                it.status.equals("Overdue", ignoreCase = true)
    }
    val returnedRequests = requestList.count {
        it.status.equals("Returned", ignoreCase = true)
    }
    val problemRequests = requestList.count {
        it.status.equals("Overdue", ignoreCase = true) ||
                it.status.equals("Lost", ignoreCase = true) ||
                it.status.equals("Damaged", ignoreCase = true)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StudentHistoryColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            StudentHistoryHeroCard(
                totalStudents = studentList.size,
                totalRequests = totalRequests
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StudentHistoryStatCard(
                    title = "Requests",
                    value = totalRequests.toString(),
                    bgColor = StudentHistoryColors.BlueLight,
                    textColor = StudentHistoryColors.BlueText,
                    modifier = Modifier.weight(1f)
                )

                StudentHistoryStatCard(
                    title = "Active",
                    value = activeRequests.toString(),
                    bgColor = StudentHistoryColors.OrangeLight,
                    textColor = StudentHistoryColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StudentHistoryStatCard(
                    title = "Returned",
                    value = returnedRequests.toString(),
                    bgColor = StudentHistoryColors.GreenLight,
                    textColor = StudentHistoryColors.GreenText,
                    modifier = Modifier.weight(1f)
                )

                StudentHistoryStatCard(
                    title = "Issues",
                    value = problemRequests.toString(),
                    bgColor = StudentHistoryColors.RedLight,
                    textColor = StudentHistoryColors.RedText,
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statusFilters.forEach { status ->
                    StudentHistoryFilterChip(
                        text = status,
                        selected = selectedStatus == status,
                        onClick = {
                            selectedStatus = status
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Student-wise Borrow History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = StudentHistoryColors.TextDark
            )

            Text(
                text = "Showing ${historyGroups.size} student group(s)",
                style = MaterialTheme.typography.bodySmall,
                color = StudentHistoryColors.TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (historyGroups.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = StudentHistoryColors.CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No student borrow history found for selected search/filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = StudentHistoryColors.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(historyGroups) { group ->
                        StudentBorrowHistoryGroupCard(group = group)
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
private fun StudentHistoryHeroCard(
    totalStudents: Int,
    totalRequests: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        StudentHistoryColors.PrimaryIndigo,
                        StudentHistoryColors.PurpleAccent
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
                    text = "Student Report",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Student Borrow History",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$totalStudents student(s), $totalRequests borrow request record(s)",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun StudentHistoryStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = StudentHistoryColors.CardWhite),
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
                color = StudentHistoryColors.TextMuted,
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
private fun StudentBorrowHistoryGroupCard(
    group: StudentBorrowHistoryGroup
) {
    val totalQuantity = group.requestList.sumOf { it.quantity }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = StudentHistoryColors.CardWhite),
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
                        text = group.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudentHistoryColors.TextDark
                    )

                    Text(
                        text = group.displayEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = StudentHistoryColors.TextMuted
                    )

                    Text(
                        text = "ID: ${group.studentId} • ${group.department} • Semester: ${group.semester}",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudentHistoryColors.TextMuted
                    )
                }

                Text(
                    text = "${group.requestList.size} request(s)",
                    modifier = Modifier
                        .background(
                            color = StudentHistoryColors.BlueLight,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = StudentHistoryColors.BlueText
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StudentSmallBadge(
                    text = "Total Qty: $totalQuantity",
                    bgColor = StudentHistoryColors.PurpleLight,
                    textColor = StudentHistoryColors.PurpleText,
                    modifier = Modifier.weight(1f)
                )

                StudentSmallBadge(
                    text = "Returned: ${
                        group.requestList.count { it.status.equals("Returned", ignoreCase = true) }
                    }",
                    bgColor = StudentHistoryColors.GreenLight,
                    textColor = StudentHistoryColors.GreenText,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(color = StudentHistoryColors.Background)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                group.requestList.forEach { request ->
                    StudentBorrowRequestRow(request = request)
                }
            }
        }
    }
}

@Composable
private fun StudentBorrowRequestRow(
    request: BorrowRequest
) {
    val statusColor = statusTextColor(request.status)
    val statusBg = statusBackgroundColor(request.status)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = StudentHistoryColors.Background,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = request.equipmentName.ifBlank { "Unknown Equipment" },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = StudentHistoryColors.TextDark
                )

                Text(
                    text = request.equipmentCategory.ifBlank { "No category" },
                    style = MaterialTheme.typography.bodySmall,
                    color = StudentHistoryColors.TextMuted
                )
            }

            Text(
                text = request.status.ifBlank { "Pending" },
                modifier = Modifier
                    .background(
                        color = statusBg,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StudentSmallBadge(
                text = "Qty: ${request.quantity}",
                bgColor = StudentHistoryColors.BlueLight,
                textColor = StudentHistoryColors.BlueText,
                modifier = Modifier.weight(1f)
            )

            StudentSmallBadge(
                text = "Due: ${request.dueDate.ifBlank { "N/A" }}",
                bgColor = if (request.status.equals("Overdue", ignoreCase = true)) {
                    StudentHistoryColors.RedLight
                } else {
                    StudentHistoryColors.GrayLight
                },
                textColor = if (request.status.equals("Overdue", ignoreCase = true)) {
                    StudentHistoryColors.RedText
                } else {
                    StudentHistoryColors.GrayText
                },
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Borrow Date: ${request.borrowDate.ifBlank { "N/A" }}",
            style = MaterialTheme.typography.bodySmall,
            color = StudentHistoryColors.TextMuted
        )

        if (request.returnedDate.isNotBlank()) {
            Text(
                text = "Returned Date: ${request.returnedDate}",
                style = MaterialTheme.typography.bodySmall,
                color = StudentHistoryColors.GreenText,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun StudentSmallBadge(
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
private fun StudentHistoryFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = StudentHistoryColors.BlueText,
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
        "pending" -> StudentHistoryColors.OrangeText
        "approved" -> StudentHistoryColors.BlueText
        "issued" -> StudentHistoryColors.PurpleText
        "returned" -> StudentHistoryColors.GreenText
        "rejected" -> StudentHistoryColors.RedText
        "overdue" -> StudentHistoryColors.RedText
        "lost" -> StudentHistoryColors.RedText
        "damaged" -> StudentHistoryColors.OrangeText
        "cancelled" -> StudentHistoryColors.GrayText
        else -> StudentHistoryColors.GrayText
    }
}

private fun statusBackgroundColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "pending" -> StudentHistoryColors.OrangeLight
        "approved" -> StudentHistoryColors.BlueLight
        "issued" -> StudentHistoryColors.PurpleLight
        "returned" -> StudentHistoryColors.GreenLight
        "rejected" -> StudentHistoryColors.RedLight
        "overdue" -> StudentHistoryColors.RedLight
        "lost" -> StudentHistoryColors.RedLight
        "damaged" -> StudentHistoryColors.OrangeLight
        "cancelled" -> StudentHistoryColors.GrayLight
        else -> StudentHistoryColors.GrayLight
    }
}