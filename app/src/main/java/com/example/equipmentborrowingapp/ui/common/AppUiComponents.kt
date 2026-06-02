package com.example.equipmentborrowingapp.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.ui.theme.*
import com.example.equipmentborrowingapp.ui.theme.Error as AppError

@Composable
fun AppButtonEffectModifier(interactionSource: MutableInteractionSource): Modifier {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "ButtonScale"
    )
    return Modifier.scale(scale)
}

@Composable
fun GradientHeaderCard(
    title: String,
    subtitle: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Primary, Secondary)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = title,
                color = TextLight.copy(alpha = 0.85f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                color = TextLight,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                color = TextLight.copy(alpha = 0.90f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SummaryStatCard(
    title: String,
    value: String,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AppStatusBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfessionalStatusBadge(
    text: String,
    type: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = getStatusBadgeColors(type)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary,
            contentColor = TextLight,
            disabledContainerColor = TextMuted,
            disabledContentColor = TextLight
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .then(AppButtonEffectModifier(interactionSource)),
        interactionSource = interactionSource
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceWhite,
            contentColor = TextPrimary
        ),
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .then(AppButtonEffectModifier(interactionSource)),
        interactionSource = interactionSource
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CompactActionBox(
    text: String,
    filled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val bg = if (filled) Primary else SurfaceWhite
    val textColor = if (filled) TextLight else TextPrimary
    val borderColor = if (filled) Primary else BorderLight

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .then(AppButtonEffectModifier(interactionSource))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 16.sp,
        color = TextPrimary,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun EmptyStateText(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun LabeledValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TwoColumnInfoRow(
    leftTitle: String,
    leftValue: String,
    rightTitle: String,
    rightValue: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LabeledValue(
            title = leftTitle,
            value = leftValue,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        LabeledValue(
            title = rightTitle,
            value = rightValue,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AlertInfoCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = InfoLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = Info,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = message,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun NotificationCard(
    title: String,
    message: String,
    type: String,
    modifier: Modifier = Modifier
) {

    val (bgColor, textColor) = getNotificationCardColors(type)
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message,
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun AnimatedListItem(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = index * 30)) +
                slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(durationMillis = 200, delayMillis = index * 30)
                )
    ) {
        content()
    }
}
private fun getStatusBadgeColors(
    type: String
): Pair<Color, Color> {
    return when (type.trim().lowercase()) {
        "success",
        "approved",
        "returned",
        "active",
        "available" -> Pair(SuccessLight, Success)

        "warning",
        "pending",
        "low stock",
        "maintenance",
        "low" -> Pair(WarningLight, Warning)

        "error",
        "rejected",
        "overdue",
        "problematic",
        "out of stock",
        "out",
        "damaged",
        "lost" -> Pair(ErrorLight, AppError)

        "info" -> Pair(InfoLight, Info)

        else -> Pair(BorderLight, TextSecondary)
    }
}

private fun getNotificationCardColors(
    type: String
): Pair<Color, Color> {
    return when (type.trim().lowercase()) {
        "warning" -> Pair(WarningLight, Warning)
        "error" -> Pair(ErrorLight, AppError)
        "success" -> Pair(SuccessLight, Success)
        else -> Pair(InfoLight, Info)
    }
}
