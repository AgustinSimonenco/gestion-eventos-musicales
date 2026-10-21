package vista;

import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Lo común a todas las pantallas: la referencia a la ventana, el método
 * {@link #actualizar()} y dos ayudas para armar formularios.
 * <p>
 * Gracias a esta clase, {@link VentanaPrincipal#mostrar(String)} trata igual
 * a cualquier pantalla (polimorfismo).
 */
public abstract class Pantalla extends JPanel {

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
}
