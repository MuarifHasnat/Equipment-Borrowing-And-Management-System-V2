package com.example.equipmentborrowingapp.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.R

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String) -> Unit,
    onGoToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var role by remember { mutableStateOf("student") } // Default role

    // Shared Colors
    val screenBg = Color(0xFFF5F2F7)
    val whiteCard = Color(0xFFFDFDFD)
    val borderColor = Color(0xFFD7D7D7)
    val textGray = Color(0xFF8A8A8A)
    val darkText = Color(0xFF222222)
    val purple = Color(0xFF7A19FF)
    val purpleDark = Color(0xFF6200EA)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = screenBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFD7E8FF),
                                Color(0xFFE8D7FF),
                                Color(0xFFFFD7E7)
                            )
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.robot_login),
                    contentDescription = "Robot",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Join the Platform",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8E8E8E),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Create a new account to continue",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8E8E8E),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp)),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = darkText, fontWeight = FontWeight.SemiBold
                ),
                placeholder = {
                    Text("Full Name", color = textGray, fontWeight = FontWeight.SemiBold)
                },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = "Name", tint = Color(0xFF2C2C2C))
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard, unfocusedContainerColor = whiteCard,
                    focusedBorderColor = borderColor, unfocusedBorderColor = borderColor,
                    cursorColor = purple
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp)),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = darkText, fontWeight = FontWeight.SemiBold
                ),
                placeholder = {
                    Text("Email", color = textGray, fontWeight = FontWeight.SemiBold)
                },
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = "Email", tint = Color(0xFF2C2C2C))
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard, unfocusedContainerColor = whiteCard,
                    focusedBorderColor = borderColor, unfocusedBorderColor = borderColor,
                    cursorColor = purple
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp)),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = darkText, fontWeight = FontWeight.SemiBold
                ),
                placeholder = {
                    Text("Password", color = textGray, fontWeight = FontWeight.SemiBold)
                },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Password", tint = Color(0xFF2C2C2C))
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle Password",
                            tint = Color(0xFF1F1F1F)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard, unfocusedContainerColor = whiteCard,
                    focusedBorderColor = borderColor, unfocusedBorderColor = borderColor,
                    cursorColor = purple
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onRegisterClick(name.trim(), email.trim(), password, role)
                    }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Custom Role Selector UI
            Text(
                text = "Select Role",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textGray,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Student Card
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .shadow(elevation = if (role == "student") 4.dp else 0.dp, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (role == "student") purple else whiteCard)
                        .border(
                            width = 1.dp,
                            color = if (role == "student") purple else borderColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { role = "student" }
                ) {
                    Text(
                        text = "Student",
                        color = if (role == "student") Color.White else textGray,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Admin Card
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .shadow(elevation = if (role == "admin") 4.dp else 0.dp, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (role == "admin") purple else whiteCard)
                        .border(
                            width = 1.dp,
                            color = if (role == "admin") purple else borderColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { role = "admin" }
                ) {
                    Text(
                        text = "Admin",
                        color = if (role == "admin") Color.White else textGray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(purpleDark, purple)
                        )
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onRegisterClick(name.trim(), email.trim(), password, role)
                    }
            ) {
                Text(
                    text = "Register",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Already have an account? ",
                    color = Color(0xFF8A8A8A),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Login",
                    color = purple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onGoToLogin()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}