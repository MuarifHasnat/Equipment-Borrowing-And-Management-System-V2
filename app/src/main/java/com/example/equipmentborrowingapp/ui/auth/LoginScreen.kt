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

@OptIn(ExperimentalMaterial3Api::class)
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

    val savedEmailSet = remember { sharedPref.getStringSet("SAVED_EMAIL_SET", emptySet()) ?: emptySet() }
    val latestEmail = sharedPref.getString("LATEST_EMAIL", "") ?: ""
    val savedPassword = sharedPref.getString("SAVED_PASSWORD", "") ?: ""
    val isRemembered = sharedPref.getBoolean("REMEMBER_ME", false)

    var email by remember { mutableStateOf(latestEmail) }
    var password by remember { mutableStateOf(if (isRemembered) savedPassword else "") }
    var rememberMe by remember { mutableStateOf(isRemembered) }

    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf(email) }
    var resetEmailError by remember { mutableStateOf<String?>(null) }

    val bgGradient = Brush.verticalGradient(listOf(Color(0xFFF9FAFB), Color(0xFFEFF6FF)))
    val whiteCard = Color(0xFFFFFFFF)
    val primaryColor = Color(0xFF4F46E5)
    val primaryLight = Color(0xFF818CF8)
    val textPrimary = Color(0xFF111827)
    val textMuted = Color(0xFF4B5563)
    val borderStrokeColor = Color(0xFFE5E7EB)

    val performLogin = {
        val finalEmail = email.trim()
        when {
            finalEmail.isBlank() -> loginError = "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches() -> loginError = "Enter a valid email"
            password.isBlank() -> loginError = "Password cannot be empty"
            else -> {
                loginError = null
                val newEmailSet = savedEmailSet.toMutableSet().apply { add(finalEmail) }

                sharedPref.edit().apply {
                    putStringSet("SAVED_EMAIL_SET", newEmailSet)
                    putString("LATEST_EMAIL", finalEmail)
                    putBoolean("REMEMBER_ME", rememberMe)
                    if (rememberMe) {
                        putString("SAVED_PASSWORD", password)
                    } else {
                        remove("SAVED_PASSWORD")
                    }
                    apply()
                }
                onLoginClick(finalEmail, password)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF9FAFB)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient)
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Fixed Size, No Blurry Stretch Logo Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .shadow(16.dp, CircleShape, ambientColor = primaryColor.copy(alpha = 0.25f), spotColor = primaryColor.copy(alpha = 0.25f))
                    .clip(CircleShape)
                    .background(whiteCard)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.robot_login),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Equipment Borrowing and Lab Computer Software Tracking",
                color = textMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Welcome Back",
                color = textPrimary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            loginError?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(
                        text = error,
                        color = Color(0xFF991B1B),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded && savedEmailSet.isNotEmpty(),
                onExpandedChange = { dropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        loginError = null
                    },
                    placeholder = { Text("Email address", color = textMuted.copy(alpha = 0.7f)) },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = primaryColor) },
                    trailingIcon = {
                        if (savedEmailSet.isNotEmpty()) {
                            IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
                                Icon(
                                    imageVector = if (dropdownExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                    tint = primaryColor
                                )
                            }
                        }
                    },
                    isError = loginError?.contains("Email", ignoreCase = true) == true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = whiteCard,
                        unfocusedContainerColor = whiteCard,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = borderStrokeColor,
                        errorBorderColor = Color(0xFFEF4444)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded && savedEmailSet.isNotEmpty(),
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier.background(whiteCard)
                ) {
                    savedEmailSet.forEach { savedEmailItem ->
                        DropdownMenuItem(
                            text = { Text(text = savedEmailItem, color = textPrimary) },
                            onClick = {
                                email = savedEmailItem
                                dropdownExpanded = false
                                if (savedEmailItem == latestEmail && isRemembered) {
                                    password = savedPassword
                                } else {
                                    password = ""
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; loginError = null },
                placeholder = { Text("Password", color = textMuted.copy(alpha = 0.7f)) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = primaryColor) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null, tint = textMuted
                        )
                    }
                },
                isError = loginError?.contains("Password", ignoreCase = true) == true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = whiteCard,
                    unfocusedContainerColor = whiteCard,
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = borderStrokeColor,
                    errorBorderColor = Color(0xFFEF4444)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { performLogin() }),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { rememberMe = !rememberMe }
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = primaryColor)
                    )
                    Text(
                        text = "Remember Me",
                        color = textMuted,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                TextButton(onClick = {
                    resetEmail = email.trim()
                    resetEmailError = null
                    showForgotPasswordDialog = true
                }) {
                    Text("Forgot Password?", color = primaryColor, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = performLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp), ambientColor = primaryColor, spotColor = primaryColor),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(primaryColor, primaryLight)),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderStrokeColor)
                Text(
                    text = " OR CONTINUE WITH ",
                    color = textMuted.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 14.dp),
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderStrokeColor)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                onClick = onGoogleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(1.dp, borderStrokeColor, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = whiteCard,
                shadowElevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Google",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Google Account", fontWeight = FontWeight.SemiBold, color = textPrimary)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row {
                Text("New to the platform? ", color = textMuted)
                Text(
                    text = "Create Account",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }

    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false; resetEmailError = null },
            icon = { Icon(Icons.Filled.LockReset, contentDescription = null, tint = primaryColor, modifier = Modifier.size(32.dp)) },
            title = { Text(text = "Reset Password", fontWeight = FontWeight.Bold, color = textPrimary) },
            text = {
                Column {
                    Text(
                        text = "Enter your registered email address to receive a secure recovery code link.",
                        color = textMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it; resetEmailError = null },
                        placeholder = { Text("Email address") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = primaryColor) },
                        isError = resetEmailError != null,
                        supportingText = { resetEmailError?.let { Text(text = it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                            focusedBorderColor = primaryColor, unfocusedBorderColor = borderStrokeColor,
                            focusedContainerColor = whiteCard, unfocusedContainerColor = whiteCard
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val finalEmail = resetEmail.trim()
                        when {
                            finalEmail.isBlank() -> resetEmailError = "Email required"
                            !Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches() -> resetEmailError = "Invalid email"
                            else -> {
                                onForgotPasswordClick(finalEmail)
                                showForgotPasswordDialog = false
                                resetEmailError = null
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Send Link", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false; resetEmailError = null }) {
                    Text("Cancel", color = textMuted)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = whiteCard
        )
    }
}