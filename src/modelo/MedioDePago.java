package modelo;

import excepciones.PagoException;
import java.io.Serializable;

/**
 * Lo que el resto del sistema sabe de un pago, sin importar si es con
 * tarjeta, transferencia u otro medio.
 * <p>
 * Es la "estrategia" del patrón Strategy: {@link Compra} guarda una lista de
 * {@code MedioDePago} y solo llama a {@link #procesar()}. Si mañana se agrega
 * otro medio, alcanza con escribir una clase que implemente esta interfaz.
 * <p>
 * Hereda de {@link Serializable} para que cualquier medio de pago se pueda
 * guardar en el archivo junto con su compra.
 */
public interface MedioDePago extends Serializable {

    /**
     * Intenta cobrar el pago.
     *
     * @throws PagoException si el pago se rechaza
     */
    void procesar() throws PagoException;

    /**
     * @return monto a cobrar
     */
    double getMonto();

    /**
     * @return estado del pago
     */
    EstadoPago getEstado();

    /**
     * @return texto corto para el comprobante, por ejemplo
     *         "Tarjeta terminada en 1234 (3 cuotas)"
     */
    String getDescripcion();
}
