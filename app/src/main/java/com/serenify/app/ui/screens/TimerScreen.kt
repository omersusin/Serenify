package com.serenify.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serenify.app.ui.components.*
import com.serenify.app.ui.theme.*
import kotlinx.coroutines.delay

data class TimerMode(
    val label: String,
    val minutes: Int,
    val color: Color
)

@Composable
fun TimerScreen() {
    val modes = remember {
        listOf(
            TimerMode("Focus", 25, Accent),
            TimerMode("Short Break", 5, Green),
            TimerMode("Long Break", 15, Blue)
        )
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentMode = modes[selectedIndex]

    var totalSeconds by remember { mutableIntStateOf(currentMode.minutes * 60) }
    var remaining by remember { mutableIntStateOf(totalSeconds) }
    var isRunning by remember { mutableStateOf(false) }
    var sessions by remember { mutableIntStateOf(0) }

    val progress = if (totalSeconds > 0) {
        (totalSeconds - remaining).toFloat() / totalSeconds.toFloat()
    } else 0f

    val min = remaining / 60
    val sec = remaining % 60
    val timeText = String.format("%02d:%02d", min, sec)

    LaunchedEffect(isRunning) {
        while (isRunning && remaining > 0) {
            delay(1000L)
            remaining--
        }
        if (remaining == 0 && isRunning) {
            isRunning = false
            if (selectedIndex == 0) sessions++
        }
    }

    // Subtle pulse when running
    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ps"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Focus",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mode tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BgSecondary)
                .border(1.dp, Border, RoundedCornerShape(14.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            modes.forEachIndexed { index, mode ->
                val isSelected = selectedIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) BgElevated else Color.Transparent)
                        .then(
                            if (isSelected) Modifier.border(1.dp, BorderLight, RoundedCornerShape(10.dp))
                            else Modifier
                        )
                        .clickable(enabled = !isRunning) {
                            selectedIndex = index
                            totalSeconds = mode.minutes * 60
                            remaining = totalSeconds
                            isRunning = false
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.label,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) TextWhite else TextDim
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Timer ring
        Box(modifier = Modifier.scale(pulseScale)) {
            ProgressRing(
                progress = progress,
                size = 260.dp,
                strokeWidth = 10.dp,
                accentColor = currentMode.color,
                trackColor = Border
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timeText,
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentMode.label.lowercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDim
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset
            IconButton(
                onClick = {
                    isRunning = false
                    remaining = totalSeconds
                },
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(BgCard)
                    .border(1.dp, Border, CircleShape)
            ) {
                Icon(
                    Icons.Rounded.Refresh,
                    contentDescription = "Reset",
                    tint = TextGray,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Play/Pause
            IconButton(
                onClick = {
                    if (remaining > 0) isRunning = !isRunning
                },
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(currentMode.color)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Play",
                    tint = BgPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Skip
            IconButton(
                onClick = {
                    isRunning = false
                    val nextIndex = when (selectedIndex) {
                        0 -> if (sessions > 0 && sessions % 4 == 0) 2 else 1
                        else -> 0
                    }
                    selectedIndex = nextIndex
                    totalSeconds = modes[nextIndex].minutes * 60
                    remaining = totalSeconds
                },
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(BgCard)
                    .border(1.dp, Border, CircleShape)
            ) {
                Icon(
                    Icons.Rounded.SkipNext,
                    contentDescription = "Skip",
                    tint = TextGray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Session stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(BgCard)
                .border(1.dp, Border, RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InlineStat(label = "Sessions", value = "$sessions", color = Accent)
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(Border)
            )
            InlineStat(label = "Minutes", value = "${sessions * 25}", color = Green)
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(Border)
            )
            InlineStat(label = "Cycles", value = "${sessions / 4}", color = Orange)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(OrangeDim)
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "💡", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Take a long break after every 4 focus sessions to maintain peak performance.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
