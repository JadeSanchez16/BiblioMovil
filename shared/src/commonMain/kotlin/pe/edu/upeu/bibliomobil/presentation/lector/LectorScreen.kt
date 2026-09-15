package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LectorScreen(
    viewModel: LectorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.cargarLectores()
    }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FormularioLectorCard(
                formulario = uiState.formulario,
                registrando = uiState.registrando,
                mensajeExito = uiState.mensajeExito,
                onNombreChange = viewModel::onNombreChange,
                onCorreoChange = viewModel::onCorreoChange,
                onTelefonoChange = viewModel::onTelefonoChange,
                onRegistrar = viewModel::registrar
            )
        }

        item {
            when (val fase = uiState.fase) {
                LectorFase.Cargando -> CargandoLectores()
                LectorFase.SinLectores -> EstadoVacio(
                    icono = Icons.Default.Group,
                    titulo = "Sin lectores",
                    descripcion = "Registra el primer lector para iniciar la cartera."
                )
                is LectorFase.ConLectores -> CarteraLectores(fase.lectores)
                is LectorFase.Error -> EstadoVacio(
                    icono = Icons.Default.ErrorOutline,
                    titulo = fase.mensaje,
                    descripcion = "Intenta recargar la cartera de lectores para continuar.",
                    esError = true,
                    accionTexto = "Reintentar",
                    onAccion = viewModel::cargarLectores
                )
            }
        }
    }
}

@Composable
private fun FormularioLectorCard(
    formulario: FormularioLector,
    registrando: Boolean,
    mensajeExito: String?,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onRegistrar: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Registrar lector", style = MaterialTheme.typography.titleLarge)
            ValidatedTextField(
                value = formulario.nombre,
                onValueChange = onNombreChange,
                label = "Nombre",
                error = formulario.errorNombre,
                modifier = Modifier.fillMaxWidth()
            )
            ValidatedTextField(
                value = formulario.correo,
                onValueChange = onCorreoChange,
                label = "Correo",
                error = formulario.errorCorreo,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )
            ValidatedTextField(
                value = formulario.telefono,
                onValueChange = onTelefonoChange,
                label = "Teléfono",
                error = formulario.errorTelefono,
                keyboardType = KeyboardType.Phone,
                modifier = Modifier.fillMaxWidth()
            )
            mensajeExito?.let { MensajeExito(it) }
            Button(
                onClick = onRegistrar,
                enabled = !registrando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (registrando) "Registrando…" else "Registrar")
            }
        }
    }
}

@Composable
private fun CargandoLectores() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CircularProgressIndicator()
        Text("Cargando lectores")
    }
}

@Composable
private fun CarteraLectores(lectores: List<LectorUi>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "${lectores.size} ${if (lectores.size == 1) "lector" else "lectores"}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        lectores.forEach { lector ->
            LectorItem(lector)
        }
    }
}

@Composable
private fun LectorItem(lector: LectorUi) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(lector.nombre, style = MaterialTheme.typography.titleMedium)
            Text(lector.correo, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Teléfono:", style = MaterialTheme.typography.bodySmall)
                Text(
                    lector.telefono,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
