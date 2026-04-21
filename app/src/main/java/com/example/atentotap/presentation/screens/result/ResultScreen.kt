package com.example.atentotap.presentation.screens.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.atentotap.R
import com.example.atentotap.domain.model.Winner

@Composable
fun ResultScreen(
    winner: Winner,
    player1Score: Int,
    player2Score: Int,
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val winnerText = when (winner) {
        Winner.PLAYER_1 -> stringResource(id = R.string.result_winner_player_1)
        Winner.PLAYER_2 -> stringResource(id = R.string.result_winner_player_2)
        Winner.DRAW -> stringResource(id = R.string.result_draw)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = winnerText,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(id = R.string.result_final_score, player1Score, player2Score),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 28.dp),
        )

        Button(
            onClick = onPlayAgain,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.result_play_again))
        }

        OutlinedButton(
            onClick = onBackToMenu,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            Text(text = stringResource(id = R.string.result_back_to_menu))
        }
    }
}

