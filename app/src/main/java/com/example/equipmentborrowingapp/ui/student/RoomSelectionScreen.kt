package com.example.equipmentborrowingapp.ui.student
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.MeetingRoom
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.Room

// Modern Premium Colors
private object RoomColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)
    val PurpleLight = Color(0xFFF5F3FF)
    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)
    val RedLight = Color(0xFFFEF2F2)   // 🔥 ফিক্সড: রেড লাইট কালার যোগ করা হয়েছে
    val RedText = Color(0xFFDC2626)    // 🔥 ফিক্সড: রেড টেক্সট কালার যোগ করা হয়েছে
}

@Composable
fun RoomSelectionScreen(
    roomList: List<Room>,
    onRoomClick: (Room) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredRooms = remember(roomList, searchQuery) {
        val query = searchQuery.trim().lowercase()
        if (query.isBlank()) {
            roomList
        } else {
            roomList.filter { room ->
                room.name.lowercase().contains(query) ||
                        room.roomType.lowercase().contains(query) ||
                        room.building.lowercase().contains(query) ||
                        room.floor.lowercase().contains(query) ||
                        room.department.lowercase().contains(query)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = RoomColors.ModernBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 🔙 Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(RoomColors.CardWhite, RoundedCornerShape(12.dp))
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = RoomColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Select Room / Lab",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoomColors.TextDark
                    )
                    Text(
                        text = "Choose a room to view available lab computers",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RoomColors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Search & Stats Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompactRoomInfoBox(
                    title = "Rooms",
                    value = "${filteredRooms.size}/${roomList.size}",
                    modifier = Modifier.weight(0.75f)
                )

                CompactRoomSearchField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1.55f)
                )
            }

            // List State or Empty States
            if (roomList.isEmpty()) {
                EmptyRoomState(
                    title = "No room or lab found",
                    message = "No room or lab is currently available for your institution."
                )
            } else if (filteredRooms.isEmpty()) {
                EmptyRoomState(
                    title = "No matching room found",
                    message = "Try searching with another room, building, floor, or department name."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(filteredRooms) { room ->
                        RoomSelectionCard(
                            room = room,
                            onClick = { onRoomClick(room) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactRoomInfoBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RoomColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, RoomColors.ModernBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = RoomColors.TextMuted,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = RoomColors.PrimaryIndigo,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CompactRoomSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .shadow(2.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RoomColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = RoomColors.TextMuted,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = RoomColors.TextDark,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isBlank()) {
                            Text(
                                text = "Search room...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = RoomColors.TextMuted
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
private fun RoomSelectionCard(
    room: Room,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.05f))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RoomColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(RoomColors.PurpleLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Business,
                        contentDescription = null,
                        tint = RoomColors.PurpleAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = room.name.ifBlank { "Unnamed Room" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RoomColors.TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = room.department.ifBlank { "Department not specified" },
                        style = MaterialTheme.typography.bodySmall,
                        color = RoomColors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RoomColors.BlueLight
                ) {
                    Text(
                        text = room.roomType.ifBlank { "Room" },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoomColors.BlueText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoomInfoChip(
                    label = "Building",
                    value = room.building.ifBlank { "N/A" },
                    modifier = Modifier.weight(1f)
                )
                RoomInfoChip(
                    label = "Floor",
                    value = room.floor.ifBlank { "N/A" },
                    modifier = Modifier.weight(1f)
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = RoomColors.ModernBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tap to view lab computers",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = RoomColors.TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Select",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoomColors.PrimaryIndigo
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomInfoChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = RoomColors.ModernBg
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = RoomColors.TextMuted,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = RoomColors.TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EmptyRoomState(
    title: String,
    message: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RoomColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, RoomColors.ModernBg)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(RoomColors.RedLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.MeetingRoom,
                    contentDescription = null,
                    tint = RoomColors.RedText,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = RoomColors.TextDark
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = RoomColors.TextMuted,
                lineHeight = 20.sp
            )
        }
    }
}