import controlador.ControladorArtistas;
import controlador.ControladorCarrito;
import controlador.ControladorCompras;
import controlador.ControladorEventos;
import controlador.ControladorUsuarios;
import controlador.Sesion;
import excepciones.SistemaException;
import javax.swing.JOptionPane;
import persistencia.DatosDeEjemplo;
import persistencia.PersistenciaArchivo;
import persistencia.Persistencia;
import vista.PantallaArtistas;
import vista.PantallaAsignarArtistas;
import vista.PantallaCarrito;
import vista.PantallaEventosDisponibles;
import vista.PantallaFormularioEvento;
import vista.PantallaHistorial;
import vista.PantallaLogin;
import vista.PantallaMisEventos;
import vista.PantallaPago;
import vista.PantallaRegistro;
import vista.VentanaPrincipal;

/**
 * Punto de entrada del programa. Solo arma los objetos y abre la ventana.
 * <p>
 * Orden (p7): persistencia, sesión (que carga los datos), ventana,
 * controladores y pantallas.
 */
public class Main {

    /** Archivo donde se guardan todos los datos, en la carpeta desde donde se ejecuta. */
    private static final String ARCHIVO_DATOS = "datos.dat";

    /**
     * Arranca el programa.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        try {
            // Se usa la interfaz como tipo: el resto del programa no sabe que es un archivo
            Persistencia persistencia = new PersistenciaArchivo(ARCHIVO_DATOS);
            Sesion sesion = new Sesion(persistencia);
            // La primera vez no hay archivo: se arranca con los datos de ejemplo
            if (sesion.getDatos().estaVacio()) {
                DatosDeEjemplo.cargarEn(sesion.getDatos());
                sesion.guardar();
            }
            VentanaPrincipal ventana = new VentanaPrincipal();
            armarPantallas(sesion, ventana);
            ventana.mostrar(VentanaPrincipal.LOGIN);
            ventana.setVisible(true);
        } catch (SistemaException e) {
            // Sin datos no se puede trabajar: se avisa y el programa termina
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Crea los controladores y las pantallas y las registra en la ventana
    private static void armarPantallas(Sesion sesion, VentanaPrincipal ventana) {
        // Controladores: todos comparten la misma sesión y la misma ventana
        ControladorUsuarios controladorUsuarios = new ControladorUsuarios(sesion, ventana);
        ControladorEventos controladorEventos = new ControladorEventos(sesion, ventana);
        ControladorArtistas controladorArtistas = new ControladorArtistas(sesion, ventana);
        ControladorCarrito controladorCarrito = new ControladorCarrito(sesion, ventana);
        ControladorCompras controladorCompras = new ControladorCompras(sesion, ventana);

        // Pantallas: cada una recibe el controlador que usa
        ventana.agregarPantalla(VentanaPrincipal.LOGIN, new PantallaLogin(ventana, controladorUsuarios));
        ventana.agregarPantalla(VentanaPrincipal.REGISTRO, new PantallaRegistro(ventana, controladorUsuarios));
        ventana.agregarPantalla(VentanaPrincipal.MIS_EVENTOS,
                new PantallaMisEventos(ventana, controladorEventos, controladorUsuarios));
        ventana.agregarPantalla(VentanaPrincipal.FORMULARIO_EVENTO,
                new PantallaFormularioEvento(ventana, controladorEventos));
        ventana.agregarPantalla(VentanaPrincipal.ASIGNAR_ARTISTAS,
                new PantallaAsignarArtistas(ventana, controladorEventos));
        ventana.agregarPantalla(VentanaPrincipal.ARTISTAS, new PantallaArtistas(ventana, controladorArtistas));
        ventana.agregarPantalla(VentanaPrincipal.EVENTOS_DISPONIBLES,
                new PantallaEventosDisponibles(ventana, controladorCarrito, controladorUsuarios));
        ventana.agregarPantalla(VentanaPrincipal.CARRITO,
                new PantallaCarrito(ventana, controladorCarrito, controladorCompras));
        ventana.agregarPantalla(VentanaPrincipal.PAGO, new PantallaPago(ventana, controladorCompras));
        ventana.agregarPantalla(VentanaPrincipal.HISTORIAL, new PantallaHistorial(ventana, controladorCompras));
    }
}
