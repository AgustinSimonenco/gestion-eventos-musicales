package excepciones;

/**
 * Error de eventos: el evento está cancelado o finalizado, o el usuario
 * no es su organizador.
 */
public class EventoException extends SistemaException {

    /**
     * Crea la excepción con el mensaje que se le va a mostrar al usuario.
     *
     * @param mensaje explicación del error, en castellano simple
     */
    public EventoException(String mensaje) {
        super(mensaje);
    }
}
