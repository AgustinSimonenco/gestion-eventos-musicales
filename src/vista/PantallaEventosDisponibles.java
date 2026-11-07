package vista;

import controlador.ControladorCarrito;
import controlador.ControladorUsuarios;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import modelo.Evento;

/**
 * Pantalla de inicio del cliente: es su "menú" (CU02, paso 3).
 * <p>
 * Muestra la tabla de eventos (CU07) y los botones <i>Ver disponibilidad</i>
 * (CU08), <i>Agregar al carrito</i> con la cantidad (CU09), <i>Carrito</i>,
 * <i>Mis compras</i> (CU13) y <i>Cerrar sesión</i>.
 */
public class PantallaEventosDisponibles extends Pantalla implements ActionListener {

    private ControladorCarrito controlador;
    private ControladorUsuarios controladorUsuarios;
    private JLabel etiquetaBienvenida;
    private JLabel etiquetaAviso;
    private DefaultTableModel modeloTabla;
    private JTable tablaEventos;
    // Eventos en el mismo orden que las filas de la tabla
    private List<Evento> eventosMostrados;
    private JTextField campoCantidad;
    private JButton botonVerDisponibilidad;
    private JButton botonAgregar;
    private JButton botonCarrito;
    private JButton botonMisCompras;
    private JButton botonCerrarSesion;

    /**
     * Arma la pantalla.
     *
     * @param ventana             ventana principal
     * @param controlador         controlador del carrito, para los eventos y la disponibilidad
     * @param controladorUsuarios controlador de usuarios, para el saludo y cerrar sesión
     */
    public PantallaEventosDisponibles(VentanaPrincipal ventana, ControladorCarrito controlador,
            ControladorUsuarios controladorUsuarios) {
        super(ventana);
        this.controlador = controlador;
        this.controladorUsuarios = controladorUsuarios;
        etiquetaBienvenida = new JLabel("", SwingConstants.CENTER);
        etiquetaAviso = new JLabel("", SwingConstants.CENTER);
        String[] columnas = {"Nombre", "Fecha", "Lugar", "Artistas", "Precio", "Disponibles"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEventos = crearTabla(modeloTabla);
        // Los nombres de los artistas son lo más largo: esa columna va más ancha
        tablaEventos.getColumnModel().getColumn(3).setPreferredWidth(280);
        campoCantidad = new JTextField(5);
        botonVerDisponibilidad = new JButton("Ver disponibilidad");
        botonAgregar = new JButton("Agregar al carrito");
        botonCarrito = new JButton("Carrito");
        botonMisCompras = new JButton("Mis compras");
        botonCerrarSesion = new JButton("Cerrar sesión");
        botonVerDisponibilidad.addActionListener(this);
        botonAgregar.addActionListener(this);
        botonCarrito.addActionListener(this);
        botonMisCompras.addActionListener(this);
        botonCerrarSesion.addActionListener(this);
        armar();
    }

    // Arriba título, saludo y aviso; al medio la tabla; abajo dos filas de botones
    private void armar() {
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.add(etiquetaBienvenida);
        textos.add(etiquetaAviso);
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(crearTitulo("Eventos disponibles"), BorderLayout.NORTH);
        arriba.add(textos, BorderLayout.SOUTH);

        JPanel filaEvento = new JPanel(new FlowLayout());
        filaEvento.add(botonVerDisponibilidad);
        filaEvento.add(new JLabel("Cantidad:"));
        filaEvento.add(campoCantidad);
        filaEvento.add(botonAgregar);

        JPanel filaMenu = new JPanel(new FlowLayout());
        filaMenu.add(botonCarrito);
        filaMenu.add(botonMisCompras);
        filaMenu.add(botonCerrarSesion);

        JPanel abajo = new JPanel(new BorderLayout());
        abajo.add(filaEvento, BorderLayout.NORTH);
        abajo.add(filaMenu, BorderLayout.SOUTH);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);
    }

    /**
     * Pone el saludo y recarga la tabla con los eventos (CU07).
     */
    @Override
    public void actualizar() {
        etiquetaBienvenida.setText("Hola, " + controladorUsuarios.getUsuarioActual().getNombre()
                + ". Entraste como cliente.");
        eventosMostrados = controlador.getEventosDisponibles();
        modeloTabla.setRowCount(0);
        for (Evento evento : eventosMostrados) {
            modeloTabla.addRow(new Object[] {
                evento.getNombre(),
                formatearFecha(evento.getFecha()),
                evento.getLugar(),
                evento.getNombresArtistas(),
                formatearPrecio(evento.getPrecioEntrada()),
                textoDisponibles(evento)
            });
        }
        campoCantidad.setText("");
        // CU07, 2a: se avisa en la misma pantalla, sin abrir un diálogo cada vez
        if (eventosMostrados.isEmpty()) {
            etiquetaAviso.setText("Por ahora no hay eventos disponibles.");
        } else {
            etiquetaAviso.setText("Elegí un evento de la tabla para consultarlo o comprar entradas.");
        }
    }

    // Un evento agotado se ve como tal en lugar de mostrar un 0 (D-008)
    private String textoDisponibles(Evento evento) {
        if (evento.getEntradasDisponibles() == 0) {
            return "AGOTADO";
        }
        return String.valueOf(evento.getEntradasDisponibles());
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
        } else if (e.getSource() == botonCarrito) {
            // Carrito y Mis compras solo cambian de pantalla, no llevan datos
            ventana.mostrar(VentanaPrincipal.CARRITO);
        } else if (e.getSource() == botonMisCompras) {
            ventana.mostrar(VentanaPrincipal.HISTORIAL);
        } else {
            // Ver disponibilidad y Agregar al carrito necesitan un evento elegido
            Evento evento = getEventoElegido();
            if (evento == null) {
                ventana.mostrarError("Elegí un evento de la tabla.");
            } else if (e.getSource() == botonVerDisponibilidad) {
                controlador.consultarDisponibilidad(evento);
            } else if (e.getSource() == botonAgregar) {
                controlador.agregarAlCarrito(evento, campoCantidad.getText());
            }
        }
    }

    // Devuelve el evento de la fila elegida, o null si no hay ninguna
    private Evento getEventoElegido() {
        int fila = tablaEventos.getSelectedRow();
        if (fila < 0 || fila >= eventosMostrados.size()) {
            return null;
        }
        return eventosMostrados.get(fila);
    }
}
