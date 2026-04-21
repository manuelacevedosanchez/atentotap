package com.example.atentotap.data.generators

import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class RoundGeneratorTest {

    private val generator = RoundGenerator(random = Random(42))

    @Test
    fun `generate should return exactly one shared symbol between both sides`() {
        val result = generator.generate(symbolsPerSide = 6)

        val overlap = result.topSymbols
            .map { it.id }
            .toSet()
            .intersect(result.bottomSymbols.map { it.id }.toSet())

        assertEquals(1, overlap.size)
        assertEquals(result.sharedSymbolId, overlap.first())
    }

    @Test
    fun `generate should return requested amount of symbols per side`() {
        val result = generator.generate(symbolsPerSide = 6)

        assertEquals(6, result.topSymbols.size)
        assertEquals(6, result.bottomSymbols.size)
    }

    @Test
    fun `generate should throw when symbols per side is less than two`() {
        assertFailsWith<IllegalArgumentException> {
            generator.generate(symbolsPerSide = 1)
        }
    }
}


