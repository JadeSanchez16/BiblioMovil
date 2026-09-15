package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    fun cargarLectores() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(fase = LectorFase.Cargando)
            listarLectores()
                .onSuccess { lectores ->
                    _uiState.value = _uiState.value.copy(
                        fase = if (lectores.isEmpty()) {
                            LectorFase.SinLectores
                        } else {
                            LectorFase.ConLectores(lectores.map { it.aUi() })
                        }
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        fase = LectorFase.Error("No se pudo cargar la cartera de lectores")
                    )
                }
        }
    }

    fun onNombreChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(nombre = valor, errorNombre = null),
            mensajeExito = null
        )
    }

    fun onCorreoChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(correo = valor, errorCorreo = null),
            mensajeExito = null
        )
    }

    fun onTelefonoChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(telefono = valor, errorTelefono = null),
            mensajeExito = null
        )
    }

    fun registrar() {
        if (_uiState.value.registrando) return

        val formulario = _uiState.value.formulario
        _uiState.value = _uiState.value.copy(registrando = true, mensajeExito = null)

        viewModelScope.launch {
            registrarLector(
                nombre = formulario.nombre,
                correo = formulario.correo,
                telefono = formulario.telefono
            ).onSuccess { lector ->
                _uiState.value = _uiState.value.copy(
                    formulario = FormularioLector(),
                    registrando = false,
                    mensajeExito = "Lector \"${lector.nombre}\" registrado correctamente"
                )
                cargarLectores()
            }.onFailure { error ->
                if (error is LectorInvalidoException) {
                    _uiState.value = _uiState.value.copy(
                        formulario = _uiState.value.formulario.copy(
                            errorNombre = error.errores.nombre,
                            errorCorreo = error.errores.correo,
                            errorTelefono = error.errores.telefono
                        ),
                        registrando = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        fase = LectorFase.Error("No se pudo cargar la cartera de lectores"),
                        registrando = false
                    )
                }
            }
        }
    }
}
