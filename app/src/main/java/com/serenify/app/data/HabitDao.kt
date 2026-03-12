package com.serenify.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT COUNT(*) FROM habits")
    fun getHabitCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM habits WHERE isCompletedToday = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("UPDATE habits SET isCompletedToday = 0")
    suspend fun resetAllCompletions()

    @Query("SELECT MAX(streak) FROM habits")
    fun getLongestStreak(): Flow<Int?>
}
