package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import excepciones.ArtistaException;
import excepciones.DatosInvalidosException;
import excepciones.EventoException;
import excepciones.SinDisponibilidadException;
import excepciones.SistemaException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/**
 * Reglas de {@link Evento}: validaciones, capacidad, artistas y estados.
 * Para probar que algo falla se usa try/fail/catch: si no se lanza la
 * excepción, se llega al fail y el test no pasa.
 */
public class EventoTest {

    @Test
    public void unEventoNuevoEstaActivoYConTodosLosLugares() throws SistemaException {
        Evento evento = Fabrica.evento(100, 1500);
        assertEquals(EstadoEvento.ACTIVO, evento.getEstado());
        assertEquals(100, evento.getEntradasDisponibles());
    }

    @Test
    public void noSeCreaConFechaDeHoy() throws SistemaException {
        try {
            new Evento(1, "Recital", LocalDate.now(), "Luna Park", 10, 100, Fabrica.organizador(),
                    Fabrica.evento(1, 1).getArtistas());
            fail("Tenía que rechazar la fecha");
        } catch (DatosInvalidosException e) {
            assertEquals("La fecha del evento tiene que ser futura.", e.getMessage());
        }
    }

    @Test
    public void noSeCreaSinArtistas() throws SistemaException {
        try {
            new Evento(1, "Recital", Fabrica.FECHA_EVENTO, "Luna Park", 10, 100, Fabrica.organizador(),
                    new ArrayList<Artista>());
            fail("Tenía que pedir un artista");
        } catch (DatosInvalidosException e) {
            assertEquals("Elegí al menos un artista para el evento.", e.getMessage());
        }
    }

    @Test
    public void noSeCreaConCapacidadCero() throws SistemaException {
        try {
            Fabrica.evento(0, 100);
            fail("Tenía que rechazar la capacidad");
        } catch (DatosInvalidosException e) {
            assertEquals("La capacidad tiene que ser mayor a 0.", e.getMessage());
        }
    }

    @Test
    public void noSeCreaConPrecioNegativo() throws SistemaException {
        try {
            Fabrica.evento(10, -1);
            fail("Tenía que rechazar el precio");
        } catch (DatosInvalidosException e) {
            assertEquals("El precio no puede ser negativo.", e.getMessage());
        }
    }

    @Test
    public void noSeVendeMasQueLaCapacidad() throws SistemaException {
        Evento evento = Fabrica.evento(5, 100);
        evento.ocuparLugares(3);
        try {
            evento.ocuparLugares(3);
            fail("Tenía que rechazar la venta");
        } catch (SinDisponibilidadException e) {
            // La excepción trae cuántas quedan, para poder informarlo (CU09, 3a)
            assertEquals(2, e.getDisponibles());
        }
        assertEquals(2, evento.getEntradasDisponibles());
    }

    @Test
    public void alVenderTodoQuedaAgotadoYAlLiberarVuelveAActivo() throws SistemaException {
        Evento evento = Fabrica.evento(2, 100);
        evento.ocuparLugares(2);
        assertEquals(EstadoEvento.AGOTADO, evento.getEstado());
        evento.liberarLugares(1);
        assertEquals(EstadoEvento.ACTIVO, evento.getEstado());
    }

    @Test
    public void laCapacidadNoBajaDeLoVendido() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        evento.ocuparLugares(5);
        try {
            evento.modificar("Recital", Fabrica.FECHA_EVENTO, "Luna Park", 3, 100);
            fail("Tenía que rechazar la capacidad");
        } catch (EventoException e) {
            assertEquals(10, evento.getCapacidad());
        }
    }

    @Test
    public void subirLaCapacidadDeUnAgotadoLoVuelveActivo() throws SistemaException {
        Evento evento = Fabrica.evento(2, 100);
        evento.ocuparLugares(2);
        evento.modificar("Recital", Fabrica.FECHA_EVENTO, "Luna Park", 5, 100);
        assertEquals(EstadoEvento.ACTIVO, evento.getEstado());
    }

    @Test
    public void unEventoCanceladoNoSeModificaNiSeVuelveACancelar() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        evento.cancelar();
        try {
            evento.modificar("Otro", Fabrica.FECHA_EVENTO, "Luna Park", 10, 100);
            fail("Tenía que rechazar la modificación");
        } catch (EventoException e) {
            assertEquals("No se puede modificar un evento cancelado.", e.getMessage());
        }
        try {
            evento.cancelar();
            fail("Tenía que rechazar la cancelación");
        } catch (EventoException e) {
            assertEquals("No se puede cancelar un evento cancelado.", e.getMessage());
        }
    }

    @Test
    public void unEventoCanceladoNoTieneEntradasALaVenta() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        evento.cancelar();
        try {
            evento.verificarDisponibilidad(1);
            fail("Tenía que rechazar la venta");
        } catch (EventoException e) {
            assertEquals("El evento Recital fue cancelado y no tiene entradas a la venta.", e.getMessage());
        }
    }

    @Test
    public void pasadaLaFechaQuedaFinalizado() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        evento.actualizarEstado(Fabrica.FECHA_EVENTO);
        assertEquals(EstadoEvento.ACTIVO, evento.getEstado());
        evento.actualizarEstado(Fabrica.FECHA_EVENTO.plusDays(1));
        assertEquals(EstadoEvento.FINALIZADO, evento.getEstado());
        assertFalse(evento.estaVigente());
    }

    @Test
    public void unEventoCanceladoNoPasaAFinalizado() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        evento.cancelar();
        evento.actualizarEstado(Fabrica.FECHA_EVENTO.plusDays(1));
        assertEquals(EstadoEvento.CANCELADO, evento.getEstado());
    }

    @Test
    public void unArtistaNoSeAsignaDosVeces() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        Artista repetido = evento.getArtistas().get(0);
        try {
            evento.asignarArtista(repetido);
            fail("Tenía que rechazar el repetido");
        } catch (ArtistaException e) {
            assertEquals(1, evento.getArtistas().size());
        }
    }

    @Test
    public void noSeQuitaElUltimoArtista() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        Artista ultimo = evento.getArtistas().get(0);
        try {
            evento.quitarArtista(ultimo);
            fail("Tenía que rechazar quitar el último");
        } catch (ArtistaException e) {
            assertTrue(evento.tieneArtista(ultimo));
        }
    }

    @Test
    public void aUnEventoCanceladoNoSeLeCambianLosArtistas() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        evento.cancelar();
        try {
            evento.asignarArtista(new Artista(9, "Charly García", "Rock"));
            fail("Tenía que rechazar el cambio");
        } catch (EventoException e) {
            assertEquals(1, evento.getArtistas().size());
        }
    }

    @Test
    public void elDetalleDiceAgotadoSiNoQuedanEntradas() throws SistemaException {
        Evento evento = Fabrica.evento(1, 100);
        evento.ocuparLugares(1);
        assertTrue(evento.generarDetalle().contains("0 (AGOTADO)"));
    }

    @Test
    public void soloElOrganizadorDelEventoLoPuedeCambiar() throws SistemaException {
        Evento evento = Fabrica.evento(10, 100);
        try {
            evento.verificarOrganizador(new Organizador("Beto", "beto@mail.com", "1234"));
            fail("Tenía que rechazar a otro organizador");
        } catch (EventoException e) {
            assertEquals("Solo el organizador del evento lo puede cambiar.", e.getMessage());
        }
    }
}
