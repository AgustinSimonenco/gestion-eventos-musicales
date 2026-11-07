package controlador;

import excepciones.SistemaException;
import java.time.LocalDate;
import java.util.List;
import modelo.Carrito;
import modelo.Cliente;
import modelo.Compra;
import modelo.Comprobante;
import modelo.ItemCarrito;
import modelo.MedioDePago;
import modelo.PagoTarjeta;
import modelo.PagoTransferencia;
import vista.VentanaPrincipal;

/**
 * Coordina los casos de uso de la compra: CU10 Confirmar compra, CU11
 * Realizar pago, CU12 Generar comprobante, CU13 Consultar historial y CU14
 * Cancelar compra.
 * <p>
 * Recuerda la compra en curso ({@code compraEnCurso}) porque el CU10 y el
 * CU11 pasan por dos pantallas: se crea en el carrito y se paga en
 * "Pago".
 */
public class ControladorCompras extends Controlador {

    // Compra PENDIENTE que se está pagando. Todavía no es del cliente: si se
    // cancela el pago se descarta y no queda ningún rastro (p7).
    private Compra compraEnCurso;

    /**
     * @param sesion  sesión compartida
     * @param ventana ventana principal
     */
    public ControladorCompras(Sesion sesion, VentanaPrincipal ventana) {
        super(sesion, ventana);
        this.compraEnCurso = null;
    }

