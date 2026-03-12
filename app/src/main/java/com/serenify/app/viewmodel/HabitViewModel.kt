package com.serenify.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.serenify.app.data.AppDatabase
import com.serenify.app.data.HabitEntity
import com.serenify.app.data.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository

    val allHabits: StateFlow<List<HabitEntity>>
    val habitCount: StateFlow<Int>
    val completedCount: StateFlow<Int>
    val longestStreak: StateFlow<Int?>

    init {
        val dao = AppDatabase.getDatabase(application).habitDao()
        repository = HabitRepository(dao)

        allHabits = repository.allHabits.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        habitCount = repository.habitCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        completedCount = repository.completedCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        longestStreak = repository.longestStreak.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )
    }

    fun addHabit(name: String, emoji: String, colorHex: Long) {
        viewModelScope.launch {
            repository.insert(
                HabitEntity(
                    name = name,
                    emoji = emoji,
                    colorHex = colorHex
                )
            )
        }
    }

    fun toggleHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.toggleCompletion(habit)
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.delete(habit)
        }
    }

    fun resetDaily() {
        viewModelScope.launch {
            repository.resetDaily()
        }
    }
}
