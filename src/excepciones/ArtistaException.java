package excepciones;

/**
 * Error de artistas: artista ya existente o ya asignado, intento de quitar
 * el último artista de un evento, o baja de un artista que está en un evento
 * que todavía no pasó.
 */
public class ArtistaException extends SistemaException {

    /**
     * Crea la excepción con el mensaje que se le va a mostrar al usuario.
     *
     * @param mensaje explicación del error, en castellano simple
     */
    public ArtistaException(String mensaje) {
        super(mensaje);
    }
}
