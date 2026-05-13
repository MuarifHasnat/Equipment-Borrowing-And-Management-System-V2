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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.AppUser

@Composable
fun ManageStudentsScreen(
    studentList: List<AppUser>,
    onStatusChangeClick: (AppUser, String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedDepartment by remember { mutableStateOf("All") }
    var selectedSemester by remember { mutableStateOf("All") }
    var selectedStudent by remember { mutableStateOf<AppUser?>(null) }

    val departmentList = listOf("All") + studentList
        .map { it.department.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .sorted()

    val semesterList = listOf("All") + studentList
        .map { it.semester.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .sorted()

    val statusList = listOf(
        "All",
        "pending",
        "verified",
        "rejected",
        "suspended"
    )

    val filteredStudents = studentList.filter { student ->
        val query = searchText.trim().lowercase()

        val matchesSearch =
            query.isBlank() ||
                    student.name.lowercase().contains(query) ||
                    student.email.lowercase().contains(query) ||
                    student.studentId.lowercase().contains(query)

        val matchesStatus =
            selectedStatus == "All" ||
                    student.verificationStatus.equals(selectedStatus, ignoreCase = true)

        val matchesDepartment =
            selectedDepartment == "All" ||
                    student.department.equals(selectedDepartment, ignoreCase = true)

        val matchesSemester =
            selectedSemester == "All" ||
                    student.semester.equals(selectedSemester, ignoreCase = true)

        matchesSearch && matchesStatus && matchesDepartment && matchesSemester
    }.sortedWith(
        compareBy<AppUser> { statusSortOrder(it.verificationStatus) }
            .thenBy { it.name.lowercase() }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Manage Students",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Text(
            text = "Search, filter and manage pending, verified, rejected and suspended students.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text("Search by name, email or student ID")
            },
            shape = RoundedCornerShape(14.dp)
        )

        Text(
            text = "Status",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            statusList.forEach { status ->
                StudentFilterChip(
                    text = displayStatus(status),
                    selected = selectedStatus == status,
                    onClick = {
                        selectedStatus = status
                    }
                )
            }
        }

        Text(
            text = "Department",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            departmentList.forEach { department ->
                StudentFilterChip(
                    text = department,
                    selected = selectedDepartment == department,
                    onClick = {
                        selectedDepartment = department
                    }
                )
            }
        }

        Text(
            text = "Semester",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            semesterList.forEach { semester ->
                StudentFilterChip(
                    text = semester,
                    selected = selectedSemester == semester,
                    onClick = {
                        selectedSemester = semester
                    }
                )
            }
        }

        if (
            searchText.isNotBlank() ||
            selectedStatus != "All" ||
            selectedDepartment != "All" ||
            selectedSemester != "All"
        ) {
            OutlinedButton(
                onClick = {
                    searchText = ""
                    selectedStatus = "All"
                    selectedDepartment = "All"
                    selectedSemester = "All"
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Clear Search and Filters")
            }
        }

        selectedStudent?.let { student ->
            StudentDetailsCard(
                student = student,
                onStatusChangeClick = onStatusChangeClick,
                onCloseClick = {
                    selectedStudent = null
                }
            )
        }

        Text(
            text = "Students Found: ${filteredStudents.size}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        if (filteredStudents.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No students found for the selected filter.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredStudents) { student ->
                    StudentManagementCard(
                        student = student,
                        onViewDetailsClick = {
                            selectedStudent = student
                        },
                        onStatusChangeClick = onStatusChangeClick
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun StudentDetailsScreen(
    student: AppUser,
    onStatusChangeClick: (AppUser, String) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Student Details",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        StudentDetailsCard(
            student = student,
            onStatusChangeClick = onStatusChangeClick,
            onCloseClick = onBackClick
        )
    }
}

@Composable
private fun StudentManagementCard(
    student: AppUser,
    onViewDetailsClick: () -> Unit,
    onStatusChangeClick: (AppUser, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.name.ifBlank { "Unnamed Student" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Text(
                        text = student.email.ifBlank { "No email found" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }

                StudentStatusBadge(status = student.verificationStatus)
            }

            StudentInfoRow(
                label = "Student ID",
                value = student.studentId.ifBlank { "N/A" }
            )

            StudentInfoRow(
                label = "Department",
                value = student.department.ifBlank { "N/A" }
            )

            StudentInfoRow(
                label = "Semester",
                value = student.semester.ifBlank { "N/A" }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetailsClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Details")
                }

                StudentStatusActionButtons(
                    student = student,
                    modifier = Modifier.weight(2f),
                    onStatusChangeClick = onStatusChangeClick
                )
            }
        }
    }
}

@Composable
private fun StudentDetailsCard(
    student: AppUser,
    onStatusChangeClick: (AppUser, String) -> Unit,
    onCloseClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                        text = student.name.ifBlank { "Unnamed Student" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Text(
                        text = student.email.ifBlank { "No email found" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }

                StudentStatusBadge(status = student.verificationStatus)
            }

            Spacer(modifier = Modifier.height(4.dp))

            StudentInfoRow("UID", student.uid.ifBlank { "N/A" })
            StudentInfoRow("Institution ID", student.institutionId.ifBlank { "N/A" })
            StudentInfoRow("Student ID", student.studentId.ifBlank { "N/A" })
            StudentInfoRow("Department", student.department.ifBlank { "N/A" })
            StudentInfoRow("Semester", student.semester.ifBlank { "N/A" })
            StudentInfoRow("Phone", student.phone.ifBlank { "N/A" })

            StudentStatusActionButtons(
                student = student,
                modifier = Modifier.fillMaxWidth(),
                onStatusChangeClick = onStatusChangeClick
            )

            OutlinedButton(
                onClick = onCloseClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Close")
            }
        }
    }
}

@Composable
private fun StudentStatusActionButtons(
    student: AppUser,
    modifier: Modifier = Modifier,
    onStatusChangeClick: (AppUser, String) -> Unit
) {
    val currentStatus = student.verificationStatus.trim().lowercase()

    val actions = when (currentStatus) {
        "pending" -> listOf(
            "verified" to "Verify",
            "rejected" to "Reject"
        )

        "verified" -> listOf(
            "suspended" to "Suspend",
            "rejected" to "Reject"
        )

        "rejected" -> listOf(
            "verified" to "Verify"
        )

        "suspended" -> listOf(
            "verified" to "Verify"
        )

        else -> listOf(
            "verified" to "Verify"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        actions.forEach { action ->
            val targetStatus = action.first
            val buttonText = action.second

            Button(
                onClick = {
                    onStatusChangeClick(student, targetStatus)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = actionColor(targetStatus),
                    contentColor = Color.White
                )
            ) {
                Text(buttonText)
            }
        }
    }
}

@Composable
private fun StudentStatusBadge(status: String) {
    val normalizedStatus = status.trim().lowercase().ifBlank { "pending" }

    val backgroundColor = when (normalizedStatus) {
        "pending" -> Color(0xFFFEF3C7)
        "verified" -> Color(0xFFDCFCE7)
        "rejected" -> Color(0xFFFEE2E2)
        "suspended" -> Color(0xFFEDE9FE)
        else -> Color(0xFFE5E7EB)
    }

    val textColor = when (normalizedStatus) {
        "pending" -> Color(0xFF92400E)
        "verified" -> Color(0xFF166534)
        "rejected" -> Color(0xFFB91C1C)
        "suspended" -> Color(0xFF6D28D9)
        else -> Color(0xFF374151)
    }

    Text(
        text = displayStatus(normalizedStatus),
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
private fun StudentInfoRow(
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
            color = Color(0xFF6B7280)
        )

        Text(
            text = value.ifBlank { "N/A" },
            modifier = Modifier.weight(0.62f),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF111827)
        )
    }
}

@Composable
private fun StudentFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
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

private fun actionColor(status: String): Color {
    return when (status.trim().lowercase()) {
        "verified" -> Color(0xFF16A34A)
        "rejected" -> Color(0xFFDC2626)
        "suspended" -> Color(0xFF7C3AED)
        else -> Color(0xFF2563EB)
    }
}

private fun displayStatus(status: String): String {
    return when (status.trim().lowercase()) {
        "all" -> "All"
        "pending" -> "Pending"
        "verified" -> "Verified"
        "rejected" -> "Rejected"
        "suspended" -> "Suspended"
        else -> status.ifBlank { "Unknown" }
    }
}

private fun statusSortOrder(status: String): Int {
    return when (status.trim().lowercase()) {
        "pending" -> 0
        "verified" -> 1
        "rejected" -> 2
        "suspended" -> 3
        else -> 4
    }
}