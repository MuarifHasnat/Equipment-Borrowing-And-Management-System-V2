package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LockReset
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.data.model.AppUser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
private object StudentProfileColors {
    val Bg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val Primary = Color(0xFF4F46E5)
    val Purple = Color(0xFF7C3AED)
    val GreenBg = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)
    val OrangeBg = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)
    val RedBg = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)
    val GrayBg = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

@Composable
fun StudentProfileScreen(
    user: AppUser?,
    onSaveClick: (
        phone: String,
        profileImageUrl: String
    ) -> Unit,
    onPasswordResetClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }

    LaunchedEffect(user?.uid) {
        phone = user?.phone.orEmpty()
        profileImageUrl = user?.profileImageUrl.orEmpty()
        isEditMode = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StudentProfileColors.Bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(StudentProfileColors.CardWhite, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = StudentProfileColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Student Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudentProfileColors.TextDark
                    )
                    Text(
                        text = "Manage your account details",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StudentProfileColors.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            ProfileHeroCard(user = user)

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = StudentProfileColors.CardWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Profile Information",
                                color = StudentProfileColors.TextDark,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isEditMode) "Update your details and save" else "Tap Edit to update your details",
                                color = StudentProfileColors.TextMuted,
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

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        leadingIcon = {
                            Icon(Icons.Rounded.Phone, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        enabled = isEditMode
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = profileImageUrl,
                        onValueChange = { profileImageUrl = it },
                        label = { Text("Profile Image URL") },
                        leadingIcon = {
                            Icon(Icons.Rounded.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        enabled = isEditMode
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = user?.studentId.orEmpty(),
                        onValueChange = { },
                        label = { Text("Student ID") },
                        leadingIcon = {
                            Icon(Icons.Rounded.Badge, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = user?.department.orEmpty(),
                        onValueChange = { },
                        label = { Text("Department") },
                        leadingIcon = {
                            Icon(Icons.Rounded.School, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = user?.semester.orEmpty(),
                        onValueChange = { },
                        label = { Text("Semester") },
                        leadingIcon = {
                            Icon(Icons.Rounded.School, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    if (isEditMode) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    phone = user?.phone.orEmpty()
                                    profileImageUrl = user?.profileImageUrl.orEmpty()
                                    isEditMode = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    onSaveClick(
                                        phone.trim(),
                                        profileImageUrl.trim()
                                    )
                                    isEditMode = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StudentProfileColors.Primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Save,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Save", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            val email = user?.email.orEmpty()
                            if (email.isNotBlank()) {
                                onPasswordResetClick(email)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LockReset,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send Password Reset Email", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeroCard(user: AppUser?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = StudentProfileColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color(0xFFEFF6FF)),
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
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user?.name?.ifBlank { "Student" } ?: "Student",
                        color = StudentProfileColors.TextDark,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = user?.email?.ifBlank { "No email found" } ?: "No email found",
                        color = StudentProfileColors.TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoRow(
                icon = Icons.Rounded.Email,
                label = "Email",
                value = user?.email.orEmpty().ifBlank { "N/A" }
            )

            ProfileInfoRow(
                icon = Icons.Rounded.VerifiedUser,
                label = "Verification Status",
                value = user?.verificationStatus.orEmpty()
                    .ifBlank { "pending" }
                    .replaceFirstChar { it.uppercase() }
            )

            Spacer(modifier = Modifier.height(10.dp))

            VerificationBadge(status = user?.verificationStatus.orEmpty().ifBlank { "pending" })
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = StudentProfileColors.Purple,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = label,
                color = StudentProfileColors.TextMuted,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                color = StudentProfileColors.TextDark,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun VerificationBadge(status: String) {
    val cleanStatus = status.trim().lowercase()

    val bgColor = when (cleanStatus) {
        "verified" -> StudentProfileColors.GreenBg
        "rejected", "suspended" -> StudentProfileColors.RedBg
        "pending" -> StudentProfileColors.OrangeBg
        else -> StudentProfileColors.GrayBg
    }

    val textColor = when (cleanStatus) {
        "verified" -> StudentProfileColors.GreenText
        "rejected", "suspended" -> StudentProfileColors.RedText
        "pending" -> StudentProfileColors.OrangeText
        else -> StudentProfileColors.GrayText
    }

    Text(
        text = cleanStatus.replaceFirstChar { it.uppercase() },
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        color = textColor,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold
    )
}