package modelo;

import excepciones.DatosInvalidosException;

/**
 * Usuario que crea y administra eventos y gestiona artistas.
 * <p>
 * No tiene atributos propios: cada {@link Evento} guarda quién lo organiza,
 * y para listar sus eventos se usa {@link DatosSistema#getEventosDe(Organizador)}.
 */
public class Organizador extends Usuario {

    private static final long serialVersionUID = 1L;

    /**
     * Crea un organizador.
     *
     * @param nombre      nombre de la persona
     * @param email       email para iniciar sesión
     * @param contrasenia contraseña para iniciar sesión
     * @throws DatosInvalidosException si falta algún dato
     */
    public Organizador(String nombre, String email, String contrasenia) throws DatosInvalidosException {
        super(nombre, email, contrasenia);
    }

    /**
     * @return {@code "ORGANIZADOR"}
     */
    @Override
    public String getTipo() {
        return "ORGANIZADOR";
    }
}
