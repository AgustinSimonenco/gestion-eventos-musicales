package vista;

import controlador.ControladorCompras;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Pago de la compra en curso (CU11): muestra el monto y pide los datos del
 * medio elegido, tarjeta o transferencia.
 * <p>
 * Tiene los dos formularios y muestra solo el del medio elegido. La pantalla
 * no valida nada: le pasa los textos al controlador y el modelo los controla.
 */
public class PantallaPago extends Pantalla implements ActionListener {

    private ControladorCompras controlador;
    private JLabel etiquetaMonto;
    private JRadioButton opcionTarjeta;
    private JRadioButton opcionTransferencia;
    private JPanel panelTarjeta;
    private JPanel panelTransferencia;
    private JTextField campoNumeroTarjeta;
    private JTextField campoTitular;
    private JTextField campoMesVencimiento;
    private JTextField campoAnioVencimiento;
    private JPasswordField campoCodigo;
    private JComboBox<String> comboCuotas;
    private JTextField campoCbu;
    private JTextField campoOperacion;
    private JButton botonPagar;
    private JButton botonCancelarPago;

    /**
     * Arma la pantalla.
     *
     * @param ventana     ventana principal
     * @param controlador controlador de compras
     */
    public PantallaPago(VentanaPrincipal ventana, ControladorCompras controlador) {
        super(ventana);
        this.controlador = controlador;
        etiquetaMonto = new JLabel("", SwingConstants.CENTER);
        crearOpciones();
        crearCampos();
        botonPagar = new JButton("Pagar");
        botonCancelarPago = new JButton("Cancelar pago");
        botonPagar.addActionListener(this);
        botonCancelarPago.addActionListener(this);
        armar();
    }

    // Los dos botones de opción van en un ButtonGroup: elegir uno apaga el otro
    private void crearOpciones() {
        opcionTarjeta = new JRadioButton("Tarjeta");
        opcionTransferencia = new JRadioButton("Transferencia");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(opcionTarjeta);
        grupo.add(opcionTransferencia);
        opcionTarjeta.addActionListener(this);
        opcionTransferencia.addActionListener(this);
    }

    private void crearCampos() {
        campoNumeroTarjeta = new JTextField(20);
        campoTitular = new JTextField(20);
        campoMesVencimiento = new JTextField(20);
        campoAnioVencimiento = new JTextField(20);
        campoCodigo = new JPasswordField(20);
        comboCuotas = new JComboBox<String>(new String[] {"1", "3", "6", "12"});
        campoCbu = new JTextField(24);
        campoOperacion = new JTextField(24);
    }

    private void armarFormularios() {
        panelTarjeta = new JPanel(new GridLayout(0, 2, 8, 8));
        agregarFila(panelTarjeta, "Número de tarjeta (16 dígitos):", campoNumeroTarjeta);
        agregarFila(panelTarjeta, "Titular:", campoTitular);
        agregarFila(panelTarjeta, "Mes de vencimiento:", campoMesVencimiento);
        agregarFila(panelTarjeta, "Año de vencimiento:", campoAnioVencimiento);
        agregarFila(panelTarjeta, "Código de seguridad:", campoCodigo);
        agregarFila(panelTarjeta, "Cuotas:", comboCuotas);

        panelTransferencia = new JPanel(new GridLayout(0, 2, 8, 8));
        agregarFila(panelTransferencia, "CBU de origen (22 dígitos):", campoCbu);
        agregarFila(panelTransferencia, "Número de operación:", campoOperacion);
    }

    // Arriba título y monto; al medio las opciones y los formularios; abajo los botones
    private void armar() {
        armarFormularios();
        JPanel opciones = new JPanel(new FlowLayout());
        opciones.add(new JLabel("Medio de pago:"));
        opciones.add(opcionTarjeta);
        opciones.add(opcionTransferencia);

        JPanel formularios = new JPanel(new FlowLayout());
        formularios.add(panelTarjeta);
        formularios.add(panelTransferencia);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(opciones, BorderLayout.NORTH);
        centro.add(formularios, BorderLayout.CENTER);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(crearTitulo("Pago"), BorderLayout.NORTH);
        arriba.add(etiquetaMonto, BorderLayout.SOUTH);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonPagar);
        botones.add(botonCancelarPago);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(arriba, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Muestra el monto a pagar y deja los campos vacíos, con tarjeta elegida.
     */
    @Override
    public void actualizar() {
        etiquetaMonto.setText("Monto a pagar: " + formatearPrecio(controlador.getMontoAPagar()));
        campoNumeroTarjeta.setText("");
        campoTitular.setText("");
        campoMesVencimiento.setText("");
        campoAnioVencimiento.setText("");
        campoCodigo.setText("");
        comboCuotas.setSelectedIndex(0);
        campoCbu.setText("");
        campoOperacion.setText("");
        opcionTarjeta.setSelected(true);
        mostrarFormularioElegido();
    }

    /**
     * Se ejecuta cuando se elige un medio de pago o se toca un botón.
     *
     * @param e evento de Swing, dice qué componente se usó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonPagar) {
            pagar();
        } else if (e.getSource() == botonCancelarPago) {
            controlador.cancelarPago();
        } else {
            mostrarFormularioElegido();
        }
    }

    private void mostrarFormularioElegido() {
        panelTarjeta.setVisible(opcionTarjeta.isSelected());
        panelTransferencia.setVisible(opcionTransferencia.isSelected());
    }

    // Le pasa los textos al controlador; el método depende del medio elegido
    private void pagar() {
        if (opcionTarjeta.isSelected()) {
            // getPassword() devuelve un arreglo de char: se convierte a String
            String codigo = new String(campoCodigo.getPassword());
            controlador.pagarConTarjeta(campoNumeroTarjeta.getText(), campoTitular.getText(),
                    campoMesVencimiento.getText(), campoAnioVencimiento.getText(), codigo,
                    (String) comboCuotas.getSelectedItem());
        } else {
            controlador.pagarConTransferencia(campoCbu.getText(), campoOperacion.getText());
        }
    }
}
