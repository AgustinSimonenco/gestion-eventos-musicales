package excepciones;

/**
 * Excepción base de todos los errores de negocio del sistema.
 * <p>
 * Nunca se lanza sola: siempre se lanza una de sus subclases. Sirve para que
 * un controlador pueda capturar cualquier error del modelo con un único
 * {@code catch (SistemaException e)} y mostrar el mensaje al usuario.
 * <p>
 * Es una excepción verificada (hereda de {@link Exception}): el compilador
 * obliga a capturarla o a declararla con {@code throws}.
 */
public class SistemaException extends Exception {

    /**
     * Crea la excepción con el mensaje que se le va a mostrar al usuario.
     *
     * @param mensaje explicación del error, en castellano simple
     */
    public SistemaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción guardando también la excepción original que la causó.
     * La usa {@link PersistenciaException} para envolver una {@code IOException}.
     *
     * @param mensaje explicación del error
     * @param causa   excepción original
     */
    public SistemaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
