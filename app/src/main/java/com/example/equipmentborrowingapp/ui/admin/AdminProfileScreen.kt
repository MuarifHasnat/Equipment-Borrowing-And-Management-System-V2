package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LockReset
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.VerifiedUser
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
private object AdminProfileColors {
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
    val PurpleText = Color(0xFF7C3AED)

    val GrayLight = Color(0xFFF1F5F9)
}

@Composable
fun AdminProfileScreen(
    user: AppUser?,
    onSaveClick: (phone: String, profileImageUrl: String) -> Unit,
    onPasswordResetClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var phone by remember(user) { mutableStateOf(user?.phone ?: "") }
    var profileImageUrl by remember(user) { mutableStateOf(user?.profileImageUrl ?: "") }
    var isEditMode by remember { mutableStateOf(false) }
    val email = user?.email.orEmpty()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AdminProfileColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            AdminProfileTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = AdminProfileColors.Card),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFFF5F3FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (user?.profileImageUrl.orEmpty().isNotBlank()) {
                            AsyncImage(
                                model = user?.profileImageUrl.orEmpty().trim(),
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.AdminPanelSettings,
                                contentDescription = null,
                                tint = Color(0xFF7C3AED),
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user?.name?.ifBlank { "Admin User" } ?: "Admin User",
                        color = AdminProfileColors.TextDark,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = user?.role?.replaceFirstChar { it.uppercase() } ?: "Admin",
                        color = AdminProfileColors.TextMuted,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AdminProfileColors.Card),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Account Information",
                        color = AdminProfileColors.TextDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AdminProfileInfoRow(
                        icon = Icons.Rounded.Email,
                        title = "Email",
                        value = email.ifBlank { "N/A" },
                        iconBg = AdminProfileColors.BlueLight,
                        iconColor = AdminProfileColors.BlueText
                    )

                    HorizontalDivider(color = AdminProfileColors.Bg)

                    AdminProfileInfoRow(
                        icon = Icons.Rounded.VerifiedUser,
                        title = "Verification",
                        value = user?.verificationStatus?.ifBlank { "Verified" } ?: "Verified",
                        iconBg = AdminProfileColors.GreenLight,
                        iconColor = AdminProfileColors.GreenText
                    )

                    HorizontalDivider(color = AdminProfileColors.Bg)

                    AdminProfileInfoRow(
                        icon = Icons.Rounded.School,
                        title = "Institution",
                        value = user?.institutionId?.ifBlank { "N/A" } ?: "N/A",
                        iconBg = AdminProfileColors.PurpleLight,
                        iconColor = AdminProfileColors.PurpleText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AdminProfileColors.Card),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Contact Information",
                                color = AdminProfileColors.TextDark,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = if (isEditMode) "Update phone number and profile image" else "Tap Edit to update contact details",
                                color = AdminProfileColors.TextMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (!isEditMode) {
                            OutlinedButton(
                                onClick = { isEditMode = true },
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Edit", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Phone,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        enabled = isEditMode
                    )
                    OutlinedTextField(
                        value = profileImageUrl,
                        onValueChange = { profileImageUrl = it },
                        label = { Text("Profile Image URL") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.AdminPanelSettings,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        enabled = isEditMode
                    )

                    if (isEditMode) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    phone = user?.phone ?: ""
                                    profileImageUrl = user?.profileImageUrl ?: ""
                                    isEditMode = false
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Text("Cancel", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    onSaveClick(phone.trim(), profileImageUrl.trim())
                                    isEditMode = false
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AdminProfileColors.Primary,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Save,
                                    contentDescription = null,
                                    modifier = Modifier.size(19.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text("Save", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = {
                    if (email.isNotBlank()) {
                        onPasswordResetClick(email)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                enabled = email.isNotBlank(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AdminProfileColors.OrangeText
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.LockReset,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Send Password Reset Email", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun AdminProfileTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .background(AdminProfileColors.Card, RoundedCornerShape(15.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = AdminProfileColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(46.dp)
                .background(AdminProfileColors.PurpleLight, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.AdminPanelSettings,
                contentDescription = null,
                tint = AdminProfileColors.Primary,
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Admin Profile",
                color = AdminProfileColors.TextDark,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "Manage account details",
                color = AdminProfileColors.TextMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AdminProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    iconBg: Color,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(iconBg, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = AdminProfileColors.TextMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                color = AdminProfileColors.TextDark,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
