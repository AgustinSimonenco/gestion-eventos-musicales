package vista;

import controlador.ControladorArtistas;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import modelo.Artista;

/**
 * Catálogo de artistas: alta, modificación y baja (CU15).
 * <p>
 * Al elegir una fila de la tabla, el nombre y el género se copian a los
 * campos, para poder corregirlos y tocar <i>Modificar</i>. Por eso, además
 * de {@link ActionListener}, implementa {@link ListSelectionListener}: la
 * tabla le avisa cuando cambia la fila elegida.
 */
public class PantallaArtistas extends Pantalla implements ActionListener, ListSelectionListener {

    private ControladorArtistas controlador;
    private DefaultTableModel modeloTabla;
    private JTable tablaArtistas;
    // Artistas en el mismo orden que las filas: la fila 2 es artistasMostrados.get(2)
    private List<Artista> artistasMostrados;
    private JTextField campoNombre;
    private JTextField campoGenero;
    private JButton botonAlta;
    private JButton botonModificar;
    private JButton botonBaja;
    private JButton botonVolver;

    /**
     * Arma la pantalla.
     *
     * @param ventana     ventana principal
     * @param controlador controlador de artistas
     */
    public PantallaArtistas(VentanaPrincipal ventana, ControladorArtistas controlador) {
        super(ventana);
        this.controlador = controlador;
        modeloTabla = new DefaultTableModel(new String[] {"Nombre", "Género musical"}, 0);
        tablaArtistas = crearTabla(modeloTabla);
        tablaArtistas.getSelectionModel().addListSelectionListener(this);
        campoNombre = new JTextField(20);
        campoGenero = new JTextField(20);
        botonAlta = new JButton("Alta");
        botonModificar = new JButton("Modificar");
        botonBaja = new JButton("Baja");
        botonVolver = new JButton("Volver");
        botonAlta.addActionListener(this);
        botonModificar.addActionListener(this);
        botonBaja.addActionListener(this);
        botonVolver.addActionListener(this);
        armar();
    }

    // Título arriba, tabla al medio y, abajo, el formulario con los botones
    private void armar() {
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        agregarFila(formulario, "Nombre:", campoNombre);
        agregarFila(formulario, "Género musical:", campoGenero);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonAlta);
        botones.add(botonModificar);
        botones.add(botonBaja);
        botones.add(botonVolver);

        JPanel abajo = new JPanel(new BorderLayout());
        JPanel centroFormulario = new JPanel(new FlowLayout());
        centroFormulario.add(formulario);
        abajo.add(centroFormulario, BorderLayout.CENTER);
        abajo.add(botones, BorderLayout.SOUTH);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(crearTitulo("Artistas"), BorderLayout.NORTH);
        add(new JScrollPane(tablaArtistas), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);
    }

    /**
     * Recarga la tabla con el catálogo y deja los campos vacíos.
     */
    @Override
    public void actualizar() {
        artistasMostrados = controlador.getArtistas();
        // Primero se borran las filas viejas y después se cargan de nuevo
        modeloTabla.setRowCount(0);
        for (Artista artista : artistasMostrados) {
            modeloTabla.addRow(new Object[] {artista.getNombre(), artista.getGeneroMusical()});
        }
        campoNombre.setText("");
        campoGenero.setText("");
    }

    /**
     * Se ejecuta cuando se toca un botón.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonAlta) {
            controlador.altaArtista(campoNombre.getText(), campoGenero.getText());
        } else if (e.getSource() == botonVolver) {
            ventana.mostrar(VentanaPrincipal.MIS_EVENTOS);
        } else {
            // Modificar y Baja necesitan un artista elegido en la tabla
            Artista artista = getArtistaElegido();
            if (artista == null) {
                ventana.mostrarError("Elegí un artista de la tabla.");
            } else if (e.getSource() == botonModificar) {
                controlador.modificarArtista(artista, campoNombre.getText(), campoGenero.getText());
            } else if (e.getSource() == botonBaja) {
                controlador.bajaArtista(artista);
            }
        }
    }

    /**
     * Se ejecuta cuando cambia la fila elegida: copia los datos del artista a
     * los campos.
     *
     * @param e evento de Swing con el cambio de selección
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        Artista artista = getArtistaElegido();
        if (artista != null) {
            campoNombre.setText(artista.getNombre());
            campoGenero.setText(artista.getGeneroMusical());
        }
    }

    // Devuelve el artista de la fila elegida, o null si no hay ninguna.
    // Se controla también el tamaño de la lista porque, mientras actualizar()
    // recarga la tabla, la fila elegida puede ser de la carga anterior.
    private Artista getArtistaElegido() {
        int fila = tablaArtistas.getSelectedRow();
        if (fila < 0 || fila >= artistasMostrados.size()) {
            return null;
        }
        return artistasMostrados.get(fila);
    }
}
