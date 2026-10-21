package vista;

import controlador.ControladorUsuarios;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Primera pantalla del programa: email y contraseña (CU02).
 * <p>
 * No tiene lógica: cuando se toca <i>Ingresar</i>, le pasa los textos al
 * {@link ControladorUsuarios}, que decide a qué pantalla se va.
 */
public class PantallaLogin extends Pantalla implements ActionListener {

    private ControladorUsuarios controlador;
    private JTextField campoEmail;
    private JPasswordField campoContrasenia;
    private JButton botonIngresar;
    private JButton botonRegistrarse;

    /**
     * Arma la pantalla con sus campos y botones.
     *
     * @param ventana     ventana principal
     * @param controlador controlador de usuarios
     */
    public PantallaLogin(VentanaPrincipal ventana, ControladorUsuarios controlador) {
        super(ventana);
        this.controlador = controlador;
        campoEmail = new JTextField(20);
        campoContrasenia = new JPasswordField(20);
        botonIngresar = new JButton("Ingresar");
        botonRegistrarse = new JButton("Registrarse");

        // La pantalla escucha los botones (Observer de Swing). El campo de la
        // contraseña también, para poder ingresar apretando Enter.
        botonIngresar.addActionListener(this);
        botonRegistrarse.addActionListener(this);
        campoContrasenia.addActionListener(this);

        armar();
    }

    // Ubica los componentes: título arriba, formulario al medio y botones abajo
    private void armar() {
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        agregarFila(formulario, "Email:", campoEmail);
        agregarFila(formulario, "Contraseña:", campoContrasenia);

        // El FlowLayout deja el formulario de su tamaño, sin estirarlo
        JPanel centro = new JPanel(new FlowLayout());
        centro.add(formulario);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonIngresar);
        botones.add(botonRegistrarse);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        add(crearTitulo("Gestión de Eventos Musicales"), BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Deja los campos vacíos cada vez que se llega al login (por ejemplo,
     * después de cerrar sesión).
     */
    @Override
    public void actualizar() {
        campoEmail.setText("");
        campoContrasenia.setText("");
    }

    /**
     * Se ejecuta cuando se toca un botón o se aprieta Enter en la contraseña.
     *
     * @param e evento de Swing, dice qué componente se usó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonIngresar || e.getSource() == campoContrasenia) {
            // getPassword() devuelve un arreglo de char: se convierte a String
            String contrasenia = new String(campoContrasenia.getPassword());
            controlador.iniciarSesion(campoEmail.getText(), contrasenia);
        } else if (e.getSource() == botonRegistrarse) {
            // Solo cambia de pantalla, no hay datos que procesar
            ventana.mostrar(VentanaPrincipal.REGISTRO);
        }
    }
}
