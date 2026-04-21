package com.example.atentotap.domain.model

data class RoundModel(
    val topSymbols: List<SymbolModel>,
    val bottomSymbols: List<SymbolModel>,
    val sharedSymbolId: String,
)

