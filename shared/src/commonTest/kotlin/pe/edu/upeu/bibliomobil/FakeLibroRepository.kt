package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository : LibroRepository {
    private val libros = mutableListOf<Libro>()
    private var siguienteId = 1L
    var fallaRegistrar = false
    var fallaListar = false
    var registros = 0

    override suspend fun registrar(libro: Libro): Libro {
        if (fallaRegistrar) error("Fallo al registrar libro")
        registros++
        val registrado = libro.copy(id = siguienteId++)
        libros += registrado
        return registrado
    }

    override suspend fun listar(): List<Libro> {
        if (fallaListar) error("Fallo de catálogo")
        return libros.toList()
    }
}
