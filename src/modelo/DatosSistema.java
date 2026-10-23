package modelo;

import excepciones.ArtistaException;
import excepciones.DatosInvalidosException;
import excepciones.UsuarioException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Agrupa todo lo que se guarda en el archivo: usuarios, eventos y artistas.
 * Las compras no van en una lista aparte porque cada {@link Cliente} ya
 * tiene las suyas.
 * <p>
 * No es una God class: solo guarda las listas y hace las búsquedas que
 * necesitan recorrerlas enteras (por ejemplo, ver si un email ya existe).
 * Las reglas de cada objeto están en su propia clase.
 */
public class DatosSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Usuario> usuarios;
    private List<Evento> eventos;
    private List<Artista> artistas;
    private int ultimoId;

    /**
     * Crea los datos vacíos. Se usa la primera vez que se abre el programa,
     * cuando todavía no existe el archivo.
     */
    public DatosSistema() {
        this.usuarios = new ArrayList<Usuario>();
        this.eventos = new ArrayList<Evento>();
        this.artistas = new ArrayList<Artista>();
        this.ultimoId = 0;
    }

    /**
     * Genera un número nuevo para un evento, un artista o una compra. Hay un
     * solo contador para todo, así los números nunca se repiten.
     *
     * @return número que nunca se usó
     */
    public int generarId() {
        ultimoId = ultimoId + 1;
        return ultimoId;
    }

    /**
     * Registra un usuario nuevo (CU01).
     *
     * @param usuario cliente u organizador a registrar
     * @throws UsuarioException si ya hay un usuario con ese email
     */
    public void registrarUsuario(Usuario usuario) throws UsuarioException {
        if (buscarUsuario(usuario.getEmail()) != null) {
            throw new UsuarioException("Ya hay un usuario registrado con ese email.");
        }
        usuarios.add(usuario);
    }

    /**
     * Busca el usuario por email y controla la contraseña (CU02).
     * Para un email inexistente y para una contraseña incorrecta se usa el
     * mismo mensaje: así no se puede averiguar qué emails están registrados.
     *
     * @param email       email ingresado
     * @param contrasenia contraseña ingresada
     * @return el usuario que inició sesión
     * @throws UsuarioException si el email o la contraseña no coinciden
     */
    public Usuario autenticar(String email, String contrasenia) throws UsuarioException {
        Usuario usuario = buscarUsuario(email);
        if (usuario == null || !usuario.validarContrasenia(contrasenia)) {
            throw new UsuarioException("Email o contraseña incorrectos.");
        }
        return usuario;
    }

    /**
     * Agrega un evento ya creado y validado (CU03).
     *
     * @param evento evento nuevo
     */
    public void agregarEvento(Evento evento) {
        eventos.add(evento);
    }

    /**
     * @param organizador organizador que inició sesión
     * @return eventos creados por ese organizador
     */
    public List<Evento> getEventosDe(Organizador organizador) {
        List<Evento> resultado = new ArrayList<Evento>();
        LocalDate hoy = LocalDate.now();
        for (Evento evento : eventos) {
            // Igual que en getEventosDisponibles: un evento que ya pasó se
            // tiene que ver como FINALIZADO
            evento.actualizarEstado(hoy);
            if (evento.getOrganizador().getEmail().equals(organizador.getEmail())) {
                resultado.add(evento);
            }
        }
        return resultado;
    }

    /**
     * Devuelve los eventos en los que se pueden comprar entradas (CU07).
     * Antes actualiza el estado de cada uno, porque alguno pudo haber pasado
     * de fecha desde la última vez.
     *
     * @param hoy fecha de hoy
     * @return eventos activos
     */
    public List<Evento> getEventosDisponibles(LocalDate hoy) {
        List<Evento> resultado = new ArrayList<Evento>();
        for (Evento evento : eventos) {
            evento.actualizarEstado(hoy);
            if (evento.estaActivo()) {
                resultado.add(evento);
            }
        }
        return resultado;
    }

    /**
     * Agrega un artista al catálogo (CU15).
     *
     * @param artista artista nuevo
     * @throws ArtistaException si ya hay un artista con ese nombre
     */
    public void agregarArtista(Artista artista) throws ArtistaException {
        verificarNombreLibre(artista.getNombre(), null);
        artistas.add(artista);
    }

    /**
     * Cambia los datos de un artista del catálogo (CU15, 2a). Está acá y no
     * solo en {@link Artista} porque hay que recorrer el catálogo para ver
     * que el nombre nuevo no lo tenga otro artista.
     *
     * @param artista       artista a modificar
     * @param nombre        nombre nuevo
     * @param generoMusical género nuevo
     * @throws ArtistaException        si otro artista ya tiene ese nombre
     * @throws DatosInvalidosException si el nombre o el género están vacíos
     */
    public void modificarArtista(Artista artista, String nombre, String generoMusical)
            throws ArtistaException, DatosInvalidosException {
        if (nombre != null) {
            verificarNombreLibre(nombre.trim(), artista);
        }
        artista.modificar(nombre, generoMusical);
    }

    /**
     * Da de baja un artista del catálogo (CU15).
     *
     * @param artista artista a eliminar
     * @throws ArtistaException si está asignado a un evento que todavía no pasó
     */
    public void eliminarArtista(Artista artista) throws ArtistaException {
        for (Evento evento : eventos) {
            // "Evento activo" incluye a los agotados: todavía no se hicieron
            boolean vigente = evento.getEstado() == EstadoEvento.ACTIVO
                    || evento.getEstado() == EstadoEvento.AGOTADO;
            if (vigente && evento.tieneArtista(artista)) {
                throw new ArtistaException("No se puede dar de baja: " + artista.getNombre()
                        + " está en el evento " + evento.getNombre() + ".");
            }
        }
        artistas.remove(artista);
    }

    /**
     * @return copia de la lista de artistas del catálogo
     */
    public List<Artista> getArtistas() {
        return new ArrayList<Artista>(artistas);
    }

    // Controla que ningún artista (salvo "excepto", el que se está
    // modificando) se llame igual. Sin importar mayúsculas: "Soda" y "SODA"
    // son el mismo nombre.
    private void verificarNombreLibre(String nombre, Artista excepto) throws ArtistaException {
        for (Artista existente : artistas) {
            if (existente != excepto && existente.getNombre().equalsIgnoreCase(nombre)) {
                throw new ArtistaException("Ya existe un artista llamado " + nombre + ".");
            }
        }
    }

    // Busca sin importar mayúsculas: "Ana@mail.com" y "ana@mail.com" son el mismo
    private Usuario buscarUsuario(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email.trim())) {
                return usuario;
            }
        }
        return null;
    }
}
