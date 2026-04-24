package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.LabComputer

@Composable
fun ManageLabComputersScreen(
    computerList: List<LabComputer>,
    onAddComputerClick: () -> Unit,
    onEditComputerClick: (LabComputer) -> Unit,
    onOpenSoftwareClick: (LabComputer) -> Unit,
    onViewReportsClick: (LabComputer) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val statusFilters = listOf("All", "Active", "Maintenance", "Problematic")

    val filteredList = computerList.filter { computer ->
        val matchesSearch =
            searchQuery.isBlank() ||
                    computer.pcName.contains(searchQuery, ignoreCase = true) ||
                    computer.labRoom.contains(searchQuery, ignoreCase = true) ||
                    computer.locationNote.contains(searchQuery, ignoreCase = true)

        val matchesStatus =
            selectedStatus == "All" ||
                    computer.status.equals(selectedStatus, ignoreCase = true)

        matchesSearch && matchesStatus
    }

    val totalCount = computerList.size
    val activeCount = computerList.count { it.status.equals("Active", ignoreCase = true) }
    val problematicCount = computerList.count { it.status.equals("Problematic", ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lab Monitoring",
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Manage Lab PCs",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryCard(
                title = "Total",
                value = totalCount.toString(),
                bgColor = Color(0xFFEDE7F6),
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Active",
                value = activeCount.toString(),
                bgColor = Color(0xFFE8F5E9),
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Problem",
                value = problematicCount.toString(),
                bgColor = Color(0xFFFFEBEE),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("Search PC, room or location") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(statusFilters) { filter ->
                val isSelected = selectedStatus == filter

                OutlinedButton(
                    onClick = { selectedStatus = filter },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Color(0xFF7A19FF) else Color(0xFFF5F5F5),
                        contentColor = if (isSelected) Color.White else Color.Black
                    )
                ) {
                    Text(filter)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF7A19FF), Color(0xFF9C27B0))
                    ),
                    RoundedCornerShape(16.dp)
                )
                .clickable { onAddComputerClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Add New Lab Computer",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No matching lab computers found",
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredList, key = { it.id }) { computer ->
                    ComputerCard(
                        computer = computer,
                        onEdit = onEditComputerClick,
                        onSoftware = onOpenSoftwareClick,
                        onReports = onViewReportsClick
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor.copy(alpha = 0.45f))
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                color = Color(0xFF666666),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ComputerCard(
    computer: LabComputer,
    onEdit: (LabComputer) -> Unit,
    onSoftware: (LabComputer) -> Unit,
    onReports: (LabComputer) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = computer.pcName.ifBlank { "Unnamed PC" },
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Lab Room: ${computer.labRoom.ifBlank { "N/A" }}",
                color = Color.Gray
            )

            if (computer.locationNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Location: ${computer.locationNote}",
                    color = Color.Gray
                )
            }

            if (computer.ipAddress.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "IP Address: ${computer.ipAddress}",
                    color = Color.Gray
                )
            }

            if (computer.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Remarks: ${computer.remarks}",
                    color = Color(0xFF222222)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            StatusChip(
                text = computer.status.ifBlank { "Unknown" }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionBtn(
                    text = "Edit",
                    modifier = Modifier.weight(1f),
                    filled = true,
                    onClick = { onEdit(computer) }
                )

                ActionBtn(
                    text = "Software",
                    modifier = Modifier.weight(1f),
                    filled = false,
                    onClick = { onSoftware(computer) }
                )

                ActionBtn(
                    text = "Reports",
                    modifier = Modifier.weight(1f),
                    filled = false,
                    onClick = { onReports(computer) }
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    text: String
) {
    val (bgColor, textColor) = when (text.trim().lowercase()) {
        "active" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "problematic" -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        "maintenance" -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Pair(Color(0xFFF5F5F5), Color(0xFF616161))
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ActionBtn(
    text: String,
    modifier: Modifier = Modifier,
    filled: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = Color(0xFF7A19FF)

    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (filled) selectedColor else Color.White,
            contentColor = if (filled) Color.White else Color.Black
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (filled) selectedColor else Color(0xFFE0E0E0)
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}