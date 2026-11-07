package excepciones;

/**
 * Error de usuarios: email ya registrado o credenciales incorrectas.
 */
public class UsuarioException extends SistemaException {

    /**
     * Crea la excepción con el mensaje que se le va a mostrar al usuario.
     *
     * @param mensaje explicación del error, en castellano simple
     */
    public UsuarioException(String mensaje) {
        super(mensaje);
    }
}
