package modelo;

import excepciones.CompraException;
import excepciones.EventoException;
import excepciones.PagoException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Operación de un cliente: agrupa las entradas compradas en un mismo momento,
 * con un único pago aprobado y un único comprobante.
 * <p>
 * Nace {@code PENDIENTE} al confirmar el carrito, pasa a {@code CONFIRMADA}
 * cuando se aprueba el pago y puede terminar {@code CANCELADA} (CU14).
 */
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numero;
    private LocalDateTime fechaHora;
    private double total;
    private EstadoCompra estado;
    private Cliente cliente;
    private List<Entrada> entradas;
    private List<MedioDePago> pagos;
    private Comprobante comprobante;

    /**
     * Crea una compra pendiente, sin entradas ni pagos.
     *
     * @param numero  número único, lo genera {@link DatosSistema#generarId()}
     * @param cliente cliente que compra
     * @param total   total del carrito
     */
    public Compra(int numero, Cliente cliente, double total) {
        this.numero = numero;
        this.cliente = cliente;
        this.total = total;
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoCompra.PENDIENTE;
        this.entradas = new ArrayList<Entrada>();
        this.pagos = new ArrayList<MedioDePago>();
    }

    /**
     * Registra un intento de pago y lo procesa (CU11). Si se rechaza, el
     * intento queda en la lista y el cliente puede probar con otro medio.
     * <p>
     * La compra no sabe si es tarjeta o transferencia: solo llama a
     * {@code procesar()} (patrón Strategy).
     *
     * @param pago medio de pago elegido por el cliente
     * @throws CompraException si la compra ya no está pendiente
     * @throws PagoException   si el pago se rechaza
     */
    public void registrarPago(MedioDePago pago) throws CompraException, PagoException {
        if (estado != EstadoCompra.PENDIENTE) {
            throw new CompraException("Esta compra ya no está pendiente de pago.");
        }
        pagos.add(pago);
        pago.procesar();
    }

    /**
     * Confirma la compra: ocupa los lugares en cada evento y crea una entrada
     * por cada unidad del carrito (GRASP Creador).
     *
     * @param carrito carrito del cliente, ya verificado
     * @throws CompraException si no hay un pago aprobado
     * @throws EventoException si algún evento ya no tiene lugar
     */
    public void confirmar(Carrito carrito) throws CompraException, EventoException {
        if (!tienePagoAprobado()) {
            throw new CompraException("La compra se confirma solo con un pago aprobado.");
        }
        for (ItemCarrito item : carrito.getItems()) {
            item.getEvento().ocuparLugares(item.getCantidad());
            crearEntradas(item);
        }
        estado = EstadoCompra.CONFIRMADA;
    }

    /**
     * Devuelve el comprobante (CU12). La primera vez lo crea; después
     * devuelve el mismo, así "Ver comprobante" no genera otro número.
     *
     * @return comprobante de la compra
     * @throws CompraException si la compra no tiene un pago aprobado
     */
    public Comprobante generarComprobante() throws CompraException {
        if (!tienePagoAprobado()) {
            throw new CompraException("No hay comprobante: la compra no tiene un pago aprobado.");
        }
        if (comprobante == null) {
            // El número sale del número de compra, así es único sin otro contador
            comprobante = new Comprobante("C-" + numero, this);
        }
        return comprobante;
    }

    /**
     * Controla que la compra se pueda cancelar (CU14, paso 2). Se llama antes
     * de pedirle confirmación al cliente.
     *
     * @param hoy fecha de hoy
     * @throws CompraException si no está confirmada o algún evento es hoy o ya pasó
     */
    public void verificarCancelable(LocalDate hoy) throws CompraException {
        if (estado == EstadoCompra.CANCELADA) {
            throw new CompraException("La compra ya estaba cancelada.");
        }
        if (estado != EstadoCompra.CONFIRMADA) {
            throw new CompraException("Solo se puede cancelar una compra confirmada.");
        }
        for (Entrada entrada : entradas) {
            // RF15: "antes de la fecha del evento", así que el mismo día ya no se puede
            if (!entrada.getEvento().getFecha().isAfter(hoy)) {
                throw new CompraException("No se puede cancelar: el evento "
                        + entrada.getEvento().getNombre() + " es hoy o ya se realizó.");
            }
        }
    }

    /**
     * Cancela la compra (CU14): anula las entradas y libera los lugares.
     * El reintegro queda registrado con el estado {@code CANCELADA} y el
     * total, que no se borra (D-006).
     *
     * @param hoy fecha de hoy
     * @throws CompraException si la compra no se puede cancelar
     */
    public void cancelar(LocalDate hoy) throws CompraException {
        // Se verifica de nuevo, así la regla se cumple aunque nadie haya verificado antes
        verificarCancelable(hoy);
        for (Entrada entrada : entradas) {
            entrada.anular();
            entrada.getEvento().liberarLugares(1);
        }
        estado = EstadoCompra.CANCELADA;
    }

    /** @return {@code true} si alguno de los pagos se aprobó */
    public boolean tienePagoAprobado() {
        return getPagoAprobado() != null;
    }

    /**
     * @return el pago aprobado, o {@code null} si no hay ninguno
     */
    public MedioDePago getPagoAprobado() {
        for (MedioDePago pago : pagos) {
            if (pago.getEstado() == EstadoPago.APROBADO) {
                return pago;
            }
        }
        return null;
    }

    /** @return número único de la compra */
    public int getNumero() {
        return numero;
    }

    /** @return fecha y hora en que se creó la compra */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /** @return cliente que hizo la compra */
    public Cliente getCliente() {
        return cliente;
    }

    /** @return copia de la lista de entradas */
    public List<Entrada> getEntradas() {
        return new ArrayList<Entrada>(entradas);
    }

    /** @return total de la compra */
    public double getTotal() {
        return total;
    }

    /** @return estado de la compra */
    public EstadoCompra getEstado() {
        return estado;
    }

    // Crea una entrada por cada unidad del ítem. El código es "compra-número",
    // y el número sigue la cuenta de las entradas que ya tiene la compra.
    private void crearEntradas(ItemCarrito item) {
        Evento evento = item.getEvento();
        for (int i = 0; i < item.getCantidad(); i++) {
            String codigo = numero + "-" + (entradas.size() + 1);
            entradas.add(new Entrada(codigo, evento.getPrecioEntrada(), evento));
        }
    }
}
