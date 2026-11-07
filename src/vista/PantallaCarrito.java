package vista;

import controlador.ControladorCarrito;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import modelo.Carrito;
import modelo.Evento;
import modelo.ItemCarrito;

/**
 * Carrito del cliente: ítems, subtotales y total (CU09). Desde acá se cambia
 * la cantidad de un ítem, se lo quita (5a) o se confirma la compra (CU10).
 */
public class PantallaCarrito extends Pantalla implements ActionListener {

    private ControladorCarrito controlador;
    private ControladorCompras controladorCompras;
    private DefaultTableModel modeloTabla;
    private JTable tablaItems;
    // Ítems en el mismo orden que las filas de la tabla
    private List<ItemCarrito> itemsMostrados;
    private JTextField campoCantidad;
    private JLabel etiquetaTotal;
    private JButton botonCambiarCantidad;
    private JButton botonQuitar;
    private JButton botonConfirmar;
    private JButton botonSeguirComprando;

    /**
     * Arma la pantalla.
     *
     * @param ventana            ventana principal
     * @param controlador        controlador del carrito
     * @param controladorCompras controlador de compras, para confirmar la compra
     */
    public PantallaCarrito(VentanaPrincipal ventana, ControladorCarrito controlador,
            ControladorCompras controladorCompras) {
        super(ventana);
        this.controlador = controlador;
        this.controladorCompras = controladorCompras;
        String[] columnas = {"Evento", "Fecha", "Cantidad", "Precio", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaItems = crearTabla(modeloTabla);
        campoCantidad = new JTextField(5);
        etiquetaTotal = new JLabel("", SwingConstants.CENTER);
        botonCambiarCantidad = new JButton("Cambiar cantidad");
        botonQuitar = new JButton("Quitar");
        botonConfirmar = new JButton("Confirmar compra");
        botonSeguirComprando = new JButton("Seguir comprando");
        botonCambiarCantidad.addActionListener(this);
        botonQuitar.addActionListener(this);
        botonConfirmar.addActionListener(this);
        botonSeguirComprando.addActionListener(this);
        armar();
    }

    // Título arriba, tabla al medio y, abajo, el total y dos filas de botones
    private void armar() {
        JPanel filaItem = new JPanel(new FlowLayout());
        filaItem.add(new JLabel("Cantidad:"));
        filaItem.add(campoCantidad);
        filaItem.add(botonCambiarCantidad);
        filaItem.add(botonQuitar);

        JPanel filaCompra = new JPanel(new FlowLayout());
        filaCompra.add(botonConfirmar);
        filaCompra.add(botonSeguirComprando);

        JPanel abajo = new JPanel(new BorderLayout());
        abajo.add(etiquetaTotal, BorderLayout.NORTH);
        abajo.add(filaItem, BorderLayout.CENTER);
        abajo.add(filaCompra, BorderLayout.SOUTH);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(crearTitulo("Carrito"), BorderLayout.NORTH);
        add(new JScrollPane(tablaItems), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);
    }

    /**
     * Recarga los ítems y el total. Los subtotales y el total los calcula el
     * modelo.
     */
    @Override
    public void actualizar() {
        Carrito carrito = controlador.getCarrito();
        itemsMostrados = carrito.getItems();
        modeloTabla.setRowCount(0);
        for (ItemCarrito item : itemsMostrados) {
            Evento evento = item.getEvento();
            modeloTabla.addRow(new Object[] {
                evento.getNombre(),
                formatearFecha(evento.getFecha()),
                item.getCantidad(),
                formatearPrecio(evento.getPrecioEntrada()),
                formatearPrecio(item.getSubtotal())
            });
        }
        campoCantidad.setText("");
        if (carrito.estaVacio()) {
            etiquetaTotal.setText("El carrito está vacío.");
        } else {
            etiquetaTotal.setText("Total: " + formatearPrecio(carrito.getTotal()));
        }
    }

    /**
     * Se ejecuta cuando se toca un botón.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonConfirmar) {
            controladorCompras.iniciarCompra();
        } else if (e.getSource() == botonSeguirComprando) {
            // Solo cambia de pantalla, no lleva datos
            ventana.mostrar(VentanaPrincipal.EVENTOS_DISPONIBLES);
        } else {
            // Cambiar cantidad y Quitar necesitan un ítem elegido
            ItemCarrito item = getItemElegido();
            if (item == null) {
                ventana.mostrarError("Elegí un ítem de la tabla.");
            } else if (e.getSource() == botonCambiarCantidad) {
                controlador.cambiarCantidad(item.getEvento(), campoCantidad.getText());
            } else if (e.getSource() == botonQuitar) {
                controlador.quitarItem(item.getEvento());
            }
        }
    }

    // Devuelve el ítem de la fila elegida, o null si no hay ninguna
    private ItemCarrito getItemElegido() {
        int fila = tablaItems.getSelectedRow();
        if (fila < 0 || fila >= itemsMostrados.size()) {
            return null;
        }
        return itemsMostrados.get(fila);
    }
}
