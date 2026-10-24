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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import modelo.Evento;

/**
 * Pantalla de inicio del cliente: es su "menú" (CU02, paso 3).
 * <p>
 * Muestra la tabla de eventos activos (CU07) y el botón
 * <i>Ver disponibilidad</i> (CU08). En la E9 se agregan la cantidad,
 * <i>Agregar al carrito</i> y <i>Carrito</i> (CU09), y en la E11,
 * <i>Mis compras</i> (CU13).
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
    private JButton botonVerDisponibilidad;
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
        botonVerDisponibilidad = new JButton("Ver disponibilidad");
        botonCerrarSesion = new JButton("Cerrar sesión");
        botonVerDisponibilidad.addActionListener(this);
        botonCerrarSesion.addActionListener(this);
        armar();
    }

    // Arriba título, saludo y aviso; al medio la tabla; abajo los botones
    private void armar() {
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.add(etiquetaBienvenida);
        textos.add(etiquetaAviso);
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(crearTitulo("Eventos disponibles"), BorderLayout.NORTH);
        arriba.add(textos, BorderLayout.SOUTH);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonVerDisponibilidad);
        botones.add(botonCerrarSesion);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Pone el saludo y recarga la tabla con los eventos activos (CU07).
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
                String.format("$ %.2f", evento.getPrecioEntrada()),
                evento.getEntradasDisponibles()
            });
        }
        // CU07, 2a: se avisa en la misma pantalla, sin abrir un diálogo cada vez
        if (eventosMostrados.isEmpty()) {
            etiquetaAviso.setText("Por ahora no hay eventos disponibles.");
        } else {
            etiquetaAviso.setText("Elegí un evento de la tabla para ver su disponibilidad.");
        }
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
        } else if (e.getSource() == botonVerDisponibilidad) {
            Evento evento = getEventoElegido();
            if (evento == null) {
                ventana.mostrarError("Elegí un evento de la tabla.");
            } else {
                controlador.consultarDisponibilidad(evento);
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
