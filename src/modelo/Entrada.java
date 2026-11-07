package modelo;

import java.io.Serializable;

/**
 * Derecho de acceso a un evento. Tiene un código único formado por el número
 * de compra y el número de entrada dentro de la compra (por ejemplo "15-3").
 */
public class Entrada implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;
    private double precio;
    private EstadoEntrada estado;
    private Evento evento;

    /**
     * Crea una entrada válida. Solo la crea {@link Compra} (GRASP Creador).
     *
     * @param codigo código único
     * @param precio precio pagado por la entrada
     * @param evento evento al que da acceso
     */
    public Entrada(String codigo, double precio, Evento evento) {
        this.codigo = codigo;
        this.precio = precio;
        this.evento = evento;
        this.estado = EstadoEntrada.VALIDA;
    }

    /**
     * Anula la entrada. Se llama al cancelar la compra (CU14).
     */
    public void anular() {
        estado = EstadoEntrada.ANULADA;
    }

    /** @return {@code true} si la entrada sirve para entrar */
    public boolean estaValida() {
        return estado == EstadoEntrada.VALIDA;
    }

    /** @return evento al que da acceso */
    public Evento getEvento() {
        return evento;
    }

    /** @return código único de la entrada */
    public String getCodigo() {
        return codigo;
    }

    /** @return precio pagado por la entrada */
    public double getPrecio() {
        return precio;
    }
}
