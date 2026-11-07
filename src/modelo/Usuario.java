package modelo;

import excepciones.DatosInvalidosException;
import java.io.Serializable;

/**
 * Lo común a toda persona registrada en el sistema: nombre, email y
 * contraseña.
 * <p>
 * Es abstracta porque nunca hay un "usuario" a secas: siempre es un
 * {@link Cliente} o un {@link Organizador}. Cada subclase implementa
 * {@link #getTipo()}, y con eso el controlador decide qué pantalla mostrar
 * sin preguntar por la clase (polimorfismo).
 */
public abstract class Usuario implements Serializable {

    // Número de versión de la clase para la serialización. Si no se fija,
    // Java lo calcula solo y cambia con cada cambio en la clase, y entonces
    // el archivo guardado antes ya no se podría leer.
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String email;
    private String contrasenia;

    /**
     * Crea un usuario validando que los datos no estén vacíos.
     *
     * @param nombre      nombre de la persona
     * @param email       email, que se usa para iniciar sesión
     * @param contrasenia contraseña para iniciar sesión
     * @throws DatosInvalidosException si falta algún dato o el email no tiene "@"
     */
    public Usuario(String nombre, String email, String contrasenia) throws DatosInvalidosException {
        if (estaVacio(nombre) || estaVacio(email) || estaVacio(contrasenia)) {
            throw new DatosInvalidosException("Completá el nombre, el email y la contraseña.");
        }
        if (email.indexOf('@') < 0) {
            throw new DatosInvalidosException("El email no es válido.");
        }
        this.nombre = nombre.trim();
        this.email = email.trim();
        this.contrasenia = contrasenia;
    }

    /**
     * Compara la contraseña ingresada con la del usuario.
     *
     * @param contrasenia contraseña que escribió la persona
     * @return {@code true} si coincide
     */
    public boolean validarContrasenia(String contrasenia) {
        return this.contrasenia.equals(contrasenia);
    }

    /**
     * Dice qué tipo de usuario es. Cada subclase lo implementa.
     *
     * @return {@code "CLIENTE"} u {@code "ORGANIZADOR"}
     */
    public abstract String getTipo();

    /**
     * @return nombre de la persona
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    // Un texto es "vacío" si es null o si solo tiene espacios
    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
