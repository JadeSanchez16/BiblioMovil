package pe.edu.upeu.bibliomobil

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.di.dataModule
import pe.edu.upeu.bibliomobil.di.domainModule
import pe.edu.upeu.bibliomobil.di.presentationModule
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class AppModuleTest {
    @BeforeTest
    fun limpiarAntes() {
        stopKoin()
    }

    @AfterTest
    fun limpiarDespues() {
        stopKoin()
    }

    @Test
    fun libroRepositorySeResuelveComoRepositorioEnMemoriaUnico() {
        val koin = startKoin { modules(dataModule, domainModule, presentationModule) }.koin

        val primero = koin.get<LibroRepository>()
        val segundo = koin.get<LibroRepository>()

        assertIs<LibroRepositorioEnMemoria>(primero)
        assertSame(primero, segundo)
    }

    @Test
    fun resuelveLosCuatroCasosDeUso() {
        val koin = startKoin { modules(dataModule, domainModule, presentationModule) }.koin

        koin.get<RegistrarLibroUseCase>()
        koin.get<RegistrarLectorUseCase>()
        koin.get<ListarLibrosUseCase>()
        koin.get<ListarLectoresUseCase>()
    }
}
