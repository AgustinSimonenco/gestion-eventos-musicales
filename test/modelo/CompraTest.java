package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import excepciones.CompraException;
import excepciones.PagoException;
import excepciones.SistemaException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Reglas de {@link Compra}: pago, confirmación, comprobante y cancelación.
 */
public class CompraTest {

    @Test
    public void confirmarOcupaLugaresYCreaUnaEntradaPorUnidad() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        Compra compra = Fabrica.compraConfirmada(Fabrica.cliente(), evento, 3);
        assertEquals(EstadoCompra.CONFIRMADA, compra.getEstado());
        assertEquals(7, evento.getEntradasDisponibles());
        assertEquals(3, compra.getEntradas().size());
        assertEquals("15-3", compra.getEntradas().get(2).getCodigo());
    }

    @Test
    public void sinPagoAprobadoNoSeConfirma() throws SistemaException {
        Cliente cliente = Fabrica.cliente();
        cliente.getCarrito().agregarItem(Fabrica.evento(10, 100), 1);
        Compra compra = new Compra(15, cliente, 100);
        try {
            compra.confirmar(cliente.getCarrito());
            fail("Tenía que rechazar la confirmación");
        } catch (CompraException e) {
            assertEquals(EstadoCompra.PENDIENTE, compra.getEstado());
        }
    }

    @Test
    public void unPagoRechazadoDejaLaCompraPendienteYSePuedeReintentar() throws SistemaException {
        Compra compra = new Compra(15, Fabrica.cliente(), 100);
        MedioDePago rechazado = new PagoTarjeta(100, "4509123412340000", "Juan", 12, 2099, "123", 1);
        try {
            compra.registrarPago(rechazado);
            fail("Tenía que rechazar el pago");
        } catch (PagoException e) {
            assertEquals(EstadoPago.RECHAZADO, rechazado.getEstado());
        }
        assertEquals(EstadoCompra.PENDIENTE, compra.getEstado());
        assertFalse(compra.tienePagoAprobado());
        compra.registrarPago(Fabrica.tarjetaValida(100));
        assertTrue(compra.tienePagoAprobado());
    }

    @Test
    public void sinPagoAprobadoNoHayComprobante() throws SistemaException {
        Compra compra = new Compra(15, Fabrica.cliente(), 100);
        try {
            compra.generarComprobante();
            fail("Tenía que rechazar el comprobante");
        } catch (CompraException e) {
            assertTrue(e.getMessage().contains("pago aprobado"));
        }
    }

    @Test
    public void elComprobanteEsSiempreElMismoYTieneLosDatosDelCU12() throws SistemaException {
        Compra compra = Fabrica.compraConfirmada(Fabrica.cliente(), Fabrica.evento(10, 100), 2);
        Comprobante comprobante = compra.generarComprobante();
        assertSame(comprobante, compra.generarComprobante());
        assertEquals("C-15", comprobante.getNumero());
        String texto = comprobante.generarTexto();
        assertTrue(texto.contains("juan@mail.com"));
        assertTrue(texto.contains("Luna Park"));
        assertTrue(texto.contains("15-1 15-2"));
        assertTrue(texto.contains("terminada en 1234"));
        // El número completo de la tarjeta nunca aparece
        assertFalse(texto.contains("4509"));
    }

    @Test
    public void cancelarAnulaLasEntradasYLiberaLosLugares() throws SistemaException {
        Evento evento = Fabrica.evento(2, 100);
        Compra compra = Fabrica.compraConfirmada(Fabrica.cliente(), evento, 2);
        assertEquals(EstadoEvento.AGOTADO, evento.getEstado());
        compra.cancelar(LocalDate.now());
        assertEquals(EstadoCompra.CANCELADA, compra.getEstado());
        assertFalse(compra.getEntradas().get(0).estaValida());
        assertEquals(2, evento.getEntradasDisponibles());
        assertEquals(EstadoEvento.ACTIVO, evento.getEstado());
        // El total no se borra: es el registro del reintegro (D-006)
        assertEquals(200, compra.getTotal(), 0.001);
    }

    @Test
    public void noSeCancelaDosVeces() throws SistemaException {
        Compra compra = Fabrica.compraConfirmada(Fabrica.cliente(), Fabrica.evento(10, 100), 1);
        compra.cancelar(LocalDate.now());
        try {
            compra.cancelar(LocalDate.now());
            fail("Tenía que rechazar la segunda cancelación");
        } catch (CompraException e) {
            assertEquals("La compra ya estaba cancelada.", e.getMessage());
        }
    }

    @Test
    public void elDiaDelEventoYaNoSeCancela() throws SistemaException {
        Compra compra = Fabrica.compraConfirmada(Fabrica.cliente(), Fabrica.evento(10, 100), 1);
        try {
            compra.verificarCancelable(Fabrica.FECHA_EVENTO);
            fail("Tenía que rechazar la cancelación");
        } catch (CompraException e) {
            assertEquals(EstadoCompra.CONFIRMADA, compra.getEstado());
        }
        compra.verificarCancelable(Fabrica.FECHA_EVENTO.minusDays(1));
    }

    @Test
    public void unaCompraDeUnEventoCanceladoSePuedeCancelar() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        Compra compra = Fabrica.compraConfirmada(Fabrica.cliente(), evento, 1);
        evento.cancelar();
        assertTrue(compra.getNombresEventos().contains("(CANCELADO)"));
        compra.cancelar(LocalDate.now());
        assertEquals(EstadoCompra.CANCELADA, compra.getEstado());
        assertEquals(EstadoEvento.CANCELADO, evento.getEstado());
    }
}
