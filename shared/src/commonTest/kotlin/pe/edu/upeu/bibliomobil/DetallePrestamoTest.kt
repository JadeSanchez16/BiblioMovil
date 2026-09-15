package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.DetallePrestamo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DetallePrestamoTest {
    @Test
    fun rechazaCeroDias() {
        assertFailsWith<IllegalArgumentException> {
            DetallePrestamo(libro(), dias = 0)
        }
    }

    @Test
    fun rechazaDieciseisDias() {
        assertFailsWith<IllegalArgumentException> {
            DetallePrestamo(libro(), dias = 16)
        }
    }

    @Test
    fun calculaMultaDeCuatroDiasDeRetraso() {
        val detalle = DetallePrestamo(libro(), dias = 5)

        assertEquals(6.0, detalle.multaPorRetraso(4))
    }
}
