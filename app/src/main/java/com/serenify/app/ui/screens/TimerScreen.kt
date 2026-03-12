package com.serenify.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serenify.app.ui.components.*
import com.serenify.app.ui.theme.*
import kotlinx.coroutines.delay

enum class TimerMode(val label: String, val minutes: Int, val color: Color) {
    FOCUS("Focus", 25, VividPurple),
    SHORT_BREAK("Short Break", 5, MintGreen),
    LONG_BREAK("Long Break", 15, ElectricBlue)
}

@Composable
fun TimerScreen() {
    var currentMode by remember { mutableStateOf(TimerMode.FOCUS) }
    var totalSeconds by remember { mutableIntStateOf(currentMode.minutes * 60) }
    var remainingSeconds by remember { mutableIntStateOf(totalSeconds) }
    var isRunning by remember { mutableStateOf(false) }
    var completedSessions by remember { mutableIntStateOf(0) }

    val progress = if (totalSeconds > 0) {
        (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()
    } else 0f

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    LaunchedEffect(isRunning) {
        while (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
        if (remainingSeconds == 0 && isRunning) {
            isRunning = false
            if (currentMode == TimerMode.FOCUS) {
                completedSessions++
            }
        }
    }

    val breatheScale = rememberInfiniteTransition(label = "breathe")
    val scale by breatheScale.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Focus Timer",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mode selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimerMode.entries.forEach { mode ->
                val isSelected = currentMode == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(
                                    listOf(mode.color, mode.color.copy(alpha = 0.7f))
                                )
                            } else {
                                Brush.horizontalGradient(
                                    listOf(GlassWhite.copy(alpha = 0.08f), GlassWhite.copy(alpha = 0.04f))
                                )
                            }
                        )
                        .clickable(enabled = !isRunning) {
                            currentMode = mode
                            totalSeconds = mode.minutes * 60
                            remainingSeconds = totalSeconds
                            isRunning = false
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.label,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextPrimary else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Timer Circle
        Box(modifier = Modifier.scale(scale)) {
            AnimatedCircularProgress(
                progress = progress,
                size = 260.dp,
                strokeWidth = 16.dp,
                gradientColors = listOf(
                    currentMode.color,
                    currentMode.color.copy(alpha = 0.6f),
                    CyanGlow
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = timeText,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        letterSpacing = 4.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentMode.label,
                        fontSize = 14.sp,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(GlassWhite.copy(alpha = 0.1f))
                    .clickable {
                        isRunning = false
                        remainingSeconds = totalSeconds
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(currentMode.color, currentMode.color.copy(alpha = 0.7f))
                        )
                    )
                    .clickable {
                        if (remainingSeconds > 0) {
                            isRunning = !isRunning
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start",
                    tint = TextPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(GlassWhite.copy(alpha = 0.1f))
                    .clickable {
                        isRunning = false
                        val nextMode = when (currentMode) {
                            TimerMode.FOCUS -> {
                                if (completedSessions > 0 && completedSessions % 4 == 0)
                                    TimerMode.LONG_BREAK
                                else
                                    TimerMode.SHORT_BREAK
                            }
                            else -> TimerMode.FOCUS
                        }
                        currentMode = nextMode
                        totalSeconds = nextMode.minutes * 60
                        remainingSeconds = totalSeconds
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.SkipNext,
                    contentDescription = "Skip",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Session info
        GlassCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$completedSessions",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = VividPurple
                    )
                    Text(
                        text = "Sessions",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${completedSessions * 25}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MintGreen
                    )
                    Text(
                        text = "Minutes",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${completedSessions / 4}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmOrange
                    )
                    Text(
                        text = "Cycles",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tips
        GlassCard {
            Column {
                Text(
                    text = "💡 Tip",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WarmOrange
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Take a long break after every 4 focus sessions to boost your productivity.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
