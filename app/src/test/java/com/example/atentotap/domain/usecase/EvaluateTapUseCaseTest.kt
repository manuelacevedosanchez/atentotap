package com.example.atentotap.domain.usecase

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class EvaluateTapUseCaseTest {

    private val useCase = EvaluateTapUseCase()

    @Test
    fun `invoke should return true when tapped symbol matches shared symbol`() {
        val result = useCase(tappedSymbolId = "A", sharedSymbolId = "A")

        assertTrue(result)
    }

    @Test
    fun `invoke should return false when tapped symbol does not match shared symbol`() {
        val result = useCase(tappedSymbolId = "A", sharedSymbolId = "B")

        assertFalse(result)
    }
}


