package controlador;

import excepciones.SistemaException;
import java.time.LocalDate;
import java.util.List;
import modelo.Carrito;
import modelo.Evento;
import vista.VentanaPrincipal;

/**
 * Coordina los casos de uso del cliente antes de comprar: CU07 Consultar
 * eventos disponibles, CU08 Consultar disponibilidad de entradas y CU09
 * Agregar entradas al carrito (agregar, cambiar cantidad y quitar).
 * <p>
 * No tiene reglas propias: la cantidad y los ítems los controla
 * {@link Carrito}, y los lugares disponibles, {@link Evento}.
 */
public class ControladorCarrito extends Controlador {

    /**
     * @param sesion  sesión compartida
     * @param ventana ventana principal
     */
    public ControladorCarrito(Sesion sesion, VentanaPrincipal ventana) {
        super(sesion, ventana);
    }

    /**
     * Lo usa {@code PantallaEventosDisponibles} para armar la tabla (CU07).
     *
     * @return eventos activos o agotados
     */
    public List<Evento> getEventosDisponibles() {
        return sesion.getDatos().getEventosDisponibles(LocalDate.now());
    }

    /**
     * Muestra la disponibilidad de un evento (CU08). Antes actualiza el
     * estado, porque desde que se armó la tabla la fecha del evento pudo
     * haber pasado.
     *
     * @param evento evento elegido en la tabla
     */
    public void consultarDisponibilidad(Evento evento) {
        try {
            evento.actualizarEstado(LocalDate.now());
            // Si está cancelado o finalizado, lanza la excepción (2a y 2b)
            evento.verificarALaVenta();
            ventana.mostrarTexto("Disponibilidad", evento.generarDetalle());
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
            // Se recarga la tabla para que ese evento ya no aparezca
            ventana.mostrar(VentanaPrincipal.EVENTOS_DISPONIBLES);
        }
    }

    /**
     * Lo usa {@code PantallaCarrito} para mostrar los ítems y el total.
     *
     * @return el carrito del cliente que inició sesión
     */
    public Carrito getCarrito() {
        return sesion.getCliente().getCarrito();
    }

    /**
     * Agrega entradas al carrito (CU09). Si sale bien, muestra el carrito; si
     * no, muestra el error y se queda para corregir la cantidad.
     *
     * @param evento        evento elegido en la tabla
     * @param textoCantidad cantidad que escribió el cliente
     */
    public void agregarAlCarrito(Evento evento, String textoCantidad) {
        try {
            int cantidad = convertirEntero(textoCantidad, "La cantidad");
            // Carrito.agregarItem controla la cantidad (2a) y los lugares (3a)
            getCarrito().agregarItem(evento, cantidad);
            // El carrito es parte del cliente, así que se guarda con él
            sesion.guardar();
            ventana.mostrar(VentanaPrincipal.CARRITO);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Cambia la cantidad de un ítem del carrito (CU09, 5a).
     *
     * @param evento        evento del ítem elegido
     * @param textoCantidad cantidad nueva
     */
    public void cambiarCantidad(Evento evento, String textoCantidad) {
        try {
            int cantidad = convertirEntero(textoCantidad, "La cantidad");
            getCarrito().cambiarCantidad(evento, cantidad);
            sesion.guardar();
            // Se vuelve a mostrar para que se recalculen el subtotal y el total
            ventana.mostrar(VentanaPrincipal.CARRITO);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Quita un ítem del carrito (CU09, 5a).
     *
     * @param evento evento del ítem elegido
     */
    public void quitarItem(Evento evento) {
        try {
            getCarrito().quitarItem(evento);
            sesion.guardar();
            ventana.mostrar(VentanaPrincipal.CARRITO);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }
}
