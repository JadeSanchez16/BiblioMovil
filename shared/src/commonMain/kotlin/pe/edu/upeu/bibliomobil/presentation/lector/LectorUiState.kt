package pe.edu.upeu.bibliomobil.presentation.lector

data class LectorUiState(
    val fase: LectorFase = LectorFase.SinLectores,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)

sealed interface LectorFase {
    data object Cargando : LectorFase
    data object SinLectores : LectorFase
    data class ConLectores(val lectores: List<LectorUi>) : LectorFase
    data class Error(val mensaje: String) : LectorFase
}
