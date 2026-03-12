package com.serenify.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
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
                HabitItem(1, "Meditation", "🧘", Accent, 7),
                HabitItem(2, "Reading", "📖", Blue, 12),
                HabitItem(3, "Exercise", "💪", Green, 5),
                HabitItem(4, "Hydration", "💧", Teal, 21),
                HabitItem(5, "Journaling", "✍️", Orange, 3),
                HabitItem(6, "Sleep Early", "😴", Rose, 9)
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }

    val completed = habits.count { it.isCompleted }
    val total = habits.size
    val progress = if (total > 0) completed.toFloat() / total.toFloat() else 0f

    Box(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp, end = 20.dp,
                top = 16.dp, bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$completed of $total completed",
                            fontSize = 13.sp,
                            color = TextDim
                        )
                    }

                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Accent)
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "Add",
                            tint = BgPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Progress
            item {
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BgCard)
                        .border(1.dp, Border, RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProgressRing(
                        progress = progress,
                        size = 64.dp,
                        strokeWidth = 6.dp,
                        accentColor = if (progress >= 1f) Green else Accent,
                        trackColor = Border
                    ) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when {
                                progress >= 1f -> "All done! 🎉"
                                progress >= 0.5f -> "Great progress!"
                                progress > 0f -> "Keep going!"
                                else -> "Let's start!"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Border)
                        ) {
                            val animW by animateFloatAsState(
                                targetValue = progress,
                                animationSpec = tween(800, easing = EaseOutCubic),
                                label = "pb"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(animW.coerceAtLeast(0.01f))
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (progress >= 1f) Green else Accent)
                            )
                        }
                    }
                }
            }

            // Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader(title = "Today")
            }

            // Habit cards
            itemsIndexed(
                items = habits,
                key = { _, habit -> habit.id }
            ) { index, habit ->
                HabitCard(
                    habit = habit,
                    onToggle = {
                        habits = habits.toMutableList().also { list ->
                            list[index] = habit.copy(
                                isCompleted = !habit.isCompleted,
                                streak = if (!habit.isCompleted) habit.streak + 1
                                         else (habit.streak - 1).coerceAtLeast(0)
                            )
                        }
                    },
                    onDelete = {
                        habits = habits.toMutableList().also { list ->
                            list.removeAt(index)
                        }
                    }
                )
            }

            // Empty state
            if (habits.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🌱", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No habits yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap + to create your first habit",
                            fontSize = 14.sp,
                            color = TextDim
                        )
                    }
                }
            }
        }

        // Add dialog
        if (showAddDialog) {
            AddHabitDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, emoji, color ->
                    habits = habits + HabitItem(
                        id = (habits.maxOfOrNull { it.id } ?: 0) + 1,
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
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var showDelete by remember { mutableStateOf(false) }

    val cardScale by animateFloatAsState(
        targetValue = if (habit.isCompleted) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "cs"
    )

    Row(
        modifier = Modifier
            .scale(cardScale)
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (habit.isCompleted) BgSecondary else BgCard)
            .border(
                width = 1.dp,
                color = if (habit.isCompleted) Border.copy(alpha = 0.5f) else Border,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { showDelete = !showDelete }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoji badge
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(habit.color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = habit.emoji, fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = habit.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (habit.isCompleted) TextDim else TextWhite
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🔥", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${habit.streak} day streak",
                    fontSize = 12.sp,
                    color = TextDim
                )
            }
        }

        // Delete button (animated)
        AnimatedVisibility(
            visible = showDelete,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(RoseDim)
            ) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = "Delete",
                    tint = Rose,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Check button
        IconButton(
            onClick = onToggle,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .then(
                    if (habit.isCompleted) {
                        Modifier.background(Green)
                    } else {
                        Modifier
                            .background(Color.Transparent)
                            .border(2.dp, Border, CircleShape)
                    }
                )
        ) {
            if (habit.isCompleted) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = "Done",
                    tint = BgPrimary,
                    modifier = Modifier.size(20.dp)
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
    var selectedColor by remember { mutableStateOf(Accent) }

    val emojis = listOf(
        "⭐", "📖", "💪", "🧘", "💧", "🎯",
        "🎨", "🎵", "🏃", "😴", "✍️", "🍎"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BgElevated,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Habit",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(BgCard)
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = TextDim,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        text = {
            Column {
                // Name input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text("e.g. Morning run", color = TextFaint)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Border,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        cursorColor = Accent
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Icon
                Text(
                    text = "ICON",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDim,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (row in emojis.chunked(6)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { emoji ->
                                val isSel = selectedEmoji == emoji
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSel) AccentSubtle else BgCard)
                                        .then(
                                            if (isSel) Modifier.border(1.dp, Accent, RoundedCornerShape(12.dp))
                                            else Modifier.border(1.dp, Border, RoundedCornerShape(12.dp))
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

                // Color
                Text(
                    text = "COLOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDim,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HabitColors.forEach { color ->
                        val isSel = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .then(
                                    if (isSel) Modifier.border(2.dp, TextWhite, CircleShape)
                                    else Modifier
                                )
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSel) {
                                Icon(
                                    Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = BgPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            PrimaryButton(
                text = "Create",
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(name.trim(), selectedEmoji, selectedColor)
                    }
                },
                enabled = name.isNotBlank()
            )
        }
    )
}
