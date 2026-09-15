package pe.edu.upeu.bibliomobil.presentation.libro

data class FormularioLibro(
    val titulo: String = "",
    val autor: String = "",
    val anio: String = "",
    val ejemplares: String = "",
    val errorTitulo: String? = null,
    val errorAutor: String? = null,
    val errorAnio: String? = null,
    val errorEjemplares: String? = null
)
