package modelo;

import excepciones.CompraException;
import excepciones.DatosInvalidosException;
import excepciones.EventoException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Selección provisoria de entradas que el cliente todavía no compró.
 * <p>
 * Agregar al carrito no ocupa lugares en el evento: eso pasa recién al
 * pagar ({@link Compra#confirmar(Carrito)}).
 */
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ItemCarrito> items;

    /**
     * Crea un carrito vacío.
     */
    public Carrito() {
        this.items = new ArrayList<ItemCarrito>();
    }

    /**
     * Agrega entradas de un evento (CU09). Si el evento ya está en el
     * carrito, suma la cantidad en el mismo ítem.
     *
     * @param evento   evento elegido
     * @param cantidad entradas a agregar, mayor a 0
     * @throws DatosInvalidosException si la cantidad no es mayor a 0
     * @throws EventoException         si el evento no tiene lugar para la suma
     */
    public void agregarItem(Evento evento, int cantidad) throws DatosInvalidosException, EventoException {
        validarCantidad(cantidad);
        ItemCarrito item = buscarItem(evento);
        int total = cantidad;
        if (item != null) {
            total = total + item.getCantidad();
        }
        // CU09 incluye CU08: se verifica la cantidad total, no solo lo nuevo
        evento.verificarDisponibilidad(total);
        if (item == null) {
            items.add(new ItemCarrito(evento, total));
        } else {
            item.setCantidad(total);
        }
    }

    /**
     * Cambia la cantidad de un ítem (CU09, alternativo 5a).
     *
     * @param evento   evento del ítem
     * @param cantidad cantidad nueva, mayor a 0
     * @throws DatosInvalidosException si la cantidad no es mayor a 0
     * @throws CompraException         si el evento no está en el carrito
     * @throws EventoException         si el evento no tiene lugar para esa cantidad
     */
    public void cambiarCantidad(Evento evento, int cantidad)
            throws DatosInvalidosException, CompraException, EventoException {
        validarCantidad(cantidad);
        ItemCarrito item = buscarItemExistente(evento);
        evento.verificarDisponibilidad(cantidad);
        item.setCantidad(cantidad);
    }

    /**
     * Quita un ítem del carrito.
     *
     * @param evento evento del ítem
     * @throws CompraException si el evento no está en el carrito
     */
    public void quitarItem(Evento evento) throws CompraException {
        items.remove(buscarItemExistente(evento));
    }

    /**
     * Verifica que el carrito no esté vacío y que todos los eventos sigan
     * teniendo lugar (CU10). Se hace de nuevo porque, desde que se agregó un
     * ítem, el organizador pudo haber cancelado el evento.
     *
     * @throws CompraException si el carrito está vacío
     * @throws EventoException si algún evento ya no tiene lugar
     */
    public void verificarDisponibilidad() throws CompraException, EventoException {
        if (estaVacio()) {
            throw new CompraException("El carrito está vacío.");
        }
        for (ItemCarrito item : items) {
            item.getEvento().verificarDisponibilidad(item.getCantidad());
        }
    }

    /**
     * @return suma de los subtotales de todos los ítems
     */
    public double getTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total = total + item.getSubtotal();
        }
        return total;
    }

    /**
     * Devuelve una copia, para que los ítems solo se cambien con los métodos
     * del carrito.
     *
     * @return ítems del carrito
     */
    public List<ItemCarrito> getItems() {
        return new ArrayList<ItemCarrito>(items);
    }

    /** @return {@code true} si no tiene ítems */
    public boolean estaVacio() {
        return items.isEmpty();
    }

    /**
     * Saca todos los ítems. Se llama después de pagar.
     */
    public void vaciar() {
        items.clear();
    }

    // Busca el ítem de un evento; devuelve null si no está
    private ItemCarrito buscarItem(Evento evento) {
        for (ItemCarrito item : items) {
            if (item.getEvento() == evento) {
                return item;
            }
        }
        return null;
    }

    // Igual que buscarItem, pero si no está avisa con una excepción
    private ItemCarrito buscarItemExistente(Evento evento) throws CompraException {
        ItemCarrito item = buscarItem(evento);
        if (item == null) {
            throw new CompraException("Ese evento no está en el carrito.");
        }
        return item;
    }

    private void validarCantidad(int cantidad) throws DatosInvalidosException {
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad tiene que ser mayor a 0.");
        }
    }
}
