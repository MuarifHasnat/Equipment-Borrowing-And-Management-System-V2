package com.example.equipmentborrowingapp.ui.auth

import android.util.Patterns
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.R

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String) -> Unit,
    onGoToLogin: () -> Unit
) {
    // States
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var role by remember { mutableStateOf("student") }
    var registerError by remember { mutableStateOf<String?>(null) }

    // Colors
    val screenBg = Color(0xFFF7F4FF)
    val whiteCard = Color(0xFFFFFFFF)
    val borderColor = Color(0xFFE2DDF0)
    val textGray = Color(0xFF7B728A)
    val darkText = Color(0xFF1F1B2D)
    val purpleDark = Color(0xFF4F1DFF)
    val purple = Color(0xFF7A19FF)
    val purpleLight = Color(0xFF9B5CFF)

    // Validation Logic
    val registerAction = {
        val finalName = name.trim()
        val finalEmail = email.trim()

        when {
            finalName.isBlank() -> registerError = "Full Name cannot be empty"
            finalEmail.isBlank() -> registerError = "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches() -> registerError = "Please enter a valid email address"
            password.isBlank() -> registerError = "Password cannot be empty"
            password.length < 6 -> registerError = "Password must be at least 6 characters"
            else -> {
                registerError = null
                onRegisterClick(finalName, finalEmail, password, role)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = screenBg
    ) {
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
            Spacer(modifier = Modifier.height(22.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(145.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFE9E2FF), Color(0xFFF3EEFF), Color.White)
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.robot_login),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(105.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "EBSM System",
                color = textGray,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Create Account",
                color = darkText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Join the platform to continue",
                color = textGray,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Error Message Display
            if (registerError != null) {
                Text(
                    text = registerError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
            }

            AuthField(
                value = name,
                onValueChange = {
                    name = it
                    registerError = null // Clear error when typing
                },
                placeholder = "Full Name",
                icon = Icons.Filled.Person,
                contentDescription = "Full Name Icon",
                borderColor = borderColor,
                whiteCard = whiteCard,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                isError = registerError?.contains("Name", ignoreCase = true) == true
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthField(
                value = email,
                onValueChange = {
                    email = it
                    registerError = null // Clear error when typing
                },
                placeholder = "Email",
                icon = Icons.Filled.Email,
                contentDescription = "Email Icon",
                borderColor = borderColor,
                whiteCard = whiteCard,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                isError = registerError?.contains("Email", ignoreCase = true) == true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    registerError = null // Clear error when typing
                },
                placeholder = { Text("Password", color = textGray) },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Password Icon", tint = darkText)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            tint = darkText
                        )
                    }
                },
                isError = registerError?.contains("Password", ignoreCase = true) == true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard,
                    unfocusedContainerColor = whiteCard,
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor,
                    cursorColor = purple,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { registerAction() }),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Select Role",
                modifier = Modifier.fillMaxWidth(),
                color = textGray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoleOptionCard(
                    text = "Student",
                    selected = role == "student",
                    onClick = { role = "student" },
                    modifier = Modifier.weight(1f),
                    purple = purple,
                    purpleLight = purpleLight,
                    whiteCard = whiteCard,
                    borderColor = borderColor,
                    textGray = textGray
                )

                RoleOptionCard(
                    text = "Admin",
                    selected = role == "admin",
                    onClick = { role = "admin" },
                    modifier = Modifier.weight(1f),
                    purple = purple,
                    purpleLight = purpleLight,
                    whiteCard = whiteCard,
                    borderColor = borderColor,
                    textGray = textGray
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Semantically correct Register Button
            Button(
                onClick = registerAction,
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
                        text = "Register",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Already have an account? ",
                    color = textGray,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Login",
                    color = purple,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable { onGoToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    contentDescription: String,
    borderColor: Color,
    whiteCard: Color,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFF7B728A)) },
        leadingIcon = {
            Icon(icon, contentDescription = contentDescription, tint = Color(0xFF1F1B2D))
        },
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = whiteCard,
            unfocusedContainerColor = whiteCard,
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            errorBorderColor = MaterialTheme.colorScheme.error
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        singleLine = true
    )
}

@Composable
private fun RoleOptionCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    purple: Color,
    purpleLight: Color,
    whiteCard: Color,
    borderColor: Color,
    textGray: Color
) {
    // Replaced Box with Surface for proper semantics and automatic clipping/shadow
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        shadowElevation = if (selected) 5.dp else 2.dp,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (selected) {
                        Brush.horizontalGradient(listOf(purple, purpleLight))
                    } else {
                        Brush.horizontalGradient(listOf(whiteCard, whiteCard))
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (selected) purple else borderColor,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = text,
                color = if (selected) Color.White else textGray,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}