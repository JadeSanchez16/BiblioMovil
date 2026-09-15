package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.usecase.resultadoDe
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ResultadoDeTest {
    @Test
    fun convierteExcepcionComunEnResultFailure() = runTest {
        val result = resultadoDe<String> { error("Falla") }

        assertTrue(result.isFailure)
        assertEquals("Falla", result.exceptionOrNull()?.message)
    }

    @Test
    fun relanzaCancellationException() = runTest {
        assertFailsWith<CancellationException> {
            resultadoDe<String> { throw CancellationException("Cancelado") }
        }
    }
}
