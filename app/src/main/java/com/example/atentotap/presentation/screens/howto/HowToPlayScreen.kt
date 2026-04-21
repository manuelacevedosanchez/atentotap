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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.atentotap.R

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
            text = stringResource(id = R.string.how_to_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(id = R.string.how_to_rules),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(id = R.string.how_to_modes),
            style = MaterialTheme.typography.bodyMedium,
        )

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.common_back))
        }
    }
}

