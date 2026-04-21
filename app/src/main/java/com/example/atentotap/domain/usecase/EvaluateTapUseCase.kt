package com.example.atentotap.domain.usecase

class EvaluateTapUseCase {
    operator fun invoke(tappedSymbolId: String, sharedSymbolId: String): Boolean {
        return tappedSymbolId == sharedSymbolId
    }
}

