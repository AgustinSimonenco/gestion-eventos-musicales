package controlador;

import excepciones.SistemaException;
import java.util.List;
import modelo.Artista;
import modelo.DatosSistema;
import vista.VentanaPrincipal;

/**
 * Coordina el CU15 Gestionar artistas: alta, modificación y baja del
 * catálogo de artistas, que comparten todos los organizadores.
 * <p>
 * No tiene reglas propias: los datos vacíos los controla {@link Artista}, y
 * el nombre repetido y la baja de un artista que está en un evento,
 * {@link DatosSistema}.
 */
public class ControladorArtistas extends Controlador {

    /**
     * @param sesion  sesión compartida
     * @param ventana ventana principal
     */
    public ControladorArtistas(Sesion sesion, VentanaPrincipal ventana) {
        super(sesion, ventana);
    }

    /**
     * Lo usa {@code PantallaArtistas} para armar la tabla.
     *
     * @return artistas del catálogo
     */
    public List<Artista> getArtistas() {
        return sesion.getDatos().getArtistas();
    }

    /**
     * Da de alta un artista nuevo (CU15, flujo principal).
     *
     * @param nombre nombre del músico o la banda
     * @param genero género musical
     */
    public void altaArtista(String nombre, String genero) {
        try {
            DatosSistema datos = sesion.getDatos();
            // El constructor controla que no estén vacíos (3a)
            Artista artista = new Artista(datos.generarId(), nombre, genero);
            // DatosSistema controla que no exista otro con el mismo nombre (3a)
            datos.agregarArtista(artista);
            sesion.guardar();
            ventana.mostrarMensaje("Se dio de alta a " + artista.getNombre() + ".");
            // Se vuelve a mostrar la misma pantalla para que la tabla se recargue
            ventana.mostrar(VentanaPrincipal.ARTISTAS);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Cambia el nombre y el género de un artista (CU15, 2a).
     *
     * @param artista artista elegido en la tabla
     * @param nombre  nombre nuevo
     * @param genero  género nuevo
     */
    public void modificarArtista(Artista artista, String nombre, String genero) {
        try {
            sesion.getDatos().modificarArtista(artista, nombre, genero);
            sesion.guardar();
            ventana.mostrarMensaje("Se guardaron los cambios de " + artista.getNombre() + ".");
            ventana.mostrar(VentanaPrincipal.ARTISTAS);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Da de baja un artista del catálogo (CU15, 2b), después de pedir
     * confirmación.
     *
     * @param artista artista elegido en la tabla
     */
    public void bajaArtista(Artista artista) {
        if (!ventana.confirmar("¿Dar de baja a " + artista.getNombre() + "?")) {
            return;
        }
        try {
            // DatosSistema no deja borrar un artista que está en un evento vigente
            sesion.getDatos().eliminarArtista(artista);
            sesion.guardar();
            ventana.mostrarMensaje("Se dio de baja a " + artista.getNombre() + ".");
            ventana.mostrar(VentanaPrincipal.ARTISTAS);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }
}
