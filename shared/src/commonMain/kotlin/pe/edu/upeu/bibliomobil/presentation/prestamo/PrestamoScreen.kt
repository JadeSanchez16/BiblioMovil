package pe.edu.upeu.bibliomobil.presentation.prestamo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio

@Composable
fun PrestamoScreen(
    modifier: Modifier = Modifier
) {
    EstadoVacio(
        icono = Icons.Default.Bookmark,
        titulo = "Préstamos en construcción",
        descripcion = "El módulo de préstamos ya tiene dominio preparado y estará disponible en una versión futura.",
        modifier = modifier
            .fillMaxSize()
    )
}
