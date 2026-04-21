package com.example.atentotap.presentation.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.atentotap.R

@Composable
fun MenuScreen(
    onPlayClick: () -> Unit,
    onHowToPlayClick: () -> Unit,
    showDebugLanguageSelector: Boolean,
    selectedLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.menu_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(id = R.string.menu_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
            textAlign = TextAlign.Center,
        )

        Button(onClick = onPlayClick) {
            Text(text = stringResource(id = R.string.menu_play))
        }

        Button(
            onClick = onHowToPlayClick,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = stringResource(id = R.string.menu_how_to_play))
        }

        if (showDebugLanguageSelector) {
            Text(
                text = stringResource(id = R.string.debug_language_title),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (selectedLanguageTag.startsWith("en")) {
                    Button(onClick = { onLanguageSelected("en") }) {
                        Text(text = stringResource(id = R.string.debug_language_en))
                    }
                } else {
                    OutlinedButton(onClick = { onLanguageSelected("en") }) {
                        Text(text = stringResource(id = R.string.debug_language_en))
                    }
                }

                if (selectedLanguageTag.startsWith("es")) {
                    Button(onClick = { onLanguageSelected("es") }) {
                        Text(text = stringResource(id = R.string.debug_language_es))
                    }
                } else {
                    OutlinedButton(onClick = { onLanguageSelected("es") }) {
                        Text(text = stringResource(id = R.string.debug_language_es))
                    }
                }
            }
        }
    }
}

