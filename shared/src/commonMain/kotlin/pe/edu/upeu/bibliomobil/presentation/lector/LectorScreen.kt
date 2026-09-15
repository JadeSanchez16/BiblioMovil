package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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
                LectorFase.SinLectores -> Text(
                    text = "Aún no hay lectores registrados",
                    style = MaterialTheme.typography.bodyLarge
                )
                is LectorFase.ConLectores -> CarteraLectores(fase.lectores)
                is LectorFase.Error -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = fase.mensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(onClick = viewModel::cargarLectores) {
                        Text("Reintentar")
                    }
                }
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
            OutlinedTextField(
                value = formulario.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre") },
                isError = formulario.errorNombre != null,
                supportingText = formulario.errorNombre?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = formulario.correo,
                onValueChange = onCorreoChange,
                label = { Text("Correo") },
                isError = formulario.errorCorreo != null,
                supportingText = formulario.errorCorreo?.let { { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = formulario.telefono,
                onValueChange = onTelefonoChange,
                label = { Text("Teléfono") },
                isError = formulario.errorTelefono != null,
                supportingText = formulario.errorTelefono?.let { { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            mensajeExito?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
