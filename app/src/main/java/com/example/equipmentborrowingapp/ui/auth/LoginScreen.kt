package com.example.equipmentborrowingapp.ui.auth

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.R

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onGoToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {}
) {
    // 1. Get Context and SharedPreferences to save/load the email locally
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE) }

    // 2. Auto-load the saved email when the screen opens
    var email by remember { mutableStateOf(sharedPref.getString("SAVED_EMAIL", "") ?: "") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Custom Color Palette (Kept exactly as you designed)
    val screenBg = Color(0xFFF5F2F7)
    val whiteCard = Color(0xFFFDFDFD)
    val borderColor = Color(0xFFD7D7D7)
    val textGray = Color(0xFF8A8A8A)
    val darkText = Color(0xFF222222)
    val purple = Color(0xFF7A19FF)
    val purpleDark = Color(0xFF6200EA)

    // A helper function to save email and trigger login
    val performLogin = {
        val finalEmail = email.trim()
        // Save the email to device memory so it stays there next time
        sharedPref.edit().putString("SAVED_EMAIL", finalEmail).apply()
        onLoginClick(finalEmail, password)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = screenBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp), // Rule: Global screen padding 16dp
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- Robot Image ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFD7E8FF), Color(0xFFE8D7FF), Color(0xFFFFD7E7))
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.robot_login),
                    contentDescription = "Robot",
                    modifier = Modifier.size(110.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Headers ---
            Text(
                text = "Equipment Borrowing System",
                style = MaterialTheme.typography.titleMedium,
                color = textGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp)) // Rule: title to subtitle gap 8.dp

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = darkText,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = textGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp)) // Rule: subtitle to content gap 24.dp

            // --- Form Fields ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = textGray) },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = darkText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard, unfocusedContainerColor = whiteCard,
                    focusedBorderColor = borderColor, unfocusedBorderColor = borderColor,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp)) // Rule: field-to-field gap 12.dp

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = textGray) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = darkText) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle Password", tint = darkText
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard, unfocusedContainerColor = whiteCard,
                    focusedBorderColor = borderColor, unfocusedBorderColor = borderColor,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { performLogin() }),
                singleLine = true
            )

            // Forgot Password
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onForgotPasswordClick) {
                    Text(text = "Forgot Password?", color = purple, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Main Login Button ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(purpleDark, purple)), RoundedCornerShape(16.dp))
                    .clickable { performLogin() }
            ) {
                Text("Login", color = Color.White, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- OR Divider ---
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
                Text("OR", modifier = Modifier.padding(horizontal = 12.dp), color = textGray, fontWeight = FontWeight.Bold)
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Google Button ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(whiteCard, RoundedCornerShape(16.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .clickable { onGoogleClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = "Google", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Continue with Google", color = darkText, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Footer ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = textGray, style = MaterialTheme.typography.bodyLarge)
                Text("Sign Up", color = purple, fontWeight = FontWeight.ExtraBold, modifier = Modifier.clickable { onGoToRegister() })
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}