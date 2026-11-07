package vista;

import controlador.ControladorEventos;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import modelo.Artista;
import modelo.Evento;

/**
 * Artistas de un evento (CU06). A la izquierda están los artistas asignados
 * al evento y a la derecha todos los registrados en el catálogo.
 * <p>
 * El evento es el que el organizador eligió en "Mis eventos": la pantalla se
 * lo pregunta al controlador con {@code getEventoSeleccionado()}, igual que
 * el formulario al modificar.
 */
public class PantallaAsignarArtistas extends Pantalla implements ActionListener {

    private ControladorEventos controlador;
    private JLabel titulo;
    private DefaultListModel<Artista> modeloAsignados;
    private JList<Artista> listaAsignados;
    private DefaultListModel<Artista> modeloRegistrados;
    private JList<Artista> listaRegistrados;
    private JButton botonAsignar;
    private JButton botonQuitar;
    private JButton botonNuevoArtista;
    private JButton botonVolver;

    /**
     * Arma la pantalla.
     *
     * @param ventana     ventana principal
     * @param controlador controlador de eventos
     */
    public PantallaAsignarArtistas(VentanaPrincipal ventana, ControladorEventos controlador) {
        super(ventana);
        this.controlador = controlador;
        titulo = crearTitulo("");
        modeloAsignados = new DefaultListModel<Artista>();
        listaAsignados = crearLista(modeloAsignados);
        modeloRegistrados = new DefaultListModel<Artista>();
        listaRegistrados = crearLista(modeloRegistrados);
        botonAsignar = new JButton("Asignar al evento");
        botonQuitar = new JButton("Quitar del evento");
        botonNuevoArtista = new JButton("Nuevo artista");
        botonVolver = new JButton("Volver");
        botonAsignar.addActionListener(this);
        botonQuitar.addActionListener(this);
        botonNuevoArtista.addActionListener(this);
        botonVolver.addActionListener(this);
        armar();
    }

    // Una lista en la que se elige un artista por vez. Muestra cada artista
    // con su toString(), que es el nombre.
    private JList<Artista> crearLista(DefaultListModel<Artista> modelo) {
        JList<Artista> lista = new JList<Artista>(modelo);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return lista;
    }

    // Título arriba; al medio las dos listas, cada una con su botón; abajo
    // Nuevo artista y Volver
    private void armar() {
        JPanel centro = new JPanel(new GridLayout(1, 2, 20, 0));
        centro.add(armarColumna("Artistas del evento", listaAsignados, botonQuitar));
        centro.add(armarColumna("Artistas registrados", listaRegistrados, botonAsignar));

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonNuevoArtista);
        botones.add(botonVolver);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titulo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    // Una columna: el nombre arriba, la lista al medio y el botón abajo
    private JPanel armarColumna(String nombre, JList<Artista> lista, JButton boton) {
        JPanel columna = new JPanel(new BorderLayout(0, 5));
        columna.add(new JLabel(nombre), BorderLayout.NORTH);
        columna.add(new JScrollPane(lista), BorderLayout.CENTER);
        JPanel abajo = new JPanel(new FlowLayout());
        abajo.add(boton);
        columna.add(abajo, BorderLayout.SOUTH);
        return columna;
    }

    /**
     * Recarga las dos listas: los artistas del evento seleccionado y todo el
     * catálogo (CU06, paso 2).
     */
    @Override
    public void actualizar() {
        Evento evento = controlador.getEventoSeleccionado();
        titulo.setText("Artistas de " + evento.getNombre());
        modeloAsignados.clear();
        for (Artista artista : evento.getArtistas()) {
            modeloAsignados.addElement(artista);
        }
        modeloRegistrados.clear();
        for (Artista artista : controlador.getArtistas()) {
            modeloRegistrados.addElement(artista);
        }
    }

    /**
     * Se ejecuta cuando se toca un botón.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonAsignar) {
            Artista artista = listaRegistrados.getSelectedValue();
            if (artista == null) {
                ventana.mostrarError("Elegí un artista de la lista de registrados.");
            } else {
                controlador.asignarArtista(artista);
            }
        } else if (e.getSource() == botonQuitar) {
            Artista artista = listaAsignados.getSelectedValue();
            if (artista == null) {
                ventana.mostrarError("Elegí un artista de la lista del evento.");
            } else {
                controlador.quitarArtista(artista);
            }
        } else if (e.getSource() == botonNuevoArtista) {
            // CU06, 3b: si el artista no está registrado, se va al catálogo (CU15)
            ventana.mostrar(VentanaPrincipal.ARTISTAS);
        } else if (e.getSource() == botonVolver) {
            ventana.mostrar(VentanaPrincipal.MIS_EVENTOS);
        }
    }
}