    /**
     * Empieza la compra (CU10, pasos 1 a 4): verifica el carrito, muestra el
     * resumen y, si el cliente acepta, crea la compra y va a la pantalla de
     * pago.
     */
    public void iniciarCompra() {
        try {
            Cliente cliente = sesion.getCliente();
            Carrito carrito = cliente.getCarrito();
            // Carrito vacío (1a) o evento cancelado o sin lugar (4a): se sale al catch
            carrito.verificarDisponibilidad();
            if (!ventana.confirmar(armarResumen(carrito))) {
                // 3a: se queda en el carrito para modificarlo
                return;
            }
            int numero = sesion.getDatos().generarId();
            compraEnCurso = new Compra(numero, cliente, carrito.getTotal());
            ventana.mostrar(VentanaPrincipal.PAGO);
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Lo usa {@code PantallaPago} para mostrar el monto (CU11, paso 1).
     *
     * @return total de la compra en curso
     */
    public double getMontoAPagar() {
        return compraEnCurso.getTotal();
    }

    /**
     * Paga con tarjeta (CU11). El constructor de {@link PagoTarjeta} valida
     * el formato de los datos (4a).
     *
     * @param numero  número de la tarjeta
     * @param titular nombre del titular
     * @param mes     texto con el mes de vencimiento
     * @param anio    texto con el año de vencimiento
     * @param codigo  código de seguridad
     * @param cuotas  texto con la cantidad de cuotas
     */
    public void pagarConTarjeta(String numero, String titular, String mes, String anio,
            String codigo, String cuotas) {
        try {
            int mesVencimiento = convertirEntero(mes, "El mes de vencimiento");
            int anioVencimiento = convertirEntero(anio, "El año de vencimiento");
            int cantidadCuotas = convertirEntero(cuotas, "La cantidad de cuotas");
            procesarPago(new PagoTarjeta(compraEnCurso.getTotal(), numero, titular,
                    mesVencimiento, anioVencimiento, codigo, cantidadCuotas));
        } catch (SistemaException e) {
            // Datos inválidos o pago rechazado: sigue en la pantalla de pago,
            // con la misma compra en curso, para reintentar (4a y 5a)
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Paga por transferencia (CU11). El constructor de
     * {@link PagoTransferencia} valida el formato de los datos (4a).
     *
     * @param cbu             CBU de origen
     * @param numeroOperacion número de operación que da el banco
     */
    public void pagarConTransferencia(String cbu, String numeroOperacion) {
        try {
            procesarPago(new PagoTransferencia(compraEnCurso.getTotal(), cbu, numeroOperacion));
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Abandona el pago (CU11, 2a): se descarta la compra y se vuelve al
     * carrito, que sigue igual.
     */
    public void cancelarPago() {
        compraEnCurso = null;
        ventana.mostrar(VentanaPrincipal.CARRITO);
    }

    /**
     * Lo usa {@code PantallaHistorial} para armar la tabla (CU13).
     *
     * @return compras del cliente que inició sesión
     */
    public List<Compra> getHistorial() {
        return sesion.getCliente().getCompras();
    }

    /**
     * Muestra el comprobante de una compra del historial (CU12).
     *
     * @param compra compra elegida en la tabla
     */
    public void verComprobante(Compra compra) {
        try {
            ventana.mostrarTexto("Comprobante", compra.generarComprobante().generarTexto());
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    /**
     * Cancela una compra (CU14). Primero controla que se pueda, así no se
     * pregunta "¿seguro?" por algo que después va a fallar.
     *
     * @param compra compra elegida en la tabla
     */
    public void cancelarCompra(Compra compra) {
        try {
            LocalDate hoy = LocalDate.now();
            compra.verificarCancelable(hoy);
            String monto = String.format("$ %.2f", compra.getTotal());
            if (!ventana.confirmar("¿Cancelar la compra N.º " + compra.getNumero() + " de "
                    + compra.getNombresEventos() + "? Se reintegran " + monto + ".")) {
                return;
            }
            compra.cancelar(hoy);
            // Se recarga antes de guardar, para que la tabla diga CANCELADA
            // aunque falle el guardado: la compra ya cambió en memoria
            ventana.mostrar(VentanaPrincipal.HISTORIAL);
            sesion.guardar();
            ventana.mostrarMensaje("Compra cancelada. Se reintegran " + monto + ".");
        } catch (SistemaException e) {
            ventana.mostrarError(e.getMessage());
        }
    }

    // Lo común a los dos medios de pago (CU10, pasos 5 a 8, y CU12). Recibe la
    // interfaz: no sabe si es tarjeta o transferencia. Las excepciones suben
    // a pagarConTarjeta o pagarConTransferencia, que las muestran.
    private void procesarPago(MedioDePago pago) throws SistemaException {
        Cliente cliente = sesion.getCliente();
        Carrito carrito = cliente.getCarrito();
        // Si se rechaza, lanza PagoException y no se ejecuta nada de lo que sigue
        compraEnCurso.registrarPago(pago);
        compraEnCurso.confirmar(carrito);
        cliente.agregarCompra(compraEnCurso);
        Comprobante comprobante = compraEnCurso.generarComprobante();
        carrito.vaciar();
        compraEnCurso = null;
        // Se sale de la pantalla de pago antes de guardar: la compra ya
        // terminó, y si falla el guardado no se puede volver a pagarla
        ventana.mostrar(VentanaPrincipal.EVENTOS_DISPONIBLES);
        try {
            sesion.guardar();
        } finally {
            // "finally" se ejecuta aunque falle el guardado: la compra ya se
            // pagó, así que el cliente siempre ve su comprobante
            ventana.mostrarTexto("Comprobante", comprobante.generarTexto());
        }
    }

    // Arma el texto que se muestra antes de pagar (CU10, paso 2)
    private String armarResumen(Carrito carrito) {
        StringBuilder resumen = new StringBuilder("Resumen de la compra:\n\n");
        for (ItemCarrito item : carrito.getItems()) {
            resumen.append(item.getEvento().getNombre()).append(": ")
                    .append(item.getCantidad()).append(" x $")
                    .append(String.format("%.2f", item.getEvento().getPrecioEntrada()))
                    .append(" = $").append(String.format("%.2f", item.getSubtotal())).append("\n");
        }
        resumen.append("\nTotal: $").append(String.format("%.2f", carrito.getTotal()));
        resumen.append("\n\n¿Confirmás la compra?");
        return resumen.toString();
    }
}
