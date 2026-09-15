package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.KoinContext
import pe.edu.upeu.bibliomobil.navigation.DESTINOS
import pe.edu.upeu.bibliomobil.navigation.Screen
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.prestamo.PrestamoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            var modoOscuro by rememberSaveable { mutableStateOf(false) }
            val screenSaver = Saver<Screen, String>(
                save = { it.ruta },
                restore = { Screen.desdeRuta(it) }
            )
            var pantallaActual by rememberSaveable(stateSaver = screenSaver) {
                mutableStateOf<Screen>(Screen.Inicio)
            }
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val tituloActual = DESTINOS.first { it == pantallaActual }.titulo

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "BiblioMobil",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                DESTINOS.forEach { destino ->
                                    NavigationDrawerItem(
                                        icon = { Icon(destino.icono, contentDescription = null) },
                                        label = { Text(destino.titulo) },
                                        selected = pantallaActual == destino,
                                        onClick = {
                                            pantallaActual = destino
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }
                            }
                            NavigationDrawerItem(
                                label = { Text("Modo oscuro") },
                                selected = false,
                                onClick = { modoOscuro = !modoOscuro },
                                badge = { Switch(checked = modoOscuro, onCheckedChange = { modoOscuro = it }) }
                            )
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(tituloActual) },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                                }
                            }
                        )
                    }
                ) { padding ->
                    when (pantallaActual) {
                        Screen.Inicio -> Column(modifier = Modifier.padding(padding)) {
                            InicioScreen(onNavegar = { pantallaActual = it })
                        }
                        Screen.Libros -> LibroScreen(
                            viewModel = koinViewModel(),
                            modifier = Modifier.padding(padding)
                        )
                        Screen.Lectores -> LectorScreen(
                            viewModel = koinViewModel(),
                            modifier = Modifier.padding(padding)
                        )
                        Screen.Prestamos -> PrestamoScreen(
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }
}
