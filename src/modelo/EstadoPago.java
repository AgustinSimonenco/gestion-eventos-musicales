package modelo;

/**
 * Estados posibles de un pago ({@link MedioDePago}).
 */
public enum EstadoPago {
    /** Se creó y todavía no se procesó. */
    PENDIENTE,
    /** Se procesó y se aprobó. */
    APROBADO,
    /** Se procesó y se rechazó. */
    RECHAZADO
}
