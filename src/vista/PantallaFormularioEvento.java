package vista;

import controlador.ControladorEventos;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import modelo.Artista;
import modelo.Evento;

/**
 * Formulario para crear un evento (CU03) o modificarlo (CU04). Es la misma
 * pantalla para los dos casos, así no se repite el formulario.
 * <p>
 * El modo lo decide el controlador: si {@code getEventoSeleccionado()} es
 * {@code null}, se crea; si no, se modifica ese evento. Al modificar no se
 * muestra la lista de artistas, porque se cambian en la pantalla de
 * asignación (CU06).
 */
public class PantallaFormularioEvento extends Pantalla implements ActionListener {

    private ControladorEventos controlador;
    private JLabel titulo;
    private JTextField campoNombre;
    private JTextField campoFecha;
    private JTextField campoLugar;
    private JTextField campoCapacidad;
    private JTextField campoPrecio;
    private DefaultListModel<Artista> modeloArtistas;
    private JList<Artista> listaArtistas;
    private JPanel panelArtistas;
    private JButton botonGuardar;
    private JButton botonNuevoArtista;
    private JButton botonVolver;
    private boolean modoModificar;

    /**
     * Arma la pantalla.
     *
     * @param ventana     ventana principal
     * @param controlador controlador de eventos
     */
    public PantallaFormularioEvento(VentanaPrincipal ventana, ControladorEventos controlador) {
        super(ventana);
        this.controlador = controlador;
        titulo = crearTitulo("");
        campoNombre = new JTextField(20);
        campoFecha = new JTextField(20);
        campoLugar = new JTextField(20);
        campoCapacidad = new JTextField(20);
        campoPrecio = new JTextField(20);
        modeloArtistas = new DefaultListModel<Artista>();
        // La JList muestra cada artista con su toString(), que es el nombre
        listaArtistas = new JList<Artista>(modeloArtistas);
        botonGuardar = new JButton("Guardar");
        botonNuevoArtista = new JButton("Nuevo artista");
        botonVolver = new JButton("Volver");
        botonGuardar.addActionListener(this);
        botonNuevoArtista.addActionListener(this);
        botonVolver.addActionListener(this);
        armar();
    }

    // Título arriba; al medio los datos y, debajo, la lista de artistas; botones abajo
    private void armar() {
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        agregarFila(formulario, "Nombre:", campoNombre);
        agregarFila(formulario, "Fecha (dd/mm/aaaa):", campoFecha);
        agregarFila(formulario, "Lugar:", campoLugar);
        agregarFila(formulario, "Capacidad:", campoCapacidad);
        agregarFila(formulario, "Precio de la entrada:", campoPrecio);
        JPanel arriba = new JPanel(new FlowLayout());
        arriba.add(formulario);

        // La lista va aparte y no en el GridLayout, porque ahí tendría la
        // misma altura que un campo de texto
        JScrollPane desplazamiento = new JScrollPane(listaArtistas);
        desplazamiento.setPreferredSize(new Dimension(300, 120));
        panelArtistas = new JPanel(new FlowLayout());
        panelArtistas.add(new JLabel("Artistas (Ctrl + clic para elegir varios):"));
        panelArtistas.add(desplazamiento);
        panelArtistas.add(botonNuevoArtista);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(arriba, BorderLayout.NORTH);
        centro.add(panelArtistas, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonGuardar);
        botones.add(botonVolver);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titulo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Prepara el formulario según el modo: vacío y con la lista de artistas
     * para crear, o con los datos del evento para modificar.
     */
    @Override
    public void actualizar() {
        Evento evento = controlador.getEventoSeleccionado();
        modoModificar = evento != null;
        if (modoModificar) {
            titulo.setText("Modificar evento");
            campoNombre.setText(evento.getNombre());
            campoFecha.setText(formatearFecha(evento.getFecha()));
            campoLugar.setText(evento.getLugar());
            campoCapacidad.setText(String.valueOf(evento.getCapacidad()));
            campoPrecio.setText(String.valueOf(evento.getPrecioEntrada()));
        } else {
            titulo.setText("Nuevo evento");
            campoNombre.setText("");
            campoFecha.setText("");
            campoLugar.setText("");
            campoCapacidad.setText("");
            campoPrecio.setText("");
            cargarArtistas();
        }
        panelArtistas.setVisible(!modoModificar);
    }

    // Llena la lista con el catálogo; ninguno queda elegido
    private void cargarArtistas() {
        modeloArtistas.clear();
        for (Artista artista : controlador.getArtistas()) {
            modeloArtistas.addElement(artista);
        }
    }

    /**
     * Se ejecuta cuando se toca un botón.
     *
     * @param e evento de Swing, dice qué botón se tocó
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonGuardar) {
            guardar();
        } else if (e.getSource() == botonNuevoArtista) {
            // CU03, 2a: si falta un artista, se va al catálogo (CU15)
            ventana.mostrar(VentanaPrincipal.ARTISTAS);
        } else if (e.getSource() == botonVolver) {
            ventana.mostrar(VentanaPrincipal.MIS_EVENTOS);
        }
    }

    // Le pasa los textos al controlador; el método depende del modo
    private void guardar() {
        if (modoModificar) {
            controlador.modificarEvento(campoNombre.getText(), campoFecha.getText(),
                    campoLugar.getText(), campoCapacidad.getText(), campoPrecio.getText());
        } else {
            List<Artista> elegidos = listaArtistas.getSelectedValuesList();
            controlador.crearEvento(campoNombre.getText(), campoFecha.getText(),
                    campoLugar.getText(), campoCapacidad.getText(), campoPrecio.getText(), elegidos);
        }
    }
}
