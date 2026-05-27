package com.example.equipmentborrowingapp.ui.auth

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.Institution

@Composable
fun RegisterScreen(
    institutionList: List<Institution>,
    isGoogleMode: Boolean = false,
    prefilledName: String = "",
    prefilledEmail: String = "",
    onRegisterClick: (
        name: String,
        email: String,
        password: String,
        institutionId: String,
        studentId: String,
        department: String,
        semester: String,
        phone: String
    ) -> Unit,
    onGoToLogin: () -> Unit
) {
    var name by remember(prefilledName) { mutableStateOf(prefilledName) }
    var email by remember(prefilledEmail) { mutableStateOf(prefilledEmail) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var studentId by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var registerError by remember { mutableStateOf<String?>(null) }
    var selectedInstitution by remember { mutableStateOf<Institution?>(null) }
    var institutionMenuExpanded by remember { mutableStateOf(false) }

    val bgGradient = Brush.verticalGradient(listOf(Color(0xFFF9FAFB), Color(0xFFEFF6FF)))
    val whiteCard = Color(0xFFFFFFFF)
    val primaryColor = Color(0xFF4F46E5)
    val primarySoft = Color(0xFFEEF2FF)
    val textPrimary = Color(0xFF111827)
    val textMuted = Color(0xFF4B5563)
    val borderStrokeColor = Color(0xFFE5E7EB)

    val registerAction = {
        val finalName = name.trim()
        val finalEmail = email.trim()
        val finalStudentId = studentId.trim()
        val finalDepartment = department.trim()
        val finalSemester = semester.trim()
        val finalPhone = phone.trim()

        when {
            finalName.isBlank() -> registerError = "Full Name cannot be empty"
            finalEmail.isBlank() -> registerError = "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches() -> registerError = "Please enter a valid email"
            !isGoogleMode && password.isBlank() -> registerError = "Password cannot be empty"
            !isGoogleMode && password.length < 6 -> registerError = "Password must be at least 6 characters"
            selectedInstitution == null -> registerError = "Please select your institution"
            finalStudentId.isBlank() -> registerError = "Student ID cannot be empty"
            finalDepartment.isBlank() -> registerError = "Department cannot be empty"
            finalSemester.isBlank() -> registerError = "Semester cannot be empty"
            finalPhone.isBlank() -> registerError = "Phone number cannot be empty"
            else -> {
                registerError = null
                onRegisterClick(
                    finalName,
                    finalEmail,
                    if (isGoogleMode) "" else password,
                    selectedInstitution?.id ?: "",
                    finalStudentId,
                    finalDepartment,
                    finalSemester,
                    finalPhone
                )
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF9FAFB)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompactAuthHeader(
                title = if (isGoogleMode) "Complete Profile" else "Create Account",
                subtitle = if (isGoogleMode)
                    "Add your student information to finish Google registration"
                else
                    "Register to seamlessly track lab components & software",
                primary = primaryColor,
                primarySoft = primarySoft,
                darkText = textPrimary,
                mutedText = textMuted
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = whiteCard),
                border = BorderStroke(1.dp, borderStrokeColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isGoogleMode) "Student Details" else "Credentials",
                        color = textPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    registerError?.let { error ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                        ) {
                            Text(
                                text = error,
                                color = Color(0xFF991B1B),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    AuthInputField(
                        value = name,
                        onValueChange = { name = it; registerError = null },
                        placeholder = "Full Name",
                        icon = Icons.Filled.Person,
                        contentDescription = "Name",
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        isError = registerError?.contains("Name", ignoreCase = true) == true,
                        cardColor = whiteCard,
                        borderColor = borderStrokeColor,
                        primary = primaryColor,
                        mutedText = textMuted,
                        textPrimary = textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthInputField(
                        value = email,
                        onValueChange = { email = it; registerError = null },
                        placeholder = "Email Address",
                        icon = Icons.Filled.Email,
                        contentDescription = "Email",
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        isError = registerError?.contains("Email", ignoreCase = true) == true,
                        cardColor = whiteCard,
                        borderColor = borderStrokeColor,
                        primary = primaryColor,
                        mutedText = textMuted,
                        textPrimary = textPrimary,
                        enabled = !isGoogleMode
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!isGoogleMode) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; registerError = null },
                            placeholder = { Text("Password", color = textMuted.copy(alpha = 0.7f)) },
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = primaryColor) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = null,
                                        tint = textMuted
                                    )
                                }
                            },
                            isError = registerError?.contains("Password", ignoreCase = true) == true,
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    InstitutionSelector(
                        institutionList = institutionList,
                        selectedInstitution = selectedInstitution,
                        expanded = institutionMenuExpanded,
                        onExpandedChange = { institutionMenuExpanded = it },
                        onInstitutionSelected = {
                            selectedInstitution = it
                            institutionMenuExpanded = false
                            registerError = null
                        },
                        whiteCard = whiteCard,
                        primaryColor = primaryColor,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        borderStrokeColor = borderStrokeColor,
                        isError = registerError?.contains("institution", ignoreCase = true) == true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthInputField(
                        value = studentId,
                        onValueChange = { studentId = it; registerError = null },
                        placeholder = "Student ID",
                        icon = Icons.Filled.Badge,
                        contentDescription = "Student ID",
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        isError = registerError?.contains("Student ID", ignoreCase = true) == true,
                        cardColor = whiteCard,
                        borderColor = borderStrokeColor,
                        primary = primaryColor,
                        mutedText = textMuted,
                        textPrimary = textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthInputField(
                        value = department,
                        onValueChange = { department = it; registerError = null },
                        placeholder = "Department",
                        icon = Icons.Filled.School,
                        contentDescription = "Department",
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        isError = registerError?.contains("Department", ignoreCase = true) == true,
                        cardColor = whiteCard,
                        borderColor = borderStrokeColor,
                        primary = primaryColor,
                        mutedText = textMuted,
                        textPrimary = textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // FIXED: Removed Row and placed Semester vertically
                    AuthInputField(
                        value = semester,
                        onValueChange = { semester = it; registerError = null },
                        placeholder = "Semester",
                        icon = Icons.Filled.School,
                        contentDescription = "Semester",
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        isError = registerError?.contains("Semester", ignoreCase = true) == true,
                        cardColor = whiteCard,
                        borderColor = borderStrokeColor,
                        primary = primaryColor,
                        mutedText = textMuted,
                        textPrimary = textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // FIXED: Placed Phone vertically
                    AuthInputField(
                        value = phone,
                        onValueChange = { phone = it; registerError = null },
                        placeholder = "Phone Number",
                        icon = Icons.Filled.Phone,
                        contentDescription = "Phone",
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                        isError = registerError?.contains("Phone", ignoreCase = true) == true,
                        cardColor = whiteCard,
                        borderColor = borderStrokeColor,
                        primary = primaryColor,
                        mutedText = textMuted,
                        textPrimary = textPrimary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = registerAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text(
                            text = if (isGoogleMode) "Complete Registration" else "Create Account",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = if (isGoogleMode) "Wrong account? " else "Already registered? ", color = textMuted)
                Text(
                    text = "Sign In",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun InstitutionSelector(
    institutionList: List<Institution>,
    selectedInstitution: Institution?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onInstitutionSelected: (Institution) -> Unit,
    whiteCard: Color,
    primaryColor: Color,
    textPrimary: Color,
    textMuted: Color,
    borderStrokeColor: Color,
    isError: Boolean
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            onClick = { onExpandedChange(true) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = whiteCard,
            border = BorderStroke(1.dp, if (isError) Color(0xFFEF4444) else borderStrokeColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Business, contentDescription = null, tint = primaryColor, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = selectedInstitution?.let {
                        if (it.shortName.isNotBlank()) "${it.name} (${it.shortName})" else it.name
                    } ?: "Choose your Institution",
                    color = if (selectedInstitution == null) textMuted.copy(alpha = 0.7f) else textPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = textMuted)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(whiteCard)
        ) {
            if (institutionList.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No approved institutions found", color = textMuted) },
                    onClick = { onExpandedChange(false) }
                )
            } else {
                institutionList.forEach { institution ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (institution.shortName.isNotBlank()) "${institution.name} (${institution.shortName})" else institution.name,
                                color = textPrimary
                            )
                        },
                        onClick = { onInstitutionSelected(institution) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactAuthHeader(
    title: String,
    subtitle: String,
    primary: Color,
    primarySoft: Color,
    darkText: Color,
    mutedText: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(110.dp)
            .shadow(16.dp, CircleShape, ambientColor = primary.copy(alpha = 0.25f), spotColor = primary.copy(alpha = 0.25f))
            .clip(CircleShape)
            .background(Color.White)
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
        color = mutedText,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = title,
        color = darkText,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = subtitle,
        color = mutedText,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AuthInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    contentDescription: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isError: Boolean,
    cardColor: Color,
    borderColor: Color,
    primary: Color,
    mutedText: Color,
    textPrimary: Color,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = { Text(placeholder, color = mutedText.copy(alpha = 0.7f), maxLines = 1) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = contentDescription, tint = primary) },
        isError = isError,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = cardColor,
            unfocusedContainerColor = cardColor,
            disabledContainerColor = Color(0xFFF8FAFC),
            focusedTextColor = textPrimary,
            unfocusedTextColor = textPrimary,
            disabledTextColor = textPrimary,
            focusedBorderColor = primary,
            unfocusedBorderColor = borderColor,
            disabledBorderColor = borderColor,
            errorBorderColor = Color(0xFFEF4444)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        singleLine = true
    )
}