package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class FakeLectorRepository : LectorRepository {
    private val lectores = mutableListOf<Lector>()
    private var siguienteId = 1L
    var fallaListar = false

    override suspend fun registrar(lector: Lector): Lector {
        val registrado = lector.copy(id = siguienteId++)
        lectores += registrado
        return registrado
    }

    override suspend fun listar(): List<Lector> {
        if (fallaListar) error("Fallo de lectores")
        return lectores.toList()
    }
}
