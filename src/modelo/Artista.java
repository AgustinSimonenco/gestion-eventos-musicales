package modelo;

import excepciones.DatosInvalidosException;
import java.io.Serializable;

/**
 * Músico o banda. Es un catálogo compartido por todos los organizadores.
 */
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String generoMusical;

    /**
     * Crea un artista.
     *
     * @param id            número único, lo genera {@link DatosSistema#generarId()}
     * @param nombre        nombre del músico o la banda
     * @param generoMusical género, por ejemplo "Rock"
     * @throws DatosInvalidosException si el nombre o el género están vacíos
     */
    public Artista(int id, String nombre, String generoMusical) throws DatosInvalidosException {
        validar(nombre, generoMusical);
        this.id = id;
        this.nombre = nombre.trim();
        this.generoMusical = generoMusical.trim();
    }

    /**
     * Cambia los datos del artista (CU15).
     *
     * @param nombre        nombre nuevo
     * @param generoMusical género nuevo
     * @throws DatosInvalidosException si el nombre o el género están vacíos
     */
    public void modificar(String nombre, String generoMusical) throws DatosInvalidosException {
        validar(nombre, generoMusical);
        this.nombre = nombre.trim();
        this.generoMusical = generoMusical.trim();
    }

    /**
     * @return número único del artista
     */
    public int getId() {
        return id;
    }

    /**
     * @return nombre del músico o la banda
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return género musical
     */
    public String getGeneroMusical() {
        return generoMusical;
    }

    /**
     * Texto que muestran las listas de Swing ({@code JList}) para cada artista.
     *
     * @return el nombre del artista
     */
    @Override
    public String toString() {
        return nombre;
    }

    // La misma validación sirve para crear y para modificar: así no se repite
    private void validar(String nombre, String generoMusical) throws DatosInvalidosException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del artista no puede estar vacío.");
        }
        if (generoMusical == null || generoMusical.trim().isEmpty()) {
            throw new DatosInvalidosException("El género musical no puede estar vacío.");
        }
    }
}
