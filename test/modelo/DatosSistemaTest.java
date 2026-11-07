package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import excepciones.ArtistaException;
import excepciones.DatosInvalidosException;
import excepciones.SistemaException;
import excepciones.UsuarioException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Reglas de {@link DatosSistema} y de {@link Usuario}: las que necesitan
 * recorrer todas las listas.
 */
public class DatosSistemaTest {

    @Test
    public void noSeRepiteElEmailSinImportarMayusculas() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        datos.registrarUsuario(new Cliente("Juan", "juan@mail.com", "1234"));
        try {
            datos.registrarUsuario(new Cliente("Otro", "JUAN@mail.com", "5678"));
            fail("Tenía que rechazar el email");
        } catch (UsuarioException e) {
            assertEquals("Ya hay un usuario registrado con ese email.", e.getMessage());
        }
    }

    @Test
    public void unEmailDebeTenerArroba() {
        try {
            new Cliente("Juan", "juan.mail.com", "1234");
            fail("Tenía que rechazar el email");
        } catch (DatosInvalidosException e) {
            assertEquals("El email no es válido.", e.getMessage());
        }
    }

    @Test
    public void autenticarDaElMismoMensajeParaEmailYContrasenia() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        Cliente juan = new Cliente("Juan", "juan@mail.com", "1234");
        datos.registrarUsuario(juan);
        assertEquals(juan, datos.autenticar("juan@mail.com", "1234"));
        String mensajeContrasenia = mensajeAlAutenticar(datos, "juan@mail.com", "mal");
        String mensajeEmail = mensajeAlAutenticar(datos, "nadie@mail.com", "1234");
        assertEquals(mensajeContrasenia, mensajeEmail);
    }

    // Devuelve el mensaje de error de un inicio de sesión que tiene que fallar
    private String mensajeAlAutenticar(DatosSistema datos, String email, String contrasenia) {
        try {
            datos.autenticar(email, contrasenia);
            fail("Tenía que rechazar el inicio de sesión");
            return null;
        } catch (UsuarioException e) {
            return e.getMessage();
        }
    }

    @Test
    public void losIdNoSeRepiten() {
        DatosSistema datos = new DatosSistema();
        assertFalse(datos.generarId() == datos.generarId());
    }

    @Test
    public void noSeRepiteElNombreDeUnArtista() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        datos.agregarArtista(new Artista(1, "Soda Stereo", "Rock"));
        try {
            datos.agregarArtista(new Artista(2, "SODA stereo", "Pop"));
            fail("Tenía que rechazar el nombre");
        } catch (ArtistaException e) {
            assertEquals(1, datos.getArtistas().size());
        }
    }

    @Test
    public void unArtistaPuedeConservarSuNombreAlModificarse() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        Artista soda = new Artista(1, "Soda Stereo", "Rock");
        datos.agregarArtista(soda);
        datos.modificarArtista(soda, "Soda Stereo", "Pop");
        assertEquals("Pop", soda.getGeneroMusical());
    }

    @Test
    public void noSeBorraUnArtistaDeUnEventoVigente() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        Evento evento = eventoEnDatos(datos);
        Artista artista = evento.getArtistas().get(0);
        try {
            datos.eliminarArtista(artista);
            fail("Tenía que rechazar la baja");
        } catch (ArtistaException e) {
            assertEquals(1, datos.getArtistas().size());
        }
        evento.cancelar();
        datos.eliminarArtista(artista);
        assertTrue(datos.getArtistas().isEmpty());
    }

    @Test
    public void alClienteSeLeMuestranActivosYAgotados() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        Evento evento = eventoEnDatos(datos);
        LocalDate hoy = LocalDate.now();
        assertEquals(1, datos.getEventosDisponibles(hoy).size());
        evento.ocuparLugares(evento.getCapacidad());
        assertEquals(1, datos.getEventosDisponibles(hoy).size());
        evento.cancelar();
        assertTrue(datos.getEventosDisponibles(hoy).isEmpty());
    }

    @Test
    public void unEventoPasadoNoSeMuestra() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        Evento evento = eventoEnDatos(datos);
        assertTrue(datos.getEventosDisponibles(Fabrica.FECHA_EVENTO.plusDays(1)).isEmpty());
        assertEquals(EstadoEvento.FINALIZADO, evento.getEstado());
    }

    @Test
    public void cadaOrganizadorVeSusEventos() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        Evento evento = eventoEnDatos(datos);
        assertEquals(1, datos.getEventosDe(evento.getOrganizador()).size());
        assertTrue(datos.getEventosDe(new Organizador("Beto", "beto@mail.com", "1234")).isEmpty());
    }

    // Crea un artista y un evento y los agrega a los datos
    private Evento eventoEnDatos(DatosSistema datos) throws SistemaException {
        Artista artista = new Artista(datos.generarId(), "Soda Stereo", "Rock");
        datos.agregarArtista(artista);
        List<Artista> artistas = new ArrayList<Artista>();
        artistas.add(artista);
        Evento evento = new Evento(datos.generarId(), "Recital", Fabrica.FECHA_EVENTO, "Luna Park",
                10, 100, Fabrica.organizador(), artistas);
        datos.agregarEvento(evento);
        return evento;
    }
}
