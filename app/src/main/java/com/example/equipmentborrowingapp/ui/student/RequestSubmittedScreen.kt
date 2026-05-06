package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper

// Modern Colors
private object SuccessColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

    val GreenBg = Color(0xFFD1FAE5)
    val GreenIcon = Color(0xFF10B981)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)
}

@Composable
fun RequestSubmittedScreen(
    equipment: Equipment,
    quantity: Int,
    borrowDate: String,
    dueDate: String,
    purpose: String,
    onViewRequestClick: () -> Unit,
    onBackHomeClick: () -> Unit
) {
    val fallbackImage = EquipmentImageMapper.getImageRes(equipment.imageName)
        .takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SuccessColors.ModernBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 Premium Success Icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(SuccessColors.GreenBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Success",
                    tint = SuccessColors.GreenIcon,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Request Submitted!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = SuccessColors.TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your borrow request has been successfully sent. You will be notified once the admin approves it.",
                style = MaterialTheme.typography.bodyMedium,
                color = SuccessColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 🔥 Equipment Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessColors.CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SuccessColors.ModernBg)
                    ) {
                        if (equipment.imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = equipment.imageUrl,
                                contentDescription = equipment.name,
                                placeholder = painterResource(id = fallbackImage),
                                error = painterResource(id = fallbackImage),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = fallbackImage),
                                contentDescription = equipment.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = equipment.name.ifBlank { "Equipment" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Requested Qty: $quantity",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = SuccessColors.PrimaryIndigo
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 Request Details Group
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessColors.CardWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ModernInfoRow(icon = Icons.Rounded.CalendarToday, label = "Borrow Date", value = borrowDate)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp), color = SuccessColors.ModernBg)
                    ModernInfoRow(icon = Icons.Rounded.Schedule, label = "Return Date", value = dueDate)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp), color = SuccessColors.ModernBg)
                    ModernInfoRow(icon = Icons.Rounded.EditNote, label = "Purpose", value = purpose.ifBlank { "Lab Project" })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Important Guidelines",
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SuccessColors.TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 Guidelines Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessColors.CardWhite)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModernGuidelineRow(icon = Icons.Rounded.Security, title = "Handle with Care", subtitle = "Keep the equipment safe from damage")
                    ModernGuidelineRow(icon = Icons.Rounded.Schedule, title = "Return on Time", subtitle = "Must be returned before the due date")
                    ModernGuidelineRow(icon = Icons.Rounded.CheckCircle, title = "Use Responsibly", subtitle = "Use only for learning & lab projects")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔥 Action Buttons
            Button(
                onClick = onViewRequestClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = SuccessColors.PrimaryIndigo.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(listOf(SuccessColors.PrimaryIndigo, SuccessColors.PurpleAccent)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Rounded.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View My Requests", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBackHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SuccessColors.TextDark),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SuccessColors.TextMuted.copy(alpha = 0.2f))
            ) {
                Icon(imageVector = Icons.Rounded.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to Home", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ModernInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(SuccessColors.BlueLight, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SuccessColors.BlueText,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = SuccessColors.TextMuted,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = SuccessColors.TextDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ModernGuidelineRow(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SuccessColors.ModernBg)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(SuccessColors.CardWhite, CircleShape)
                .shadow(1.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SuccessColors.PrimaryIndigo,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                color = SuccessColors.TextDark,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = subtitle,
                color = SuccessColors.TextMuted,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}