package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import excepciones.DatosInvalidosException;
import excepciones.PagoException;
import excepciones.SistemaException;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

/**
 * Validaciones y procesamiento de {@link PagoTarjeta} y
 * {@link PagoTransferencia}.
 */
public class PagoTest {

    private static final String TARJETA = "4509123412341234";
    private static final String CBU = "0123456789012345678901";

    // Espera que crear la tarjeta falle con el mensaje dado
    private void verificarTarjetaInvalida(String numero, int mes, int anio, String codigo, int cuotas,
            String mensaje) {
        try {
            new PagoTarjeta(100, numero, "Juan", mes, anio, codigo, cuotas);
            fail("Tenía que rechazar la tarjeta");
        } catch (DatosInvalidosException e) {
            assertEquals(mensaje, e.getMessage());
        }
    }

    @Test
    public void validaElNumeroDeTarjeta() {
        String mensaje = "El número de tarjeta tiene que tener 16 dígitos.";
        verificarTarjetaInvalida("123", 12, 2099, "123", 1, mensaje);
        verificarTarjetaInvalida("450912341234123x", 12, 2099, "123", 1, mensaje);
    }

    @Test
    public void validaElVencimiento() {
        verificarTarjetaInvalida(TARJETA, 13, 2099, "123", 1, "El mes de vencimiento tiene que ser de 1 a 12.");
        verificarTarjetaInvalida(TARJETA, 1, 2020, "123", 1, "La tarjeta está vencida.");
        verificarTarjetaInvalida(TARJETA, 1, 2000000000, "123", 1, "El año de vencimiento no es válido.");
    }

    @Test
    public void laTarjetaSirveHastaElMesDeVencimiento() throws SistemaException {
        YearMonth esteMes = YearMonth.now();
        new PagoTarjeta(100, TARJETA, "Juan", esteMes.getMonthValue(), esteMes.getYear(), "123", 1);
    }

    @Test
    public void validaElCodigoYLasCuotas() {
        verificarTarjetaInvalida(TARJETA, 12, 2099, "12", 1, "El código de seguridad tiene que tener 3 dígitos.");
        verificarTarjetaInvalida(TARJETA, 12, 2099, "123", 2, "Las cuotas pueden ser 1, 3, 6 o 12.");
    }

    @Test
    public void laTarjetaSoloGuardaLosUltimosCuatroDigitos() throws SistemaException {
        assertEquals("Tarjeta terminada en 1234 (3 cuotas)", Fabrica.tarjetaValida(100).getDescripcion());
    }

    @Test
    public void unaTarjetaTerminadaEn0000SeRechaza() throws SistemaException {
        PagoTarjeta pago = new PagoTarjeta(100, "4509123412340000", "Juan", 12, 2099, "123", 1);
        try {
            pago.procesar();
            fail("Tenía que rechazar el pago");
        } catch (PagoException e) {
            assertEquals(EstadoPago.RECHAZADO, pago.getEstado());
        }
    }

    @Test
    public void unPagoNoSeProcesaDosVeces() throws SistemaException {
        PagoTarjeta pago = Fabrica.tarjetaValida(100);
        pago.procesar();
        assertEquals(EstadoPago.APROBADO, pago.getEstado());
        try {
            pago.procesar();
            fail("Tenía que rechazar el segundo proceso");
        } catch (PagoException e) {
            assertEquals("Este pago ya se procesó.", e.getMessage());
        }
    }

    @Test
    public void validaLosDatosDeLaTransferencia() {
        try {
            new PagoTransferencia(100, "123", "555");
            fail("Tenía que rechazar el CBU");
        } catch (DatosInvalidosException e) {
            assertEquals("El CBU tiene que tener 22 dígitos.", e.getMessage());
        }
        try {
            new PagoTransferencia(100, CBU, " ");
            fail("Tenía que pedir la operación");
        } catch (DatosInvalidosException e) {
            assertEquals("Completá el número de operación.", e.getMessage());
        }
    }

    @Test
    public void unaTransferenciaTerminadaEn000SeRechaza() throws SistemaException {
        new PagoTransferencia(100, CBU, "555").procesar();
        PagoTransferencia rechazada = new PagoTransferencia(100, CBU, "1000");
        try {
            rechazada.procesar();
            fail("Tenía que rechazar el pago");
        } catch (PagoException e) {
            assertTrue(e.getMessage().contains("rechazado"));
            assertEquals(EstadoPago.RECHAZADO, rechazada.getEstado());
        }
    }

    @Test
    public void elMontoNoPuedeSerNegativo() {
        try {
            new PagoTransferencia(-1, CBU, "555");
            fail("Tenía que rechazar el monto");
        } catch (DatosInvalidosException e) {
            assertEquals("El monto a pagar no puede ser negativo.", e.getMessage());
        }
    }
}
