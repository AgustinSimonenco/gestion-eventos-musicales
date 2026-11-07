package controlador;

import excepciones.DatosInvalidosException;
import excepciones.SistemaException;
import modelo.Cliente;
import modelo.Organizador;
import modelo.Usuario;
import vista.VentanaPrincipal;

/**
 * Coordina los casos de uso del módulo de usuarios: CU01 Registrar usuario,
 * CU02 Iniciar sesión (con el menú según el rol) y cerrar sesión.
 * <p>
 * No tiene reglas propias: el email repetido y la contraseña los controla
 * {@link modelo.DatosSistema}, y los datos vacíos, {@link Usuario}.
 */
public class ControladorUsuarios extends Controlador {

    // Textos del combo de PantallaRegistro. Están acá para que la pantalla y
    // el controlador usen exactamente el mismo texto.
    public static final String TIPO_CLIENTE = "Cliente";
    public static final String TIPO_ORGANIZADOR = "Organizador";

    /**
     * @param sesion  sesión compartida
     * @param ventana ventana principal
     */
    public ControladorUsuarios(Sesion sesion, VentanaPrincipal ventana) {
        super(sesion, ventana);
    }

    /**
     * Registra un usuario nuevo (CU01). Si sale bien, guarda y vuelve al
     * login; si no, muestra el error y se queda en el registro.
     *
     * @param nombre      nombre de la persona
     * @param email       email para iniciar sesión
     * @param contrasenia contraseña
     * @param tipo        {@link #TIPO_CLIENTE} o {@link #TIPO_ORGANIZADOR}
     */
    public void registrar(String nombre, String email, String contrasenia, String tipo) {
        try {
            Usuario usuario = crearUsuario(nombre, email, contrasenia, tipo);
            // El modelo controla que el email no esté repetido (2a)
            sesion.getDatos().registrarUsuario(usuario);
            sesion.guardar();
            ventana.mostrarMensaje("Listo, " + usuario.getNombre() + ". Ya podés iniciar sesión.");
            ventana.mostrar(VentanaPrincipal.LOGIN);
        } catch (SistemaException e) {
            // El modelo avisó que algo no se puede hacer: se muestra el motivo
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Inicia sesión (CU02) y muestra la pantalla de inicio del rol: la del
     * organizador o la del cliente.
     *
     * @param email       email ingresado
     * @param contrasenia contraseña ingresada
     */
    public void iniciarSesion(String email, String contrasenia) {
        try {
            Usuario usuario = sesion.getDatos().autenticar(email, contrasenia);
            sesion.setUsuarioActual(usuario);
            // Polimorfismo: cada subclase de Usuario responde su propio tipo,
            // así no hace falta preguntar de qué clase es el objeto
            if (usuario.getTipo().equals("ORGANIZADOR")) {
                ventana.mostrar(VentanaPrincipal.MIS_EVENTOS);
            } else {
                ventana.mostrar(VentanaPrincipal.EVENTOS_DISPONIBLES);
            }
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Cierra la sesión y vuelve al login. No se guarda nada: cerrar sesión no
     * cambia ningún dato.
     */
    public void cerrarSesion() {
        sesion.setUsuarioActual(null);
        ventana.mostrar(VentanaPrincipal.LOGIN);
    }

    /**
     * Lo usan las pantallas de inicio para saludar al usuario.
     *
     * @return el usuario que inició sesión
     */
    public Usuario getUsuarioActual() {
        return sesion.getUsuarioActual();
    }

    // Crea el objeto de la clase que corresponde según lo elegido en el combo.
    // Los constructores validan que no falten datos.
    private Usuario crearUsuario(String nombre, String email, String contrasenia, String tipo)
            throws DatosInvalidosException {
        if (TIPO_ORGANIZADOR.equals(tipo)) {
            return new Organizador(nombre, email, contrasenia);
        }
        return new Cliente(nombre, email, contrasenia);
    }
}
