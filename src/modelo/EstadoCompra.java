package modelo;

/**
 * Estados posibles de una {@link Compra}.
 */
public enum EstadoCompra {
    /** Se creó al confirmar el carrito y todavía no tiene un pago aprobado. */
    PENDIENTE,
    /** Se pagó: tiene entradas y comprobante. */
    CONFIRMADA,
    /** El cliente la canceló: sus entradas están anuladas. */
    CANCELADA
}
