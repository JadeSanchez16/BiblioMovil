package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

data class ErroresDeLibro(
    val titulo: String? = null,
    val autor: String? = null,
    val anio: String? = null,
    val ejemplares: String? = null
) {
    val hayErrores: Boolean
        get() = titulo != null || autor != null || anio != null || ejemplares != null
}

class LibroInvalidoException(
    val errores: ErroresDeLibro
) : IllegalArgumentException()

class RegistrarLibroUseCase(
    private val repository: LibroRepository
) {
    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String
    ): Result<Libro> = resultadoDe {
        val tituloNormalizado = titulo.trim()
        val autorNormalizado = autor.trim()
        val anioNormalizado = anio.trim()
        val ejemplaresNormalizados = ejemplares.trim()

        var anioConvertido: Int? = null
        var ejemplaresConvertidos: Int? = null

        val errores = ErroresDeLibro(
            titulo = if (tituloNormalizado.isBlank()) "El título es obligatorio" else null,
            autor = if (autorNormalizado.isBlank()) "El autor es obligatorio" else null,
            anio = when {
                anioNormalizado.isBlank() -> "El año es obligatorio"
                anioNormalizado.toIntOrNull() == null -> "El año debe ser un número entero"
                else -> {
                    anioConvertido = anioNormalizado.toInt()
                    if (anioConvertido !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO) {
                        "El año debe estar entre 1450 y 2026"
                    } else {
                        null
                    }
                }
            },
            ejemplares = when {
                ejemplaresNormalizados.isBlank() -> "Los ejemplares son obligatorios"
                ejemplaresNormalizados.toIntOrNull() == null -> "Los ejemplares deben ser un número entero"
                else -> {
                    ejemplaresConvertidos = ejemplaresNormalizados.toInt()
                    if (ejemplaresConvertidos < 0) {
                        "Los ejemplares no pueden ser negativos"
                    } else {
                        null
                    }
                }
            }
        )

        if (errores.hayErrores) {
            throw LibroInvalidoException(errores)
        }

        repository.registrar(
            Libro(
                id = 0L,
                titulo = tituloNormalizado,
                autor = autorNormalizado,
                anio = anioConvertido ?: error("Año validado sin conversión"),
                ejemplares = ejemplaresConvertidos ?: error("Ejemplares validados sin conversión")
            )
        )
    }
}
