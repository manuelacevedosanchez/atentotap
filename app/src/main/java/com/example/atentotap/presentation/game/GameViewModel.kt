package com.example.atentotap.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.atentotap.domain.model.GameMode
import com.example.atentotap.domain.model.Player
import com.example.atentotap.domain.model.Winner
import com.example.atentotap.domain.usecase.EvaluateTapUseCase
import com.example.atentotap.domain.usecase.GenerateRoundUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GameViewModel(
    private val mode: GameMode,
    private val symbolsPerSide: Int,
    private val generateRoundUseCase: GenerateRoundUseCase,
    private val evaluateTapUseCase: EvaluateTapUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameUiState(
            mode = mode,
            symbolsPerSide = symbolsPerSide,
            remainingTimeSeconds = if (mode is GameMode.Time) mode.durationSeconds else 0,
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val roundMutex = Mutex()
    private var timerJob: Job? = null
    private var nextRoundJob: Job? = null
    private var feedbackPlayer1Job: Job? = null
    private var feedbackPlayer2Job: Job? = null

    init {
        generateAndSetRound()
        startTimerIfNeeded()
    }

    fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnSymbolTapped -> onSymbolTapped(intent.player, intent.symbolId)
            GameIntent.OnRestart -> restartGame()
        }
    }

    private fun onSymbolTapped(player: Player, symbolId: String) {
        viewModelScope.launch {
            roundMutex.withLock {
                val currentState = _uiState.value
                if (currentState.isGameFinished || currentState.isRoundLocked) {
                    return@withLock
                }

                val isCorrect = evaluateTapUseCase(symbolId, currentState.sharedSymbolId)
                if (!isCorrect) {
                    setWrongFeedback(player)
                    return@withLock
                }

                val updatedState = when (player) {
                    Player.PLAYER_1 -> currentState.copy(
                        player1Score = currentState.player1Score + 1,
                        player1Feedback = TapFeedback.CORRECT,
                        isRoundLocked = true,
                    )

                    Player.PLAYER_2 -> currentState.copy(
                        player2Score = currentState.player2Score + 1,
                        player2Feedback = TapFeedback.CORRECT,
                        isRoundLocked = true,
                    )
                }

                _uiState.value = updatedState
                maybeFinishGameOrContinue(updatedState)
            }
        }
    }

    private fun maybeFinishGameOrContinue(state: GameUiState) {
        when (val currentMode = state.mode) {
            is GameMode.Score -> {
                val winner = when {
                    state.player1Score >= currentMode.targetScore -> Winner.PLAYER_1
                    state.player2Score >= currentMode.targetScore -> Winner.PLAYER_2
                    else -> null
                }

                if (winner != null) {
                    finishGame(winner)
                } else {
                    scheduleNextRound()
                }
            }

            is GameMode.Time -> {
                if (state.remainingTimeSeconds <= 0) {
                    finishGame(resolveWinnerByScore(state.player1Score, state.player2Score))
                } else {
                    scheduleNextRound()
                }
            }
        }
    }

    private fun scheduleNextRound() {
        nextRoundJob?.cancel()
        nextRoundJob = viewModelScope.launch {
            delay(ROUND_END_DELAY_MS)
            if (_uiState.value.isGameFinished) {
                return@launch
            }
            clearFeedback()
            generateAndSetRound()
        }
    }

    private fun generateAndSetRound() {
        val round = generateRoundUseCase(symbolsPerSide)
        _uiState.update {
            it.copy(
                topSymbols = round.topSymbols,
                bottomSymbols = round.bottomSymbols,
                sharedSymbolId = round.sharedSymbolId,
                isRoundLocked = false,
            )
        }
    }

    private fun setWrongFeedback(player: Player) {
        when (player) {
            Player.PLAYER_1 -> {
                feedbackPlayer1Job?.cancel()
                _uiState.update { it.copy(player1Feedback = TapFeedback.WRONG) }
                feedbackPlayer1Job = viewModelScope.launch {
                    delay(WRONG_FEEDBACK_DURATION_MS)
                    _uiState.update { state -> state.copy(player1Feedback = TapFeedback.NONE) }
                }
            }

            Player.PLAYER_2 -> {
                feedbackPlayer2Job?.cancel()
                _uiState.update { it.copy(player2Feedback = TapFeedback.WRONG) }
                feedbackPlayer2Job = viewModelScope.launch {
                    delay(WRONG_FEEDBACK_DURATION_MS)
                    _uiState.update { state -> state.copy(player2Feedback = TapFeedback.NONE) }
                }
            }
        }
    }

    private fun clearFeedback() {
        feedbackPlayer1Job?.cancel()
        feedbackPlayer2Job?.cancel()
        _uiState.update {
            it.copy(
                player1Feedback = TapFeedback.NONE,
                player2Feedback = TapFeedback.NONE,
            )
        }
    }

    private fun startTimerIfNeeded() {
        if (mode !is GameMode.Time) return

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1_000)
                val currentState = _uiState.value
                if (currentState.isGameFinished) {
                    return@launch
                }
                val newRemaining = (currentState.remainingTimeSeconds - 1).coerceAtLeast(0)
                _uiState.update { it.copy(remainingTimeSeconds = newRemaining) }

                if (newRemaining == 0) {
                    nextRoundJob?.cancel()
                    finishGame(resolveWinnerByScore(_uiState.value.player1Score, _uiState.value.player2Score))
                    return@launch
                }
            }
        }
    }

    private fun finishGame(winner: Winner) {
        timerJob?.cancel()
        nextRoundJob?.cancel()
        _uiState.update {
            it.copy(
                isGameFinished = true,
                isRoundLocked = true,
                winner = winner,
            )
        }
    }

    private fun restartGame() {
        timerJob?.cancel()
        nextRoundJob?.cancel()
        clearFeedback()

        _uiState.value = GameUiState(
            mode = mode,
            symbolsPerSide = symbolsPerSide,
            remainingTimeSeconds = if (mode is GameMode.Time) mode.durationSeconds else 0,
        )

        generateAndSetRound()
        startTimerIfNeeded()
    }

    private fun resolveWinnerByScore(player1Score: Int, player2Score: Int): Winner {
        return when {
            player1Score > player2Score -> Winner.PLAYER_1
            player2Score > player1Score -> Winner.PLAYER_2
            else -> Winner.DRAW
        }
    }

    companion object {
        private const val ROUND_END_DELAY_MS = 550L
        private const val WRONG_FEEDBACK_DURATION_MS = 220L
    }
}

class GameViewModelFactory(
    private val mode: GameMode,
    private val symbolsPerSide: Int,
    private val generateRoundUseCase: GenerateRoundUseCase,
    private val evaluateTapUseCase: EvaluateTapUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(GameViewModel::class.java)) {
            "Unknown ViewModel class: ${modelClass.name}"
        }
        return GameViewModel(
            mode = mode,
            symbolsPerSide = symbolsPerSide,
            generateRoundUseCase = generateRoundUseCase,
            evaluateTapUseCase = evaluateTapUseCase,
        ) as T
    }
}

