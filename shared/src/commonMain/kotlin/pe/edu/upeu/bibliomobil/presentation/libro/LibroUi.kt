package pe.edu.upeu.bibliomobil.presentation.libro

import pe.edu.upeu.bibliomobil.domain.model.Libro

data class LibroUi(
    val id: Long,
    val titulo: String,
    val autor: String,
    val detalle: String,
    val mostrarBadgeReposicion: Boolean,
    val badgeReposicion: String = "Pocos ejemplares"
)

fun Libro.aUi(): LibroUi =
    LibroUi(
        id = id,
        titulo = titulo,
        autor = autor,
        detalle = "$anio · $ejemplares ${if (ejemplares == 1) "ejemplar" else "ejemplares"}",
        mostrarBadgeReposicion = requiereReposicion
    )
