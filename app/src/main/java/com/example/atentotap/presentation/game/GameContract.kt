package com.example.atentotap.presentation.game

import com.example.atentotap.domain.model.GameMode
import com.example.atentotap.domain.model.Player
import com.example.atentotap.domain.model.SymbolModel
import com.example.atentotap.domain.model.Winner

enum class TapFeedback {
    NONE,
    CORRECT,
    WRONG,
}

data class GameUiState(
    val mode: GameMode = GameMode.Score(targetScore = 5),
    val symbolsPerSide: Int = 6,
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val remainingTimeSeconds: Int = 0,
    val topSymbols: List<SymbolModel> = emptyList(),
    val bottomSymbols: List<SymbolModel> = emptyList(),
    val sharedSymbolId: String = "",
    val isRoundLocked: Boolean = false,
    val isGameFinished: Boolean = false,
    val winner: Winner? = null,
    val player1Feedback: TapFeedback = TapFeedback.NONE,
    val player2Feedback: TapFeedback = TapFeedback.NONE,
)

sealed interface GameIntent {
    data class OnSymbolTapped(val player: Player, val symbolId: String) : GameIntent
    data object OnRestart : GameIntent
}

