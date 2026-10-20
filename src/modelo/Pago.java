package modelo;

import excepciones.DatosInvalidosException;
import excepciones.PagoException;
import java.time.LocalDateTime;

/**
 * Lo común a los pagos con tarjeta y por transferencia: monto, fecha y estado.
 * <p>
 * {@link #procesar()} es el <b>método plantilla</b> (patrón Template Method):
 * los pasos son siempre los mismos y solo cambia {@link #autorizar()}, que
 * implementa cada subclase. Así los pasos no se repiten en cada medio.
 * <p>
 * El pago es simulado: no se conecta con ningún banco.
 */
public abstract class Pago implements MedioDePago {

    private static final long serialVersionUID = 1L;

    private double monto;
    private LocalDateTime fechaHora;
    private EstadoPago estado;

    /**
     * Crea un pago pendiente.
     *
     * @param monto monto a cobrar, 0 o más (puede ser 0 si el evento es gratis)
     * @throws DatosInvalidosException si el monto es negativo
     */
    public Pago(double monto) throws DatosInvalidosException {
        if (monto < 0) {
            throw new DatosInvalidosException("El monto a pagar no puede ser negativo.");
        }
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoPago.PENDIENTE;
    }

    /**
     * Procesa el pago: pide la autorización a la subclase y guarda el
     * resultado.
     *
     * @throws PagoException si ya se había procesado o si se rechaza
     */
    @Override
    public void procesar() throws PagoException {
        if (estado != EstadoPago.PENDIENTE) {
            throw new PagoException("Este pago ya se procesó.");
        }
        // Acá está el polimorfismo: Java ejecuta el autorizar() de la subclase real
        if (autorizar()) {
            estado = EstadoPago.APROBADO;
        } else {
            estado = EstadoPago.RECHAZADO;
            throw new PagoException("El pago fue rechazado: " + getDescripcion()
                    + ". Probá con otro medio de pago.");
        }
    }

    /**
     * Decide si el pago se aprueba. Cada medio de pago lo implementa.
     *
     * @return {@code true} si se aprueba
     */
    protected abstract boolean autorizar();

    /**
     * @return texto corto que describe el pago
     */
    @Override
    public abstract String getDescripcion();

    /** @return monto del pago */
    @Override
    public double getMonto() {
        return monto;
    }

    /** @return estado del pago */
    @Override
    public EstadoPago getEstado() {
        return estado;
    }

    /** @return fecha y hora en que se creó el pago */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Dice si un texto tiene exactamente {@code cantidad} dígitos. Lo usan las
     * dos subclases (número de tarjeta, código, CBU), por eso está acá.
     *
     * @param texto    texto a revisar
     * @param cantidad cantidad de dígitos esperada
     * @return {@code true} si son todos dígitos y son {@code cantidad}
     */
    protected static boolean tieneDigitos(String texto, int cantidad) {
        if (texto == null || texto.length() != cantidad) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
