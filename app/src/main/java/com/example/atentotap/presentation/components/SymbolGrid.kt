package com.example.atentotap.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.atentotap.domain.model.SymbolModel

@Composable
fun SymbolGrid(
    symbols: List<SymbolModel>,
    onSymbolTap: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        userScrollEnabled = false,
    ) {
        items(
            items = symbols,
            key = { it.id },
        ) { symbol ->
            SymbolTokenView(
                symbol = symbol,
                enabled = enabled,
                onClick = { onSymbolTap(symbol.id) },
            )
        }
    }
}

