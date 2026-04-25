package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.ui.theme.*

@Composable
fun StudentProfileScreen(
    userName: String,
    userEmail: String,
    role: String,
    onBackClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // 🔙 Back Button
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 🔥 Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(Primary, Secondary)),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Surface(
                        shape = CircleShape,
                        color = SurfaceWhite,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userName,
                        color = TextLight,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Student Account",
                        color = TextLight.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Info Card
            ProfileInfoCard(
                icon = Icons.Filled.Person,
                label = "Name",
                value = userName
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileInfoCard(
                icon = Icons.Filled.Email,
                label = "Email",
                value = userEmail
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileInfoCard(
                icon = Icons.Filled.Badge,
                label = "Role",
                value = role.uppercase()
            )
        }
    }
}

@Composable
private fun ProfileInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = PrimaryLight,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Primary)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(label, color = TextSecondary)
                Text(
                    value,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
    }
}