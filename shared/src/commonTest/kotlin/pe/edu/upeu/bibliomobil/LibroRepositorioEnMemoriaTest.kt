package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import kotlin.test.Test
import kotlin.test.assertEquals

class LibroRepositorioEnMemoriaTest {
    @Test
    fun asignaIdsCorrelativosDesdeUno() = runTest {
        val repository = LibroRepositorioEnMemoria()
        val primero = repository.registrar(libro(titulo = "Uno"))
        val segundo = repository.registrar(libro(titulo = "Dos"))

        assertEquals(1L, primero.id)
        assertEquals(2L, segundo.id)
    }

    @Test
    fun listarDevuelveOrdenDeRegistro() = runTest {
        val repository = LibroRepositorioEnMemoria()
        repository.registrar(libro(titulo = "Uno"))
        repository.registrar(libro(titulo = "Dos"))

        assertEquals(listOf("Uno", "Dos"), repository.listar().map { it.titulo })
    }
}
