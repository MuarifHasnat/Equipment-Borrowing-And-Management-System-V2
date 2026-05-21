package com.example.equipmentborrowingapp.ui.admin

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

private object ApprovedColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)

    val PrimaryIndigo = Color(0xFF4F46E5)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
}

@Composable
private fun RequestCardImage(
    imageName: String,
    imageUrl: String,
    contentDescription: String
) {
    val fallback = EquipmentImageMapper.getImageRes(imageName)

    if (imageUrl.isNotBlank()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(fallback),
            error = painterResource(fallback),
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    } else {
        Image(
            painter = painterResource(fallback),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Composable
fun ApprovedRequestsScreen(
    requestList: List<BorrowRequest>,
    onIssuedClick: (BorrowRequest) -> Unit,
    onReturnedClick: (BorrowRequest) -> Unit,
    onLostClick: (BorrowRequest) -> Unit,
    onDamagedClick: (BorrowRequest) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedRequest by remember { mutableStateOf<BorrowRequest?>(null) }
    var selectedAction by remember { mutableStateOf("") }

    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
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
                        request.status.lowercase().contains(query) ||
                        request.borrowDate.lowercase().contains(query) ||
                        request.dueDate.lowercase().contains(query)

            val matchesStatus =
                selectedStatus == "All" ||
                        request.status.equals(selectedStatus, ignoreCase = true)

            val matchesDepartment =
                selectedDepartment == "All" ||
                        request.department.equals(selectedDepartment, ignoreCase = true)

            val matchesCategory =
                selectedCategory == "All" ||
                        request.equipmentCategory.equals(selectedCategory, ignoreCase = true)

            matchesSearch && matchesStatus && matchesDepartment && matchesCategory
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

                "Status" -> list.sortedWith(
                    compareBy<BorrowRequest> { activeStatusOrder(it.status) }
                        .thenByDescending { it.requestTimestamp }
                )

                else -> list.sortedByDescending { it.requestTimestamp }
            }
        }

    val approvedCount = requestList.count {
        it.status.equals("Approved", ignoreCase = true)
    }

    val issuedCount = requestList.count {
        it.status.equals("Issued", ignoreCase = true)
    }

    val overdueCount = requestList.count {
        it.status.equals("Overdue", ignoreCase = true)
    }

    val totalQuantity = requestList.sumOf { it.quantity }

    selectedRequest?.let { request ->
        ConfirmLifecycleActionDialog(
            request = request,
            action = selectedAction,
            onDismiss = {
                selectedRequest = null
                selectedAction = ""
            },
            onConfirm = {
                isLoading = true

                when (selectedAction) {
                    "issue" -> onIssuedClick(request)
                    "return" -> onReturnedClick(request)
                    "lost" -> onLostClick(request)
                    "damaged" -> onDamagedClick(request)
                }

                val snackbarText = when (selectedAction) {
                    "issue" -> "Request marked as issued"
                    "return" -> "Item marked as returned"
                    "lost" -> "Request marked as lost"
                    "damaged" -> "Request marked as damaged"
                    else -> "Request updated"
                }

                selectedRequest = null
                selectedAction = ""

                scope.launch {
                    snackbarHostState.showSnackbar(snackbarText)
                    isLoading = false
                }
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = ApprovedColors.ModernBg
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ApprovedColors.PrimaryIndigo)
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
                                color = ApprovedColors.CardWhite,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(12.dp),
                                spotColor = Color.Black.copy(alpha = 0.05f)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = ApprovedColors.TextDark
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Active Borrow Requests",
                            style = MaterialTheme.typography.titleLarge,
                            color = ApprovedColors.TextDark,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Search, filter, sort and manage lifecycle",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ApprovedColors.TextMuted
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActiveMiniStatCard(
                        title = "Approved",
                        value = approvedCount.toString(),
                        bgColor = ApprovedColors.BlueLight,
                        textColor = ApprovedColors.BlueText,
                        modifier = Modifier.weight(1f)
                    )

                    ActiveMiniStatCard(
                        title = "Issued",
                        value = issuedCount.toString(),
                        bgColor = ApprovedColors.PurpleLight,
                        textColor = ApprovedColors.PurpleText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActiveMiniStatCard(
                        title = "Overdue",
                        value = overdueCount.toString(),
                        bgColor = ApprovedColors.RedLight,
                        textColor = ApprovedColors.RedText,
                        modifier = Modifier.weight(1f)
                    )

                    ActiveMiniStatCard(
                        title = "Total Qty",
                        value = totalQuantity.toString(),
                        bgColor = ApprovedColors.GreenLight,
                        textColor = ApprovedColors.GreenText,
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
                        Text("Search student, ID, department, equipment or status")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                ActiveFilterTitle("Status")

                ActiveHorizontalFilterRow {
                    listOf(
                        "All",
                        "Approved",
                        "Issued",
                        "Overdue"
                    ).forEach { status ->
                        ActiveFilterChip(
                            text = status,
                            selected = selectedStatus == status,
                            onClick = {
                                selectedStatus = status
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ActiveFilterTitle("Department")

                ActiveHorizontalFilterRow {
                    departmentList.forEach { department ->
                        ActiveFilterChip(
                            text = department,
                            selected = selectedDepartment == department,
                            onClick = {
                                selectedDepartment = department
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ActiveFilterTitle("Category")

                ActiveHorizontalFilterRow {
                    categoryList.forEach { category ->
                        ActiveFilterChip(
                            text = category,
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ActiveFilterTitle("Sort")

                ActiveHorizontalFilterRow {
                    listOf(
                        "Newest First",
                        "Oldest First",
                        "Due Date",
                        "Status",
                        "Student A-Z",
                        "Equipment A-Z",
                        "Quantity High-Low"
                    ).forEach { sort ->
                        ActiveFilterChip(
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
                    selectedStatus != "All" ||
                    selectedDepartment != "All" ||
                    selectedCategory != "All" ||
                    selectedSort != "Newest First"
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            searchText = ""
                            selectedStatus = "All"
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
                    text = "Showing ${filteredRequests.size} of ${requestList.size} active request(s)",
                    style = MaterialTheme.typography.titleMedium,
                    color = ApprovedColors.TextDark,
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
                                "No active borrow requests found."
                            } else {
                                "No active request matches your search/filter."
                            },
                            color = ApprovedColors.TextMuted,
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
                            BorrowLifecycleRequestCard(
                                request = request,
                                onIssueClick = {
                                    selectedRequest = request
                                    selectedAction = "issue"
                                },
                                onReturnClick = {
                                    selectedRequest = request
                                    selectedAction = "return"
                                },
                                onLostClick = {
                                    selectedRequest = request
                                    selectedAction = "lost"
                                },
                                onDamagedClick = {
                                    selectedRequest = request
                                    selectedAction = "damaged"
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
private fun ActiveMiniStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ApprovedColors.CardWhite),
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
                color = ApprovedColors.TextMuted,
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
private fun ActiveFilterTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = ApprovedColors.TextMuted,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ActiveHorizontalFilterRow(
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
private fun ActiveFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ApprovedColors.PrimaryIndigo,
                contentColor = Color.White
            )
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
private fun BorrowLifecycleRequestCard(
    request: BorrowRequest,
    onIssueClick: () -> Unit,
    onReturnClick: () -> Unit,
    onLostClick: () -> Unit,
    onDamagedClick: () -> Unit
) {
    val status = request.status.trim()

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = ApprovedColors.CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
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
                            color = ApprovedColors.TextDark,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        ProfessionalStatusBadge(
                            text = status.ifBlank { "Approved" },
                            type = status.ifBlank { "Approved" }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = ApprovedColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "${request.userName.ifBlank { "Unknown Student" }} • Qty: ${request.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ApprovedColors.TextMuted
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
                            color = ApprovedColors.TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = ApprovedColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Borrow: ${request.borrowDate.ifBlank { "N/A" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ApprovedColors.TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = ApprovedColors.TextMuted
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Due: ${request.dueDate.ifBlank { "N/A" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (status.equals("Overdue", ignoreCase = true)) {
                                ApprovedColors.RedText
                            } else {
                                ApprovedColors.TextMuted
                            },
                            fontWeight = if (status.equals("Overdue", ignoreCase = true)) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    }

                    if (request.approvedAt > 0L) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Approved and stock already reserved",
                            style = MaterialTheme.typography.labelMedium,
                            color = ApprovedColors.GreenText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (request.issuedAt > 0L) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Issued to student",
                            style = MaterialTheme.typography.labelMedium,
                            color = ApprovedColors.BlueText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (status.equals("Overdue", ignoreCase = true)) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "⚠ Overdue item. Return is required.",
                            color = ApprovedColors.RedText,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = ApprovedColors.ModernBg)
            Spacer(modifier = Modifier.height(10.dp))

            when {
                status.equals("Approved", ignoreCase = true) -> {
                    Button(
                        onClick = onIssueClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ApprovedColors.BlueLight,
                            contentColor = ApprovedColors.BlueText
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Mark as Issued",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                status.equals("Issued", ignoreCase = true) ||
                        status.equals("Overdue", ignoreCase = true) -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onReturnClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ApprovedColors.GreenLight,
                                contentColor = ApprovedColors.GreenText
                            )
                        ) {
                            Text(
                                text = "Mark as Returned",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDamagedClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = ApprovedColors.OrangeText
                                )
                            ) {
                                Text(
                                    text = "Damaged",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = onLostClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = ApprovedColors.RedText
                                )
                            ) {
                                Text(
                                    text = "Lost",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = "No action available for this request status.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ApprovedColors.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmLifecycleActionDialog(
    request: BorrowRequest,
    action: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val title = when (action) {
        "issue" -> "Confirm Issue"
        "return" -> "Confirm Return"
        "lost" -> "Confirm Lost"
        "damaged" -> "Confirm Damaged"
        else -> "Confirm Action"
    }

    val message = when (action) {
        "issue" -> "Are you sure you want to mark this approved request as issued?"
        "return" -> "Are you sure you want to mark this item as returned?"
        "lost" -> "Are you sure you want to mark this item as lost? Available quantity will not increase."
        "damaged" -> "Are you sure you want to mark this item as damaged? Available quantity will not increase."
        else -> "Are you sure you want to update this request?"
    }

    val buttonText = when (action) {
        "issue" -> "Issue"
        "return" -> "Return"
        "lost" -> "Mark Lost"
        "damaged" -> "Mark Damaged"
        else -> "Confirm"
    }

    val buttonColor = when (action) {
        "issue" -> ApprovedColors.BlueText
        "return" -> ApprovedColors.GreenText
        "lost" -> ApprovedColors.RedText
        "damaged" -> ApprovedColors.OrangeText
        else -> ApprovedColors.PrimaryIndigo
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = ApprovedColors.CardWhite,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = ApprovedColors.TextDark
            )
        },
        text = {
            Column {
                Text(
                    text = message,
                    color = ApprovedColors.TextMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = ApprovedColors.ModernBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Student: ${request.userName.ifBlank { "N/A" }}",
                            fontWeight = FontWeight.SemiBold,
                            color = ApprovedColors.TextDark
                        )

                        Text(
                            text = "Equipment: ${request.equipmentName.ifBlank { "N/A" }}",
                            color = ApprovedColors.TextDark
                        )

                        Text(
                            text = "Qty: ${request.quantity}",
                            color = ApprovedColors.TextDark
                        )

                        Text(
                            text = "Current Status: ${request.status.ifBlank { "N/A" }}",
                            color = ApprovedColors.TextDark
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                )
            ) {
                Text(buttonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = ApprovedColors.TextMuted
                )
            }
        }
    )
}

private fun activeStatusOrder(status: String): Int {
    return when (status.trim().lowercase()) {
        "approved" -> 0
        "issued" -> 1
        "overdue" -> 2
        else -> 3
    }
}