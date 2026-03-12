package com.serenify.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
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

data class HabitItem(
    val id: Int,
    val name: String,
    val emoji: String,
    val color: Color,
    val streak: Int,
    val isCompleted: Boolean = false
)

@Composable
fun HabitsScreen() {
    var habits by remember {
        mutableStateOf(
            listOf(
                HabitItem(1, "Meditation", "🧘", VividPurple, 7),
                HabitItem(2, "Reading", "📖", ElectricBlue, 12),
                HabitItem(3, "Exercise", "💪", MintGreen, 5),
                HabitItem(4, "Hydration", "💧", CyanGlow, 21),
                HabitItem(5, "Journaling", "✍️", WarmOrange, 3),
                HabitItem(6, "Sleep Early", "😴", SoftPink, 9)
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }

    val completedCount = habits.count { it.isCompleted }
    val totalCount = habits.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Habits",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$completedCount of $totalCount completed today",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(VividPurple, ElectricBlue)
                                )
                            )
                            .clickable { showAddDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add Habit",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Progress Card
            item {
                GlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Today's Progress",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary
                            )
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = VividPurple
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(GlassWhite.copy(alpha = 0.1f))
                        ) {
                            val animatedWidth by animateFloatAsState(
                                targetValue = progress,
                                animationSpec = tween(800, easing = EaseOutCubic),
                                label = "progressBar"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(animatedWidth)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(VividPurple, CyanGlow)
                                        )
                                    )
                            )
                        }
                    }
                }
            }

            // Section Title
            item {
                SectionTitle(title = "Your Habits")
            }

            // Habit Cards
            itemsIndexed(habits) { index, habit ->
                HabitCard(
                    habit = habit,
                    onToggle = {
                        habits = habits.toMutableList().also { list ->
                            list[index] = habit.copy(isCompleted = !habit.isCompleted)
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Add Habit Dialog
        if (showAddDialog) {
            AddHabitDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, emoji, color ->
                    habits = habits + HabitItem(
                        id = habits.size + 1,
                        name = name,
                        emoji = emoji,
                        color = color,
                        streak = 0
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun HabitCard(
    habit: HabitItem,
    onToggle: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (habit.isCompleted) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "cardScale"
    )

    GlassCard(
        modifier = Modifier.scale(scale)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(habit.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = habit.emoji,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (habit.isCompleted) TextMuted else TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = WarmOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${habit.streak} day streak",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            // Check button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (habit.isCompleted) {
                            Brush.linearGradient(listOf(MintGreen, MintGreen.copy(alpha = 0.7f)))
                        } else {
                            Brush.linearGradient(
                                listOf(GlassWhite.copy(alpha = 0.1f), GlassWhite.copy(alpha = 0.05f))
                            )
                        }
                    )
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Toggle",
                    tint = if (habit.isCompleted) TextPrimary else TextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, Color) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("⭐") }
    var selectedColor by remember { mutableStateOf(VividPurple) }

    val emojis = listOf("⭐", "📖", "💪", "🧘", "💧", "🎯", "🎨", "🎵", "🏃", "😴", "✍️", "🍎")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        shape = RoundedCornerShape(28.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Habit",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GlassWhite.copy(alpha = 0.1f))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit name", color = TextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VividPurple,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = VividPurple
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Choose an icon",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Emoji grid - 6 columns
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (row in emojis.chunked(6)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { emoji ->
                                val isSelected = selectedEmoji == emoji
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) VividPurple.copy(alpha = 0.2f)
                                            else GlassWhite.copy(alpha = 0.06f)
                                        )
                                        .clickable { selectedEmoji = emoji },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Choose a color",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HabitColors.forEach { color ->
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            GradientButton(
                text = "Add Habit",
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(name, selectedEmoji, selectedColor)
                    }
                },
                enabled = name.isNotBlank()
            )
        }
    )
}
