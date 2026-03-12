package com.serenify.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val emoji: String,
    val colorHex: Long,
    val streak: Int = 0,
    val isCompletedToday: Boolean = false,
    val lastCompletedDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
