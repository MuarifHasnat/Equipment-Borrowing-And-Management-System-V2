package com.example.equipmentborrowingapp.ui.student


import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.LabComputer

private object InstallRequestColors {
    val Background = Color(0xFFF4F7FB)
    val CardWhite = Color.White
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val Primary = Color(0xFF4F46E5)
    val PrimarySoft = Color(0xFFEFF6FF)
    val PurpleSoft = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
    val GreenSoft = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)
    val RedText = Color(0xFFDC2626)
}

@Composable
fun SoftwareInstallRequestScreen(
    computer: LabComputer,
    studentName: String,
    studentId: String,
    department: String,
    onSubmitClick: (
        softwareName: String,
        version: String,
        softwareLogoUrl: String,
        reason: String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var softwareName by remember { mutableStateOf("") }
    var version by remember { mutableStateOf("") }
    var softwareLogoUrl by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = InstallRequestColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(42.dp)
                        .background(InstallRequestColors.CardWhite, RoundedCornerShape(14.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = InstallRequestColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(InstallRequestColors.PurpleSoft, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Download,
                        contentDescription = null,
                        tint = InstallRequestColors.PurpleText,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Request Software",
                        color = InstallRequestColors.TextDark,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Ask admin to install software",
                        color = InstallRequestColors.TextMuted,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = InstallRequestColors.CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(InstallRequestColors.PrimarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        if (computer.computerImageUrl.isNotBlank()) {
                            AsyncImage(
                                model = computer.computerImageUrl.trim(),
                                contentDescription = computer.pcName,
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
                                tint = InstallRequestColors.Primary,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = computer.pcName.ifBlank { "Unknown PC" },
                            color = InstallRequestColors.TextDark,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = computer.labRoom.ifBlank { "Lab Room" },
                            color = InstallRequestColors.TextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Student: ${studentName.ifBlank { "N/A" }}",
                            color = InstallRequestColors.TextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "ID: ${studentId.ifBlank { "N/A" }} • ${department.ifBlank { "N/A" }}",
                            color = InstallRequestColors.TextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = InstallRequestColors.CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Memory,
                            contentDescription = null,
                            tint = InstallRequestColors.Primary,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Software Details",
                            color = InstallRequestColors.TextDark,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black
                        )
                    }

                    OutlinedTextField(
                        value = softwareName,
                        onValueChange = {
                            softwareName = it
                            localError = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        label = { Text("Software Name *") },
                        placeholder = { Text("Example: VS Code, Arduino IDE, Code::Blocks") }
                    )

                    OutlinedTextField(
                        value = version,
                        onValueChange = { version = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        label = { Text("Version") },
                        placeholder = { Text("Example: 1.92, 2.3.2, 20.03") }
                    )

                    OutlinedTextField(
                        value = softwareLogoUrl,
                        onValueChange = { softwareLogoUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        label = { Text("Software Logo URL optional") },
                        placeholder = { Text("Paste logo image link if available") }
                    )

                    OutlinedTextField(
                        value = reason,
                        onValueChange = {
                            reason = it
                            localError = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(116.dp),
                        shape = RoundedCornerShape(16.dp),
                        label = { Text("Reason / Purpose *") },
                        placeholder = { Text("Why do you need this software?") },
                        maxLines = 4
                    )

                    if (localError.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = InstallRequestColors.RedText,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = localError,
                                color = InstallRequestColors.RedText,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            when {
                                softwareName.trim().isBlank() -> {
                                    localError = "Software name is required"
                                }

                                reason.trim().isBlank() -> {
                                    localError = "Reason is required"
                                }

                                else -> {
                                    onSubmitClick(
                                        softwareName.trim(),
                                        version.trim(),
                                        softwareLogoUrl.trim(),
                                        reason.trim()
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = InstallRequestColors.Primary,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Submit Install Request",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = InstallRequestColors.GreenSoft,
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Verified,
                        contentDescription = null,
                        tint = InstallRequestColors.GreenText,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Your request will be sent to admin for review. You can request software required for lab tasks, coding, simulation, or microcontroller work.",
                        color = InstallRequestColors.GreenText,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}