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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.PendingActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.SoftwareInstallRequest

private object InstallAdminColors {
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
    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun SoftwareInstallRequestsAdminScreen(
    requestList: List<SoftwareInstallRequest>,
    onApproveClick: (SoftwareInstallRequest) -> Unit,
    onRejectClick: (SoftwareInstallRequest) -> Unit,
    onInstalledClick: (SoftwareInstallRequest) -> Unit,
    onBackClick: () -> Unit
) {
    val pendingCount = requestList.count { it.status.equals("Pending", ignoreCase = true) }
    val approvedCount = requestList.count { it.status.equals("Approved", ignoreCase = true) }
    val installedCount = requestList.count { it.status.equals("Installed", ignoreCase = true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = InstallAdminColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(42.dp)
                        .background(InstallAdminColors.Card, RoundedCornerShape(14.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = InstallAdminColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(InstallAdminColors.BlueLight, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Download,
                        contentDescription = null,
                        tint = InstallAdminColors.BlueText,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Install Requests",
                        color = InstallAdminColors.TextDark,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Review student software installation requests",
                        color = InstallAdminColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                InstallMiniStat("Pending", pendingCount.toString(), InstallAdminColors.OrangeLight, InstallAdminColors.OrangeText, Modifier.weight(1f))
                InstallMiniStat("Approved", approvedCount.toString(), InstallAdminColors.BlueLight, InstallAdminColors.BlueText, Modifier.weight(1f))
                InstallMiniStat("Installed", installedCount.toString(), InstallAdminColors.GreenLight, InstallAdminColors.GreenText, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (requestList.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = InstallAdminColors.Card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No software install request found.",
                            color = InstallAdminColors.TextMuted,
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
                    items(requestList) { request ->
                        InstallRequestCard(
                            request = request,
                            onApproveClick = { onApproveClick(request) },
                            onRejectClick = { onRejectClick(request) },
                            onInstalledClick = { onInstalledClick(request) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstallRequestCard(
    request: SoftwareInstallRequest,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit,
    onInstalledClick: () -> Unit
) {
    val status = request.status.ifBlank { "Pending" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = InstallAdminColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(InstallAdminColors.BlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (request.computerImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = request.computerImageUrl.trim(),
                            contentDescription = request.computerName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                            error = painterResource(id = android.R.drawable.ic_menu_gallery)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Computer,
                            contentDescription = null,
                            tint = InstallAdminColors.BlueText,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.softwareName.ifBlank { "Unknown Software" },
                        color = InstallAdminColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Version: ${request.version.ifBlank { "N/A" }}",
                        color = InstallAdminColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )

                    Text(
                        text = "PC: ${request.computerName.ifBlank { "Unknown PC" }}",
                        color = InstallAdminColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = status,
                        modifier = Modifier
                            .background(statusBg(status), RoundedCornerShape(50.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp),
                        color = statusText(status),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Student: ${request.requestedByUserName.ifBlank { "N/A" }} • ID: ${request.requestedByStudentId.ifBlank { "N/A" }} • ${request.requestedByDepartment.ifBlank { "N/A" }}",
                color = InstallAdminColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Reason: ${request.reason.ifBlank { "N/A" }}",
                color = InstallAdminColors.TextDark,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (status.equals("Pending", ignoreCase = true)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onApproveClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = InstallAdminColors.GreenText,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Approve", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onRejectClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = InstallAdminColors.RedText
                        )
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Reject", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (status.equals("Approved", ignoreCase = true)) {
                Button(
                    onClick = onInstalledClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InstallAdminColors.Primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Rounded.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mark Installed", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InstallMiniStat(
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
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = value,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
    }
}

private fun statusText(status: String): Color {
    return when (status.trim().lowercase()) {
        "approved" -> InstallAdminColors.BlueText
        "rejected" -> InstallAdminColors.RedText
        "installed" -> InstallAdminColors.GreenText
        else -> InstallAdminColors.OrangeText
    }
}

private fun statusBg(status: String): Color {
    return when (status.trim().lowercase()) {
        "approved" -> InstallAdminColors.BlueLight
        "rejected" -> InstallAdminColors.RedLight
        "installed" -> InstallAdminColors.GreenLight
        else -> InstallAdminColors.OrangeLight
    }
}