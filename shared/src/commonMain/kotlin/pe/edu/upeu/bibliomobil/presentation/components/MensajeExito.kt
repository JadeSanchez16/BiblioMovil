package pe.edu.upeu.bibliomobil.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MensajeExito(
    mensaje: String
) {
    Text(
        text = mensaje,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyMedium
    )
}
