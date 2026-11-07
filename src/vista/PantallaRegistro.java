package vista;

import controlador.ControladorUsuarios;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Alta de un usuario nuevo, cliente u organizador (CU01).
 * <p>
 * No valida nada: le pasa los textos al {@link ControladorUsuarios}, y el
 * modelo controla que no falten datos y que el email no esté repetido.
 */
public class PantallaRegistro extends Pantalla implements ActionListener {

    private ControladorUsuarios controlador;
    private JTextField campoNombre;
    private JTextField campoEmail;
    private JPasswordField campoContrasenia;
    private JComboBox<String> comboTipo;
    private JButton botonRegistrar;
    private JButton botonVolver;

    /**
     * Arma la pantalla con sus campos y botones.
     *
     * @param ventana     ventana principal
     * @param controlador controlador de usuarios
     */
    public PantallaRegistro(VentanaPrincipal ventana, ControladorUsuarios controlador) {
        super(ventana);
        this.controlador = controlador;
        campoNombre = new JTextField(20);
        campoEmail = new JTextField(20);
        campoContrasenia = new JPasswordField(20);
        // Se usan las constantes del controlador para que el texto coincida
        String[] tipos = {ControladorUsuarios.TIPO_CLIENTE, ControladorUsuarios.TIPO_ORGANIZADOR};
        comboTipo = new JComboBox<String>(tipos);
        botonRegistrar = new JButton("Registrar");
        botonVolver = new JButton("Volver");

        botonRegistrar.addActionListener(this);
        botonVolver.addActionListener(this);

        armar();
    }

    // Ubica los componentes: título arriba, formulario al medio y botones abajo
    private void armar() {
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        agregarFila(formulario, "Nombre:", campoNombre);
        agregarFila(formulario, "Email:", campoEmail);
        agregarFila(formulario, "Contraseña:", campoContrasenia);
        agregarFila(formulario, "Tipo de usuario:", comboTipo);

        JPanel centro = new JPanel(new FlowLayout());
        centro.add(formulario);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonRegistrar);
        botones.add(botonVolver);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        add(crearTitulo("Registrarse"), BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Deja el formulario vacío, con "Cliente" elegido, cada vez que se llega.
     */
    @Override
    public void actualizar() {
        campoNombre.setText("");
        campoEmail.setText("");
        campoContrasenia.setText("");
        comboTipo.setSelectedIndex(0);
    }

    /**
     * Se ejecuta cuando se toca <i>Registrar</i> o <i>Volver</i>.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonRegistrar) {
            String contrasenia = new String(campoContrasenia.getPassword());
            String tipo = (String) comboTipo.getSelectedItem();
            controlador.registrar(campoNombre.getText(), campoEmail.getText(), contrasenia, tipo);
        } else if (e.getSource() == botonVolver) {
            ventana.mostrar(VentanaPrincipal.LOGIN);
        }
    }
}
