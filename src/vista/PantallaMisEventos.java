package vista;

import controlador.ControladorUsuarios;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Pantalla de inicio del organizador: es su "menú" (CU02, paso 3).
 * <p>
 * Por ahora (E5) solo saluda y permite cerrar sesión. En las etapas E6 a E8
 * se agregan la tabla de eventos y los botones <i>Nuevo</i>,
 * <i>Modificar</i>, <i>Cancelar evento</i>, <i>Artistas del evento</i> y
 * <i>Gestionar artistas</i>, que usan el {@code ControladorEventos}.
 */
public class PantallaMisEventos extends Pantalla implements ActionListener {

    private ControladorUsuarios controladorUsuarios;
    private JLabel etiquetaBienvenida;
    private JButton botonCerrarSesion;

    /**
     * Arma la pantalla.
     *
     * @param ventana             ventana principal
     * @param controladorUsuarios controlador de usuarios, para cerrar sesión
     */
    public PantallaMisEventos(VentanaPrincipal ventana, ControladorUsuarios controladorUsuarios) {
        super(ventana);
        this.controladorUsuarios = controladorUsuarios;
        etiquetaBienvenida = new JLabel("", SwingConstants.CENTER);
        botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.addActionListener(this);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonCerrarSesion);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(crearTitulo("Mis eventos"), BorderLayout.NORTH);
        add(etiquetaBienvenida, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Pone el nombre del organizador que inició sesión.
     */
    @Override
    public void actualizar() {
        etiquetaBienvenida.setText("Hola, " + controladorUsuarios.getUsuarioActual().getNombre()
                + ". Entraste como organizador.");
    }

    /**
     * Se ejecuta cuando se toca un botón.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonCerrarSesion) {
            controladorUsuarios.cerrarSesion();
        }
    }
}
