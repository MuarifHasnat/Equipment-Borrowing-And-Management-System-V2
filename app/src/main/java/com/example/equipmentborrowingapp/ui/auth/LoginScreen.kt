package com.example.equipmentborrowingapp.ui.auth

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.R

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onGoToRegister: () -> Unit,
    onForgotPasswordClick: (String) -> Unit = {},
    onGoogleClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val sharedPref = remember {
        context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
    }

    // States
    var email by remember { mutableStateOf(sharedPref.getString("SAVED_EMAIL", "") ?: "") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }

    // Forgot Password States
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf(email) }
    var resetEmailError by remember { mutableStateOf<String?>(null) }

    // Colors (Consider moving these to your ui/theme/Color.kt eventually)
    val screenBg = Color(0xFFF7F4FF)
    val whiteCard = Color(0xFFFFFFFF)
    val borderColor = Color(0xFFE2DDF0)
    val textGray = Color(0xFF7B728A)
    val darkText = Color(0xFF1F1B2D)
    val purpleDark = Color(0xFF4F1DFF)
    val purple = Color(0xFF7A19FF)
    val purpleLight = Color(0xFF9B5CFF)

    // Validation & Login Logic
    val performLogin = {
        val finalEmail = email.trim()
        when {
            finalEmail.isBlank() -> loginError = "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches() -> loginError = "Please enter a valid email"
            password.isBlank() -> loginError = "Password cannot be empty"
            else -> {
                loginError = null
                sharedPref.edit().putString("SAVED_EMAIL", finalEmail).apply()
                onLoginClick(finalEmail, password)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = screenBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFF6F1FF), Color(0xFFFFFFFF), Color(0xFFF3EEFF))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo Box
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFE9E2FF), Color(0xFFF3EEFF), Color.White)
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.robot_login),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(110.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "EBSM System",
                color = textGray,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Welcome Back",
                color = darkText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Text(text = "Sign in to continue", color = textGray)

            Spacer(modifier = Modifier.height(24.dp))

            // Main Login Error Message
            if (loginError != null) {
                Text(
                    text = loginError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
            }

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    loginError = null // Clear error when typing
                },
                placeholder = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = "Email Icon", tint = textGray)
                },
                isError = loginError?.contains("Email", ignoreCase = true) == true,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard,
                    unfocusedContainerColor = whiteCard,
                    focusedBorderColor = purple,
                    unfocusedBorderColor = borderColor,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    loginError = null // Clear error when typing
                },
                placeholder = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Password Icon", tint = textGray)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            tint = textGray
                        )
                    }
                },
                isError = loginError?.contains("Password", ignoreCase = true) == true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard,
                    unfocusedContainerColor = whiteCard,
                    focusedBorderColor = purple,
                    unfocusedBorderColor = borderColor,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { performLogin() }
                ),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    resetEmail = email.trim()
                    resetEmailError = null
                    showForgotPasswordDialog = true
                }) {
                    Text("Forgot Password?", color = purple)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button (Semantically correct structure for gradient buttons)
            Button(
                onClick = performLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(purpleDark, purple, purpleLight)),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
                Text(" OR ", color = textGray, modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Google Button
            Surface(
                onClick = onGoogleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = whiteCard,
                border = BorderStroke(1.dp, borderColor)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Continue with Google", fontWeight = FontWeight.Bold, color = darkText)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Text
            Row {
                Text("Don't have an account? ", color = textGray)
                Text(
                    text = "Sign Up",
                    color = purple,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                showForgotPasswordDialog = false
                resetEmailError = null
            },
            icon = {
                Icon(Icons.Filled.LockReset, contentDescription = "Reset Password Icon", tint = purple)
            },
            title = { Text(text = "Reset Password", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Enter your registered email address. We will send you a password reset link.",
                        color = textGray,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = {
                            resetEmail = it
                            resetEmailError = null
                        },
                        placeholder = { Text("Email address") },
                        leadingIcon = {
                            Icon(Icons.Filled.Email, contentDescription = "Email Icon")
                        },
                        isError = resetEmailError != null,
                        supportingText = {
                            resetEmailError?.let { Text(text = it) }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = whiteCard,
                            unfocusedContainerColor = whiteCard,
                            focusedBorderColor = purple,
                            unfocusedBorderColor = borderColor,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val finalEmail = resetEmail.trim()
                        when {
                            finalEmail.isBlank() -> resetEmailError = "Please enter your email address"
                            !Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches() -> resetEmailError = "Please enter a valid email address"
                            else -> {
                                onForgotPasswordClick(finalEmail)
                                showForgotPasswordDialog = false
                                resetEmailError = null
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = purple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Send Reset Link")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showForgotPasswordDialog = false
                    resetEmailError = null
                }) {
                    Text("Cancel", color = textGray)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = whiteCard
        )
    }
}