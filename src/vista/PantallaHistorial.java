package vista;

import controlador.ControladorCompras;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import modelo.Compra;

/**
 * Historial de compras del cliente (CU13). Desde acá se ve el comprobante de
 * una compra (CU12) o se la cancela (CU14).
 */
public class PantallaHistorial extends Pantalla implements ActionListener {

    private ControladorCompras controlador;
    private JLabel etiquetaAviso;
    private DefaultTableModel modeloTabla;
    private JTable tablaCompras;
    // Compras en el mismo orden que las filas de la tabla
    private List<Compra> comprasMostradas;
    private JButton botonVerComprobante;
    private JButton botonCancelarCompra;
    private JButton botonVolver;

    /**
     * Arma la pantalla.
     *
     * @param ventana     ventana principal
     * @param controlador controlador de compras
     */
    public PantallaHistorial(VentanaPrincipal ventana, ControladorCompras controlador) {
        super(ventana);
        this.controlador = controlador;
        etiquetaAviso = new JLabel("", SwingConstants.CENTER);
        String[] columnas = {"N.º", "Fecha de compra", "Eventos", "Entradas", "Total", "Pago", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaCompras = crearTabla(modeloTabla);
        // Los nombres de los eventos son lo más largo: esa columna va más ancha
        tablaCompras.getColumnModel().getColumn(2).setPreferredWidth(320);
        botonVerComprobante = new JButton("Ver comprobante");
        botonCancelarCompra = new JButton("Cancelar compra");
        botonVolver = new JButton("Volver");
        botonVerComprobante.addActionListener(this);
        botonCancelarCompra.addActionListener(this);
        botonVolver.addActionListener(this);
        armar();
    }

    // Arriba título y aviso, al medio la tabla y abajo los botones
    private void armar() {
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(crearTitulo("Mis compras"), BorderLayout.NORTH);
        arriba.add(etiquetaAviso, BorderLayout.SOUTH);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonVerComprobante);
        botones.add(botonCancelarCompra);
        botones.add(botonVolver);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tablaCompras), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Recarga la tabla con las compras del cliente.
     */
    @Override
    public void actualizar() {
        comprasMostradas = controlador.getHistorial();
        modeloTabla.setRowCount(0);
        for (Compra compra : comprasMostradas) {
            modeloTabla.addRow(new Object[] {
                compra.getNumero(),
                formatearFecha(compra.getFechaHora().toLocalDate()),
                compra.getNombresEventos(),
                compra.getEntradas().size(),
                formatearPrecio(compra.getTotal()),
                compra.getPagoAprobado().getEstado(),
                compra.getEstado()
            });
        }
        // CU13, 2a: el historial vacío se avisa en la misma pantalla
        if (comprasMostradas.isEmpty()) {
            etiquetaAviso.setText("Todavía no hiciste ninguna compra.");
        } else {
            etiquetaAviso.setText("");
        }
    }

    /**
     * Se ejecuta cuando se toca un botón.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonVolver) {
            ventana.mostrar(VentanaPrincipal.EVENTOS_DISPONIBLES);
        } else {
            // Ver comprobante y Cancelar compra necesitan una compra elegida
            Compra compra = getCompraElegida();
            if (compra == null) {
                ventana.mostrarError("Elegí una compra de la tabla.");
            } else if (e.getSource() == botonVerComprobante) {
                controlador.verComprobante(compra);
            } else if (e.getSource() == botonCancelarCompra) {
                controlador.cancelarCompra(compra);
            }
        }
    }

    // Devuelve la compra de la fila elegida, o null si no hay ninguna
    private Compra getCompraElegida() {
        int fila = tablaCompras.getSelectedRow();
        if (fila < 0 || fila >= comprasMostradas.size()) {
            return null;
        }
        return comprasMostradas.get(fila);
    }
}
