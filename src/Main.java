import controlador.ControladorArtistas;
import controlador.ControladorEventos;
import controlador.ControladorUsuarios;
import controlador.Sesion;
import excepciones.PersistenciaException;
import javax.swing.JOptionPane;
import persistencia.PersistenciaArchivo;
import persistencia.Persistencia;
import vista.PantallaArtistas;
import vista.PantallaEventosDisponibles;
import vista.PantallaFormularioEvento;
import vista.PantallaLogin;
import vista.PantallaMisEventos;
import vista.PantallaRegistro;
import vista.VentanaPrincipal;

/**
 * Punto de entrada del programa. Solo arma los objetos y abre la ventana.
 * <p>
 * Orden (p7): persistencia, sesión (que carga los datos), ventana,
 * controladores y pantallas. Cada etapa agrega acá los controladores y las
 * pantallas de sus casos de uso.
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
            VentanaPrincipal ventana = new VentanaPrincipal();

            // Controladores: todos comparten la misma sesión y la misma ventana
            ControladorUsuarios controladorUsuarios = new ControladorUsuarios(sesion, ventana);
            ControladorEventos controladorEventos = new ControladorEventos(sesion, ventana);
            ControladorArtistas controladorArtistas = new ControladorArtistas(sesion, ventana);

            // Pantallas: cada una recibe el controlador que usa
            ventana.agregarPantalla(VentanaPrincipal.LOGIN, new PantallaLogin(ventana, controladorUsuarios));
            ventana.agregarPantalla(VentanaPrincipal.REGISTRO, new PantallaRegistro(ventana, controladorUsuarios));
            ventana.agregarPantalla(VentanaPrincipal.MIS_EVENTOS,
                    new PantallaMisEventos(ventana, controladorEventos, controladorUsuarios));
            ventana.agregarPantalla(VentanaPrincipal.FORMULARIO_EVENTO,
                    new PantallaFormularioEvento(ventana, controladorEventos));
            ventana.agregarPantalla(VentanaPrincipal.ARTISTAS, new PantallaArtistas(ventana, controladorArtistas));
            ventana.agregarPantalla(VentanaPrincipal.EVENTOS_DISPONIBLES,
                    new PantallaEventosDisponibles(ventana, controladorUsuarios));

            ventana.mostrar(VentanaPrincipal.LOGIN);
            ventana.setVisible(true);
        } catch (PersistenciaException e) {
            // Sin datos no se puede trabajar: se avisa y el programa termina
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
