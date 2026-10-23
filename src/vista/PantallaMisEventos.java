package vista;

import controlador.ControladorEventos;
import controlador.ControladorUsuarios;
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
import modelo.Evento;

/**
 * Pantalla de inicio del organizador: es su "menú" (CU02, paso 3).
 * <p>
 * Muestra la tabla con sus eventos y los botones <i>Nuevo</i> (CU03),
 * <i>Modificar</i> (CU04), <i>Cancelar evento</i> (CU05),
 * <i>Gestionar artistas</i> (CU15) y <i>Cerrar sesión</i>. En la E8 se
 * agrega <i>Artistas del evento</i> (CU06).
 */
public class PantallaMisEventos extends Pantalla implements ActionListener {

    private ControladorEventos controlador;
    private ControladorUsuarios controladorUsuarios;
    private JLabel etiquetaBienvenida;
    private DefaultTableModel modeloTabla;
    private JTable tablaEventos;
    // Eventos en el mismo orden que las filas de la tabla
    private List<Evento> eventosMostrados;
    private JButton botonNuevo;
    private JButton botonModificar;
    private JButton botonCancelarEvento;
    private JButton botonGestionarArtistas;
    private JButton botonCerrarSesion;

    /**
     * Arma la pantalla.
     *
     * @param ventana             ventana principal
     * @param controlador         controlador de eventos
     * @param controladorUsuarios controlador de usuarios, para el saludo y cerrar sesión
     */
    public PantallaMisEventos(VentanaPrincipal ventana, ControladorEventos controlador,
            ControladorUsuarios controladorUsuarios) {
        super(ventana);
        this.controlador = controlador;
        this.controladorUsuarios = controladorUsuarios;
        etiquetaBienvenida = new JLabel("", SwingConstants.CENTER);
        String[] columnas = {"Nombre", "Fecha", "Lugar", "Capacidad", "Disponibles", "Precio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEventos = crearTabla(modeloTabla);
        botonNuevo = new JButton("Nuevo");
        botonModificar = new JButton("Modificar");
        botonCancelarEvento = new JButton("Cancelar evento");
        botonGestionarArtistas = new JButton("Gestionar artistas");
        botonCerrarSesion = new JButton("Cerrar sesión");
        botonNuevo.addActionListener(this);
        botonModificar.addActionListener(this);
        botonCancelarEvento.addActionListener(this);
        botonGestionarArtistas.addActionListener(this);
        botonCerrarSesion.addActionListener(this);
        armar();
    }

    // Arriba título y saludo, al medio la tabla y abajo los botones
    private void armar() {
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(crearTitulo("Mis eventos"), BorderLayout.NORTH);
        arriba.add(etiquetaBienvenida, BorderLayout.SOUTH);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonNuevo);
        botones.add(botonModificar);
        botones.add(botonCancelarEvento);
        botones.add(botonGestionarArtistas);
        botones.add(botonCerrarSesion);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Pone el saludo y recarga la tabla con los eventos del organizador.
     */
    @Override
    public void actualizar() {
        etiquetaBienvenida.setText("Hola, " + controladorUsuarios.getUsuarioActual().getNombre()
                + ". Entraste como organizador.");
        eventosMostrados = controlador.getMisEventos();
        // Primero se borran las filas viejas y después se cargan de nuevo
        modeloTabla.setRowCount(0);
        for (Evento evento : eventosMostrados) {
            modeloTabla.addRow(new Object[] {
                evento.getNombre(),
                formatearFecha(evento.getFecha()),
                evento.getLugar(),
                evento.getCapacidad(),
                evento.getEntradasDisponibles(),
                String.format("$ %.2f", evento.getPrecioEntrada()),
                evento.getEstado()
            });
        }
    }

    /**
     * Se ejecuta cuando se toca un botón.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonNuevo) {
            controlador.nuevoEvento();
        } else if (e.getSource() == botonGestionarArtistas) {
            // Solo cambia de pantalla, no lleva datos
            ventana.mostrar(VentanaPrincipal.ARTISTAS);
        } else if (e.getSource() == botonCerrarSesion) {
            controladorUsuarios.cerrarSesion();
        } else {
            // Modificar y Cancelar evento necesitan un evento elegido (p9)
            Evento evento = getEventoElegido();
            if (evento == null) {
                ventana.mostrarError("Elegí un evento de la tabla.");
            } else if (e.getSource() == botonModificar) {
                controlador.elegirParaModificar(evento);
            } else if (e.getSource() == botonCancelarEvento) {
                controlador.cancelarEvento(evento);
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
