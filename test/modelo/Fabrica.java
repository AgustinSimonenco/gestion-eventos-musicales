package modelo;

import excepciones.SistemaException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Crea los objetos que usan los tests, para no repetir el mismo código en
 * cada uno.
 */
public class Fabrica {

    /** Fecha de todos los eventos de prueba: dentro de 30 días. */
    public static final LocalDate FECHA_EVENTO = LocalDate.now().plusDays(30);

    /**
     * @return un organizador de prueba
     * @throws SistemaException si los datos no son válidos
     */
    public static Organizador organizador() throws SistemaException {
        return new Organizador("Ana", "ana@mail.com", "1234");
    }

    /**
     * @return un cliente de prueba
     * @throws SistemaException si los datos no son válidos
     */
    public static Cliente cliente() throws SistemaException {
        return new Cliente("Juan", "juan@mail.com", "1234");
    }

    /**
     * @param capacidad capacidad del evento
     * @param precio    precio de la entrada
     * @return un evento activo con un artista
     * @throws SistemaException si los datos no son válidos
     */
    public static Evento evento(int capacidad, double precio) throws SistemaException {
        List<Artista> artistas = new ArrayList<Artista>();
        artistas.add(new Artista(1, "Soda Stereo", "Rock"));
        return new Evento(2, "Recital", FECHA_EVENTO, "Luna Park", capacidad, precio, organizador(), artistas);
    }

    /**
     * @param monto monto a pagar
     * @return una tarjeta que se aprueba
     * @throws SistemaException si los datos no son válidos
     */
    public static PagoTarjeta tarjetaValida(double monto) throws SistemaException {
        return new PagoTarjeta(monto, "4509123412341234", "Juan", 12, 2099, "123", 3);
    }

    /**
     * Hace una compra completa y pagada de un evento.
     *
     * @param cliente  quién compra
     * @param evento   evento
     * @param cantidad entradas
     * @return la compra confirmada
     * @throws SistemaException si algo falla
     */
    public static Compra compraConfirmada(Cliente cliente, Evento evento, int cantidad)
            throws SistemaException {
        Carrito carrito = cliente.getCarrito();
        carrito.agregarItem(evento, cantidad);
        Compra compra = new Compra(15, cliente, carrito.getTotal());
        compra.registrarPago(tarjetaValida(carrito.getTotal()));
        compra.confirmar(carrito);
        carrito.vaciar();
        return compra;
    }
}
