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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serenify.app.ui.components.*
import com.serenify.app.ui.theme.*

@Composable
fun ProfileScreen() {
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
            text = "Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(VividPurple, ElectricBlue, CyanGlow)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🌟",
                fontSize = 44.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Serenify User",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "Finding peace, one day at a time",
            fontSize = 14.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Stats Row
        GlassCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat(value = "28", label = "Days", color = VividPurple)
                ProfileDivider()
                ProfileStat(value = "156", label = "Sessions", color = ElectricBlue)
                ProfileDivider()
                ProfileStat(value = "42h", label = "Focus", color = MintGreen)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Achievements
        SectionTitle(title = "Achievements")
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AchievementBadge(
                emoji = "🔥",
                title = "On Fire",
                subtitle = "7 day streak",
                color = WarmOrange,
                unlocked = true,
                modifier = Modifier.weight(1f)
            )
            AchievementBadge(
                emoji = "🏆",
                title = "Champion",
                subtitle = "100 sessions",
                color = VividPurple,
                unlocked = true,
                modifier = Modifier.weight(1f)
            )
            AchievementBadge(
                emoji = "💎",
                title = "Diamond",
                subtitle = "30 day streak",
                color = CyanGlow,
                unlocked = false,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weekly Activity
        SectionTitle(title = "This Week")
        Spacer(modifier = Modifier.height(12.dp))

        GlassCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val activity = listOf(0.8f, 0.6f, 1.0f, 0.4f, 0.9f, 0.3f, 0f)

                days.forEachIndexed { index, day ->
                    WeekDayBar(
                        day = day,
                        progress = activity[index],
                        isToday = index == 4,
                        color = VividPurple
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Settings
        SectionTitle(title = "Settings")
        Spacer(modifier = Modifier.height(12.dp))

        GlassCard {
            Column {
                SettingsItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    subtitle = "Reminders & alerts",
                    color = WarmOrange
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Outlined.Palette,
                    title = "Appearance",
                    subtitle = "Theme & display",
                    color = VividPurple
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Outlined.CloudSync,
                    title = "Backup",
                    subtitle = "Sync your data",
                    color = ElectricBlue
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Outlined.Info,
                    title = "About",
                    subtitle = "Version 1.0 • Made with ❤️",
                    color = MintGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Serenify v1.0",
            fontSize = 12.sp,
            color = TextMuted
        )
        Text(
            text = "Crafted with passion ✨",
            fontSize = 12.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileStat(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextMuted
        )
    }
}

@Composable
fun ProfileDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(GlassBorder)
    )
}

@Composable
fun AchievementBadge(
    emoji: String,
    title: String,
    subtitle: String,
    color: Color,
    unlocked: Boolean,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (unlocked) color.copy(alpha = 0.15f)
                        else GlassWhite.copy(alpha = 0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (unlocked) emoji else "🔒",
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (unlocked) TextPrimary else TextMuted
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun WeekDayBar(
    day: String,
    progress: Float,
    isToday: Boolean,
    color: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800, easing = EaseOutCubic),
        label = "weekBar"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(GlassWhite.copy(alpha = 0.06f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(animatedProgress)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (progress > 0f) {
                            Brush.verticalGradient(
                                listOf(color, color.copy(alpha = 0.5f))
                            )
                        } else {
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Transparent)
                            )
                        }
                    )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = day,
            fontSize = 11.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (isToday) color else TextMuted
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted
            )
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(1.dp)
            .background(GlassBorder.copy(alpha = 0.3f))
    )
}
