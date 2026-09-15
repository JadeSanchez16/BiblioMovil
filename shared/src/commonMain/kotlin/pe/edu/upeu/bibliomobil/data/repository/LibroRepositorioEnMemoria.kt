package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class LibroRepositorioEnMemoria : LibroRepository {
    private val mutex = Mutex()
    private val libros = mutableListOf<Libro>()
    private var siguienteId = 1L

    override suspend fun registrar(libro: Libro): Libro {
        delay(DELAY_ARTIFICIAL_MS)
        return mutex.withLock {
            val registrado = libro.copy(id = siguienteId++)
            libros += registrado
            registrado
        }
    }

    override suspend fun listar(): List<Libro> {
        delay(DELAY_ARTIFICIAL_MS)
        return mutex.withLock {
            libros.toList()
        }
    }

    private companion object {
        const val DELAY_ARTIFICIAL_MS = 400L
    }
}
