package controlador;

import excepciones.DatosInvalidosException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import vista.VentanaPrincipal;

/**
 * Lo común a los cinco controladores: la sesión, la ventana y las
 * conversiones de texto a número o fecha. Está acá para no repetirlo en cada
 * controlador.
 * <p>
 * Cada controlador coordina los casos de uso de un módulo: recibe lo que
 * escribió el usuario, le pide al modelo que haga la operación, guarda y le
 * dice a la ventana qué mostrar. Nunca decide una regla de negocio.
 */
public abstract class Controlador {

    // "uuuu" es el año en el modo estricto; con STRICT, "31/02/2026" da error
    // en lugar de convertirse solo en otra fecha
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /** Sesión compartida por todos los controladores. */
    protected Sesion sesion;

    /** Ventana única del programa. */
    protected VentanaPrincipal ventana;

    /**
     * @param sesion  sesión compartida
     * @param ventana ventana principal
     */
    public Controlador(Sesion sesion, VentanaPrincipal ventana) {
        this.sesion = sesion;
        this.ventana = ventana;
    }

    /**
     * Convierte el texto de un campo en un número entero.
     *
     * @param texto texto que escribió el usuario
     * @param campo nombre del campo para el mensaje, por ejemplo "La capacidad"
     * @return el número
     * @throws DatosInvalidosException si el texto no es un número entero
     */
    protected int convertirEntero(String texto, String campo) throws DatosInvalidosException {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatosInvalidosException(campo + " tiene que ser un número entero.");
        }
    }

    /**
     * Convierte el texto de un campo en un número con decimales. Acepta coma
     * o punto, porque en Argentina se escribe "1500,50".
     *
     * @param texto texto que escribió el usuario
     * @param campo nombre del campo para el mensaje, por ejemplo "El precio"
     * @return el número
     * @throws DatosInvalidosException si el texto no es un número
     */
    protected double convertirDecimal(String texto, String campo) throws DatosInvalidosException {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new DatosInvalidosException(campo + " tiene que ser un número.");
        }
    }

    /**
     * Convierte un texto con formato {@code dd/mm/aaaa} en una fecha.
     *
     * @param texto texto que escribió el usuario
     * @return la fecha
     * @throws DatosInvalidosException si el texto no es una fecha válida
     */
    protected LocalDate convertirFecha(String texto) throws DatosInvalidosException {
        try {
            return LocalDate.parse(texto.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new DatosInvalidosException("La fecha tiene que tener el formato dd/mm/aaaa.");
        }
    }
}
