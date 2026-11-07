package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import excepciones.CompraException;
import excepciones.DatosInvalidosException;
import excepciones.EventoException;
import excepciones.SinDisponibilidadException;
import excepciones.SistemaException;
import org.junit.jupiter.api.Test;

/**
 * Reglas de {@link Carrito}: cantidades, disponibilidad y total.
 */
public class CarritoTest {

    @Test
    public void calculaSubtotalesYTotal() throws SistemaException {
        Carrito carrito = new Carrito();
        carrito.agregarItem(Fabrica.evento(10, 100), 2);
        carrito.agregarItem(Fabrica.evento(10, 50.5), 3);
        assertEquals(351.5, carrito.getTotal(), 0.001);
    }

    @Test
    public void agregarNoOcupaLugaresEnElEvento() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        new Carrito().agregarItem(evento, 4);
        assertEquals(10, evento.getEntradasDisponibles());
    }

    @Test
    public void elMismoEventoSumaEnElMismoItem() throws SistemaException {
        Carrito carrito = new Carrito();
        Evento evento = Fabrica.evento(10, 100);
        carrito.agregarItem(evento, 2);
        carrito.agregarItem(evento, 3);
        assertEquals(1, carrito.getItems().size());
        assertEquals(5, carrito.getItems().get(0).getCantidad());
    }

    @Test
    public void noSeEsquivaElLimiteAgregandoDeAPoco() throws SistemaException {
        Carrito carrito = new Carrito();
        Evento evento = Fabrica.evento(5, 100);
        carrito.agregarItem(evento, 3);
        try {
            carrito.agregarItem(evento, 3);
            fail("Tenía que rechazar la suma");
        } catch (SinDisponibilidadException e) {
            assertEquals(3, carrito.getItems().get(0).getCantidad());
        }
    }

    @Test
    public void unaCantidadEnormeNoDesbordaLaSuma() throws SistemaException {
        Carrito carrito = new Carrito();
        Evento evento = Fabrica.evento(5, 100);
        carrito.agregarItem(evento, 1);
        try {
            carrito.agregarItem(evento, Integer.MAX_VALUE);
            fail("Tenía que rechazar la cantidad");
        } catch (SinDisponibilidadException e) {
            assertEquals(1, carrito.getItems().get(0).getCantidad());
        }
    }

    @Test
    public void laCantidadTieneQueSerMayorACero() throws SistemaException {
        Carrito carrito = new Carrito();
        try {
            carrito.agregarItem(Fabrica.evento(5, 100), 0);
            fail("Tenía que rechazar el 0");
        } catch (DatosInvalidosException e) {
            assertTrue(carrito.estaVacio());
        }
    }

    @Test
    public void cambiarCantidadVerificaLaDisponibilidad() throws SistemaException {
        Carrito carrito = new Carrito();
        Evento evento = Fabrica.evento(5, 100);
        carrito.agregarItem(evento, 1);
        carrito.cambiarCantidad(evento, 5);
        assertEquals(500, carrito.getTotal(), 0.001);
        try {
            carrito.cambiarCantidad(evento, 6);
            fail("Tenía que rechazar la cantidad");
        } catch (SinDisponibilidadException e) {
            assertEquals(500, carrito.getTotal(), 0.001);
        }
    }

    @Test
    public void quitarUnItem() throws SistemaException {
        Carrito carrito = new Carrito();
        Evento evento = Fabrica.evento(5, 100);
        carrito.agregarItem(evento, 1);
        carrito.quitarItem(evento);
        assertTrue(carrito.estaVacio());
    }

    @Test
    public void noSeQuitaUnEventoQueNoEsta() throws SistemaException {
        try {
            new Carrito().quitarItem(Fabrica.evento(5, 100));
            fail("Tenía que avisar que no está");
        } catch (CompraException e) {
            assertEquals("Ese evento no está en el carrito.", e.getMessage());
        }
    }

    @Test
    public void noSeConfirmaUnCarritoVacio() {
        try {
            new Carrito().verificarDisponibilidad();
            fail("Tenía que rechazar el carrito vacío");
        } catch (SistemaException e) {
            assertEquals("El carrito está vacío.", e.getMessage());
        }
    }

    @Test
    public void siCancelanElEventoElCarritoNoPasaLaVerificacion() throws SistemaException {
        Carrito carrito = new Carrito();
        Evento evento = Fabrica.evento(5, 100);
        carrito.agregarItem(evento, 1);
        evento.cancelar();
        try {
            carrito.verificarDisponibilidad();
            fail("Tenía que rechazar el evento cancelado");
        } catch (EventoException e) {
            assertTrue(e.getMessage().contains("fue cancelado"));
        }
    }
}
