package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.*
import com.example.equipmentborrowingapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.draw.clip
@Composable
fun MyRequestsScreen(
    requestList: List<BorrowRequest>,
    onBackClick: () -> Unit
) {

    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Pending", "Approved", "Overdue", "Returned", "Rejected")

    val filteredList = requestList.filter {
        val status = getDisplayStatus(it)
        selectedFilter == "All" || status.equals(selectedFilter, true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        GradientHeaderCard(
            title = "My Requests",
            subtitle = "Borrow History",
            description = "Track your equipment requests and statuses."
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                CompactActionBox(
                    text = filter,
                    filled = selectedFilter == filter,
                    onClick = { selectedFilter = filter }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        SectionTitle("Request List")

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            EmptyStateText(
                text = "No $selectedFilter requests available",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList.sortedByDescending { it.requestTimestamp }) { request ->
                    ModernRequestCard(request)
                }
            }
        }

        SecondaryActionButton(
            text = "Back",
            onClick = onBackClick
        )
    }
}

@Composable
private fun ModernRequestCard(request: BorrowRequest) {

    val fallbackImageResId = getFallbackImageRes(request.equipmentImageName)
    val hasImageUrl = request.equipmentImageUrl.isNotBlank()

    val status = getDisplayStatus(request)

    val badgeColor = when (status) {
        "Approved" -> SuccessLight
        "Pending" -> WarningLight
        "Rejected" -> ErrorLight
        "Returned" -> InfoLight
        "Overdue" -> ErrorLight
        else -> InfoLight
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row {

                if (hasImageUrl) {
                    AsyncImage(
                        model = request.equipmentImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = fallbackImageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = formatEquipmentName(request.equipmentName),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(formatCategory(request.equipmentCategory))

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfessionalStatusBadge(
                        text = status,
                        type = status
                    )
                }
            }

            if (status == "Overdue") {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "⚠ Overdue item. Please return it as soon as possible.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabeledValue("Qty", request.quantity.toString())
                LabeledValue("Borrow", request.borrowDate)
                LabeledValue("Due", request.dueDate)
            }
        }
    }
}

private fun getDisplayStatus(request: BorrowRequest): String {
    return when {
        request.status.equals("Returned", true) -> "Returned"
        request.status.equals("Rejected", true) -> "Rejected"
        request.status.equals("Pending", true) -> "Pending"
        request.status.equals("Overdue", true) -> "Overdue"
        request.status.equals("Approved", true) && isPastDue(request.dueDate) -> "Overdue"
        request.status.equals("Approved", true) -> "Approved"
        else -> request.status.ifBlank { "Unknown" }
    }
}

private fun isPastDue(dueDate: String): Boolean {
    if (dueDate.isBlank()) return false

    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val due = format.parse(dueDate) ?: return false
        val today = format.parse(format.format(System.currentTimeMillis())) ?: return false
        due.before(today)
    } catch (_: Exception) {
        false
    }
}

private fun getFallbackImageRes(imageName: String): Int {
    val mappedRes = EquipmentImageMapper.getImageRes(imageName.trim())
    return if (mappedRes != 0) mappedRes else R.drawable.ic_launcher_foreground
}

private fun formatEquipmentName(name: String): String {
    return name.replace("_", " ").trim()
}

private fun formatCategory(category: String): String {
    return category.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}