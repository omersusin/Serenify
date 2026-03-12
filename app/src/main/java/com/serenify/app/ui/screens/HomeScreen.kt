package com.serenify.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serenify.app.ui.components.*
import com.serenify.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    completedHabits: Int = 0,
    totalHabits: Int = 0,
    totalFocusMin: Int = 0,
    longestStreak: Int = 0
) {
    val scrollState = rememberScrollState()
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val greeting = when {
        hour < 5 -> "Good Night"
        hour < 12 -> "Good Morning"
        hour < 17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH)
    val todayDate = dateFormat.format(Date())

    val quotes = listOf(
        Pair("The journey of a thousand miles begins with a single step.", "Lao Tzu"),
        Pair("What hurts you today makes you stronger tomorrow.", "Jay Cutler"),
        Pair("Small progress is still progress.", "Unknown"),
        Pair("Believe in yourself and all that you are.", "Christian Larson"),
        Pair("Patience is bitter, but its fruit is sweet.", "Aristotle"),
        Pair("The only way to do great work is to love what you do.", "Steve Jobs"),
        Pair("It does not matter how slowly you go as long as you do not stop.", "Confucius")
    )

    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    val todayQuote = quotes[dayOfYear % quotes.size]

    val habitProgress = if (totalHabits > 0) {
        completedHabits.toFloat() / totalHabits.toFloat()
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        // -- HEADER --
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = todayDate.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDim,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = greeting,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = (-0.5).sp
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AccentSubtle)
                    .border(1.dp, Border, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "☀️", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // -- TODAY'S PROGRESS --
        SurfaceCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProgressRing(
                    progress = habitProgress,
                    size = 100.dp,
                    strokeWidth = 8.dp,
                    accentColor = Green,
                    trackColor = Border
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(habitProgress * 100).toInt()}%",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Today",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDim
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (totalHabits > 0) "$completedHabits of $totalHabits habits done"
                               else "No habits yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (totalHabits > 0) {
                        // Mini progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Border)
                        ) {
                            val animWidth by animateFloatAsState(
                                targetValue = habitProgress,
                                animationSpec = tween(800, easing = EaseOutCubic),
                                label = "bar"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(animWidth)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Green)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -- STATS ROW --
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Focus time
            SurfaceCard(modifier = Modifier.weight(1f)) {
                Column {
                    IconBadge(
                        icon = {
                            Icon(
                                Icons.Rounded.Timer,
                                contentDescription = null,
                                tint = Blue,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        color = Blue,
                        size = 36.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (totalFocusMin >= 60) "${totalFocusMin / 60}h ${totalFocusMin % 60}m"
                               else "${totalFocusMin}m",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Focus time",
                        fontSize = 12.sp,
                        color = TextDim
                    )
                }
            }

            // Streak
            SurfaceCard(modifier = Modifier.weight(1f)) {
                Column {
                    IconBadge(
                        icon = {
                            Icon(
                                Icons.Rounded.LocalFireDepartment,
                                contentDescription = null,
                                tint = Orange,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        color = Orange,
                        size = 36.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "$longestStreak",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Best streak",
                        fontSize = 12.sp,
                        color = TextDim
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -- QUOTE --
        SectionHeader(title = "Daily Inspiration")
        Spacer(modifier = Modifier.height(12.dp))

        QuoteBlock(
            quote = todayQuote.first,
            author = todayQuote.second
        )

        Spacer(modifier = Modifier.height(24.dp))

        // -- QUICK ACTIONS --
        SectionHeader(title = "Quick Start")
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                emoji = "🧘",
                label = "Meditate",
                subtitle = "5 min",
                color = Accent,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "🎯",
                label = "Focus",
                subtitle = "25 min",
                color = Blue,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "✍️",
                label = "Journal",
                subtitle = "Free write",
                color = Green,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun QuickActionCard(
    emoji: String,
    label: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(BgCard)
            .border(1.dp, Border, RoundedCornerShape(20.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextWhite
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = TextDim
        )
    }
}
