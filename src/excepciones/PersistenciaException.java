package excepciones;

/**
 * No se pudo leer o escribir el archivo de datos.
 * <p>
 * Envuelve la excepción original de Java (por ejemplo, una
 * {@code IOException}) para que los controladores solo tengan que capturar
 * excepciones del sistema.
 */
public class PersistenciaException extends SistemaException {

    /**
     * Crea la excepción con el mensaje y la excepción original.
     *
     * @param mensaje explicación del error
     * @param causa   excepción original de Java
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
