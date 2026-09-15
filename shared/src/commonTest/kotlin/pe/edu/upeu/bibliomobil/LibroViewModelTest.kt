package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.presentation.libro.LibroFase
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class LibroViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun configurarMain() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun restaurarMain() {
        Dispatchers.resetMain()
    }

    @Test
    fun arrancaEnSinLibros() {
        val viewModel = viewModel(FakeLibroRepository())

        assertIs<LibroFase.SinLibros>(viewModel.uiState.value.fase)
    }

    @Test
    fun muestraDetalleDeLibroConFormatoExacto() = runTest {
        val repository = FakeLibroRepository()
        repository.registrar(libro(anio = 1998, ejemplares = 3))
        val viewModel = viewModel(repository)

        viewModel.cargarLibros()
        advanceUntilIdle()

        val fase = assertIs<LibroFase.ConLibros>(viewModel.uiState.value.fase)
        assertEquals("1998 · 3 ejemplares", fase.libros.first().detalle)
    }

    @Test
    fun muestraSingularParaUnEjemplar() = runTest {
        val repository = FakeLibroRepository()
        repository.registrar(libro(anio = 1998, ejemplares = 1))
        val viewModel = viewModel(repository)

        viewModel.cargarLibros()
        advanceUntilIdle()

        val fase = assertIs<LibroFase.ConLibros>(viewModel.uiState.value.fase)
        assertEquals("1998 · 1 ejemplar", fase.libros.first().detalle)
    }

    @Test
    fun pasaAErrorSiListadoFalla() = runTest {
        val repository = FakeLibroRepository().apply { fallaListar = true }
        val viewModel = viewModel(repository)

        viewModel.cargarLibros()
        advanceUntilIdle()

        val fase = assertIs<LibroFase.Error>(viewModel.uiState.value.fase)
        assertEquals("No se pudo cargar el catálogo", fase.mensaje)
    }

    @Test
    fun erroresDeValidacionCaenEnFormularioSinCambiarFaseAError() = runTest {
        val viewModel = viewModel(FakeLibroRepository())

        viewModel.registrar()
        advanceUntilIdle()

        assertEquals("El título es obligatorio", viewModel.uiState.value.formulario.errorTitulo)
        assertIs<LibroFase.SinLibros>(viewModel.uiState.value.fase)
    }

    @Test
    fun onCampoChangeBorraSoloElErrorDelCampo() = runTest {
        val viewModel = viewModel(FakeLibroRepository())
        viewModel.registrar()
        advanceUntilIdle()

        viewModel.onTituloChange("Clean Code")

        assertNull(viewModel.uiState.value.formulario.errorTitulo)
        assertEquals("El autor es obligatorio", viewModel.uiState.value.formulario.errorAutor)
    }

    @Test
    fun registrarLimpiaFormularioYRecargaCatalogo() = runTest {
        val viewModel = viewModel(FakeLibroRepository())
        viewModel.onTituloChange("Clean Code")
        viewModel.onAutorChange("Robert Martin")
        viewModel.onAnioChange("1998")
        viewModel.onEjemplaresChange("3")

        viewModel.registrar()
        advanceUntilIdle()

        assertEquals("", viewModel.uiState.value.formulario.titulo)
        assertEquals("Libro \"Clean Code\" registrado correctamente", viewModel.uiState.value.mensajeExito)
        assertEquals(1, assertIs<LibroFase.ConLibros>(viewModel.uiState.value.fase).libros.size)
    }

    @Test
    fun dobleTapNoDuplicaRegistro() = runTest {
        val repository = FakeLibroRepository()
        val viewModel = viewModel(repository)
        viewModel.onTituloChange("Clean Code")
        viewModel.onAutorChange("Robert Martin")
        viewModel.onAnioChange("1998")
        viewModel.onEjemplaresChange("3")

        viewModel.registrar()
        viewModel.registrar()
        advanceUntilIdle()

        assertEquals(1, repository.registros)
    }

    private fun viewModel(repository: FakeLibroRepository) =
        LibroViewModel(
            registrarLibro = RegistrarLibroUseCase(repository),
            listarLibros = ListarLibrosUseCase(repository)
        )
}
