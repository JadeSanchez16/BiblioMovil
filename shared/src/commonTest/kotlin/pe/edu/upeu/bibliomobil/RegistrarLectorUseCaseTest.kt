package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RegistrarLectorUseCaseTest {
    @Test
    fun validaCorreoInvalido() = runTest {
        val result = useCase()("Ana", "correo-invalido", "")

        val error = assertIs<LectorInvalidoException>(result.exceptionOrNull())
        assertEquals("El correo no tiene un formato válido", error.errores.correo)
    }

    @Test
    fun validaTelefonoCorto() = runTest {
        val result = useCase()("Ana", "ana@test.com", "12345")

        val error = assertIs<LectorInvalidoException>(result.exceptionOrNull())
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", error.errores.telefono)
    }

    @Test
    fun validaTelefonoConLetras() = runTest {
        val result = useCase()("Ana", "ana@test.com", "123abc")

        val error = assertIs<LectorInvalidoException>(result.exceptionOrNull())
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", error.errores.telefono)
    }

    @Test
    fun telefonoEnBlancoSeGuardaComoNull() = runTest {
        val result = useCase()(" Ana ", " ana@test.com ", "   ")

        assertEquals(null, result.getOrThrow().telefono)
        assertEquals("Ana", result.getOrThrow().nombre)
        assertEquals("ana@test.com", result.getOrThrow().correo)
    }

    @Test
    fun validaNombreYCorreoObligatorios() = runTest {
        val result = useCase()("", "", "")

        val error = assertIs<LectorInvalidoException>(result.exceptionOrNull())
        assertEquals("El nombre es obligatorio", error.errores.nombre)
        assertEquals("El correo es obligatorio", error.errores.correo)
    }

    private fun useCase() = RegistrarLectorUseCase(FakeLectorRepository())
}
