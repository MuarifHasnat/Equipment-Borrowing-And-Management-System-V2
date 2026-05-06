package com.example.equipmentborrowingapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.equipmentborrowingapp.R
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.ui.common.EquipmentImageMapper
import java.text.SimpleDateFormat
import java.util.*

// Modern Premium Colors
private object RequestsColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val GrayLight = Color(0xFFF1F5F9)
    val GrayText = Color(0xFF475569)
}

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

    Surface(modifier = Modifier.fillMaxSize(), color = RequestsColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 🔙 Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(RequestsColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = RequestsColors.TextDark
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "My Requests",
                        style = MaterialTheme.typography.titleLarge,
                        color = RequestsColors.TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Track your borrow history",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RequestsColors.TextMuted
                    )
                }
            }

            // 🔥 Modern Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        onClick = { selectedFilter = filter },
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) RequestsColors.PrimaryIndigo else RequestsColors.CardWhite,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, RequestsColors.TextMuted.copy(alpha = 0.2f)),
                        modifier = Modifier.shadow(if (isSelected) 4.dp else 0.dp, RoundedCornerShape(50), spotColor = RequestsColors.PrimaryIndigo.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else RequestsColors.TextMuted,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // 🔥 Request List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.Inventory2,
                            contentDescription = null,
                            tint = RequestsColors.TextMuted.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No $selectedFilter requests found",
                            color = RequestsColors.TextMuted,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredList.sortedByDescending { it.requestTimestamp }) { request ->
                        ModernRequestCard(request)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernRequestCard(request: BorrowRequest) {
    val fallbackImageResId = getFallbackImageRes(request.equipmentImageName)
    val hasImageUrl = request.equipmentImageUrl.isNotBlank()
    val status = getDisplayStatus(request)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = RequestsColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header: Image + Title + Badge
            Row(verticalAlignment = Alignment.Top) {
                // Image Box
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(RequestsColors.ModernBg)
                ) {
                    if (hasImageUrl) {
                        AsyncImage(
                            model = request.equipmentImageUrl,
                            contentDescription = request.equipmentName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = fallbackImageResId),
                            contentDescription = request.equipmentName,
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = formatEquipmentName(request.equipmentName),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RequestsColors.TextDark,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        ModernStatusBadge(status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = formatCategory(request.equipmentCategory),
                        style = MaterialTheme.typography.bodySmall,
                        color = RequestsColors.TextMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Overdue Warning
            if (status == "Overdue") {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RequestsColors.RedLight, RoundedCornerShape(10.dp))
                        .border(1.dp, RequestsColors.RedText.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Warning, contentDescription = null, tint = RequestsColors.RedText, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Overdue item. Please return it as soon as possible.",
                        color = RequestsColors.RedText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = RequestsColors.ModernBg)
            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Details
            Surface(
                color = RequestsColors.ModernBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModernInfoColumn("Qty", request.quantity.toString())
                    ModernInfoColumn("Borrow Date", request.borrowDate)
                    ModernInfoColumn("Due Date", request.dueDate)
                }
            }
        }
    }
}

@Composable
private fun ModernInfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = RequestsColors.TextMuted,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = RequestsColors.TextDark,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun ModernStatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "Approved" -> Pair(RequestsColors.GreenLight, RequestsColors.GreenText)
        "Pending" -> Pair(RequestsColors.OrangeLight, RequestsColors.OrangeText)
        "Rejected" -> Pair(RequestsColors.RedLight, RequestsColors.RedText)
        "Returned" -> Pair(RequestsColors.BlueLight, RequestsColors.BlueText)
        "Overdue" -> Pair(RequestsColors.RedLight, RequestsColors.RedText)
        else -> Pair(RequestsColors.GrayLight, RequestsColors.GrayText)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// Logic Functions (Kept Intact)
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