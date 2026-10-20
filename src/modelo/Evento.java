package modelo;

import excepciones.ArtistaException;
import excepciones.DatosInvalidosException;
import excepciones.EventoException;
import excepciones.SinDisponibilidadException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Un recital. Es el experto en sus lugares: sabe cuántas entradas quedan,
 * no deja vender más que la capacidad y maneja sus artistas y su estado.
 */
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private LocalDate fecha;
    private String lugar;
    private int capacidad;
    private double precioEntrada;
    private int entradasVendidas;
    private EstadoEvento estado;
    private Organizador organizador;
    private List<Artista> artistas;

    /**
     * Crea un evento validando todos sus datos. Así nunca existe un evento
     * inválido en el sistema (CU03).
     *
     * @param id            número único, lo genera {@link DatosSistema#generarId()}
     * @param nombre        nombre del evento
     * @param fecha         fecha del evento, tiene que ser futura
     * @param lugar         lugar donde se hace
     * @param capacidad     cantidad máxima de entradas, mayor a 0
     * @param precioEntrada precio de cada entrada, 0 o más
     * @param organizador   quién lo crea
     * @param artistas      artistas que participan, al menos uno (D-002)
     * @throws DatosInvalidosException si algún dato no es válido
     */
    public Evento(int id, String nombre, LocalDate fecha, String lugar, int capacidad,
            double precioEntrada, Organizador organizador, List<Artista> artistas)
            throws DatosInvalidosException {
        validarDatos(nombre, fecha, lugar, capacidad, precioEntrada);
        if (artistas == null || artistas.isEmpty()) {
            throw new DatosInvalidosException("Elegí al menos un artista para el evento.");
        }
        this.id = id;
        this.organizador = organizador;
        // Se copia la lista para que el evento tenga la suya propia
        this.artistas = new ArrayList<Artista>(artistas);
        this.entradasVendidas = 0;
        this.estado = EstadoEvento.ACTIVO;
        guardarDatos(nombre, fecha, lugar, capacidad, precioEntrada);
    }

    /**
     * Cambia los datos del evento (CU04).
     *
     * @param nombre        nombre nuevo
     * @param fecha         fecha nueva, futura
     * @param lugar         lugar nuevo
     * @param capacidad     capacidad nueva, no menor a lo ya vendido
     * @param precioEntrada precio nuevo
     * @throws DatosInvalidosException si algún dato no es válido
     * @throws EventoException         si el evento ya no se puede modificar o
     *                                 la capacidad es menor a lo vendido
     */
    public void modificar(String nombre, LocalDate fecha, String lugar, int capacidad,
            double precioEntrada) throws DatosInvalidosException, EventoException {
        verificarVigente("modificar");
        validarDatos(nombre, fecha, lugar, capacidad, precioEntrada);
        if (capacidad < entradasVendidas) {
            throw new EventoException("La capacidad no puede ser menor a las "
                    + entradasVendidas + " entradas ya vendidas.");
        }
        guardarDatos(nombre, fecha, lugar, capacidad, precioEntrada);
        // Con otra capacidad, el evento puede pasar de agotado a activo o al revés
        actualizarCupo();
    }

    /**
     * Cancela el evento (CU05).
     *
     * @throws EventoException si ya estaba cancelado o ya pasó
     */
    public void cancelar() throws EventoException {
        verificarVigente("cancelar");
        estado = EstadoEvento.CANCELADO;
    }

    /**
     * Controla que quien quiere cambiar el evento sea su organizador.
     * Se comparan los emails porque el email identifica a cada usuario.
     *
     * @param organizador el organizador que inició sesión
     * @throws EventoException si no es el organizador del evento
     */
    public void verificarOrganizador(Organizador organizador) throws EventoException {
        if (!this.organizador.getEmail().equals(organizador.getEmail())) {
            throw new EventoException("Solo el organizador del evento lo puede cambiar.");
        }
    }

    /**
     * Controla que se puedan vender {@code cantidad} entradas (CU08).
     *
     * @param cantidad entradas que se quieren comprar
     * @throws EventoException            si el evento está cancelado o finalizado
     * @throws SinDisponibilidadException si no quedan suficientes entradas
     */
    public void verificarDisponibilidad(int cantidad) throws EventoException {
        if (estado == EstadoEvento.CANCELADO || estado == EstadoEvento.FINALIZADO) {
            throw new EventoException("El evento " + nombre + " no está disponible.");
        }
        int disponibles = getEntradasDisponibles();
        if (cantidad > disponibles) {
            throw new SinDisponibilidadException("Para " + nombre + " quedan solo "
                    + disponibles + " entradas.", disponibles);
        }
    }

    /**
     * Ocupa lugares cuando se confirma una compra. Antes vuelve a verificar
     * la disponibilidad, así nunca se vende más que la capacidad.
     *
     * @param cantidad entradas vendidas
     * @throws EventoException si no se pueden vender
     */
    public void ocuparLugares(int cantidad) throws EventoException {
        verificarDisponibilidad(cantidad);
        entradasVendidas = entradasVendidas + cantidad;
        actualizarCupo();
    }

    /**
     * Libera lugares cuando se cancela una compra (CU14). Si el evento estaba
     * agotado, vuelve a estar activo.
     *
     * @param cantidad entradas que se devuelven
     */
    public void liberarLugares(int cantidad) {
        entradasVendidas = entradasVendidas - cantidad;
        actualizarCupo();
    }

    /**
     * @return entradas que todavía se pueden vender
     */
    public int getEntradasDisponibles() {
        return capacidad - entradasVendidas;
    }

    /**
     * Agrega un artista al evento (CU06).
     *
     * @param artista artista a agregar
     * @throws ArtistaException si ya estaba asignado
     */
    public void asignarArtista(Artista artista) throws ArtistaException {
        if (tieneArtista(artista)) {
            throw new ArtistaException(artista.getNombre() + " ya está en el evento.");
        }
        artistas.add(artista);
    }

    /**
     * Quita un artista del evento (CU06). El evento tiene que quedar con al
     * menos uno.
     *
     * @param artista artista a quitar
     * @throws ArtistaException si no estaba asignado o es el último
     */
    public void quitarArtista(Artista artista) throws ArtistaException {
        if (!tieneArtista(artista)) {
            throw new ArtistaException(artista.getNombre() + " no está en el evento.");
        }
        if (artistas.size() == 1) {
            throw new ArtistaException("El evento tiene que tener al menos un artista.");
        }
        artistas.remove(artista);
    }

    /**
     * @param artista artista a buscar
     * @return {@code true} si el artista participa del evento
     */
    public boolean tieneArtista(Artista artista) {
        return artistas.contains(artista);
    }

    /**
     * @return {@code true} si se pueden comprar entradas
     */
    public boolean estaActivo() {
        return estado == EstadoEvento.ACTIVO;
    }

    /**
     * Pasa el evento a {@code FINALIZADO} si su fecha ya pasó. Se llama antes
     * de mostrar eventos, porque el estado no cambia solo con el tiempo.
     *
     * @param hoy fecha de hoy
     */
    public void actualizarEstado(LocalDate hoy) {
        boolean vigente = estado == EstadoEvento.ACTIVO || estado == EstadoEvento.AGOTADO;
        if (vigente && fecha.isBefore(hoy)) {
            estado = EstadoEvento.FINALIZADO;
        }
    }

    /** @return número único del evento */
    public int getId() {
        return id;
    }

    /** @return nombre del evento */
    public String getNombre() {
        return nombre;
    }

    /** @return fecha del evento */
    public LocalDate getFecha() {
        return fecha;
    }

    /** @return lugar del evento */
    public String getLugar() {
        return lugar;
    }

    /** @return cantidad máxima de entradas */
    public int getCapacidad() {
        return capacidad;
    }

    /** @return precio de cada entrada */
    public double getPrecioEntrada() {
        return precioEntrada;
    }

    /** @return estado actual del evento */
    public EstadoEvento getEstado() {
        return estado;
    }

    /** @return organizador del evento */
    public Organizador getOrganizador() {
        return organizador;
    }

    /**
     * Devuelve una copia, para que los artistas solo se cambien con
     * {@link #asignarArtista(Artista)} y {@link #quitarArtista(Artista)}.
     *
     * @return artistas del evento
     */
    public List<Artista> getArtistas() {
        return new ArrayList<Artista>(artistas);
    }

    // Validación común al constructor y a modificar: así no se repite
    private void validarDatos(String nombre, LocalDate fecha, String lugar, int capacidad,
            double precioEntrada) throws DatosInvalidosException {
        if (nombre == null || nombre.trim().isEmpty() || lugar == null || lugar.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre y el lugar no pueden estar vacíos.");
        }
        if (fecha == null || !fecha.isAfter(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha del evento tiene que ser futura.");
        }
        if (capacidad <= 0) {
            throw new DatosInvalidosException("La capacidad tiene que ser mayor a 0.");
        }
        if (precioEntrada < 0) {
            throw new DatosInvalidosException("El precio no puede ser negativo.");
        }
    }

    private void guardarDatos(String nombre, LocalDate fecha, String lugar, int capacidad,
            double precioEntrada) {
        this.nombre = nombre.trim();
        this.fecha = fecha;
        this.lugar = lugar.trim();
        this.capacidad = capacidad;
        this.precioEntrada = precioEntrada;
    }

    // Un evento cancelado o finalizado ya no se puede modificar ni cancelar
    private void verificarVigente(String accion) throws EventoException {
        if (estado == EstadoEvento.CANCELADO || estado == EstadoEvento.FINALIZADO) {
            throw new EventoException("No se puede " + accion + " un evento "
                    + estado.toString().toLowerCase() + ".");
        }
    }

    // Pone ACTIVO o AGOTADO según queden lugares. No toca CANCELADO ni FINALIZADO.
    private void actualizarCupo() {
        if (estado == EstadoEvento.ACTIVO || estado == EstadoEvento.AGOTADO) {
            if (getEntradasDisponibles() == 0) {
                estado = EstadoEvento.AGOTADO;
            } else {
                estado = EstadoEvento.ACTIVO;
            }
        }
    }
}
