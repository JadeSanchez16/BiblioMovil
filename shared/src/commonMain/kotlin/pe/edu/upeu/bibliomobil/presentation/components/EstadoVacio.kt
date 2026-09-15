package pe.edu.upeu.bibliomobil.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EstadoVacio(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    esError: Boolean = false,
    accionTexto: String? = null,
    onAccion: (() -> Unit)? = null
) {
    val color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = color
        )
        Text(
            text = titulo,
            color = color,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = descripcion,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        if (accionTexto != null && onAccion != null) {
            Button(onClick = onAccion) {
                Text(accionTexto)
            }
        }
    }
}
