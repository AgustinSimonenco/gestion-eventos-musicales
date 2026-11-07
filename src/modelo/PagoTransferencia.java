package modelo;

import excepciones.DatosInvalidosException;

/**
 * Pago por transferencia bancaria (simulado).
 */
public class PagoTransferencia extends Pago {

    private static final long serialVersionUID = 1L;

    private String cbuOrigen;
    private String numeroOperacion;

    /**
     * Crea un pago por transferencia validando los datos.
     *
     * @param monto           monto a cobrar
     * @param cbuOrigen       CBU desde el que se transfirió, 22 dígitos
     * @param numeroOperacion número de operación que da el banco
     * @throws DatosInvalidosException si algún dato no es válido
     */
    public PagoTransferencia(double monto, String cbuOrigen, String numeroOperacion)
            throws DatosInvalidosException {
        super(monto);
        if (!tieneDigitos(cbuOrigen, 22)) {
            throw new DatosInvalidosException("El CBU tiene que tener 22 dígitos.");
        }
        if (numeroOperacion == null || numeroOperacion.trim().isEmpty()) {
            throw new DatosInvalidosException("Completá el número de operación.");
        }
        this.cbuOrigen = cbuOrigen;
        this.numeroOperacion = numeroOperacion.trim();
    }

    /**
     * Simulación: se rechazan las operaciones terminadas en 000, para poder
     * mostrar el caso de rechazo en la demo.
     *
     * @return {@code true} si se aprueba
     */
    @Override
    protected boolean autorizar() {
        return !numeroOperacion.endsWith("000");
    }

    /**
     * @return por ejemplo "Transferencia desde CBU terminado en 5678 (operación 123)"
     */
    @Override
    public String getDescripcion() {
        return "Transferencia desde CBU terminado en " + cbuOrigen.substring(18)
                + " (operación " + numeroOperacion + ")";
    }
}
