package pe.edu.upeu.bibliomobil.presentation.libro

data class LibroUiState(
    val fase: LibroFase = LibroFase.SinLibros,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)

sealed interface LibroFase {
    data object Cargando : LibroFase
    data object SinLibros : LibroFase
    data class ConLibros(val libros: List<LibroUi>) : LibroFase
    data class Error(val mensaje: String) : LibroFase
}
