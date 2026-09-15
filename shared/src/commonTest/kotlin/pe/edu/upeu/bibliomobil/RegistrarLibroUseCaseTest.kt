package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RegistrarLibroUseCaseTest {
    @Test
    fun validaCamposObligatorios() = runTest {
        val result = useCase()("", " ", "", "")

        val error = assertIs<LibroInvalidoException>(result.exceptionOrNull())
        assertEquals("El título es obligatorio", error.errores.titulo)
        assertEquals("El autor es obligatorio", error.errores.autor)
        assertEquals("El año es obligatorio", error.errores.anio)
        assertEquals("Los ejemplares son obligatorios", error.errores.ejemplares)
    }

    @Test
    fun validaAnioNoEnteroAntesDeRango() = runTest {
        val result = useCase()("Libro", "Autora", "abc", "2")

        val error = assertIs<LibroInvalidoException>(result.exceptionOrNull())
        assertEquals("El año debe ser un número entero", error.errores.anio)
    }

    @Test
    fun validaAnioFueraDeRango() = runTest {
        val result = useCase()("Libro", "Autora", "1449", "2")

        val error = assertIs<LibroInvalidoException>(result.exceptionOrNull())
        assertEquals("El año debe estar entre 1450 y 2026", error.errores.anio)
    }

    @Test
    fun validaEjemplaresNoEnterosYNegativos() = runTest {
        val noEntero = useCase()("Libro", "Autora", "2020", "dos")
        val negativo = useCase()("Libro", "Autora", "2020", "-1")

        assertEquals(
            "Los ejemplares deben ser un número entero",
            assertIs<LibroInvalidoException>(noEntero.exceptionOrNull()).errores.ejemplares
        )
        assertEquals(
            "Los ejemplares no pueden ser negativos",
            assertIs<LibroInvalidoException>(negativo.exceptionOrNull()).errores.ejemplares
        )
    }

    @Test
    fun registraLibroValidoNormalizadoConIdAsignadoPorRepository() = runTest {
        val result = useCase()("  Clean Code  ", "  Robert Martin  ", "1998", "3")

        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrThrow().id)
        assertEquals("Clean Code", result.getOrThrow().titulo)
        assertEquals("Robert Martin", result.getOrThrow().autor)
    }

    private fun useCase() = RegistrarLibroUseCase(FakeLibroRepository())
}
