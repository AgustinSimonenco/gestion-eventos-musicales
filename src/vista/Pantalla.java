package vista;

import javax.swing.JPanel;

/**
 * Lo común a todas las pantallas: la referencia a la ventana y el método
 * {@link #actualizar()}.
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
}
