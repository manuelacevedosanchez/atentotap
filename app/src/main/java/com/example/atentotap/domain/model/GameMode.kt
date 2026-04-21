package com.example.atentotap.domain.model

sealed class GameMode {
    data class Score(val targetScore: Int) : GameMode()
    data class Time(val durationSeconds: Int) : GameMode()
}

