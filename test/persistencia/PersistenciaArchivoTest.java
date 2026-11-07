package persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import excepciones.PersistenciaException;
import excepciones.SistemaException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import modelo.Cliente;
import modelo.DatosSistema;
import modelo.Evento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Guardar y cargar el archivo de datos, y los datos de ejemplo.
 */
public class PersistenciaArchivoTest {

    private File archivo;

    @BeforeEach
    public void crearNombreDeArchivo() throws IOException {
        archivo = File.createTempFile("datos-test", ".dat");
        archivo.delete();
    }

    @AfterEach
    public void borrarArchivo() {
        archivo.delete();
    }

    @Test
    public void siNoHayArchivoDevuelveDatosVacios() throws SistemaException {
        assertTrue(new PersistenciaArchivo(archivo.getPath()).cargar().estaVacio());
    }

    @Test
    public void losDatosDeEjemploPasanLasValidaciones() throws SistemaException {
        DatosSistema datos = new DatosSistema();
        DatosDeEjemplo.cargarEn(datos);
        Cliente juan = (Cliente) datos.autenticar("juan@demo.com", "1234");
        assertEquals(3, juan.getCompras().size());
        // Hay cuatro eventos, pero uno está cancelado y no se muestra
        assertEquals(3, datos.getEventosDisponibles(LocalDate.now()).size());
    }

    @Test
    public void guardaYCargaConservandoLasReferencias() throws SistemaException {
        Persistencia persistencia = new PersistenciaArchivo(archivo.getPath());
        DatosSistema datos = new DatosSistema();
        DatosDeEjemplo.cargarEn(datos);
        persistencia.guardar(datos);

        DatosSistema cargados = persistencia.cargar();
        Cliente juan = (Cliente) cargados.autenticar("juan@demo.com", "1234");
        Evento deLaCompra = juan.getCompras().get(0).getEntradas().get(0).getEvento();
        List<Evento> eventos = cargados.getEventosDisponibles(LocalDate.now());
        assertEquals("Rock en el Luna", deLaCompra.getNombre());
        // Tiene que ser el mismo objeto, no una copia
        assertSame(eventos.get(0), deLaCompra);
    }

    @Test
    public void unArchivoRotoDaPersistenciaException() throws IOException {
        FileWriter escritor = new FileWriter(archivo);
        escritor.write("esto no son datos");
        escritor.close();
        try {
            new PersistenciaArchivo(archivo.getPath()).cargar();
            fail("Tenía que avisar que el archivo no se puede leer");
        } catch (PersistenciaException e) {
            assertTrue(e.getMessage().contains("No se pudo leer"));
        }
    }
}
