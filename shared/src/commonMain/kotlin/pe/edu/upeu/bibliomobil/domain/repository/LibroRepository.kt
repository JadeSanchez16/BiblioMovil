package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/**
 * Define las operaciones disponibles para mantener el catalogo de libros de la biblioteca.
 */
interface LibroRepository {
    suspend fun registrar(libro: Libro): Libro

    suspend fun listar(): List<Libro>
}
