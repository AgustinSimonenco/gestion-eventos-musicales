package modelo;

import excepciones.DatosInvalidosException;
import java.time.YearMonth;

/**
 * Pago con tarjeta de crédito o débito (simulado).
 * <p>
 * Valida los datos en el constructor y <b>solo guarda los últimos 4
 * dígitos</b>: el número completo y el código de seguridad nunca se guardan.
 */
public class PagoTarjeta extends Pago {

    private static final long serialVersionUID = 1L;

    private String titular;
    private String ultimos4Digitos;
    private int cuotas;

    /**
     * Crea un pago con tarjeta validando todos los datos.
     *
     * @param monto            monto a cobrar
     * @param numero           número de la tarjeta, 16 dígitos
     * @param titular          nombre del titular
     * @param mesVencimiento   mes de vencimiento, de 1 a 12
     * @param anioVencimiento  año de vencimiento, por ejemplo 2028
     * @param codigoSeguridad  código de 3 dígitos
     * @param cuotas           1, 3, 6 o 12
     * @throws DatosInvalidosException si algún dato no es válido
     */
    public PagoTarjeta(double monto, String numero, String titular, int mesVencimiento,
            int anioVencimiento, String codigoSeguridad, int cuotas) throws DatosInvalidosException {
        super(monto);
        if (!tieneDigitos(numero, 16)) {
            throw new DatosInvalidosException("El número de tarjeta tiene que tener 16 dígitos.");
        }
        if (titular == null || titular.trim().isEmpty()) {
            throw new DatosInvalidosException("Completá el titular de la tarjeta.");
        }
        validarVencimiento(mesVencimiento, anioVencimiento);
        if (!tieneDigitos(codigoSeguridad, 3)) {
            throw new DatosInvalidosException("El código de seguridad tiene que tener 3 dígitos.");
        }
        if (cuotas != 1 && cuotas != 3 && cuotas != 6 && cuotas != 12) {
            throw new DatosInvalidosException("Las cuotas pueden ser 1, 3, 6 o 12.");
        }
        this.titular = titular.trim();
        this.ultimos4Digitos = numero.substring(12);
        this.cuotas = cuotas;
    }

    /**
     * Simulación: se rechazan las tarjetas terminadas en 0000, para poder
     * mostrar el caso de rechazo en la demo.
     *
     * @return {@code true} si se aprueba
     */
    @Override
    protected boolean autorizar() {
        return !ultimos4Digitos.equals("0000");
    }

    /**
     * @return por ejemplo "Tarjeta terminada en 1234 (3 cuotas)"
     */
    @Override
    public String getDescripcion() {
        String textoCuotas = cuotas + " cuotas";
        if (cuotas == 1) {
            textoCuotas = "1 pago";
        }
        return "Tarjeta terminada en " + ultimos4Digitos + " (" + textoCuotas + ")";
    }

    /** @return nombre del titular */
    public String getTitular() {
        return titular;
    }

    // La tarjeta sirve hasta el último día del mes de vencimiento
    private void validarVencimiento(int mes, int anio) throws DatosInvalidosException {
        if (mes < 1 || mes > 12) {
            throw new DatosInvalidosException("El mes de vencimiento tiene que ser de 1 a 12.");
        }
        if (YearMonth.of(anio, mes).isBefore(YearMonth.now())) {
            throw new DatosInvalidosException("La tarjeta está vencida.");
        }
    }
}
