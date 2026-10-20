import controlador.Sesion;
import excepciones.PersistenciaException;
import javax.swing.JOptionPane;
import persistencia.PersistenciaArchivo;
import persistencia.Persistencia;
import vista.VentanaPrincipal;

/**
 * Punto de entrada del programa. Solo arma los objetos y abre la ventana.
 * <p>
 * Orden (p7): persistencia, sesión (que carga los datos), ventana,
 * controladores y pantallas. Los controladores y las pantallas se agregan
 * desde la etapa E5.
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
            // E5: acá se crean los controladores con (sesion, ventana) y las pantallas
            ventana.setVisible(true);
        } catch (PersistenciaException e) {
            // Sin datos no se puede trabajar: se avisa y el programa termina
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
