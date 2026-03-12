package com.serenify.app.data

import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {

    val allHabits: Flow<List<HabitEntity>> = habitDao.getAllHabits()

    val habitCount: Flow<Int> = habitDao.getHabitCount()

    val completedCount: Flow<Int> = habitDao.getCompletedCount()

    val longestStreak: Flow<Int?> = habitDao.getLongestStreak()

    suspend fun insert(habit: HabitEntity) {
        habitDao.insertHabit(habit)
    }

    suspend fun update(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }

    suspend fun delete(habit: HabitEntity) {
        habitDao.deleteHabit(habit)
    }

    suspend fun toggleCompletion(habit: HabitEntity) {
        val updated = habit.copy(
            isCompletedToday = !habit.isCompletedToday,
            streak = if (!habit.isCompletedToday) habit.streak + 1 else (habit.streak - 1).coerceAtLeast(0)
        )
        habitDao.updateHabit(updated)
    }

    suspend fun resetDaily() {
        habitDao.resetAllCompletions()
    }
}
