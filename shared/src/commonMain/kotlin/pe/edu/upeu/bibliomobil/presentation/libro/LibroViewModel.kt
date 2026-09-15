package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    fun cargarLibros() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(fase = LibroFase.Cargando)
            listarLibros()
                .onSuccess { libros ->
                    _uiState.value = _uiState.value.copy(
                        fase = if (libros.isEmpty()) {
                            LibroFase.SinLibros
                        } else {
                            LibroFase.ConLibros(libros.map { it.aUi() })
                        }
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        fase = LibroFase.Error("No se pudo cargar el catálogo")
                    )
                }
        }
    }

    fun onTituloChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(titulo = valor, errorTitulo = null),
            mensajeExito = null
        )
    }

    fun onAutorChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(autor = valor, errorAutor = null),
            mensajeExito = null
        )
    }

    fun onAnioChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(anio = valor, errorAnio = null),
            mensajeExito = null
        )
    }

    fun onEjemplaresChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(ejemplares = valor, errorEjemplares = null),
            mensajeExito = null
        )
    }

    fun registrar() {
        if (_uiState.value.registrando) return

        val formulario = _uiState.value.formulario
        _uiState.value = _uiState.value.copy(registrando = true, mensajeExito = null)

        viewModelScope.launch {
            registrarLibro(
                titulo = formulario.titulo,
                autor = formulario.autor,
                anio = formulario.anio,
                ejemplares = formulario.ejemplares
            ).onSuccess { libro ->
                _uiState.value = _uiState.value.copy(
                    formulario = FormularioLibro(),
                    registrando = false,
                    mensajeExito = "Libro \"${libro.titulo}\" registrado correctamente"
                )
                cargarLibros()
            }.onFailure { error ->
                if (error is LibroInvalidoException) {
                    _uiState.value = _uiState.value.copy(
                        formulario = _uiState.value.formulario.copy(
                            errorTitulo = error.errores.titulo,
                            errorAutor = error.errores.autor,
                            errorAnio = error.errores.anio,
                            errorEjemplares = error.errores.ejemplares
                        ),
                        registrando = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        fase = LibroFase.Error("No se pudo cargar el catálogo"),
                        registrando = false
                    )
                }
            }
        }
    }
}
