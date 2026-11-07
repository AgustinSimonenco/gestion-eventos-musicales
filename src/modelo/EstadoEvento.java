package modelo;

/**
 * Estados posibles de un {@link Evento}.
 * <p>
 * Se usa un enum y no un {@code String} para que sea imposible poner un
 * estado inválido: el compilador solo acepta estos cuatro valores.
 */
public enum EstadoEvento {
    /** Se pueden comprar entradas. */
    ACTIVO,
    /** No quedan entradas, pero el evento todavía no pasó. */
    AGOTADO,
    /** El organizador lo canceló. */
    CANCELADO,
    /** La fecha del evento ya pasó. */
    FINALIZADO
}
