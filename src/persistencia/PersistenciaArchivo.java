package persistencia;

import excepciones.PersistenciaException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import modelo.DatosSistema;

/**
 * Guarda todos los datos en un único archivo usando la serialización de Java
 * (D-001). Se escribe el {@link DatosSistema} entero, y con él todos los
 * objetos a los que apunta: usuarios, eventos, compras, entradas, pagos.
 * <p>
 * La ventaja es que las referencias entre objetos se conservan solas: una
 * entrada sigue apuntando al mismo evento después de cargar.
 */
public class PersistenciaArchivo implements Persistencia {

    private String rutaArchivo;

    /**
     * @param rutaArchivo ruta del archivo de datos, por ejemplo "datos.dat"
     */
    public PersistenciaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Lee el archivo. Si no existe (primera vez que se abre el programa),
     * devuelve datos vacíos.
     *
     * @return los datos guardados
     * @throws PersistenciaException si el archivo existe pero no se puede leer
     */
    @Override
    public DatosSistema cargar() throws PersistenciaException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return new DatosSistema();
        }
        // "try con recursos": Java cierra el archivo solo al terminar, aunque haya error
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            return (DatosSistema) entrada.readObject();
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo de datos " + rutaArchivo + ".", e);
        } catch (ClassNotFoundException e) {
            throw new PersistenciaException("El archivo de datos " + rutaArchivo + " no es válido.", e);
        }
    }

    /**
     * Escribe todos los datos en el archivo, reemplazando lo que había.
     *
     * @param datos datos a guardar
     * @throws PersistenciaException si no se puede escribir
     */
    @Override
    public void guardar(DatosSistema datos) throws PersistenciaException {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            salida.writeObject(datos);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudieron guardar los datos en " + rutaArchivo + ".", e);
        }
    }
}
