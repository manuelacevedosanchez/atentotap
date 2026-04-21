package com.example.atentotap.presentation.screens.howto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HowToPlayScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "How to Play",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "1) The screen is split in half.\n" +
                "2) Top is Player 1, bottom is Player 2.\n" +
                "3) Each half has 6 symbols.\n" +
                "4) Exactly one symbol appears in both halves.\n" +
                "5) Tap the shared symbol in your own area first to score.",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "In Score mode, first to the target wins. In Time mode, highest score when time ends wins.",
            style = MaterialTheme.typography.bodyMedium,
        )

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Back")
        }
    }
}

