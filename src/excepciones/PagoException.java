package excepciones;

/**
 * El pago fue rechazado o no se pudo procesar.
 */
public class PagoException extends SistemaException {

    /**
     * Crea la excepción con el mensaje que se le va a mostrar al usuario.
     *
     * @param mensaje explicación del error, en castellano simple
     */
    public PagoException(String mensaje) {
        super(mensaje);
    }
}
