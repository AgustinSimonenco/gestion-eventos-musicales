package modelo;

import excepciones.DatosInvalidosException;
import java.util.ArrayList;
import java.util.List;

/**
 * Usuario que compra entradas.
 * <p>
 * Tiene un único {@link Carrito} (composición: el carrito nace y muere con
 * el cliente) y la lista de sus compras, que es su historial.
 */
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    private Carrito carrito;
    private List<Compra> compras;

    /**
     * Crea un cliente con el carrito vacío y sin compras.
     *
     * @param nombre      nombre de la persona
     * @param email       email para iniciar sesión
     * @param contrasenia contraseña para iniciar sesión
     * @throws DatosInvalidosException si falta algún dato
     */
    public Cliente(String nombre, String email, String contrasenia) throws DatosInvalidosException {
        super(nombre, email, contrasenia);
        this.carrito = new Carrito();
        this.compras = new ArrayList<Compra>();
    }

    /**
     * @return {@code "CLIENTE"}
     */
    @Override
    public String getTipo() {
        return "CLIENTE";
    }

    /**
     * @return el carrito del cliente
     */
    public Carrito getCarrito() {
        return carrito;
    }

    /**
     * Agrega una compra al historial. El controlador la llama recién cuando
     * la compra está confirmada, así una compra a medio pagar nunca aparece.
     *
     * @param compra compra confirmada
     */
    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }

    /**
     * Devuelve una copia de la lista, para que nadie pueda agregar o borrar
     * compras desde afuera sin pasar por {@link #agregarCompra(Compra)}.
     *
     * @return compras del cliente, de la más vieja a la más nueva
     */
    public List<Compra> getCompras() {
        return new ArrayList<Compra>(compras);
    }
}
