package persistencia;

import excepciones.PersistenciaException;
import modelo.DatosSistema;

/**
 * Forma de cargar y guardar los datos del sistema.
 * <p>
 * Los controladores (a través de {@code Sesion}) solo conocen esta interfaz:
 * no saben si los datos van a un archivo, a una base de datos o a otro lado.
 * Así se puede cambiar cómo se guarda sin tocar el resto (extensibilidad).
 */
public interface Persistencia {

    /**
     * Lee los datos guardados.
     *
     * @return los datos; si todavía no hay nada guardado, datos vacíos
     * @throws PersistenciaException si no se pudieron leer
     */
    DatosSistema cargar() throws PersistenciaException;

    /**
     * Guarda todos los datos.
     *
     * @param datos datos a guardar
     * @throws PersistenciaException si no se pudieron escribir
     */
    void guardar(DatosSistema datos) throws PersistenciaException;
}
