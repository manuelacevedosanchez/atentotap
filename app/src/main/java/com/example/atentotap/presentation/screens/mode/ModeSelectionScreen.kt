package com.example.atentotap.presentation.screens.mode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.atentotap.R

enum class ModeTypeUi {
    SCORE,
    TIME,
}

@Composable
fun ModeSelectionScreen(
    onStartMatch: (mode: ModeTypeUi, value: Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedMode by remember { mutableStateOf(ModeTypeUi.SCORE) }
    var selectedScoreTarget by remember { mutableIntStateOf(5) }
    var selectedTimeDuration by remember { mutableIntStateOf(60) }

    val options = if (selectedMode == ModeTypeUi.SCORE) {
        listOf(5, 10, 15)
    } else {
        listOf(30, 60, 90)
    }

    val selectedValue =
        if (selectedMode == ModeTypeUi.SCORE) selectedScoreTarget else selectedTimeDuration

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(id = R.string.mode_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ToggleButton(
                title = stringResource(id = R.string.mode_score),
                selected = selectedMode == ModeTypeUi.SCORE,
                onClick = { selectedMode = ModeTypeUi.SCORE },
            )
            ToggleButton(
                title = stringResource(id = R.string.mode_time),
                selected = selectedMode == ModeTypeUi.TIME,
                onClick = { selectedMode = ModeTypeUi.TIME },
            )
        }

        Text(
            text = if (selectedMode == ModeTypeUi.SCORE) {
                stringResource(id = R.string.mode_target_score)
            } else {
                stringResource(id = R.string.mode_match_duration)
            },
            style = MaterialTheme.typography.titleMedium,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { value ->
                ToggleButton(
                    title = if (selectedMode == ModeTypeUi.SCORE) {
                        value.toString()
                    } else {
                        stringResource(id = R.string.common_seconds_short, value)
                    },
                    selected = selectedValue == value,
                    onClick = {
                        if (selectedMode == ModeTypeUi.SCORE) {
                            selectedScoreTarget = value
                        } else {
                            selectedTimeDuration = value
                        }
                    },
                )
            }
        }

        Text(
            text = stringResource(id = R.string.mode_symbols_per_side, 6),
            style = MaterialTheme.typography.bodyLarge,
        )

        Button(
            onClick = { onStartMatch(selectedMode, selectedValue) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.mode_start_match))
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.common_back))
        }
    }
}

@Composable
private fun ToggleButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    if (selected) {
        Button(onClick = onClick) {
            Text(title)
        }
    } else {
        OutlinedButton(onClick = onClick) {
            Text(title)
        }
    }
}

