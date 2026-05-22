package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import com.example.equipmentborrowingapp.ui.common.ProfessionalStatusBadge
import kotlinx.coroutines.launch

private object PendingColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
}

@Composable
private fun RequestCardImage(
    imageName: String,
    imageUrl: String,
    contentDescription: String
) {
    val fallbackImageResId = EquipmentImageMapper.getImageRes(imageName)
    val safeImageUrl = EquipmentImageMapper.getSafeImageUrl(imageUrl)
    val hasImageUrl = EquipmentImageMapper.hasValidImageUrl(imageUrl)

    val imageModifier = Modifier
        .size(85.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(PendingColors.ModernBg)

    if (hasImageUrl) {
        AsyncImage(
            model = safeImageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = fallbackImageResId),
            error = painterResource(id = fallbackImageResId),
            fallback = painterResource(id = fallbackImageResId),
            modifier = imageModifier
        )
    } else {
        Image(
            painter = painterResource(id = fallbackImageResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = imageModifier.padding(8.dp)
        )
    }
}

@Composable
fun PendingRequestsScreen(
    requestList: List<BorrowRequest>,
    onApproveClick: (BorrowRequest) -> Unit,
    onRejectClick: (BorrowRequest) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedRequest by remember { mutableStateOf<BorrowRequest?>(null) }
    var dialogType by remember { mutableStateOf("") }

    var searchText by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf("All") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Newest First") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val departmentList = remember(requestList) {
        listOf("All") + requestList
            .map { it.department.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    val categoryList = remember(requestList) {
        listOf("All") + requestList
            .map { it.equipmentCategory.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    val filteredRequests = requestList
        .filter { request ->
            val query = searchText.trim().lowercase()

            val matchesSearch =
                query.isBlank() ||
                        request.userName.lowercase().contains(query) ||
                        request.userEmail.lowercase().contains(query) ||
                        request.studentId.lowercase().contains(query) ||
                        request.department.lowercase().contains(query) ||
                        request.equipmentName.lowercase().contains(query) ||
                        request.equipmentCategory.lowercase().contains(query) ||
                        request.borrowDate.lowercase().contains(query) ||
                        request.dueDate.lowercase().contains(query)

            val matchesDepartment =
                selectedDepartment == "All" ||
                        request.department.equals(selectedDepartment, ignoreCase = true)

            val matchesCategory =
                selectedCategory == "All" ||
                        request.equipmentCategory.equals(selectedCategory, ignoreCase = true)

            matchesSearch && matchesDepartment && matchesCategory
        }
        .let { list ->
            when (selectedSort) {
                "Oldest First" -> list.sortedBy { it.requestTimestamp }

                "Student A-Z" -> list.sortedBy { it.userName.lowercase() }

                "Equipment A-Z" -> list.sortedBy { it.equipmentName.lowercase() }

                "Quantity High-Low" -> list.sortedWith(
                    compareByDescending<BorrowRequest> { it.quantity }
                        .thenBy { it.equipmentName.lowercase() }
                )

                "Due Date" -> list.sortedBy { it.dueDate }

                else -> list.sortedByDescending { it.requestTimestamp }
            }
        }

    val totalQuantity = requestList.sumOf { it.quantity }
    val uniqueStudents = requestList
        .map { it.userId.ifBlank { it.userName } }
        .filter { it.isNotBlank() }
        .distinct()
        .size

    selectedRequest?.let { request ->
        val isApprove = dialogType == "approve"

        AlertDialog(
            onDismissRequest = {
                selectedRequest = null
                dialogType = ""
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = PendingColors.CardWhite,
            title = {
                Text(
                    text = if (isApprove) "Confirm Approval" else "Confirm Rejection",
                    fontWeight = FontWeight.Bold,
                    color = PendingColors.TextDark
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isApprove) {
                            "Are you sure you want to approve this request?"
                        } else {
                            "Are you sure you want to reject this request?"
                        },
                        color = PendingColors.TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = PendingColors.ModernBg,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Student: ${request.userName.ifBlank { "Unknown Student" }}",
                                fontWeight = FontWeight.SemiBold,
                                color = PendingColors.TextDark
                            )

                            Text(
                                text = "Equipment: ${request.equipmentName.ifBlank { "Unknown Equipment" }}",
                                color = PendingColors.TextDark
                            )

                            Text(
                                text = "Qty: ${request.quantity}",
                                color = PendingColors.TextDark
                            )

                            Text(
                                text = "Due: ${request.dueDate.ifBlank { "N/A" }}",
                                color = PendingColors.TextDark
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isLoading = true

                        if (isApprove) {
                            onApproveClick(request)
                        } else {
                            onRejectClick(request)
                        }

                        selectedRequest = null
                        dialogType = ""

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (isApprove) {
                                    "Request approved successfully"
                                } else {
                                    "Request rejected successfully"
                                }
                            )
                            isLoading = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isApprove) {
                            PendingColors.GreenText
                        } else {
                            PendingColors.RedText
                        }
                    )
                ) {
                    Text(if (isApprove) "Approve" else "Reject")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedRequest = null
                        dialogType = ""
                    }
                ) {
                    Text("Cancel", color = PendingColors.TextMuted)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = PendingColors.ModernBg
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PendingColors.PrimaryIndigo)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(
                                PendingColors.CardWhite,
                                RoundedCornerShape(12.dp)
                            )
                            .shadow(
                                2.dp,
                                RoundedCornerShape(12.dp),
                                spotColor = Color.Black.copy(alpha = 0.05f)
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = PendingColors.TextDark
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Pending Requests",
                            style = MaterialTheme.typography.titleLarge,
                            color = PendingColors.TextDark,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Search, filter, sort and review requests",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PendingColors.TextMuted
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PendingMiniStatCard(
                        title = "Requests",
                        value = requestList.size.toString(),
                        bgColor = PendingColors.OrangeLight,
                        textColor = PendingColors.OrangeText,
                        modifier = Modifier.weight(1f)
                    )

                    PendingMiniStatCard(
                        title = "Students",
                        value = uniqueStudents.toString(),
                        bgColor = PendingColors.BlueLight,
                        textColor = PendingColors.BlueText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PendingMiniStatCard(
                        title = "Total Qty",
                        value = totalQuantity.toString(),
                        bgColor = PendingColors.PurpleLight,
                        textColor = PendingColors.PurpleText,
                        modifier = Modifier.weight(1f)
                    )

                    PendingMiniStatCard(
                        title = "Showing",
                        value = filteredRequests.size.toString(),
                        bgColor = PendingColors.GreenLight,
                        textColor = PendingColors.GreenText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    label = {
                        Text("Search student, ID, department, equipment or date")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                PendingFilterTitle("Department")

                PendingHorizontalFilterRow {
                    departmentList.forEach { department ->
                        PendingFilterChip(
                            text = department,
                            selected = selectedDepartment == department,
                            onClick = {
                                selectedDepartment = department
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                PendingFilterTitle("Category")

                PendingHorizontalFilterRow {
                    categoryList.forEach { category ->
                        PendingFilterChip(
                            text = category,
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                PendingFilterTitle("Sort")

                PendingHorizontalFilterRow {
                    listOf(
                        "Newest First",
                        "Oldest First",
                        "Due Date",
                        "Student A-Z",
                        "Equipment A-Z",
                        "Quantity High-Low"
                    ).forEach { sort ->
                        PendingFilterChip(
                            text = sort,
                            selected = selectedSort == sort,
                            onClick = {
                                selectedSort = sort
                            }
                        )
                    }
                }

                if (
                    searchText.isNotBlank() ||
                    selectedDepartment != "All" ||
                    selectedCategory != "All" ||
                    selectedSort != "Newest First"
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            searchText = ""
                            selectedDepartment = "All"
                            selectedCategory = "All"
                            selectedSort = "Newest First"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Clear Search, Filters and Sort")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Showing ${filteredRequests.size} of ${requestList.size} pending request(s)",
                    style = MaterialTheme.typography.titleMedium,
                    color = PendingColors.TextDark,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (filteredRequests.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (requestList.isEmpty()) {
                                "No pending requests found."
                            } else {
                                "No pending request matches your search/filter."
                            },
                            color = PendingColors.TextMuted,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(filteredRequests) { request ->
                            PendingRequestCard(
                                request = request,
                                onApproveClick = {
                                    selectedRequest = request
                                    dialogType = "approve"
                                },
                                onRejectClick = {
                                    selectedRequest = request
                                    dialogType = "reject"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PendingMiniStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PendingColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = PendingColors.TextMuted,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                modifier = Modifier
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun PendingFilterTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = PendingColors.TextMuted,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun PendingHorizontalFilterRow(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun PendingFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PendingColors.PrimaryIndigo,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
private fun PendingRequestCard(
    request: BorrowRequest,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = PendingColors.CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                4.dp,
                RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                RequestCardImage(
                    imageName = request.equipmentImageName,
                    imageUrl = request.equipmentImageUrl,
                    contentDescription = request.equipmentName
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = request.equipmentName.ifBlank { "Unknown Equipment" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PendingColors.TextDark,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        ProfessionalStatusBadge(
                            text = request.status.ifBlank { "Pending" },
                            type = request.status.ifBlank { "Pending" }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = PendingColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "${request.userName.ifBlank { "Unknown Student" }} • Qty: ${request.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PendingColors.TextMuted
                        )
                    }

                    if (request.studentId.isNotBlank() || request.department.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = buildString {
                                if (request.studentId.isNotBlank()) {
                                    append("ID: ${request.studentId}")
                                }

                                if (request.department.isNotBlank()) {
                                    if (isNotBlank()) append(" • ")
                                    append(request.department)
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = PendingColors.TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = PendingColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Borrow: ${request.borrowDate.ifBlank { "N/A" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PendingColors.TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = PendingColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Due: ${request.dueDate.ifBlank { "N/A" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PendingColors.TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = PendingColors.ModernBg)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onApproveClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PendingColors.GreenLight,
                        contentColor = PendingColors.GreenText
                    )
                ) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Approve",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PendingColors.RedText
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = PendingColors.RedText.copy(alpha = 0.3f)
                    )
                ) {
                    Icon(
                        Icons.Rounded.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Reject",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}