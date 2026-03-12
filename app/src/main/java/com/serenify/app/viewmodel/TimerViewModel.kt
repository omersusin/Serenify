package com.serenify.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class TimerPhase(val label: String, val minutes: Int) {
    FOCUS("Focus", 25),
    SHORT_BREAK("Short Break", 5),
    LONG_BREAK("Long Break", 15)
}

class TimerViewModel : ViewModel() {

    var currentPhase by mutableStateOf(TimerPhase.FOCUS)
        private set

    var totalSeconds by mutableIntStateOf(TimerPhase.FOCUS.minutes * 60)
        private set

    var remainingSeconds by mutableIntStateOf(totalSeconds)
        private set

    var isRunning by mutableStateOf(false)
        private set

    var completedSessions by mutableIntStateOf(0)
        private set

    var totalFocusMinutes by mutableIntStateOf(0)
        private set

    private var timerJob: Job? = null

    val progress: Float
        get() = if (totalSeconds > 0) {
            (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()
        } else 0f

    val timeText: String
        get() {
            val min = remainingSeconds / 60
            val sec = remainingSeconds % 60
            return String.format("%02d:%02d", min, sec)
        }

    val completedCycles: Int
        get() = completedSessions / 4

    fun toggleTimer() {
        if (remainingSeconds <= 0) return
        isRunning = !isRunning
        if (isRunning) startTimer() else stopTimer()
    }

    fun resetTimer() {
        stopTimer()
        isRunning = false
        remainingSeconds = totalSeconds
    }

    fun switchPhase(phase: TimerPhase) {
        if (isRunning) return
        currentPhase = phase
        totalSeconds = phase.minutes * 60
        remainingSeconds = totalSeconds
    }

    fun skipToNext() {
        stopTimer()
        isRunning = false
        val nextPhase = when (currentPhase) {
            TimerPhase.FOCUS -> {
                if (completedSessions > 0 && completedSessions % 4 == 0)
                    TimerPhase.LONG_BREAK
                else
                    TimerPhase.SHORT_BREAK
            }
            else -> TimerPhase.FOCUS
        }
        switchPhase(nextPhase)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (remainingSeconds > 0 && isRunning) {
                delay(1000L)
                if (isRunning) {
                    remainingSeconds--
                }
            }
            if (remainingSeconds == 0) {
                isRunning = false
                if (currentPhase == TimerPhase.FOCUS) {
                    completedSessions++
                    totalFocusMinutes += currentPhase.minutes
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}
