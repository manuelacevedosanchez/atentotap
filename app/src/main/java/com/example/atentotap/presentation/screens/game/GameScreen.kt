package com.example.atentotap.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atentotap.R
import com.example.atentotap.data.generators.RoundGenerator
import com.example.atentotap.domain.model.GameMode
import com.example.atentotap.domain.model.Player
import com.example.atentotap.domain.model.SymbolModel
import com.example.atentotap.domain.model.Winner
import com.example.atentotap.domain.usecase.EvaluateTapUseCase
import com.example.atentotap.domain.usecase.GenerateRoundUseCase
import com.example.atentotap.presentation.components.ScoreBoard
import com.example.atentotap.presentation.components.SymbolGrid
import com.example.atentotap.presentation.game.GameIntent
import com.example.atentotap.presentation.game.GameUiState
import com.example.atentotap.presentation.game.GameViewModel
import com.example.atentotap.presentation.game.GameViewModelFactory
import com.example.atentotap.presentation.game.TapFeedback

@Composable
fun GameRoute(
    modeArg: String,
    valueArg: Int,
    onBackToModeSelection: () -> Unit,
    onGameFinished: (player1Score: Int, player2Score: Int, winner: Winner) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mode = remember(modeArg, valueArg) {
        if (modeArg == "time") {
            GameMode.Time(durationSeconds = valueArg)
        } else {
            GameMode.Score(targetScore = valueArg)
        }
    }

    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(
            mode = mode,
            symbolsPerSide = 6,
            generateRoundUseCase = GenerateRoundUseCase(RoundGenerator()),
            evaluateTapUseCase = EvaluateTapUseCase(),
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isGameFinished, uiState.winner) {
        val winner = uiState.winner
        if (uiState.isGameFinished && winner != null) {
            onGameFinished(uiState.player1Score, uiState.player2Score, winner)
        }
    }

    GameScreen(
        uiState = uiState,
        onTopSymbolTap = { symbolId ->
            viewModel.onIntent(GameIntent.OnSymbolTapped(Player.PLAYER_1, symbolId))
        },
        onBottomSymbolTap = { symbolId ->
            viewModel.onIntent(GameIntent.OnSymbolTapped(Player.PLAYER_2, symbolId))
        },
        onBackClick = onBackToModeSelection,
        modifier = modifier,
    )
}

@Composable
private fun GameScreen(
    uiState: GameUiState,
    onTopSymbolTap: (String) -> Unit,
    onBottomSymbolTap: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val timerText = when (val mode = uiState.mode) {
        is GameMode.Score -> stringResource(id = R.string.game_target_score, mode.targetScore)
        is GameMode.Time -> stringResource(
            id = R.string.game_time,
            uiState.remainingTimeSeconds.toClockText(),
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF10131A))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ScoreBoard(
            player1Score = uiState.player1Score,
            player2Score = uiState.player2Score,
            timerText = timerText,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            OutlinedButton(onClick = onBackClick) {
                Text(text = stringResource(id = R.string.common_back))
            }
        }

        PlayerZone(
            title = stringResource(id = R.string.game_player_zone, 1),
            feedback = uiState.player1Feedback,
            symbols = uiState.topSymbols,
            onSymbolTap = onTopSymbolTap,
            enabled = !uiState.isRoundLocked && !uiState.isGameFinished,
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .background(Color.White.copy(alpha = 0.2f))
                .padding(vertical = 1.dp)
        )

        PlayerZone(
            title = stringResource(id = R.string.game_player_zone, 2),
            feedback = uiState.player2Feedback,
            symbols = uiState.bottomSymbols,
            onSymbolTap = onBottomSymbolTap,
            enabled = !uiState.isRoundLocked && !uiState.isGameFinished,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PlayerZone(
    title: String,
    feedback: TapFeedback,
    symbols: List<SymbolModel>,
    onSymbolTap: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val feedbackColor = when (feedback) {
        TapFeedback.NONE -> Color.White.copy(alpha = 0.05f)
        TapFeedback.CORRECT -> Color(0xFF4CAF50).copy(alpha = 0.25f)
        TapFeedback.WRONG -> Color(0xFFE53935).copy(alpha = 0.2f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(feedbackColor, shape = MaterialTheme.shapes.medium)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )

        SymbolGrid(
            symbols = symbols,
            enabled = enabled,
            onSymbolTap = onSymbolTap,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        if (!enabled) {
            Text(
                text = stringResource(id = R.string.game_round_locked),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

private fun Int.toClockText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}


