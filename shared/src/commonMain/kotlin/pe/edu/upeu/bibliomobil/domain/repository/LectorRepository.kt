package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/**
 * Define las operaciones disponibles para mantener la cartera de lectores de la biblioteca.
 */
interface LectorRepository {
    suspend fun registrar(lector: Lector): Lector

    suspend fun listar(): List<Lector>
}
