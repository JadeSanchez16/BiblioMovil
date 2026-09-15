package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || correo != null || telefono != null
}

class LectorInvalidoException(
    val errores: ErroresDeLector
) : IllegalArgumentException()

class RegistrarLectorUseCase(
    private val repository: LectorRepository
) {
    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String
    ): Result<Lector> = resultadoDe {
        val nombreNormalizado = nombre.trim()
        val correoNormalizado = correo.trim()
        val telefonoNormalizado = telefono.trim()
        val telefonoGuardado = telefonoNormalizado.ifBlank { null }

        val errores = ErroresDeLector(
            nombre = if (nombreNormalizado.isBlank()) "El nombre es obligatorio" else null,
            correo = when {
                correoNormalizado.isBlank() -> "El correo es obligatorio"
                !CORREO_REGEX.matches(correoNormalizado) -> "El correo no tiene un formato válido"
                else -> null
            },
            telefono = when {
                telefonoGuardado == null -> null
                !telefonoGuardado.all { it.isDigit() } -> "El teléfono debe tener entre 6 y 9 dígitos"
                telefonoGuardado.length !in 6..9 -> "El teléfono debe tener entre 6 y 9 dígitos"
                else -> null
            }
        )

        if (errores.hayErrores) {
            throw LectorInvalidoException(errores)
        }

        repository.registrar(
            Lector(
                id = 0L,
                nombre = nombreNormalizado,
                correo = correoNormalizado,
                telefono = telefonoGuardado
            )
        )
    }

    private companion object {
        val CORREO_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
