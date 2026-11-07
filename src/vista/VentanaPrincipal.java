package vista;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * La única ventana del programa.
 * <p>
 * Usa un {@link CardLayout}: todas las pantallas están cargadas como
 * "tarjetas" y se muestra una por vez. Así no se abren y cierran ventanas y
 * la navegación queda en un solo lugar.
 * <p>
 * También tiene los métodos para hablar con el usuario ({@code JOptionPane}),
 * así ningún controlador usa Swing directamente.
 */
public class VentanaPrincipal extends JFrame {

    // Nombres de las pantallas. Son constantes para que, si se escribe mal un
    // nombre, lo detecte el compilador.
    public static final String LOGIN = "LOGIN";
    public static final String REGISTRO = "REGISTRO";
    public static final String MIS_EVENTOS = "MIS_EVENTOS";
    public static final String FORMULARIO_EVENTO = "FORMULARIO_EVENTO";
    public static final String ASIGNAR_ARTISTAS = "ASIGNAR_ARTISTAS";
    public static final String ARTISTAS = "ARTISTAS";
    public static final String EVENTOS_DISPONIBLES = "EVENTOS_DISPONIBLES";
    public static final String CARRITO = "CARRITO";
    public static final String PAGO = "PAGO";
    public static final String HISTORIAL = "HISTORIAL";

    private CardLayout tarjetas;
    private JPanel contenedor;
    private Map<String, Pantalla> pantallas;

    /**
     * Crea la ventana vacía. Las pantallas se agregan después con
     * {@link #agregarPantalla(String, Pantalla)}.
     */
    public VentanaPrincipal() {
        super("Gestión de Eventos Musicales");
        tarjetas = new CardLayout();
        contenedor = new JPanel(tarjetas);
        pantallas = new HashMap<String, Pantalla>();
        add(contenedor);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(900, 600));
        // null: centrar la ventana en la pantalla de la computadora
        setLocationRelativeTo(null);
    }

    /**
     * Registra una pantalla para poder mostrarla después.
     *
     * @param nombre   una de las constantes de esta clase
     * @param pantalla la pantalla
     */
    public void agregarPantalla(String nombre, Pantalla pantalla) {
        pantallas.put(nombre, pantalla);
        contenedor.add(pantalla, nombre);
    }

    /**
     * Muestra una pantalla. Antes le pide que recargue sus datos.
     *
     * @param nombre una de las constantes de esta clase
     */
    public void mostrar(String nombre) {
        Pantalla pantalla = pantallas.get(nombre);
        // Polimorfismo: cada pantalla sabe cómo actualizarse
        pantalla.actualizar();
        tarjetas.show(contenedor, nombre);
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param mensaje texto a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje explicación del error
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un texto largo en un área con barra de desplazamiento. Se usa
     * para el comprobante (CU12).
     *
     * @param titulo título del diálogo
     * @param texto  texto a mostrar
     */
    public void mostrarTexto(String titulo, String texto) {
        JTextArea area = new JTextArea(texto, 18, 50);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), titulo, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Hace una pregunta de sí o no.
     *
     * @param pregunta texto de la pregunta
     * @return {@code true} si el usuario eligió "Sí"
     */
    public boolean confirmar(String pregunta) {
        int respuesta = JOptionPane.showConfirmDialog(this, pregunta, "Confirmar", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
}
