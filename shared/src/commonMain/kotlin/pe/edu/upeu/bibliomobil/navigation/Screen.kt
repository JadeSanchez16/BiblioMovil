package pe.edu.upeu.bibliomobil.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    data object Inicio : Screen("inicio", "Inicio", Icons.Default.Home)
    data object Libros : Screen("libros", "Libros", Icons.Default.LocalLibrary)
    data object Lectores : Screen("lectores", "Lectores", Icons.Default.Group)
    data object Prestamos : Screen("prestamos", "Préstamos", Icons.Default.Bookmark)

    companion object {
        fun desdeRuta(ruta: String): Screen =
            DESTINOS.firstOrNull { it.ruta == ruta } ?: Inicio
    }
}

val DESTINOS = listOf(
    Screen.Inicio,
    Screen.Libros,
    Screen.Lectores,
    Screen.Prestamos
)
