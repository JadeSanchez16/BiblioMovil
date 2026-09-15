package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.Libro

fun libro(
    id: Long = 0L,
    titulo: String = "Clean Code",
    autor: String = "Robert Martin",
    anio: Int = 1998,
    ejemplares: Int = 3
) = Libro(
    id = id,
    titulo = titulo,
    autor = autor,
    anio = anio,
    ejemplares = ejemplares
)
