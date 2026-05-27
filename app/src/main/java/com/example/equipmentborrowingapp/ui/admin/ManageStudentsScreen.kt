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
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
private object StudentManageColors {
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
}

@Composable
fun ManageStudentsScreen(
    studentList: List<AppUser>,
    onStatusChangeClick: (AppUser, String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val filteredStudents = studentList.filter { student ->
        val query = searchText.trim().lowercase()

        val matchesSearch =
            query.isBlank() ||
                    student.name.lowercase().contains(query) ||
                    student.email.lowercase().contains(query) ||
                    student.studentId.lowercase().contains(query) ||
                    student.department.lowercase().contains(query) ||
                    student.phone.lowercase().contains(query)

        val matchesStatus =
            selectedStatus == "All" ||
                    student.verificationStatus.equals(selectedStatus, ignoreCase = true)

        matchesSearch && matchesStatus
    }.sortedBy { it.name.lowercase() }

    val verifiedCount = studentList.count {
        it.verificationStatus.equals("Verified", ignoreCase = true)
    }

    val pendingCount = studentList.count {
        it.verificationStatus.equals("Pending", ignoreCase = true)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StudentManageColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            ManageStudentsTopBar(
                totalCount = studentList.size,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StudentMiniStat("Total", studentList.size.toString(), StudentManageColors.BlueLight, StudentManageColors.BlueText, Modifier.weight(1f))
                StudentMiniStat("Verified", verifiedCount.toString(), StudentManageColors.GreenLight, StudentManageColors.GreenText, Modifier.weight(1f))
                StudentMiniStat("Pending", pendingCount.toString(), StudentManageColors.OrangeLight, StudentManageColors.OrangeText, Modifier.weight(1f))
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
                        tint = StudentManageColors.TextMuted
                    )
                },
                placeholder = { Text("Search student") }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Verified", "Pending", "Rejected", "Block").forEach { status ->
                    StudentStatusChip(
                        text = status.replaceFirstChar { it.uppercase() },
                        selected = selectedStatus == if (status == "Block") "Blocked" else status,
                        onClick = { selectedStatus = if (status == "Block") "Blocked" else status }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Showing ${filteredStudents.size} of ${studentList.size} student(s)",
                color = StudentManageColors.TextDark,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (filteredStudents.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = StudentManageColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No student found.",
                            color = StudentManageColors.TextMuted,
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
                    items(filteredStudents) { student ->
                        StudentManageCard(
                            student = student,
                            onStatusChangeClick = onStatusChangeClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ManageStudentsTopBar(
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
                .background(StudentManageColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = StudentManageColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(StudentManageColors.PurpleLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.VerifiedUser,
                contentDescription = null,
                tint = StudentManageColors.Primary,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Manage Students",
                color = StudentManageColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "$totalCount student account(s)",
                color = StudentManageColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun StudentMiniStat(
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
private fun StudentStatusChip(
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
                containerColor = StudentManageColors.Primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
private fun StudentManageCard(
    student: AppUser,
    onStatusChangeClick: (AppUser, String) -> Unit
) {
    val status = student.verificationStatus.ifBlank { "Pending" }
    val bgColor = when (status.lowercase()) {
        "verified" -> StudentManageColors.GreenLight
        "rejected", "blocked" -> StudentManageColors.RedLight
        "pending" -> StudentManageColors.OrangeLight
        else -> StudentManageColors.BlueLight
    }
    val textColor = when (status.lowercase()) {
        "verified" -> StudentManageColors.GreenText
        "rejected", "blocked" -> StudentManageColors.RedText
        "pending" -> StudentManageColors.OrangeText
        else -> StudentManageColors.BlueText
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = StudentManageColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(StudentManageColors.BlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (student.profileImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = student.profileImageUrl.trim(),
                            contentDescription = student.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            tint = StudentManageColors.BlueText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.name.ifBlank { "Unknown Student" },
                        color = StudentManageColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = student.email.ifBlank { "No email" },
                        color = StudentManageColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(text = status.replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .background(bgColor, RoundedCornerShape(50.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = textColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = StudentManageColors.Bg)
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "ID: ${student.studentId.ifBlank { "N/A" }} • ${student.department.ifBlank { "Department N/A" }}",
                color = StudentManageColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (student.semester.isNotBlank() || student.phone.isNotBlank()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = buildString {
                        if (student.semester.isNotBlank()) append("Semester: ${student.semester}")
                        if (student.phone.isNotBlank()) {
                            if (isNotBlank()) append(" • ")
                            append(student.phone)
                        }
                    },
                    color = StudentManageColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!status.equals("Verified", ignoreCase = true)) {
                    Button(
                        onClick = { onStatusChangeClick(student, "verified") },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudentManageColors.GreenText,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text("Verify", fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = { onStatusChangeClick(student, "blocked") },
                    modifier = Modifier
                        .then(
                            if (status.equals("Verified", ignoreCase = true)) {
                                Modifier.fillMaxWidth()
                            } else {
                                Modifier.weight(1f)
                            }
                        )
                        .height(38.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = StudentManageColors.RedText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Block,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text("Block", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
