package excepciones;

/**
 * Error de compras: carrito vacío, compra ya cancelada, evento ya realizado
 * o comprobante sin pago aprobado.
 */
public class CompraException extends SistemaException {

    /**
     * Crea la excepción con el mensaje que se le va a mostrar al usuario.
     *
     * @param mensaje explicación del error, en castellano simple
     */
    public CompraException(String mensaje) {
        super(mensaje);
    }
}
