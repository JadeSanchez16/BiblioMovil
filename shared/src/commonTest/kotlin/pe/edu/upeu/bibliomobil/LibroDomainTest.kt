package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.DetallePrestamo
import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibroDomainTest {
    @Test
    fun requiereReposicionSoloConMenosDeTresEjemplares() {
        assertTrue(libro(ejemplares = 2).requiereReposicion)
        assertFalse(libro(ejemplares = 3).requiereReposicion)
    }

    @Test
    fun rechazaTituloVacio() {
        assertFailsWith<IllegalArgumentException> { libro(titulo = " ") }
    }

    @Test
    fun rechazaAnioFueraDeRango() {
        assertFailsWith<IllegalArgumentException> { libro(anio = Libro.ANIO_MINIMO - 1) }
        assertFailsWith<IllegalArgumentException> { libro(anio = Libro.ANIO_MAXIMO + 1) }
    }

    @Test
    fun rechazaEjemplaresNegativos() {
        assertFailsWith<IllegalArgumentException> { libro(ejemplares = -1) }
    }

    @Test
    fun detalleCalculaMultaSinValidarRetrasoAdicional() {
        val detalle = DetallePrestamo(libro(), dias = 5)
        assertEquals(4.5, detalle.multaPorRetraso(3))
    }

    @Test
    fun detalleRechazaDiasFueraDelRangoPermitido() {
        assertFailsWith<IllegalArgumentException> { DetallePrestamo(libro(), dias = 16) }
    }
}
