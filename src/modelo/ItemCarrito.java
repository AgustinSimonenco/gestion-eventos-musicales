package modelo;

import java.io.Serializable;

/**
 * Una línea del carrito: un evento y la cantidad de entradas elegidas.
 */
public class ItemCarrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private Evento evento;
    private int cantidad;

    /**
     * Crea un ítem. Solo lo crea {@link Carrito}, que ya validó la cantidad.
     *
     * @param evento   evento elegido
     * @param cantidad cantidad de entradas
     */
    public ItemCarrito(Evento evento, int cantidad) {
        this.evento = evento;
        this.cantidad = cantidad;
    }

    /**
     * Calcula el subtotal. Lo hace el ítem porque tiene el evento (con el
     * precio) y la cantidad: GRASP Experto en información.
     *
     * @return precio de la entrada por la cantidad
     */
    public double getSubtotal() {
        return evento.getPrecioEntrada() * cantidad;
    }

    /** @return evento del ítem */
    public Evento getEvento() {
        return evento;
    }

    /** @return cantidad de entradas */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Cambia la cantidad. Solo lo llama {@link Carrito}, después de validar.
     *
     * @param cantidad cantidad nueva
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
