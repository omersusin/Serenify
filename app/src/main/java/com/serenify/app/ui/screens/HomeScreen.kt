package com.serenify.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serenify.app.ui.components.*
import com.serenify.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val greeting = when {
        hour < 6 -> "Good Night"
        hour < 12 -> "Good Morning"
        hour < 18 -> "Good Afternoon"
        else -> "Good Evening"
    }

    val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH)
    val todayDate = dateFormat.format(Date())

    val quotes = listOf(
        Pair("The journey of a thousand miles begins with a single step.", "Lao Tzu"),
        Pair("What hurts you today makes you stronger tomorrow.", "Jay Cutler"),
        Pair("Every day is a new beginning.", "Unknown"),
        Pair("Change begins in your own hands.", "Unknown"),
        Pair("Small progress is still progress.", "Unknown"),
        Pair("Believe in yourself and all that you are.", "Christian Larson"),
        Pair("Patience is bitter, but its fruit is sweet.", "Aristotle")
    )

    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    val todayQuote = quotes[dayOfYear % quotes.size]

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingOrbs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = greeting,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = todayDate,
                        fontSize = 14.sp,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(VividPurple, ElectricBlue)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SelfImprovement,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Daily Progress
            GlassCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Daily Progress",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedCircularProgress(
                        progress = 0.65f,
                        size = 180.dp,
                        strokeWidth = 14.dp,
                        gradientColors = listOf(VividPurple, CyanGlow, MintGreen)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "65%",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "completed",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MiniStat(label = "Focus", value = "2.5 hr", color = VividPurple)
                        MiniStat(label = "Habits", value = "4/6", color = MintGreen)
                        MiniStat(label = "Streak", value = "7 days", color = WarmOrange)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quote
            QuoteCard(
                quote = todayQuote.first,
                author = todayQuote.second
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Stats Grid
            SectionTitle(title = "Statistics")
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Focus",
                    value = "24.5 hr",
                    icon = {
                        Icon(
                            Icons.Filled.Timer,
                            contentDescription = null,
                            tint = ElectricBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    accentColor = ElectricBlue,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Best Streak",
                    value = "12 days",
                    icon = {
                        Icon(
                            Icons.Filled.LocalFireDepartment,
                            contentDescription = null,
                            tint = WarmOrange,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    accentColor = WarmOrange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Habits",
                    value = "6",
                    icon = {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MintGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    accentColor = MintGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "This Week",
                    value = "85%",
                    icon = {
                        Icon(
                            Icons.Filled.LocalFireDepartment,
                            contentDescription = null,
                            tint = SoftPink,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    accentColor = SoftPink,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MiniStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextMuted
        )
    }
}

@Composable
fun FloatingOrbs() {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    VividPurple.copy(alpha = 0.15f),
                    VividPurple.copy(alpha = 0f)
                ),
                radius = 200f
            ),
            radius = 200f,
            center = Offset(80f + offset1, 200f + offset2)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricBlue.copy(alpha = 0.1f),
                    ElectricBlue.copy(alpha = 0f)
                ),
                radius = 250f
            ),
            radius = 250f,
            center = Offset(size.width - 50f - offset2, 500f + offset1)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    CyanGlow.copy(alpha = 0.08f),
                    CyanGlow.copy(alpha = 0f)
                ),
                radius = 180f
            ),
            radius = 180f,
            center = Offset(size.width / 2 + offset1, size.height - 300f - offset2)
        )
    }
}
