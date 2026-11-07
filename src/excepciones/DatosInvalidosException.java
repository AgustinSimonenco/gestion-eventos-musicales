package excepciones;

/**
 * Un dato ingresado tiene mal formato: campo vacío, número negativo,
 * fecha pasada, tarjeta con dígitos de menos, etc.
 */
public class DatosInvalidosException extends SistemaException {

    /**
     * Crea la excepción con el mensaje que se le va a mostrar al usuario.
     *
     * @param mensaje explicación del error, en castellano simple
     */
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
