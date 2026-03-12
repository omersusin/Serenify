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
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serenify.app.ui.components.*
import com.serenify.app.ui.theme.*

@Composable
fun ProfileScreen(
    totalSessions: Int = 0,
    totalFocusMin: Int = 0,
    totalHabits: Int = 0,
    longestStreak: Int = 0
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        // Header
        Text(
            text = "Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Avatar section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(AccentSubtle)
                    .border(2.dp, Border, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌟", fontSize = 40.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Serenify User",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Finding peace, one day at a time",
                fontSize = 13.sp,
                color = TextDim
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Stats bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(BgCard)
                .border(1.dp, Border, RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatColumn(value = "$totalHabits", label = "Habits", color = Accent)
            StatDivider()
            StatColumn(value = "$totalSessions", label = "Sessions", color = Blue)
            StatDivider()
            StatColumn(
                value = if (totalFocusMin >= 60) "${totalFocusMin / 60}h" else "${totalFocusMin}m",
                label = "Focus",
                color = Green
            )
            StatDivider()
            StatColumn(value = "$longestStreak", label = "Streak", color = Orange)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Achievements
        SectionHeader(title = "Achievements")
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AchievementCard(
                emoji = "🔥",
                title = "On Fire",
                desc = "7 day streak",
                color = Orange,
                unlocked = longestStreak >= 7,
                modifier = Modifier.weight(1f)
            )
            AchievementCard(
                emoji = "💪",
                title = "Dedicated",
                desc = "50 sessions",
                color = Blue,
                unlocked = totalSessions >= 50,
                modifier = Modifier.weight(1f)
            )
            AchievementCard(
                emoji = "💎",
                title = "Diamond",
                desc = "30 day streak",
                color = Teal,
                unlocked = longestStreak >= 30,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Weekly overview
        SectionHeader(title = "This Week")
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(BgCard)
                .border(1.dp, Border, RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val days = listOf("M", "T", "W", "T", "F", "S", "S")
            val values = listOf(0.9f, 0.5f, 1f, 0.7f, 0.6f, 0.2f, 0f)
            val today = 4 // Friday index

            days.forEachIndexed { idx, day ->
                DayBar(
                    label = day,
                    fill = values[idx],
                    active = idx == today,
                    accent = Accent
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Settings
        SectionHeader(title = "Settings")
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(BgCard)
                .border(1.dp, Border, RoundedCornerShape(20.dp))
        ) {
            SettingRow(
                icon = Icons.Rounded.Notifications,
                title = "Notifications",
                subtitle = "Reminders & alerts",
                color = Orange
            )
            RowDivider()
            SettingRow(
                icon = Icons.Rounded.Palette,
                title = "Appearance",
                subtitle = "Theme & display",
                color = Accent
            )
            RowDivider()
            SettingRow(
                icon = Icons.Rounded.Cloud,
                title = "Backup",
                subtitle = "Sync your data",
                color = Blue
            )
            RowDivider()
            SettingRow(
                icon = Icons.Rounded.Info,
                title = "About",
                subtitle = "v1.0 · Made with ❤️",
                color = Green
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Serenify",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextFaint
            )
            Text(
                text = "Crafted with passion ✨",
                fontSize = 12.sp,
                color = TextFaint
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatColumn(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextDim
        )
    }
}

@Composable
fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(Border)
    )
}

@Composable
fun AchievementCard(
    emoji: String,
    title: String,
    desc: String,
    color: Color,
    unlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(BgCard)
            .border(
                width = 1.dp,
                color = if (unlocked) color.copy(alpha = 0.3f) else Border,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (unlocked) color.copy(alpha = 0.12f)
                    else BgSecondary
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (unlocked) emoji else "🔒",
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (unlocked) TextWhite else TextDim
        )
        Text(
            text = desc,
            fontSize = 10.sp,
            color = TextDim
        )
    }
}

@Composable
fun DayBar(
    label: String,
    fill: Float,
    active: Boolean,
    accent: Color
) {
    val animFill by animateFloatAsState(
        targetValue = fill,
        animationSpec = tween(700, easing = EaseOutCubic),
        label = "daybar"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(72.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Border.copy(alpha = 0.5f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (fill > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(animFill)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (fill >= 1f) Green
                            else accent.copy(alpha = 0.7f)
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            color = if (active) accent else TextDim
        )
    }
}

@Composable
fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextDim
            )
        }
        Icon(
            Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = TextFaint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(Border.copy(alpha = 0.5f))
    )
}
