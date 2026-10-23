package vista;

import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Lo común a todas las pantallas: la referencia a la ventana, el método
 * {@link #actualizar()} y ayudas para armar formularios y tablas.
 * <p>
 * Gracias a esta clase, {@link VentanaPrincipal#mostrar(String)} trata igual
 * a cualquier pantalla (polimorfismo).
 */
public abstract class Pantalla extends JPanel {

    // Mismo formato que se pide al escribir una fecha (ver Controlador)
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Ventana donde se muestra la pantalla. */
    protected VentanaPrincipal ventana;

    /**
     * @param ventana ventana principal
     */
    public Pantalla(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    /**
     * Recarga los datos que muestra la pantalla (tablas, listas, totales).
     * La ventana lo llama cada vez que la muestra.
     */
    public abstract void actualizar();

    /**
     * Crea el título grande que va arriba de cada pantalla. Está acá para que
     * todas las pantallas se vean iguales sin repetir el código.
     *
     * @param texto texto del título
     * @return la etiqueta del título
     */
    protected JLabel crearTitulo(String texto) {
        JLabel titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        return titulo;
    }

    /**
     * Agrega una fila "etiqueta + campo" a un formulario. El formulario tiene
     * que usar un {@code GridLayout} de dos columnas.
     *
     * @param formulario panel del formulario
     * @param etiqueta   texto que va a la izquierda, por ejemplo "Email:"
     * @param campo      campo de texto, combo, etc.
     */
    protected void agregarFila(JPanel formulario, String etiqueta, JComponent campo) {
        formulario.add(new JLabel(etiqueta));
        formulario.add(campo);
    }

    /**
     * Crea una tabla de solo lectura en la que se elige una fila por vez.
     * Las filas se cargan después con {@code modelo.addRow(...)}.
     *
     * @param modelo modelo de la tabla, con los nombres de las columnas
     * @return la tabla lista para poner en un {@code JScrollPane}
     */
    protected JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        // Sin editor, el usuario no puede escribir en las celdas: los datos se
        // cambian solo con los botones, que pasan por el controlador
        tabla.setDefaultEditor(Object.class, null);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tabla;
    }

    /**
     * Convierte una fecha en texto {@code dd/mm/aaaa}, el mismo formato con
     * el que se escribe en los formularios.
     *
     * @param fecha fecha a mostrar
     * @return la fecha como texto, por ejemplo "15/12/2026"
     */
    protected String formatearFecha(LocalDate fecha) {
        return fecha.format(FORMATO_FECHA);
    }
}
