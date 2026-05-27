package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.data.model.Room

private object ManageLabColors {
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
    val PurpleLight = Color(0xFFF5F3FF)
}

@Composable
fun ManageLabComputersScreen(
    computerList: List<LabComputer>,
    onAddComputerClick: () -> Unit,
    onEditComputerClick: (LabComputer) -> Unit,
    onOpenSoftwareClick: (LabComputer) -> Unit,
    onViewReportsClick: (LabComputer) -> Unit,
    onBackClick: () -> Unit,
    roomList: List<Room> = emptyList()
) {
    val workingCount = computerList.count {
        val status = it.status.trim().lowercase()
        status == "working" || status == "active"
    }

    val issueCount = computerList.size - workingCount

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ManageLabColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            ManageLabTopBar(
                totalCount = computerList.size,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LabMiniStat(
                    title = "Working",
                    value = workingCount.toString(),
                    bgColor = ManageLabColors.GreenLight,
                    textColor = ManageLabColors.GreenText,
                    modifier = Modifier.weight(1f)
                )

                LabMiniStat(
                    title = "Need Check",
                    value = issueCount.toString(),
                    bgColor = ManageLabColors.OrangeLight,
                    textColor = ManageLabColors.OrangeText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAddComputerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ManageLabColors.Primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Add Lab Computer",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (computerList.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ManageLabColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No lab computer added yet.",
                            color = ManageLabColors.TextMuted,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(computerList) { computer ->
                        LabComputerCard(
                            computer = computer,
                            onEditClick = { onEditComputerClick(computer) },
                            onOpenSoftwareClick = { onOpenSoftwareClick(computer) },
                            onViewReportsClick = { onViewReportsClick(computer) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ManageLabTopBar(
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
                .background(ManageLabColors.Card, RoundedCornerShape(14.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = ManageLabColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(ManageLabColors.PurpleLight, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Computer,
                contentDescription = null,
                tint = ManageLabColors.Primary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Lab Computers",
                color = ManageLabColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "$totalCount computer(s) in system",
                color = ManageLabColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LabMiniStat(
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
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
private fun LabComputerCard(
    computer: LabComputer,
    onEditClick: () -> Unit,
    onOpenSoftwareClick: () -> Unit,
    onViewReportsClick: () -> Unit
) {
    val isWorking = computer.status.trim().lowercase().let { it == "working" || it == "active" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ManageLabColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(ManageLabColors.BlueLight, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (computer.computerImageUrl.trim().isNotBlank()) {
                        AsyncImage(
                            model = computer.computerImageUrl.trim(),
                            contentDescription = computer.pcName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentScale = ContentScale.Crop,
                            placeholder = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery),
                            error = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = ManageLabColors.BlueText,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = computer.pcName.ifBlank { "Unnamed Computer" },
                        color = ManageLabColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = computer.labRoom.ifBlank { "No lab room" },
                        color = ManageLabColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }

                Text(
                    text = computer.status.ifBlank { "Unknown" },
                    modifier = Modifier
                        .background(
                            if (isWorking) ManageLabColors.GreenLight else ManageLabColors.OrangeLight,
                            RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    color = if (isWorking) ManageLabColors.GreenText else ManageLabColors.OrangeText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = ManageLabColors.Bg)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "IP: ${computer.ipAddress.ifBlank { "N/A" }} • Location: ${computer.locationNote.ifBlank { "N/A" }}",
                color = ManageLabColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (computer.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = computer.remarks,
                    color = ManageLabColors.TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ManageLabColors.Primary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text("Edit Computer", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onOpenSoftwareClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ReportProblem,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("Software", fontWeight = FontWeight.Bold, maxLines = 1)
                }

                OutlinedButton(
                    onClick = onViewReportsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Assignment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("Reports", fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
        }
    }
}
