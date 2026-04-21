package com.example.atentotap.data.generators

import com.example.atentotap.domain.model.RoundModel
import kotlin.random.Random

class RoundGenerator(
    private val random: Random = Random.Default,
) {
    private val pool = SymbolPoolFactory.createPool()

    fun generate(symbolsPerSide: Int): RoundModel {
        require(symbolsPerSide > 1) { "symbolsPerSide must be greater than 1" }

        val neededUniqueCount = (symbolsPerSide * 2) - 1
        require(pool.size >= neededUniqueCount) {
            "Not enough unique symbols to generate a round"
        }

        val shuffledPool = pool.shuffled(random)
        val shared = shuffledPool.first()
        val remaining = shuffledPool.drop(1)

        val topUnique = remaining.take(symbolsPerSide - 1)
        val bottomUnique = remaining.drop(symbolsPerSide - 1).take(symbolsPerSide - 1)

        val topSymbols = (topUnique + shared).shuffled(random)
        val bottomSymbols = (bottomUnique + shared).shuffled(random)

        val overlap = topSymbols.map { it.id }.toSet().intersect(bottomSymbols.map { it.id }.toSet())
        require(overlap.size == 1) { "Round generation failed: overlap size was ${overlap.size}" }

        return RoundModel(
            topSymbols = topSymbols,
            bottomSymbols = bottomSymbols,
            sharedSymbolId = shared.id,
        )
    }
}

