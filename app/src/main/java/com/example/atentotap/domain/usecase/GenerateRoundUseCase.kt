package com.example.atentotap.domain.usecase

import com.example.atentotap.data.generators.RoundGenerator
import com.example.atentotap.domain.model.RoundModel

class GenerateRoundUseCase(
    private val roundGenerator: RoundGenerator,
) {
    operator fun invoke(symbolsPerSide: Int): RoundModel = roundGenerator.generate(symbolsPerSide)
}

