package modelo;

/**
 * Estados posibles de una {@link Entrada}.
 */
public enum EstadoEntrada {
    /** Sirve para entrar al evento. */
    VALIDA,
    /** Se anuló porque se canceló la compra. */
    ANULADA
}
