package controlador;

import excepciones.SistemaException;
import java.time.LocalDate;
import java.util.List;
import modelo.Artista;
import modelo.DatosSistema;
import modelo.Evento;
import modelo.Organizador;
import vista.VentanaPrincipal;

/**
 * Coordina los casos de uso de eventos del organizador: CU03 Crear evento,
 * CU04 Modificar evento y CU05 Cancelar evento. En la E8 se agrega el CU06
 * Asignar artistas.
 * <p>
 * Recuerda el evento en uso ({@code eventoSeleccionado}) porque el CU04
 * pasa por dos pantallas: se elige en "Mis eventos" y se edita en el
 * formulario.
 */
public class ControladorEventos extends Controlador {

    // Evento que se está modificando. Si es null, el formulario está en
    // modo "crear" (p7, "Estado entre pantallas").
    private Evento eventoSeleccionado;

    /**
     * @param sesion  sesión compartida
     * @param ventana ventana principal
     */
    public ControladorEventos(Sesion sesion, VentanaPrincipal ventana) {
        super(sesion, ventana);
        this.eventoSeleccionado = null;
    }

    /**
     * Lo usa {@code PantallaMisEventos} para armar la tabla.
     *
     * @return eventos del organizador que inició sesión
     */
    public List<Evento> getMisEventos() {
        return sesion.getDatos().getEventosDe(sesion.getOrganizador());
    }

    /**
     * Lo usa el formulario para mostrar la lista de artistas a elegir (CU03, paso 2).
     *
     * @return artistas del catálogo
     */
    public List<Artista> getArtistas() {
        return sesion.getDatos().getArtistas();
    }

    /**
     * @return el evento que se está modificando, o {@code null} si se está creando uno
     */
    public Evento getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    /**
     * Abre el formulario vacío para crear un evento (CU03).
     */
    public void nuevoEvento() {
        eventoSeleccionado = null;
        ventana.mostrar(VentanaPrincipal.FORMULARIO_EVENTO);
    }

    /**
     * Abre el formulario con los datos del evento elegido (CU04, paso 1).
     * Antes controla que se pueda modificar, así no se completa un formulario
     * para nada.
     *
     * @param evento evento elegido en la tabla
     */
    public void elegirParaModificar(Evento evento) {
        try {
            evento.verificarOrganizador(sesion.getOrganizador());
            evento.verificarVigente("modificar");
            eventoSeleccionado = evento;
            ventana.mostrar(VentanaPrincipal.FORMULARIO_EVENTO);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Crea un evento nuevo en estado ACTIVO (CU03).
     *
     * @param nombre    nombre del evento
     * @param fecha     fecha en formato dd/mm/aaaa
     * @param lugar     lugar del evento
     * @param capacidad texto con la cantidad máxima de entradas
     * @param precio    texto con el precio de cada entrada
     * @param artistas  artistas elegidos en la lista
     */
    public void crearEvento(String nombre, String fecha, String lugar, String capacidad,
            String precio, List<Artista> artistas) {
        try {
            // Si un texto no se puede convertir, se lanza la excepción acá mismo
            LocalDate fechaEvento = convertirFecha(fecha);
            int capacidadEvento = convertirEntero(capacidad, "La capacidad");
            double precioEntrada = convertirDecimal(precio, "El precio");
            DatosSistema datos = sesion.getDatos();
            Organizador organizador = sesion.getOrganizador();
            // El constructor de Evento valida todo (3a): si algo está mal,
            // el evento no llega a existir
            Evento evento = new Evento(datos.generarId(), nombre, fechaEvento, lugar,
                    capacidadEvento, precioEntrada, organizador, artistas);
            datos.agregarEvento(evento);
            sesion.guardar();
            ventana.mostrarMensaje("Evento creado.");
            ventana.mostrar(VentanaPrincipal.MIS_EVENTOS);
        } catch (SistemaException e) {
            // Se queda en el formulario con lo que había escrito
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Guarda los cambios del evento seleccionado (CU04, pasos 2 y 3).
     *
     * @param nombre    nombre nuevo
     * @param fecha     fecha nueva en formato dd/mm/aaaa
     * @param lugar     lugar nuevo
     * @param capacidad texto con la capacidad nueva
     * @param precio    texto con el precio nuevo
     */
    public void modificarEvento(String nombre, String fecha, String lugar, String capacidad,
            String precio) {
        try {
            LocalDate fechaEvento = convertirFecha(fecha);
            int capacidadEvento = convertirEntero(capacidad, "La capacidad");
            double precioEntrada = convertirDecimal(precio, "El precio");
            // Evento.modificar valida los datos y que la capacidad no quede
            // por debajo de lo vendido
            eventoSeleccionado.modificar(nombre, fechaEvento, lugar, capacidadEvento, precioEntrada);
            sesion.guardar();
            ventana.mostrarMensaje("Se guardaron los cambios del evento.");
            ventana.mostrar(VentanaPrincipal.MIS_EVENTOS);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Cancela un evento (CU05). Primero controla que se pueda cancelar, así
     * no se pregunta "¿seguro?" por algo que después va a fallar.
     *
     * @param evento evento elegido en la tabla
     */
    public void cancelarEvento(Evento evento) {
        try {
            evento.verificarOrganizador(sesion.getOrganizador());
            evento.verificarVigente("cancelar");
            if (!ventana.confirmar("¿Cancelar el evento " + evento.getNombre()
                    + "? Ya no se van a poder comprar entradas.")) {
                return;
            }
            evento.cancelar();
            sesion.guardar();
            ventana.mostrarMensaje("El evento " + evento.getNombre() + " quedó cancelado.");
            // Se vuelve a mostrar para que la tabla diga CANCELADO
            ventana.mostrar(VentanaPrincipal.MIS_EVENTOS);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }
}
