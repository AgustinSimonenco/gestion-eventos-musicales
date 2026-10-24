package controlador;

import excepciones.SistemaException;
import java.time.LocalDate;
import java.util.List;
import modelo.Evento;
import vista.VentanaPrincipal;

/**
 * Coordina los casos de uso del cliente antes de comprar: CU07 Consultar
 * eventos disponibles y CU08 Consultar disponibilidad de entradas. En la E9
 * se agrega el CU09 Agregar entradas al carrito (agregar, cambiar cantidad y
 * quitar).
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
     * @return eventos activos, con entradas para vender
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
}
