package excepciones;

/**
 * No quedan entradas suficientes para la cantidad pedida.
 * <p>
 * Hereda de {@link EventoException} porque es un problema del evento.
 * Guarda cuántas entradas quedan, para que la pantalla le pueda decir al
 * cliente "quedan 3" (CU09, alternativo 3a).
 */
public class SinDisponibilidadException extends EventoException {

    /** Cantidad de entradas que todavía quedan en el evento. */
    private int disponibles;

    /**
     * Crea la excepción con el mensaje y la cantidad que queda.
     *
     * @param mensaje     explicación del error
     * @param disponibles entradas que todavía se pueden comprar
     */
    public SinDisponibilidadException(String mensaje, int disponibles) {
        super(mensaje);
        this.disponibles = disponibles;
    }

    /**
     * Devuelve cuántas entradas quedan en el evento.
     *
     * @return entradas disponibles
     */
    public int getDisponibles() {
        return disponibles;
    }
}
