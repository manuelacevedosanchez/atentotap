package com.example.atentotap.presentation.game

import com.example.atentotap.data.generators.RoundGenerator
import com.example.atentotap.domain.model.GameMode
import com.example.atentotap.domain.model.Player
import com.example.atentotap.domain.model.Winner
import com.example.atentotap.domain.usecase.EvaluateTapUseCase
import com.example.atentotap.domain.usecase.GenerateRoundUseCase
import com.example.atentotap.testutil.TestCoroutineExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestCoroutineExtension::class)
class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @BeforeEach
    fun setUp(@Suppress("UNUSED_PARAMETER") testDispatcher: TestDispatcher) {
        viewModel = GameViewModel(
            mode = GameMode.Score(targetScore = 1),
            symbolsPerSide = 6,
            generateRoundUseCase = GenerateRoundUseCase(RoundGenerator(Random(7))),
            evaluateTapUseCase = EvaluateTapUseCase(),
        )
    }

    @Test
    fun `player one should win game when reaches target score first`() = runTest {
        val sharedSymbolId = viewModel.uiState.value.sharedSymbolId

        viewModel.onIntent(GameIntent.OnSymbolTapped(Player.PLAYER_1, sharedSymbolId))
        advanceTimeBy(1)

        val state = viewModel.uiState.value
        assertEquals(1, state.player1Score)
        assertTrue(state.isGameFinished)
        assertEquals(Winner.PLAYER_1, state.winner)
    }

    @Test
    fun `wrong tap should not change score`() = runTest {
        val currentState = viewModel.uiState.value
        val wrongSymbolId = currentState.topSymbols.first { it.id != currentState.sharedSymbolId }.id

        viewModel.onIntent(GameIntent.OnSymbolTapped(Player.PLAYER_1, wrongSymbolId))
        advanceTimeBy(1)

        val state = viewModel.uiState.value
        assertEquals(0, state.player1Score)
        assertFalse(state.isGameFinished)
    }
}


