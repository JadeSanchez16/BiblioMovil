package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
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
fun LibroScreen(
    viewModel: LibroViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.cargarLibros()
    }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FormularioLibroCard(
                formulario = uiState.formulario,
                registrando = uiState.registrando,
                mensajeExito = uiState.mensajeExito,
                onTituloChange = viewModel::onTituloChange,
                onAutorChange = viewModel::onAutorChange,
                onAnioChange = viewModel::onAnioChange,
                onEjemplaresChange = viewModel::onEjemplaresChange,
                onRegistrar = viewModel::registrar
            )
        }

        item {
            when (val fase = uiState.fase) {
                LibroFase.Cargando -> CargandoLibros()
                LibroFase.SinLibros -> Text(
                    text = "Aún no hay libros registrados",
                    style = MaterialTheme.typography.bodyLarge
                )
                is LibroFase.ConLibros -> CatalogoLibros(fase.libros)
                is LibroFase.Error -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = fase.mensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(onClick = viewModel::cargarLibros) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}

@Composable
private fun FormularioLibroCard(
    formulario: FormularioLibro,
    registrando: Boolean,
    mensajeExito: String?,
    onTituloChange: (String) -> Unit,
    onAutorChange: (String) -> Unit,
    onAnioChange: (String) -> Unit,
    onEjemplaresChange: (String) -> Unit,
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
            Text("Registrar libro", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = formulario.titulo,
                onValueChange = onTituloChange,
                label = { Text("Título") },
                isError = formulario.errorTitulo != null,
                supportingText = formulario.errorTitulo?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = formulario.autor,
                onValueChange = onAutorChange,
                label = { Text("Autor") },
                isError = formulario.errorAutor != null,
                supportingText = formulario.errorAutor?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = formulario.anio,
                    onValueChange = onAnioChange,
                    label = { Text("Año") },
                    isError = formulario.errorAnio != null,
                    supportingText = formulario.errorAnio?.let { { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = formulario.ejemplares,
                    onValueChange = onEjemplaresChange,
                    label = { Text("Ejemplares") },
                    isError = formulario.errorEjemplares != null,
                    supportingText = formulario.errorEjemplares?.let { { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
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
private fun CargandoLibros() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CircularProgressIndicator()
        Text("Cargando catálogo")
    }
}

@Composable
private fun CatalogoLibros(libros: List<LibroUi>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "${libros.size} ${if (libros.size == 1) "libro" else "libros"}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        libros.forEach { libro ->
            LibroItem(libro)
        }
    }
}

@Composable
private fun LibroItem(libro: LibroUi) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
            Text(libro.autor, style = MaterialTheme.typography.bodyMedium)
            Text(
                libro.detalle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            if (libro.mostrarBadgeReposicion) {
                Spacer(Modifier.height(2.dp))
                AssistChip(
                    onClick = {},
                    label = { Text(libro.badgeReposicion) }
                )
            }
        }
    }
}
