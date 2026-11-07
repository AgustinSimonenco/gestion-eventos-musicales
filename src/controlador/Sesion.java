package controlador;

import excepciones.PersistenciaException;
import modelo.Cliente;
import modelo.DatosSistema;
import modelo.Organizador;
import modelo.Usuario;
import persistencia.Persistencia;

/**
 * Estado de la aplicación mientras está abierta: los datos cargados, la
 * persistencia y el usuario que inició sesión.
 * <p>
 * Todos los controladores comparten el mismo objeto {@code Sesion}: así,
 * cuando uno guarda el usuario actual, los demás ya lo ven.
 */
public class Sesion {

    private DatosSistema datos;
    private Persistencia persistencia;
    private Usuario usuarioActual;

    /**
     * Crea la sesión y carga los datos guardados.
     *
     * @param persistencia forma de cargar y guardar
     * @throws PersistenciaException si no se pudieron cargar los datos
     */
    public Sesion(Persistencia persistencia) throws PersistenciaException {
        this.persistencia = persistencia;
        this.datos = persistencia.cargar();
        this.usuarioActual = null;
    }

    /** @return todos los datos del sistema */
    public DatosSistema getDatos() {
        return datos;
    }

    /** @return el usuario que inició sesión, o {@code null} si no hay ninguno */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * @param usuario usuario que inició sesión, o {@code null} al cerrar sesión
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    /**
     * Devuelve el usuario actual como cliente. El cast es seguro porque a las
     * pantallas del cliente solo se llega si el usuario es cliente (p9).
     *
     * @return el cliente que inició sesión
     */
    public Cliente getCliente() {
        return (Cliente) usuarioActual;
    }

    /**
     * Devuelve el usuario actual como organizador. El cast es seguro por la
     * misma razón que en {@link #getCliente()}.
     *
     * @return el organizador que inició sesión
     */
    public Organizador getOrganizador() {
        return (Organizador) usuarioActual;
    }

    /**
     * Guarda todos los datos. Es el único lugar desde donde se guarda.
     *
     * @throws PersistenciaException si no se pudieron guardar
     */
    public void guardar() throws PersistenciaException {
        persistencia.guardar(datos);
    }
}
