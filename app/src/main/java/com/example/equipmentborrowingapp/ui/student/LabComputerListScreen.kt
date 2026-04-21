package com.example.equipmentborrowingapp.ui.student

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.LabComputer

private fun getLabPcStatusColors(status: String): Pair<Color, Color> {
    return when (status.trim().lowercase()) {
        "active" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "problematic" -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        "maintenance" -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Pair(Color(0xFFF5F5F5), Color(0xFF616161))
    }
}

@Composable
private fun LabPcStatusBadge(status: String) {
    val colors = getLabPcStatusColors(status)

    Box(
        modifier = Modifier
            .background(colors.first, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = status.ifBlank { "Unknown" },
            color = colors.second,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LabComputerListScreen(
    computerList: List<LabComputer>,
    onReportClick: (LabComputer) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lab Computers",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "View available lab PCs and report software issues",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (computerList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No lab computers found",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 14.dp)
            ) {
                items(
                    items = computerList.sortedBy { it.pcName.lowercase() },
                    key = { it.id }
                ) { computer ->
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
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Lab Room: ${computer.labRoom.ifBlank { "N/A" }}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (computer.locationNote.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Location: ${computer.locationNote}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )
                            }

                            if (computer.ipAddress.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "IP Address: ${computer.ipAddress}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            if (computer.remarks.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Remarks: ${computer.remarks}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            LabPcStatusBadge(status = computer.status)

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = Color(0xFFEAEAEA))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ActionButtonBox(
                                    text = "Report Issue",
                                    filled = true,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onReportClick(computer) }
                                )

                                ActionButtonBox(
                                    text = "Back",
                                    filled = false,
                                    modifier = Modifier.weight(1f),
                                    onClick = onBackClick
                                )
                            }
                        }
                    }
                }
            }
        }

        if (computerList.isEmpty()) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
private fun ActionButtonBox(
    text: String,
    filled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val purple = Color(0xFF7A19FF)
    val bg = if (filled) purple else Color.White
    val textColor = if (filled) Color.White else Color(0xFF222222)
    val borderColor = if (filled) purple else Color(0xFFE0E0E0)

    Box(
        modifier = modifier
            .height(42.dp)
            .background(bg, RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}